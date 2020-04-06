package com.mibanco.microservicio.app.atmdeposit.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card implements Serializable {
	private String cardNumber;
	private Boolean active;
}
