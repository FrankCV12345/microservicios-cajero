package com.mibanco.microservicio.app.atmdeposit.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmDepositResponse extends Object {
	private String fingerprintEntityName;
	private List<Account> validAccounts;
	private Double customerAmount;
	
	public AtmDepositResponse(String fingerprintEntityName, Double deposit , List<Account> validAccounts  ) {
		this.fingerprintEntityName = fingerprintEntityName;
		this.validAccounts = validAccounts;
		this.customerAmount = deposit;
		this.validAccounts.forEach(account -> customerAmount += account.getAmount());
	}
	
}
