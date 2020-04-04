package com.mibanco.microservicio.app.person.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class ResponseError {
	public ResponseEntity<Object> responseEntityOnError(Throwable  ex){
		int statusCode = statusCode(ex.getClass());
		Map<String, Object> rpta = new HashMap<>();
		if(ex.getMessage().isEmpty()) {
			rpta.put("message", "Error desconocido");
			
		}else {
			rpta.put("message", ex.getMessage());
		}
		rpta.put("statusCode", statusCode);
		return ResponseEntity.status(statusCode).body(rpta);
	}
	private int statusCode(Object clase) {
		if(clase == EntityNotFoundException.class) {
			return  HttpStatus.NOT_FOUND.value();
		}
		else{
			return HttpStatus.INTERNAL_SERVER_ERROR.value();
		}
	}
	
}
