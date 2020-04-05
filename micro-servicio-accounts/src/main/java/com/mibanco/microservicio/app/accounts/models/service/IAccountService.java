package com.mibanco.microservicio.app.accounts.models.service;

import io.reactivex.Single;

public interface IAccountService {
	public Single<Object> getCuenta(String numberAccout);
}
