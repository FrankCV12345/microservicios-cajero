package com.mibanco.microservicio.app.atmdeposit.models.sercice;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mibanco.microservicio.app.atmdeposit.models.*;
import com.mibanco.microservicio.app.atmdeposit.utlitarios.ResponseError;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
@Service
public class DepositService implements IDepositSercice {
	private final String urlServicePerson="http://localhost:8081/";
	@Autowired
	UtilitarioRetrofit Servicio;
	
	@Autowired
	ResponseError responseError;
	
	@Override
	public Single<Object> guardaDeposito(Deposit deposi) {
		Retrofit ServicioPerson =  Servicio.createRetrofit(urlServicePerson);
		return Single.create(s ->{
			IPersonService personService =ServicioPerson.create(IPersonService.class);
			Call<Person> person = personService.buscaPersonaPorNroDoc(deposi.getDocumentNumbre());
			person.enqueue(new Callback<Person>() {
				@Override
				public void onResponse(Call<Person> call, Response<Person> response) {
					if(response.isSuccessful()) {
						s.onSuccess(response.body());
					}else {
						try {
							s.tryOnError(new Exception(response.errorBody().string() ));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				@Override
				public void onFailure(Call<Person> call, Throwable t) {
					s.tryOnError(new Exception("Error inesperado"));
				}
			});
		});
	}

}
