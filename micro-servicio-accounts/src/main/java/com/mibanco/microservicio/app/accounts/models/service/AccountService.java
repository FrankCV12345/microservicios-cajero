package com.mibanco.microservicio.app.accounts.models.service;

import io.reactivex.Single;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mibanco.microservicio.app.accounts.models.*;
@Service
public class AccountService implements IAccountService {
	List<Account> accounts = Arrays.asList(new Account("1111222233334441XXX",1000.0),new Account("1111222233334442XXX",500.0),new Account("1111222233334443XXX",1500.0));
	@Override
	public Single<Object> getCuenta(String numberAccout) {
		// TODO Auto-generated method stub
		return Single.create(a  -> {
				Thread.sleep(5000);
				Optional<Account> cuentas = accounts.stream()
					.filter( c  -> c.getAccountNumber().equals(addAlias(numberAccout)))
					.findFirst();
					if(!cuentas.isPresent()) {
						a.tryOnError(new Exception("No se encontro cuenta"));
					}else {
						a.onSuccess(cuentas);
					}

		});
	}
	
	private String addAlias(String numberCard) {
		return numberCard+"XXX";
	}

}
