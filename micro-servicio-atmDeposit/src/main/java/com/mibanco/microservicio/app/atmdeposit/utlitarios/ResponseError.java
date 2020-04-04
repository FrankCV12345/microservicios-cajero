package com.mibanco.microservicio.app.atmdeposit.utlitarios;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseError {
	public ResponseEntity<Object> responseEntityOnError(Throwable  ex){
		int statusCode = statusCode(ex.getClass());
		Map<String, Object> rpta = new HashMap<>();
		rpta.put("message", ex.getMessage());
		rpta.put("statusCode", statusCode);
		return ResponseEntity.status(statusCode).body(rpta);
	}
	private int statusCode(Object clase) {
		if(clase == ExceptionInInitializerError.class) {
			return  HttpStatus.NOT_FOUND.value();
		}
		else{
			return HttpStatus.INTERNAL_SERVER_ERROR.value();
		}
	}
	
	public ResponseEntity<Object> responseEntityOnError(String jsonRpta){
		JSONObject json = new JSONObject(jsonRpta);
		
		Map<String, Object> rpta = new HashMap<>();
		rpta.put("message", json.getString("message"));
		rpta.put("statusCode", json.getInt("statusCode"));
		
		return ResponseEntity.status(json.getInt("statusCode")).body(rpta);
	}
	
	
}