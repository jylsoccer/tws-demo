package com.scy.rx.service.impl;

import com.scy.rx.client.EConnClient;
import com.scy.rx.model.OpenOrderResponse;
import com.scy.rx.model.PlaceOrderRequest;
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
    public synchronized Integer reqId() throws Exception {
        try {
            CompletableFuture<Integer> future = new CompletableFuture<>();
            futureMap.put(KEY_REQID, future);
            eConnClient.getClientSocket().reqIds(KEY_REQID);
            return future.get(1, TimeUnit.SECONDS);
        } finally {
            futureMap.remove(KEY_REQID);
        }
    }

    @Override
    public CompletableFuture<OpenOrderResponse> placeOrder(PlaceOrderRequest placeOrderRequest) {
        CompletableFuture<OpenOrderResponse> future = new CompletableFuture<>();
        futureMap.put(placeOrderRequest.getReqId(), future);
        eConnClient.getClientSocket().placeOrder(placeOrderRequest.getReqId(), placeOrderRequest.getContract(), placeOrderRequest.getOrder());
        return future;
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
