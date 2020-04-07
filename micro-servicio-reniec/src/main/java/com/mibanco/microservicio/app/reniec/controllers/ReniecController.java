package com.mibanco.microservicio.app.reniec.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mibanco.microservicio.app.reniec.models.RequestUser;
import com.mibanco.microservicio.app.reniec.models.ResponseValidUser;

import io.reactivex.Single;
@RestController
@RequestMapping("/external/reniec")
public class ReniecController {
	
	@PostMapping("/valid")
	public Single<ResponseEntity<Object>> validarUsuario(@RequestBody RequestUser user){
		return Single.just(ResponseEntity.status(HttpStatus.OK).body(new ResponseValidUser("Reniec", true)));
	}
}
