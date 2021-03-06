package com.scy.rx.service;


import com.alibaba.fastjson.JSON;
import com.scy.rx.model.AccountSummaryRequest;
import com.scy.rx.model.AccountSummaryResponse;
import com.scy.rx.model.PositionsMultiRequest;
import com.scy.rx.model.PositionsMultiResponse;
import com.scy.rx.service.impl.AccountApiImpl;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
public class AccountApiServiceTest {
    private AccountApi accountApi = new AccountApiImpl();

    @Test
    public void test_reqManagedAccts() throws Exception {
        CompletableFuture<List<String>> future = accountApi.reqManagedAccts();
        future.thenAccept(list -> {
            Flowable<PositionsMultiResponse> flowable = accountApi.reqPositionsMulti(new PositionsMultiRequest(9003, list.get(0), ""));
            flowable.subscribeOn(Schedulers.newThread())
                    .subscribe(response -> {
                                log.info("position:{}", JSON.toJSONString(response));
                            },
                            error -> {
                                log.error("reqMktData error.", error);
                            },
                            () -> {
                                log.info("position end");
                            });
        });
        Thread.sleep(10000);
    }

    @Test
    public void test_reqPositionsMulti() throws Exception {
        Flowable<PositionsMultiResponse> flowable = accountApi.reqPositionsMulti(new PositionsMultiRequest(9003, "DU1812147", ""));
        flowable.subscribeOn(Schedulers.newThread())
                .subscribe(response -> {
                            log.info("position:{}", JSON.toJSONString(response));
                        },
                        error -> {
                            log.error("reqMktData error.", error);
                        },
                        () -> {
                            log.debug("position end");
                        });
        Thread.sleep(10000);
    }

    @Test
    public void test_reqAccountSummary() throws Exception {
        Flowable<AccountSummaryResponse> flowable = accountApi.reqAccountSummary(new AccountSummaryRequest( 9001,"All", "AccountType,NetLiquidation,TotalCashValue"));
        flowable.subscribeOn(Schedulers.newThread())
                .subscribe(response -> {
                            log.info("account summary:{}", JSON.toJSONString(response));
                        },
                        error -> {
                            log.error("reqAccountSummary error.", error);
                        },
                        () -> {
                            log.info("account end");
                        });
        Thread.sleep(10000);
    }


}
