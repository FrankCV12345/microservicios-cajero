package com.mibanco.microservicio.app.atmdeposit.models.sercice;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mibanco.microservicio.app.atmdeposit.models.*;

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
	private final IReniecService reniecService = (IReniecService) getService(urlServiceReniec,IReniecService.class);
	private final IFingerPrintsService fingerPrintService = (IFingerPrintsService) getService(urlServiceFingerPrint,IFingerPrintsService.class);
	private final ICardsService cardsService = (ICardsService) getService(urlServiceCards,ICardsService.class);
	private final IAccountService accountService = (IAccountService) getService(urlServiceAccounts,IAccountService.class);
	
	@Override
	public Single<Object> guardaDeposito(Deposit deposit) {
		
		return  find(deposit);
		
		}

	 	private Single<Object> find(Deposit deposit){
	 		return Single.create(s ->{
				personService.buscaPersonaPorNroDoc(deposit.getDocumentNumbre())
				.subscribe(a -> {
					Person p =  (Person) a;
					if(p.getBlackist() == true) {
						s.tryOnError(new Exception("Este usuario esta en lista negra"));
					}else {	
						if(p.getFingerprint().equals(true)) {
							reniecService.validaUser(new RequestUser(deposit.getDocumentNumbre()))
							.subscribe(r->{
								cardsService.listaTargetasPorUsuario(deposit.getDocumentNumbre())
								.subscribe(y -> {
												List<Account> c =	y.parallelStream()
															.filter(card -> card.getActive() == true)	
															.map(card -> accountService.getCuentas(card.getCardNumber()).map(j -> j).blockingGet())
															.collect(Collectors.toList());
										
										s.onSuccess(c);	
									});
									
							});
						}else{
							fingerPrintService.validaUser(new RequestUser(deposit.getDocumentNumbre()))
							.subscribe(r->{
								cardsService.listaTargetasPorUsuario(deposit.getDocumentNumbre())
								.subscribe(y -> {

									List<Account> c =	y.parallelStream()
											.filter(card -> card.getActive() == true)	
											.map(card -> accountService.getCuentas(card.getCardNumber()).map(j -> j).blockingGet())
											.collect(Collectors.toList());
						
									s.onSuccess(c);	
					
								});
							});
						}
					}
				});
				
		});
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
	