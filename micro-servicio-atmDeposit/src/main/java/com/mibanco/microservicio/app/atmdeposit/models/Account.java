package com.mibanco.microservicio.app.atmdeposit.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {
	private String accountNumber;
	@JsonIgnore
	private Double amount;
}
