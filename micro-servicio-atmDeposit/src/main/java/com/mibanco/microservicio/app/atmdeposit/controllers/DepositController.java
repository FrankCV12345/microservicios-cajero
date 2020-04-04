package com.mibanco.microservicio.app.atmdeposit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mibanco.microservicio.app.atmdeposit.models.Deposit;
import com.mibanco.microservicio.app.atmdeposit.models.sercice.DepositService;
import com.mibanco.microservicio.app.atmdeposit.utlitarios.ResponseError;

import io.reactivex.Single;

@RestController
@RequestMapping("/deposits")
public class DepositController {
	
	@Autowired
	DepositService depositService;
	@Autowired
	ResponseError responseError;
	
	@GetMapping("/")
	public Single<ResponseEntity<Object>> deposit(@RequestBody Deposit deposit){
		return depositService.guardaDeposito(deposit)
			.map(d -> ResponseEntity.status(HttpStatus.CREATED).body(d))
			.onErrorReturn(error -> responseError.responseEntityOnError(error.getMessage()));
	}
	
}
