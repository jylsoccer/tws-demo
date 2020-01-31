package com.scy.rx.service;

import com.scy.rx.model.*;
import io.reactivex.Flowable;

import java.util.concurrent.CompletableFuture;

public interface TradeApi {

	/**
	 * 查当前有效的reqId
	 */
	Integer reqId();

	/**
	 * 下单
	 */
	CompletableFuture<OrderStatusResponse> placeOrder(PlaceOrderRequest placeOrderRequest);

	/**
	 * 撤销订单
	 */
	CompletableFuture<OrderStatusResponse> cancelOrder(int orderId);

	/**
	 * 查订单成交信息
	 */
	Flowable<ExecDetailsResponse> reqExecutions(ExecDetailsRequest request);

	/**
	 * 查订单
	 */
	Flowable<OrderResponse> reqAllOpenOrders();
	Flowable<OrderResponse> reqOpenOrders();
	Flowable<OrderResponse> reqAutoOpenOrders(boolean bAutoBind);
}
