package com.mibanco.microservicio.app.reniec.models;

public class ResponseValidUser {
	private String entityName;
	private Boolean success;
	public ResponseValidUser(String entityName, Boolean success) {
		this.entityName = entityName;
		this.success = success;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	
}
