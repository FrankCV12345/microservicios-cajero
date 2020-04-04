package com.mibanco.microservicio.app.atmdeposit.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deposit {
	private String documentNumbre;
	private Double amount;
}
