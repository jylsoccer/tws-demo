package com.scy.rx.service;


import com.scy.rx.TestDemo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestDemo.class})
@Slf4j
public class TradeApiServiceTest {
    @Autowired
    private TraderApi traderApi;

    @Test
    public void test() throws Exception {
        log.info("test begin");


        log.info("reqId:{}", traderApi.reqId());
        TimeUnit.SECONDS.sleep(1);
        log.info("reqId2:{}", traderApi.reqId());
        TimeUnit.SECONDS.sleep(1);
        log.info("reqId3:{}", traderApi.reqId());
        Thread.sleep(10000);
    }

}
