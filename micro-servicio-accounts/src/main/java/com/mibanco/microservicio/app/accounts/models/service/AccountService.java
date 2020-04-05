package com.mibanco.microservicio.app.accounts.models.service;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mibanco.microservicio.app.accounts.models.*;
@Service
public class AccountService implements IAccountService {
	@Override
	public Single<Object> getCuenta(String numberAccout) {
		return Single.create(a  -> {
				Thread.sleep(5000);
				findAccountAndEmmiter(a,numberAccout);
		});
	}
	private void findAccountAndEmmiter(SingleEmitter<Object> s,String numberAccout) {
		emmiter(s,findAccountByNumber(numberAccout));
	}
	private void emmiter(SingleEmitter<Object> s, Optional<Account> account) {
		if(account.isPresent()) {
			s.onSuccess(account.get());
		}else {
			s.tryOnError(new Exception("No se encontro la cuenta."));
		}
	}
	private Optional<Account> findAccountByNumber(String numberAccout){
		List<Account> accounts = Arrays.asList(
				new Account("1111222233334441XXX",1000.0),
				new Account("1111222233334442XXX",500.0),
				new Account("1111222233334443XXX",1500.0));
		
		return accounts.stream().filter( c  -> c.getAccountNumber().equals(addAlias(numberAccout))).findFirst();
		
	}
	private String addAlias(String numberCard) {
		return numberCard+"XXX";
	}

}
