package com.mibanco.microservicio.app.person.models.service;

import io.reactivex.Single;

public interface IPersonService {
	public Single<Object> findPersonByDocument(String nroDocument);
}
