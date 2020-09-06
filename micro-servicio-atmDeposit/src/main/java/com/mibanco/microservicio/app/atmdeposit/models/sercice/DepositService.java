package com.mibanco.microservicio.app.atmdeposit.models.sercice;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.reactivex.Maybe;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mibanco.microservicio.app.atmdeposit.models.*;
import com.mibanco.microservicio.app.atmdeposit.utlitarios.UserInBlackListException;

import static com.mibanco.microservicio.app.atmdeposit.utlitarios.UtilitarioRetroFit.*;
import static com.mibanco.microservicio.app.atmdeposit.utlitarios.LinksServices.*;
import io.reactivex.Single;
import retrofit2.Response;

@Service
public class DepositService implements IDepositSercice {
	 // Servicios para consumir
	 IPersonService personService = (IPersonService) buildService(urlServicePerson,IPersonService.class);
	 IReniecService reniecService = (IReniecService) buildService(urlServiceReniec,IReniecService.class);
	 IFingerPrintsService fingerPrintService = (IFingerPrintsService) buildService(urlServiceFingerPrint,IFingerPrintsService.class);
	 ICardsService cardsService = (ICardsService) buildService(urlServiceCards,ICardsService.class);
	 IAccountService accountService = (IAccountService) buildService(urlServiceAccounts,IAccountService.class);


	
	@Override
	public Single<Object> guardaDeposito(Deposit deposit) {
		
		return   personService.buscaPersonaPorNroDoc(deposit.getDocumentNumbre())
				.map( personResponse -> getPerson(personResponse))
				.filter(person -> person.getBlackist() ==false)
				.map( person -> validUserReniecOrFingerPrint(person).blockingGet())
				.zipWith(
						getAccounts.apply(deposit.getDocumentNumbre()),
						(responseValidUser ,accounts) -> new AtmDepositResponse(responseValidUser.getEntityName(),deposit.getAmount(),accounts)
				)
				.map(atmDeposit -> (Object) atmDeposit)
				.doOnComplete(() -> {
					throw new UserInBlackListException("Usuario en lista negra");
				})
				.toSingle();
	
		}
	
	// Valida si el usuario se debe buscar en el API de Reniec  o Fingerprints
	private Single<ResponseValidUser> validUserReniecOrFingerPrint(Person person){
	 		if(person.getFingerprint().equals(true)) {
	 			return fingerPrintService.validaUser(new RequestUser(person.getDocument()));
	 		}else {
	 			return reniecService.validaUser(new RequestUser(person.getDocument()));
	 		}
	}
	


	// Valida si la respuesta del API de Persons es 404 
	 private Person getPerson(Response<Person> personResponse) throws Exception {
	 	if(personResponse.code() == HttpStatus.NOT_FOUND.value()){
			throw  new Exception(" Usuario no encontrado");
		}
	 	return personResponse.body();
	 }

	// Lista de Cuentas por numero de tarjeta
	Function<String , Maybe<List<Account>>> getAccounts = (documentNumber) -> cardsService.listaTargetasPorUsuario(documentNumber)
			.map(cards ->cards.parallelStream()
						.filter(card -> card.getActive())
						.map(card -> accountService.getCuentas(card.getCardNumber()).blockingGet())
						.collect(Collectors.toList())
			).toMaybe();
}

	