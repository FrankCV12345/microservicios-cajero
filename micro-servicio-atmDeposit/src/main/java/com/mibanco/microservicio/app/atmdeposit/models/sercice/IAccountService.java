package com.mibanco.microservicio.app.atmdeposit.models.sercice;


import com.mibanco.microservicio.app.atmdeposit.models.Account;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IAccountService {
	@GET("accounts")
	Single<Account> getCuentas(@Query("cardNumber") String cardNumber);
}
