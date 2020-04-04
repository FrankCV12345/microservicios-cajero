package com.mibanco.microservicio.app.atmdeposit.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String document;
	private Boolean fingerprint;
	private Boolean blackist;
	
}
