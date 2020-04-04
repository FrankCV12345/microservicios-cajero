package com.mibanco.microservicio.app.person.models.service;


import javax.persistence.EntityNotFoundException;

import com.mibanco.microservicio.app.person.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mibanco.microservicio.app.person.repository.PersonRepository;

import io.reactivex.Single;
@Service
public class PersonService implements IPersonService {
	@Autowired
	PersonRepository personRepository;
	
	@Override
	public Single<Object> findPersonByDocument(String nroDocument) {
		// TODO Auto-generated method stub
		return Single.create(s  -> {
			Person person=	personRepository.findByDocument(nroDocument).orElse(null);
			if(person == null) {
				s.tryOnError(new EntityNotFoundException("Recurso no encontrado"));
			}else {
				s.onSuccess(person);
			}
		});
	}

}
