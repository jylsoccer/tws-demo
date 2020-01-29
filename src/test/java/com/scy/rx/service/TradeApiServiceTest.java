package com.scy.rx.service;


import com.alibaba.fastjson.JSON;
import com.ib.client.ExecutionFilter;
import com.scy.rx.model.ExecDetailsRequest;
import com.scy.rx.model.OrderStatusResponse;
import com.scy.rx.model.PlaceOrderRequest;
import com.scy.rx.service.impl.TradeApiImpl;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import samples.testbed.contracts.ContractSamples;
import samples.testbed.orders.OrderSamples;

import java.util.concurrent.CompletableFuture;


@Slf4j
public class TradeApiServiceTest {
    private TradeApi tradeApi = new TradeApiImpl();

    @Test
    public void test_reqId() throws Exception {
        log.info("test begin");

        log.info("1:reqId:{}", tradeApi.reqId());
        log.info("2:reqId:{}", tradeApi.reqId());
        log.info("3:reqId:{}", tradeApi.reqId());

        Thread.sleep(10000);
    }

    @Test
    public void test_placeOrder() throws Exception {
        CompletableFuture<OrderStatusResponse> future = tradeApi.placeOrder(new PlaceOrderRequest(ContractSamples.USStock(), OrderSamples.LimitOrder("SELL", 2, 50)));
        future.thenAccept(
                response -> log.info("OpenOrderResponse:{}", response)
        );
        Thread.sleep(10000);
    }

    @Test
    public void test_cancelOrder() throws Exception {
        // 下单
        CompletableFuture<OrderStatusResponse> future = tradeApi.placeOrder(new PlaceOrderRequest(ContractSamples.USStock(), OrderSamples.LimitOrder("SELL", 4, 50)));
        future.thenAccept(
                placeOrderResp -> {
                    log.info("placeOrderResp:{}", placeOrderResp);
                    // 查询所有未成交订单
                    tradeApi.reqAllOpenOrders()
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(
                                    openOrder -> {
                                        log.info("openOrder:{}", openOrder);
                                        // 撤销订单
                                        tradeApi.cancelOrder(openOrder.getOrderId())
                                                .thenAccept(orderResponse -> {
                                                    log.info("cancelOrder:{}", JSON.toJSONString(orderResponse));
                                                });
                                    }
                            );
                }
        );

        Thread.sleep(100000);
    }

    @Test
    public void test_reqAllOpenOrders() throws Exception {
        tradeApi.reqAllOpenOrders()
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                response -> log.info("OpenOrderResponse:{}", response)
        );
        Thread.sleep(20000);
    }

    @Test
    public void test_reqExecutions() throws Exception {
        tradeApi.reqExecutions(new ExecDetailsRequest(new ExecutionFilter()))
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        response -> log.info("reqExecutionsResp:{}", response)
                );
        Thread.sleep(20000);
    }
}
