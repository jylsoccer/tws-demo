package com.scy.rx.service;


import com.alibaba.fastjson.JSON;
import com.ib.client.ExecutionFilter;
import com.scy.rx.TestDemo;
import com.scy.rx.model.ExecDetailsRequest;
import com.scy.rx.model.OrderStatusResponse;
import com.scy.rx.model.PlaceOrderRequest;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import samples.testbed.contracts.ContractSamples;
import samples.testbed.orders.OrderSamples;

import java.util.concurrent.CompletableFuture;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestDemo.class})
@Slf4j
public class TradeApiServiceTest {
    @Autowired
    private TraderApi traderApi;

    @Test
    public void test_reqId() throws Exception {
        log.info("test begin");

        log.info("1:reqId:{}", traderApi.reqId());
        log.info("2:reqId:{}", traderApi.reqId());
        log.info("3:reqId:{}", traderApi.reqId());

        Thread.sleep(10000);
    }

    @Test
    public void test_placeOrder() throws Exception {
        CompletableFuture<OrderStatusResponse> future = traderApi.placeOrder(new PlaceOrderRequest(traderApi.reqId(), ContractSamples.USStock(), OrderSamples.LimitOrder("SELL", 2, 50)));
        future.thenAccept(
                response -> log.info("OpenOrderResponse:{}", response)
        );
        Thread.sleep(10000);
    }

    @Test
    public void test_cancelOrder() throws Exception {
        // 下单
        CompletableFuture<OrderStatusResponse> future = traderApi.placeOrder(new PlaceOrderRequest(traderApi.reqId(), ContractSamples.USStock(), OrderSamples.LimitOrder("SELL", 4, 50)));
        future.thenAccept(
                placeOrderResp -> {
                    log.info("placeOrderResp:{}", placeOrderResp);
                    // 查询所有未成交订单
                    traderApi.reqAllOpenOrders()
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(
                                    openOrder -> {
                                        log.info("openOrder:{}", openOrder);
                                        // 撤销订单
                                        traderApi.cancelOrder(openOrder.getOrderId())
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
        traderApi.reqAllOpenOrders()
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                response -> log.info("OpenOrderResponse:{}", response)
        );
        Thread.sleep(20000);
    }

    @Test
    public void test_reqExecutions() throws Exception {
        traderApi.reqExecutions(new ExecDetailsRequest(traderApi.reqId(), new ExecutionFilter()))
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        response -> log.info("reqExecutionsResp:{}", response)
                );
        Thread.sleep(20000);
    }
}
