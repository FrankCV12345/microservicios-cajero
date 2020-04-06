package com.mibanco.microservicio.app.atmdeposit.models.sercice;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mibanco.microservicio.app.atmdeposit.models.*;
import com.mibanco.microservicio.app.atmdeposit.utlitarios.UserInBlackListException;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
@Service
public class DepositService implements IDepositSercice {
	private final String urlServicePerson = "http://localhost:8081/";
	private final String urlServiceReniec = "http://localhost:8083/reniec/";
	private final String urlServiceFingerPrint = "http://localhost:8084/fingerprints/";
	private final String urlServiceCards = "http://localhost:8085/";
	private final String urlServiceAccounts = "http://localhost:8086/";
	
	
	private final IPersonService personService=(IPersonService) getService(urlServicePerson,IPersonService.class);
	private final IValidUserService reniecService = (IValidUserService) getService(urlServiceReniec,IValidUserService.class);
	private final IValidUserService fingerPrintService = (IValidUserService) getService(urlServiceFingerPrint,IValidUserService.class);
	private final ICardsService cardsService = (ICardsService) getService(urlServiceCards,ICardsService.class);
	private final IAccountService accountService = (IAccountService) getService(urlServiceAccounts,IAccountService.class);
	
	@Override
	public Single<Object> guardaDeposito(Deposit deposit) {
		return  getAccountsAndDesposit(deposit);
		}
	 	private Single<Object> getAccountsAndDesposit(Deposit deposit){
	 		return Single.create(emmiter ->{
					personService.buscaPersonaPorNroDoc(deposit.getDocumentNumbre())
					.subscribe(person -> {
						if(person.getBlackist() == true) {
							emmiter.tryOnError(new UserInBlackListException("Este usuario esta en lista negra"));
						}else {	
							validUserReniecOrFingerPrint(person).subscribe( responseValidator ->{
								cardsService.listaTargetasPorUsuario(deposit.getDocumentNumbre())
									.subscribe(cards -> {
											List<Account> accounts = cards.parallelStream()
															.filter(card -> card.getActive() == true)	
															.map(card -> accountService.getCuentas(card.getCardNumber()).map(account -> account).blockingGet())
															.collect(Collectors.toList());
											AtmDepositResponse despitResponse  =  new AtmDepositResponse(responseValidator.getEntityName(),deposit.getAmount(),accounts);
											emmiter.onSuccess(despitResponse);	
									});
							});
						}
					});
			});
	 	}
	 	
	 	private Single<ResponseValidUser> validUserReniecOrFingerPrint(Person person){
	 		if(person.getFingerprint().equals(true)) {
	 			return reniecService.validaUser(new RequestUser(person.getDocument()));
	 		}else {
	 			return fingerPrintService.validaUser(new RequestUser(person.getDocument()));
	 		}
	 	}
		private Object getService(String urlBase, Class<?> service) {
			Retrofit retrofit = new Retrofit
					.Builder()
					.addConverterFactory(GsonConverterFactory.create())
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
					.baseUrl(urlBase)
					.build();
			return retrofit.create(service);
		}
	}
	