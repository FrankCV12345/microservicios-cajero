package com.mibanco.microservicio.app.atmdeposit.models.sercice;

import java.util.List;

import com.mibanco.microservicio.app.atmdeposit.models.Card;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ICardsService {
	@GET("cards")
	Single<List<Card>> listaTargetasPorUsuario(@Query("documentNumber") String documentNumber);
}
