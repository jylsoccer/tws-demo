package com.scy.rx.service.impl;


import com.scy.apidemo.ApiDemo;
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

    @Override
    public CompletableFuture<List<String>> reqManagedAccts() {
        if (FutureMap.tryLock()) {
            try {
                if (futureMap.get(KEY_MANAGED_ACCOUNTS) != null) {
                    throw new RuntimeException("reqManagedAccts is not available.");
                }
                CompletableFuture<List<String>> future = new CompletableFuture<>();
                futureMap.put(KEY_MANAGED_ACCOUNTS, future);
                ApiDemo.getClient().reqManagedAccts();
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
                int reqId = ApiDemo.getAncIncReqId();
                return Flowable.<PositionsMultiResponse>create(emitter -> {
                            flowableEmitterMap.put(reqId, emitter);
                            ApiDemo.getClient().reqPositionsMulti(reqId, request.getAccount(), request.getModelCode());
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
                int reqId = ApiDemo.getAncIncReqId();
                return Flowable.<AccountSummaryResponse>create(emitter -> {
                            flowableEmitterMap.put(reqId, emitter);
                            ApiDemo.getClient().reqAccountSummary(reqId, request.getGroup(), request.getTags());
                        },
                        BackpressureStrategy.BUFFER).cache();
            } finally {
                FlowableEmitterMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");    }

    @Override
    public void cancelAccountSummary(int reqId) {
        ApiDemo.getClient().cancelAccountSummary(reqId);
    }
}
