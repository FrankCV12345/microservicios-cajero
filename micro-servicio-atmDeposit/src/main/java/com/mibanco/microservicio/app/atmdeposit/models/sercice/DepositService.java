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
		return  Single.create(emmiter ->{
				personService.buscaPersonaPorNroDoc(deposit.getDocumentNumbre())
					.subscribe(person -> {
						if(person.getBlackist() == true) {
							emmiter.tryOnError(new UserInBlackListException("Este usuario esta en lista negra"));
						}else {	
							validUserReniecOrFingerPrint(person)
							.subscribe( responseValidator ->{
								cardsService.listaTargetasPorUsuario(deposit.getDocumentNumbre())
									.subscribe(cards -> {
											List<Account> accounts = cards
															.parallelStream()
															.filter(card -> card.getActive() == true)	
															.map(card -> accountService.getCuentas(card.getCardNumber()).map(account -> account).blockingGet())
															.collect(Collectors.toList());
											AtmDepositResponse depositResponse  =  new AtmDepositResponse(responseValidator.getEntityName(),deposit.getAmount(),accounts);
											emmiter.onSuccess(depositResponse);	
										});
									});
							}
					});
			});
		}
	
	
	

	private Single<ResponseValidUser> validUserReniecOrFingerPrint(Person person){
	 		if(person.getFingerprint().equals(true)) {
	 			return fingerPrintService.validaUser(new RequestUser(person.getDocument()));
	 		}else {
	 			return reniecService.validaUser(new RequestUser(person.getDocument()));
	 		}
	}
}

	