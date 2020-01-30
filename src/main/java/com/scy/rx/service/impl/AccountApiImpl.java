package com.scy.rx.service.impl;


import com.scy.apidemo.ApiDemo;
import com.scy.rx.model.*;
import com.scy.rx.service.AccountApi;
import com.scy.rx.wrapper.FlowableEmitterMap;
import com.scy.rx.wrapper.FutureMap;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.scy.rx.wrapper.FlowableEmitterMap.KEY_REQ_ACCOUNT_UPDATES;
import static com.scy.rx.wrapper.FlowableEmitterMap.KEY_REQ_POSITIONS;
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
                if (flowableEmitterMap.get(request.getRequestId()) != null) {
                    throw new RuntimeException("reqPositionsMulti is not available.");
                }
                return Flowable.<PositionsMultiResponse>create(emitter -> {
                            flowableEmitterMap.put(request.getRequestId(), emitter);
                            ApiDemo.getClient().reqPositionsMulti(request.getRequestId(), request.getAccount(), request.getModelCode());
                        },
                        BackpressureStrategy.BUFFER).cache();
            } finally {
                FlowableEmitterMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");
    }

    @Override
    public Flowable<PositionsResponse> reqPositions() {
        if (FlowableEmitterMap.tryLock()) {
            try {
                if (flowableEmitterMap.get(KEY_REQ_POSITIONS) != null) {
                    throw new RuntimeException("reqPositionsMulti is not available.");
                }
                return Flowable.<PositionsResponse>create(emitter -> {
                            flowableEmitterMap.put(KEY_REQ_POSITIONS, emitter);
                            ApiDemo.getClient().reqPositions();
                        },
                        BackpressureStrategy.BUFFER).cache();
            } finally {
                FlowableEmitterMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");
    }

    @Override
    public void cancelPositions() {
        ApiDemo.getClient().cancelPositions();
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
                            ApiDemo.getClient().reqAccountSummary(request.getReqId(), request.getGroup(), request.getTags());
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

    @Override
    public Flowable<AccountUpdatesResponse> reqAccountUpdates(AccountUpdatesRequest request) {
        if (FlowableEmitterMap.tryLock()) {
            try {
                if (flowableEmitterMap.get(KEY_REQ_ACCOUNT_UPDATES) != null) {
                    throw new RuntimeException("reqAccountUpdates is not available.");
                }
                return Flowable.<AccountUpdatesResponse>create(
                        emitter -> {
                            flowableEmitterMap.put(KEY_REQ_ACCOUNT_UPDATES, emitter);
                            ApiDemo.getClient().reqAccountUpdates(request.isSubscribe(), request.getAcctCode());
                        },
                        BackpressureStrategy.BUFFER).cache();
            } finally {
                FlowableEmitterMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");
    }
}
