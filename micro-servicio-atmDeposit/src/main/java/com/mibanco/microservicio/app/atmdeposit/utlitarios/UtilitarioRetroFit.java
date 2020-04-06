package com.mibanco.microservicio.app.atmdeposit.utlitarios;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UtilitarioRetroFit {
	
	public static Object buildService(String urlBase, Class<?> service) {
		Retrofit retrofit = new Retrofit
				.Builder()
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.baseUrl(urlBase)
				.build();
		return retrofit.create(service);
	}
	
	
	
}
