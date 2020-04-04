package com.mibanco.microservicio.app.person.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mibanco.microservicio.app.person.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	public Optional<Person> findByDocument(String nroDocumento);
}
