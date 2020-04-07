package com.mibanco.microservicio.app.person.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mibanco.microservicio.app.person.models.service.PersonService;

import io.reactivex.Single;

@RestController
@RequestMapping("/core/persons")
public class PersonController {
	@Autowired
	PersonService personService;
	@Autowired
	ResponseError responseError;
	@GetMapping
	public Single<ResponseEntity<Object>> buscaPersonPorNroDocurmeto(@RequestParam String documentNumber){
		return personService.findPersonByDocument(documentNumber)
				.map(person -> ResponseEntity.status(HttpStatus.OK).body(person))
				.onErrorReturn(error -> responseError.responseEntityOnError(error));
	}
}
