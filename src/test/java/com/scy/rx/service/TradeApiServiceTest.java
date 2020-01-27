package com.scy.rx.service;


import com.scy.rx.TestDemo;
import com.scy.rx.model.OpenOrderResponse;
import com.scy.rx.model.PlaceOrderRequest;
import io.reactivex.Flowable;
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
import java.util.concurrent.TimeUnit;


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
        CompletableFuture<OpenOrderResponse> future = traderApi.placeOrder(new PlaceOrderRequest(traderApi.reqId(), ContractSamples.USStock(), OrderSamples.LimitOrder("SELL", 2, 50)));
        future.thenAccept(
                response -> log.info("OpenOrderResponse:{}", response)
        );
        Thread.sleep(10000);
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
}
