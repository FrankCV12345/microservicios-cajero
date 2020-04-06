package com.mibanco.microservicio.app.atmdeposit.utlitarios;

public class UserInBlackListException extends Exception {
	public UserInBlackListException(String message) {
		super(message);
	}
}
