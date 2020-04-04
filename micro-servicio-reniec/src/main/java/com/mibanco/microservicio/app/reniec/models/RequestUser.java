package com.mibanco.microservicio.app.reniec.models;

public class RequestUser {
	private String document;

	public RequestUser(String document) {
		this.document = document;
	}


	public RequestUser() {
	}

	public void setDocument(String document) {
		this.document = document;
	}


	public String getDocument() {
		return document;
	}
	
}
