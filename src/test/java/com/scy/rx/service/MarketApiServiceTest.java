package com.scy.rx.service;


import com.alibaba.fastjson.JSON;
import com.scy.rx.TestDemo;

import com.scy.rx.model.HistoricalDataRequest;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import samples.testbed.contracts.ContractSamples;

import java.text.SimpleDateFormat;
import java.util.Calendar;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestDemo.class})
@Slf4j
public class MarketApiServiceTest {
    @Autowired
    private MarketApi marketApi;

    @Test
    public void test() throws Exception {
        log.info("test begin");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6);
        SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String formatted = form.format(cal.getTime());
        HistoricalDataRequest request = new HistoricalDataRequest(4001, ContractSamples.EurGbpFx(), formatted, "1 M", "1 day", "MIDPOINT", 1, 1, null);

        log.info("request:{}", JSON.toJSONString(request));
        marketApi.historicalDataRequests(request)
                .subscribeOn(Schedulers.newThread())
                .subscribe(historicalDataResponse -> {
                    log.info("response:{}", JSON.toJSONString(historicalDataResponse));
                });
        Thread.sleep(20000);
    }
}
