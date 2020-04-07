package com.mibanco.microservicio.app.accounts.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mibanco.microservicio.app.accounts.models.service.AccountService;

import io.reactivex.Single;

@RestController
@RequestMapping("/core/accounts")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@GetMapping
	public Single<ResponseEntity<Object>> getAccount(@RequestParam String cardNumber){
		
		return accountService.getCuenta(cardNumber)
				.map(cuenta  -> ResponseEntity.status(HttpStatus.OK).body(cuenta))
				.onErrorReturn(error -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage()));
	}
}
