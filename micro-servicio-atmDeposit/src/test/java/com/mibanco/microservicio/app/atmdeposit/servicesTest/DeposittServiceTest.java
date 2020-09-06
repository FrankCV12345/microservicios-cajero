package com.mibanco.microservicio.app.atmdeposit.servicesTest;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mibanco.microservicio.app.atmdeposit.models.Account;
import com.mibanco.microservicio.app.atmdeposit.models.Card;
import com.mibanco.microservicio.app.atmdeposit.models.Deposit;
import com.mibanco.microservicio.app.atmdeposit.models.Person;
import com.mibanco.microservicio.app.atmdeposit.models.RequestUser;
import com.mibanco.microservicio.app.atmdeposit.models.ResponseValidUser;
import com.mibanco.microservicio.app.atmdeposit.models.sercice.DepositService;
import com.mibanco.microservicio.app.atmdeposit.models.sercice.IAccountService;
import com.mibanco.microservicio.app.atmdeposit.models.sercice.ICardsService;
import com.mibanco.microservicio.app.atmdeposit.models.sercice.IFingerPrintsService;
import com.mibanco.microservicio.app.atmdeposit.models.sercice.IPersonService;
import com.mibanco.microservicio.app.atmdeposit.models.sercice.IReniecService;
import com.mibanco.microservicio.app.atmdeposit.utlitarios.UserInBlackListException;


import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

@RunWith(MockitoJUnitRunner.class)
public class DeposittServiceTest {
	@InjectMocks	
	DepositService depositService;

	Deposit depositUser0 = new Deposit("10000000",100.0);
	Deposit depositUser1 = new Deposit("10000001",100.0);
	Deposit depositUser2 = new Deposit("10000002",100.0);
	Person person_0 = new Person(1L,"10000000",true,false);
	Person person_1 = new Person(2L,"10000001",false,false);
	Person person_2 = new Person(1L,"10000002",true,true);
	
	List<Card> cards = Arrays.asList(
						new Card("1111222233334441",true),
						new Card("1111222233334442",true),
						new Card("1111222233334443",true),
						new Card("1111222233334444",false),
						new Card("1111222233334445",false),
						new Card("1111222233334446",false));
	List<Account> accounts = Arrays.asList(
						new Account("1111222233334441XXX",1000.0),
						new Account("1111222233334442XXX",500.0),
						new Account("1111222233334443XXX",1500.0));

	@Mock
	IPersonService personService; 
	@Mock
	IFingerPrintsService fingerPrintService;
	@Mock
	IReniecService reniecService;
	@Mock
	ICardsService cardsService;
	@Mock
	IAccountService accountService;
	/*
	@Before
	public void setUp() {
		//person service
		when(personService.buscaPersonaPorNroDoc(depositUser0.getDocumentNumbre()))
		.thenReturn(Single.create(s -> s.onSuccess(person_0)));
		
		when(personService.buscaPersonaPorNroDoc(depositUser1.getDocumentNumbre()))
		.thenReturn(Single.create(s -> s.onSuccess(person_1)));
		
		when(personService.buscaPersonaPorNroDoc(depositUser2.getDocumentNumbre()))
		.thenReturn(Single.create(s -> s.onSuccess(person_2)));
		
		//fingert prints service
		when(fingerPrintService.validaUser(new RequestUser(person_0.getDocument())))
			.thenReturn(Single.just(new ResponseValidUser("Core",true)) );
				
				
		//reniec service
		when(reniecService.validaUser(new RequestUser(person_1.getDocument())))
			.thenReturn(Single.just(new ResponseValidUser("Reniec",true)) );
	
		
		
		//cards serviec
		when(cardsService.listaTargetasPorUsuario(depositUser0.getDocumentNumbre()))
		.thenReturn(Single.create( s -> s.onSuccess(cards)));
		
		when(cardsService.listaTargetasPorUsuario(depositUser1.getDocumentNumbre()))
		.thenReturn(Single.create( s -> s.onSuccess(cards)));

		//accounts service
		when(accountService.getCuentas(cards.get(0).getCardNumber()))
		.thenReturn(Single.create(s -> s.onSuccess( accounts.get(0) )));
		
		when(accountService.getCuentas(cards.get(1).getCardNumber()))
		.thenReturn(Single.create(s -> s.onSuccess( accounts.get(1) )));
		
		when(accountService.getCuentas(cards.get(2).getCardNumber()))
		.thenReturn(Single.create(s -> s.onSuccess( accounts.get(2) )));
		
	}

	 */
	@Test
	public void guardaDepositoValidPersonFingerPrintsTest() {
		TestObserver<Object> testObserver  = new TestObserver<>();
		depositService.guardaDeposito(depositUser0).subscribe(testObserver);
		testObserver.awaitTerminalEvent();
		testObserver
			.assertSubscribed()
			.assertComplete()
			.assertValueCount(1)
			.assertNoErrors();
	}
	@Test
	public void guardaDepositoValidPersonReniecTest() {
		TestObserver<Object> testObserver  = new TestObserver<>();
		depositService.guardaDeposito(depositUser1).subscribe(testObserver);
		testObserver.awaitTerminalEvent();
		testObserver
			.assertSubscribed()
			.assertComplete()
			.assertValueCount(1)
			.assertNoErrors();
	}
	
	@Test
	public void guardaDepositoValidExcepcionBalckListTest() {
		TestObserver<Object> testObserver  = new TestObserver<>();
		depositService.guardaDeposito(depositUser2).subscribe(testObserver);
		testObserver.awaitTerminalEvent();
		testObserver
			.assertSubscribed()
			.assertError(UserInBlackListException.class);
	}

	
}
