package com.mibanco.microservicio.app.atmdeposit.models.sercice;

import com.mibanco.microservicio.app.atmdeposit.models.RequestUser;
import com.mibanco.microservicio.app.atmdeposit.models.ResponseValidUser;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IValidUserService {
	@POST("valid")
	Single<ResponseValidUser> validaUser(@Body RequestUser user);
}
