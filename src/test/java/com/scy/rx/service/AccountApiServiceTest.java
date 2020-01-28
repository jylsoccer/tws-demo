package com.scy.rx.service;


import com.alibaba.fastjson.JSON;
import com.scy.rx.TestDemo;
import com.scy.rx.model.PositionsMultiRequest;
import com.scy.rx.model.PositionsMultiResponse;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestDemo.class})
@Slf4j
public class AccountApiServiceTest {
    @Autowired
    private AccountApi accountApi;

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
                            log.info("position end");
                        });
        Thread.sleep(10000);
    }

}
