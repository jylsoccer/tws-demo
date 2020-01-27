package com.scy.rx.service;

import com.scy.rx.model.OpenOrderResponse;
import com.scy.rx.model.PlaceOrderRequest;
import io.reactivex.Flowable;

import java.util.concurrent.CompletableFuture;

public interface TraderApi {

	Integer reqId() throws Exception ;

	CompletableFuture<OpenOrderResponse> placeOrder(PlaceOrderRequest placeOrderRequest);

	Flowable<OpenOrderResponse> reqAllOpenOrders();
}
