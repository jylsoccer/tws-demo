package com.scy.rx.service.impl;

import com.scy.rx.client.EConnClient;
import com.scy.rx.service.TraderApi;
import com.scy.rx.wrapper.FlowableEmitterMap;
import com.scy.rx.wrapper.FutureMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
}
