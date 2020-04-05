package com.mibanco.microservicio.app.fingerprints.models;

public class Card {
	private String cardNumber;
	private Boolean active;
	public Card(String cardNumber, Boolean active) {
		super();
		this.cardNumber = cardNumber;
		this.active = active;
	}
	public Card() {
		super();
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
}
