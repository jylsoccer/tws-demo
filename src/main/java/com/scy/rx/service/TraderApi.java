package com.scy.rx.service;

import com.scy.rx.model.OpenOrderResponse;
import com.scy.rx.model.OrderStatusResponse;
import com.scy.rx.model.PlaceOrderRequest;
import io.reactivex.Flowable;

import java.util.concurrent.CompletableFuture;

public interface TraderApi {

	Integer reqId() throws Exception ;

	CompletableFuture<OrderStatusResponse> placeOrder(PlaceOrderRequest placeOrderRequest);

	CompletableFuture<OrderStatusResponse> cancelOrder(int orderId);

	Flowable<OpenOrderResponse> reqAllOpenOrders();
}
