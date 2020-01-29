package com.scy.rx.service.impl;


import com.scy.rx.client.EConnClient;
import com.scy.rx.model.AccountSummaryRequest;
import com.scy.rx.model.AccountSummaryResponse;
import com.scy.rx.model.PositionsMultiRequest;
import com.scy.rx.model.PositionsMultiResponse;
import com.scy.rx.service.AccountApi;
import com.scy.rx.wrapper.FlowableEmitterMap;
import com.scy.rx.wrapper.FutureMap;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.scy.rx.wrapper.FutureMap.KEY_MANAGED_ACCOUNTS;

@Slf4j
public class AccountApiImpl implements AccountApi {

    private FlowableEmitterMap flowableEmitterMap = FlowableEmitterMap.INSTANCE;

    private FutureMap futureMap = FutureMap.INSTANCE;

    private EConnClient eConnClient = EConnClient.INSTANCE;

    @Override
    public CompletableFuture<List<String>> reqManagedAccts() {
        if (FutureMap.tryLock()) {
            try {
                if (futureMap.get(KEY_MANAGED_ACCOUNTS) != null) {
                    throw new RuntimeException("reqManagedAccts is not available.");
                }
                CompletableFuture<List<String>> future = new CompletableFuture<>();
                futureMap.put(KEY_MANAGED_ACCOUNTS, future);
                eConnClient.getClientSocket().reqManagedAccts();
                return future;
            } catch (Exception e) {
                log.error("TradeApiImpl.reqId failed.", e);
                throw new RuntimeException(e);
            } finally {
                FutureMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");    }

    @Override
    public Flowable<PositionsMultiResponse> reqPositionsMulti(PositionsMultiRequest request) {
        if (FlowableEmitterMap.tryLock()) {
            try {
                if (flowableEmitterMap.get(request.getRequestId()) != null) {
                    throw new RuntimeException("reqPositionsMulti is not available.");
                }
                return Flowable.<PositionsMultiResponse>create(emitter -> {
                            flowableEmitterMap.put(request.getRequestId(), emitter);
                            eConnClient.getClientSocket().reqPositionsMulti(request.getRequestId(), request.getAccount(), request.getModelCode());
                        },
                        BackpressureStrategy.BUFFER).cache();
            } finally {
                FlowableEmitterMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");
    }

    @Override
    public Flowable<AccountSummaryResponse> reqAccountSummary(AccountSummaryRequest request) {
        if (FlowableEmitterMap.tryLock()) {
            try {
                if (flowableEmitterMap.get(request.getReqId()) != null) {
                    throw new RuntimeException("reqAccountSummary is not available.");
                }
                return Flowable.<AccountSummaryResponse>create(emitter -> {
                            flowableEmitterMap.put(request.getReqId(), emitter);
                            eConnClient.getClientSocket().reqAccountSummary(request.getReqId(), request.getGroup(), request.getTags());
                        },
                        BackpressureStrategy.BUFFER).cache();
            } finally {
                FlowableEmitterMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");    }

    @Override
    public void cancelAccountSummary(int reqId) {
        eConnClient.getClientSocket().cancelAccountSummary(reqId);
    }
}
