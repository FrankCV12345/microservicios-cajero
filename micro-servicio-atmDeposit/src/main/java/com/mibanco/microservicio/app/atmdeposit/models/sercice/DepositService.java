package com.mibanco.microservicio.app.atmdeposit.models.sercice;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mibanco.microservicio.app.atmdeposit.models.*;
import com.mibanco.microservicio.app.atmdeposit.utlitarios.UserInBlackListException;

import static com.mibanco.microservicio.app.atmdeposit.utlitarios.UtilitarioRetroFit.*;
import static com.mibanco.microservicio.app.atmdeposit.utlitarios.LinksServices.*;
import io.reactivex.Single;
@Service
public class DepositService implements IDepositSercice {
	
	 IPersonService personService = (IPersonService) buildService(urlServicePerson,IPersonService.class);
	 IReniecService reniecService = (IReniecService) buildService(urlServiceReniec,IReniecService.class);
	 IFingerPrintsService fingerPrintService = (IFingerPrintsService) buildService(urlServiceFingerPrint,IFingerPrintsService.class);
	 ICardsService cardsService = (ICardsService) buildService(urlServiceCards,ICardsService.class);
	 IAccountService accountService = (IAccountService) buildService(urlServiceAccounts,IAccountService.class);
	
	@Override
	public Single<Object> guardaDeposito(Deposit deposit) {
		return Single.create( emitter -> personService.buscaPersonaPorNroDoc(deposit.getDocumentNumbre())
				.filter(person -> person.getBlackist() == false)
				.doOnComplete(() -> emitter.tryOnError( new UserInBlackListException("Este usuario esta en lista negra") ))
				.subscribe(
						person -> validUserReniecOrFingerPrint(person)
								.map( responseValidUser -> {
									List<Account> accounts =cardsService.listaTargetasPorUsuario(deposit.getDocumentNumbre())
											.blockingGet()
											.parallelStream()
											.filter( card -> card.getActive())
											.map( card -> accountService.getCuentas(card.getCardNumber()).blockingGet())
											.collect(Collectors.toList());
									return  new AtmDepositResponse(responseValidUser.getEntityName(),deposit.getAmount(),accounts);
								})
								.subscribe( atmDepositResponse -> emitter.onSuccess(atmDepositResponse)),
						error -> emitter.tryOnError(error)
				)
		);
		}
	
	
	

	private Single<ResponseValidUser> validUserReniecOrFingerPrint(Person person){
	 		if(person.getFingerprint().equals(true)) {
	 			return fingerPrintService.validaUser(new RequestUser(person.getDocument()));
	 		}else {
	 			return reniecService.validaUser(new RequestUser(person.getDocument()));
	 		}
	}
}

	