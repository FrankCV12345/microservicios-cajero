package com.mibanco.microservicio.app.fingerprints.controllers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mibanco.microservicio.app.fingerprints.models.Card;

import io.reactivex.Single;

@RestController
@RequestMapping("/cards")
public class CarController {
	@GetMapping("/accounts")
	public Single<ResponseEntity<Object>> getCards(@RequestParam String cardNumber){
		return Single
				.just( ResponseEntity
						.status(HttpStatus.OK)
						.body(Arrays.asList(new Card("1111222233334441",true),
											new Card("1111222233334442",true), 
											new Card("1111222233334443",true),
											new Card("1111222233334444",false),
											new Card("1111222233334445",false),
											new Card("1111222233334446",false)
											)
								)
					);
	}
}
