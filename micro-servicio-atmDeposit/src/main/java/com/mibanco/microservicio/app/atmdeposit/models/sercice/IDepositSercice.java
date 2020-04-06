package com.mibanco.microservicio.app.atmdeposit.models.sercice;

import com.mibanco.microservicio.app.atmdeposit.models.Deposit;
import com.mibanco.microservicio.app.atmdeposit.models.Person;

import io.reactivex.Single;

public interface IDepositSercice {
	public Single<Object> guardaDeposito( Deposit deposit);
}
