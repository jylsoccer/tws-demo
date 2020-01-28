package com.scy.rx.service.impl;

import com.ib.client.ExecutionFilter;
import com.scy.rx.client.EConnClient;
import com.scy.rx.model.*;
import com.scy.rx.service.TraderApi;
import com.scy.rx.wrapper.FlowableEmitterMap;
import com.scy.rx.wrapper.FutureMap;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.scy.rx.wrapper.FlowableEmitterMap.KEY_REQ_ALL_OPEN_ORDERS;
import static com.scy.rx.wrapper.FutureMap.KEY_REQID;

@Service
@Slf4j
public class TradeApiImpl implements TraderApi {

    @Autowired
    private FlowableEmitterMap flowableEmitterMap;

    @Autowired
    private FutureMap futureMap;

    @Autowired
    private EConnClient eConnClient;

    @Override
    public synchronized Integer reqId() {
        try {
            CompletableFuture<Integer> future = new CompletableFuture<>();
            futureMap.put(KEY_REQID, future);
            eConnClient.getClientSocket().reqIds(KEY_REQID);
            return future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("TradeApiImpl.reqId failed.", e);
            throw new RuntimeException(e);
        } finally {
            futureMap.remove(KEY_REQID);
        }
    }

    @Override
    public synchronized CompletableFuture<OrderStatusResponse> placeOrder(PlaceOrderRequest placeOrderRequest) {
        if (futureMap.get(placeOrderRequest.getReqId()) != null) {
            throw new RuntimeException("placeOrder is not available.");
        }
        CompletableFuture<OrderStatusResponse> future = new CompletableFuture<>();
        futureMap.put(placeOrderRequest.getReqId(), future);
        eConnClient.getClientSocket().placeOrder(placeOrderRequest.getReqId(), placeOrderRequest.getContract(), placeOrderRequest.getOrder());
        return future;
    }

    @Override
    public synchronized CompletableFuture<OrderStatusResponse> cancelOrder(int orderId) {
        if (futureMap.get(orderId) != null) {
            throw new RuntimeException("cancelOrder is not available.");
        }
        CompletableFuture<OrderStatusResponse> future = new CompletableFuture<>();
        futureMap.put(orderId, future);
        log.info("cancelOrder, orderId:{}", orderId);
        eConnClient.getClientSocket().cancelOrder(orderId);
        return future;
    }

    @Override
    public Flowable<ExecDetailsResponse> reqExecutions(ExecDetailsRequest request) {
        if (flowableEmitterMap.get(request.getReqId()) != null) {
            throw new RuntimeException("reqExecutions is not available.");
        }
        return Flowable.<ExecDetailsResponse>create(
                emitter -> {
                    flowableEmitterMap.put(request.getReqId(), emitter);
                    eConnClient.getClientSocket().reqExecutions(request.getReqId(), request.getFilter());
                },
                BackpressureStrategy.BUFFER).cache();
    }

    @Override
    public synchronized Flowable<OpenOrderResponse> reqAllOpenOrders() {
        if (flowableEmitterMap.get(KEY_REQ_ALL_OPEN_ORDERS) != null) {
            throw new RuntimeException("reqAllOpenOrders is not available.");
        }
        return Flowable.<OpenOrderResponse>create(
                emitter -> {
                    flowableEmitterMap.put(KEY_REQ_ALL_OPEN_ORDERS, emitter);
                    eConnClient.getClientSocket().reqAllOpenOrders();
                },
                BackpressureStrategy.BUFFER).cache();
    }
}
