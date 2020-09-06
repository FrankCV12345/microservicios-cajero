package com.mibanco.microservicio.app.atmdeposit.models.sercice;


import com.mibanco.microservicio.app.atmdeposit.models.Person;

import io.reactivex.Single;
//import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IPersonService {
	@GET("persons")
	public Single<Response<Person>> buscaPersonaPorNroDoc(@Query("documentNumber") String documentNumber);
}
