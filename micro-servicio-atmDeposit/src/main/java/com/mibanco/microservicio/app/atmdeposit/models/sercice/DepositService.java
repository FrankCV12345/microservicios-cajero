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
	
	 IPersonService personService = (IPersonService) buildService(urlServicePerson,IPersonService.class);
	 IReniecService reniecService = (IReniecService) buildService(urlServiceReniec,IReniecService.class);
	 IFingerPrintsService fingerPrintService = (IFingerPrintsService) buildService(urlServiceFingerPrint,IFingerPrintsService.class);
	 ICardsService cardsService = (ICardsService) buildService(urlServiceCards,ICardsService.class);
	 IAccountService accountService = (IAccountService) buildService(urlServiceAccounts,IAccountService.class);



	 private Person getPerson(Response<Person> personResponse) throws Exception {
	 	if(personResponse.code() == HttpStatus.NOT_FOUND.value()){
			throw  new Exception(" Usuario no encontrado");
		}
	 	return personResponse.body();
	 }

	Function<String , Maybe<List<Account>>> getAccounts = (documentNumber) -> cardsService.listaTargetasPorUsuario(documentNumber)
			.map(cards ->cards.parallelStream()
						.filter(card -> card.getActive())
						.map(card -> accountService.getCuentas(card.getCardNumber()).blockingGet())
						.collect(Collectors.toList())
			).toMaybe();
	@Override
	public Single<?> guardaDeposito(Deposit deposit) {
		return personService.buscaPersonaPorNroDoc(deposit.getDocumentNumbre())
				.map( personResponse -> getPerson(personResponse))
				.filter(person -> person.getBlackist() ==false)
				.map( person -> validUserReniecOrFingerPrint(person).blockingGet())
				.zipWith(
						getAccounts.apply(deposit.getDocumentNumbre()),
						(responseValidUser ,accounts) -> new AtmDepositResponse(responseValidUser.getEntityName(),deposit.getAmount(),accounts)
				)
				.doOnComplete(() -> {
					throw new UserInBlackListException("Usuario en lista negra");
				})
				.toSingle();
		}

	private Single<ResponseValidUser> validUserReniecOrFingerPrint(Person person){
	 		if(person.getFingerprint().equals(true)) {
	 			return fingerPrintService.validaUser(new RequestUser(person.getDocument()));
	 		}else {
	 			return reniecService.validaUser(new RequestUser(person.getDocument()));
	 		}
	}
}

	