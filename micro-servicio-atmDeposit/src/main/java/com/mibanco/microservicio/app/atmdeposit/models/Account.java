package com.mibanco.microservicio.app.atmdeposit.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {
	private String accountNumber;
	private Double amount;
}
