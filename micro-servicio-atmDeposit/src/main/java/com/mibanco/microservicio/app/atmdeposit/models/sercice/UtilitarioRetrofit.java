package com.mibanco.microservicio.app.atmdeposit.models.sercice;


import org.springframework.stereotype.Component;


import lombok.NoArgsConstructor;
import lombok.NonNull;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Component
@NoArgsConstructor
public class UtilitarioRetrofit {
	public Retrofit createRetrofit( @NonNull String baseUrl) {
			Retrofit retroFit = new  Retrofit
					.Builder()
					.baseUrl(baseUrl)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			
			return retroFit;
	}
}
