package com.scy.rx.service.impl;

import com.scy.apidemo.ApiDemo;
import com.scy.rx.model.*;
import com.scy.rx.service.TradeApi;
import com.scy.rx.wrapper.FlowableEmitterMap;
import com.scy.rx.wrapper.FutureMap;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.scy.rx.wrapper.FlowableEmitterMap.KEY_REQ_ALL_OPEN_ORDERS;
import static com.scy.rx.wrapper.FutureMap.KEY_REQID;

@Slf4j
public class TradeApiImpl implements TradeApi {

    private FlowableEmitterMap flowableEmitterMap = FlowableEmitterMap.INSTANCE;

    private FutureMap futureMap = FutureMap.INSTANCE;

    @Override
    public Integer reqId() {
        if (FutureMap.tryLock()) {
            try {
                CompletableFuture<Integer> future = new CompletableFuture<>();
                futureMap.put(KEY_REQID, future);
                ApiDemo.getClient().reqIds(KEY_REQID);
                return future.get(1, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("TradeApiImpl.reqId failed.", e);
                throw new RuntimeException(e);
            } finally {
                futureMap.remove(KEY_REQID);
                FutureMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");
    }

    @Override
    public CompletableFuture<OrderStatusResponse> placeOrder(PlaceOrderRequest placeOrderRequest) {
        if (FutureMap.tryLock()) {
            try {
                if (futureMap.get(placeOrderRequest.getReqId()) != null) {
                    throw new RuntimeException("placeOrder is not available.");
                }
                CompletableFuture<OrderStatusResponse> future = new CompletableFuture<>();
                futureMap.put(placeOrderRequest.getReqId(), future);
                ApiDemo.getClient().placeOrder(placeOrderRequest.getReqId(), placeOrderRequest.getContract(), placeOrderRequest.getOrder());
                return future;
            } finally {
                FutureMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");
    }

    @Override
    public CompletableFuture<OrderStatusResponse> cancelOrder(int orderId) {
        if (FutureMap.tryLock()) {
            try {
                if (futureMap.get(orderId) != null) {
                    throw new RuntimeException("cancelOrder is not available.");
                }
                CompletableFuture<OrderStatusResponse> future = new CompletableFuture<>();
                futureMap.put(orderId, future);
                log.info("cancelOrder, orderId:{}", orderId);
                ApiDemo.getClient().cancelOrder(orderId);
                return future;
            } finally {
                FutureMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");
    }

    @Override
    public Flowable<ExecDetailsResponse> reqExecutions(ExecDetailsRequest request) {
        if (FlowableEmitterMap.tryLock()) {
            try {
                if (flowableEmitterMap.get(request.getReqId()) != null) {
                    throw new RuntimeException("reqExecutions is not available.");
                }
                return Flowable.<ExecDetailsResponse>create(
                        emitter -> {
                            flowableEmitterMap.put(request.getReqId(), emitter);
                            ApiDemo.getClient().reqExecutions(request.getReqId(), request.getFilter());
                        },
                        BackpressureStrategy.BUFFER).cache();
            } finally {
                FlowableEmitterMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");
    }

    @Override
    public Flowable<OpenOrderResponse> reqAllOpenOrders() {
        if (FlowableEmitterMap.tryLock()) {
            try {
                if (flowableEmitterMap.get(KEY_REQ_ALL_OPEN_ORDERS) != null) {
                    throw new RuntimeException("reqAllOpenOrders is not available.");
                }
                return Flowable.<OpenOrderResponse>create(
                        emitter -> {
                            flowableEmitterMap.put(KEY_REQ_ALL_OPEN_ORDERS, emitter);
                            ApiDemo.getClient().reqAllOpenOrders();
                        },
                        BackpressureStrategy.BUFFER).cache();
            } finally {
                FlowableEmitterMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");
    }
}
