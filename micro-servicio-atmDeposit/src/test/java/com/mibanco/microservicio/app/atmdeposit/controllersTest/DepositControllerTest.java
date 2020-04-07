package com.mibanco.microservicio.app.atmdeposit.controllersTest;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mibanco.microservicio.app.atmdeposit.controllers.DepositController;
import com.mibanco.microservicio.app.atmdeposit.models.Account;
import com.mibanco.microservicio.app.atmdeposit.models.AtmDepositResponse;
import com.mibanco.microservicio.app.atmdeposit.models.Deposit;
import com.mibanco.microservicio.app.atmdeposit.models.sercice.DepositService;
import com.mibanco.microservicio.app.atmdeposit.utlitarios.ResponseError;
import com.mibanco.microservicio.app.atmdeposit.utlitarios.UserInBlackListException;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

@RunWith(MockitoJUnitRunner.class)
public class DepositControllerTest {
	@Autowired
	ResponseError responseError;
	@InjectMocks
	DepositController controllerDeposit = new DepositController();
	
	@Mock
	 DepositService serviceDeposit;
	
	 Deposit depositUser0 = new Deposit("10000000",100.0);
	 Deposit depositUser1 = new Deposit("10000001",100.0);
	 Deposit depositUser2 = new Deposit("10000002",100.0);
	 List<Account> accounts = Arrays.asList(
				new Account("1111222233334441XXX",1000.0),
				new Account("1111222233334442XXX",500.0),
				new Account("1111222233334443XXX",1500.0));
	 AtmDepositResponse atmResponse0 = new AtmDepositResponse("Core",depositUser0.getAmount(),accounts);
	
	 AtmDepositResponse atmResponse1 = new AtmDepositResponse("Reniec",depositUser1.getAmount(),accounts);
	 AtmDepositResponse atmResponse2 = new AtmDepositResponse();
	 UserInBlackListException userInBlackListException = new UserInBlackListException("Este usuario esta en lista negra");
	 @Before
	public  void up() {
		when(serviceDeposit.guardaDeposito(depositUser0))
		.thenReturn(Single.create(s -> s.onSuccess(atmResponse0)));
	
		when(serviceDeposit.guardaDeposito(depositUser1))
		.thenReturn(Single.create(s -> s.onSuccess(atmResponse1)));
				
		//when(serviceDeposit.guardaDeposito(depositUser2))
		//.thenReturn(Single.create(s -> s.onError(userInBlackListException) ) );


	}

	@Test
	public void depositUserReniecTest() {
		TestObserver<ResponseEntity<Object>> testobserver = new TestObserver<>();
		controllerDeposit.deposit(depositUser0)
			.subscribe(testobserver);
		
		testobserver.awaitTerminalEvent();
		
		testobserver
			.assertSubscribed()
			.assertComplete()
			.assertValue(ResponseEntity.status(HttpStatus.CREATED).body(atmResponse0))
			.assertValueCount(1);
		
	}
	@Test
	public void depositUserCoreTest() {
		TestObserver<ResponseEntity<Object>> testobserver = new TestObserver<>();
		controllerDeposit.deposit(depositUser1)
			.subscribe(testobserver);
		
		testobserver.awaitTerminalEvent();
		
		testobserver
			.assertSubscribed()
			.assertComplete()
			.assertValue(ResponseEntity.status(HttpStatus.CREATED).body(atmResponse1))
			.assertValueCount(1);
		
	}
	//@Test
	public void depositUserInBlackListTest() {
		TestObserver<ResponseEntity<Object>> testobserver = new TestObserver<>();
		controllerDeposit.deposit(depositUser2)
			.subscribe(testobserver);
		
		testobserver.awaitTerminalEvent();
		
		testobserver
			.assertSubscribed()
			
			.assertError(error  -> error.equals(userInBlackListException));
	}

}
