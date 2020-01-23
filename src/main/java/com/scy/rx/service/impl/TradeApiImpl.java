package com.scy.rx.service.impl;

import com.ib.client.EClientSocket;
import com.scy.rx.client.EConnClient;
import com.scy.rx.model.HistoricalDataRequest;
import com.scy.rx.model.HistoricalDataResponse;
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
        if (futureMap.get(-1) != null) {
            throw new RuntimeException("reqId-future already exists.");
        }
        CompletableFuture<Integer> future = new CompletableFuture<>();
        futureMap.put(-1, future);
        EClientSocket client = eConnClient.getClientSocket();
        client.reqIds(-1);
        return future.get(1, TimeUnit.SECONDS);
    }
}
