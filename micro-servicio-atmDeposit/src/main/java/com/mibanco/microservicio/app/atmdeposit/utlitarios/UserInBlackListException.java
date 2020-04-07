package com.mibanco.microservicio.app.atmdeposit.utlitarios;

import java.io.Serializable;

public class UserInBlackListException extends Exception implements Serializable {
	public UserInBlackListException(String message) {
		super(message);
	}
}
