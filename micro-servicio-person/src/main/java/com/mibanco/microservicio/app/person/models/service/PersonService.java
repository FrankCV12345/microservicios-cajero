package com.mibanco.microservicio.app.person.models.service;


import javax.persistence.EntityNotFoundException;

import com.mibanco.microservicio.app.person.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mibanco.microservicio.app.person.repository.PersonRepository;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
@Service
public class PersonService implements IPersonService {
	@Autowired
	PersonRepository personRepository;
	
	@Override
	public Single<Object> findPersonByDocument(String nroDocumento) {
		return Single.create(s  -> findByDocument(s, nroDocumento));
	}

	public void findByDocument(SingleEmitter<Object> s, String nroDocumento){
		Person person =  personRepository.findByDocument(nroDocumento).orElse(null);
		if(person == null) {
			s.tryOnError(new EntityNotFoundException("No se encontro persona"));
		}else {
			s.onSuccess(person);
		}
	}
}
