package com.scy.rx.service;


import com.alibaba.fastjson.JSON;
import com.ib.client.MarketDataType;
import com.scy.rx.model.HistoricalDataRequest;
import com.scy.rx.model.MktDataRequest;
import com.scy.rx.service.impl.MarketApiImpl;
import com.scy.rx.service.impl.TradeApiImpl;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import samples.testbed.contracts.ContractSamples;

import java.text.SimpleDateFormat;
import java.util.Calendar;


@Slf4j
public class MarketApiServiceTest {
    private MarketApi marketApi = new MarketApiImpl();

    @Test
    public void test() throws Exception {
        log.info("test begin");

        HistoricalDataRequest request = getHistoricalDataRequest();

        log.info("request:{}", JSON.toJSONString(request));
        marketApi.historicalDataRequests(request)
                .subscribeOn(Schedulers.newThread())
                .subscribe(historicalDataResponse -> log.info("response:{}", JSON.toJSONString(historicalDataResponse)));
        Thread.sleep(10000);
    }

    private HistoricalDataRequest getHistoricalDataRequest() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6);
        SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String formatted = form.format(cal.getTime());
        return new HistoricalDataRequest(ContractSamples.EurGbpFx(), formatted, "1 M", "1 day", "MIDPOINT", 1, 1, null);
    }

    @Test
    public void test_reqMktData() throws Exception {
        marketApi.reqMarketDataType(MarketDataType.DELAYED);
        marketApi.reqMktData(new MktDataRequest(ContractSamples.USStock(), "", false, null))
                .subscribeOn(Schedulers.newThread())
                .subscribe(tickResponse -> {
                            log.info("tickResponse:{}", JSON.toJSONString(tickResponse));
                        },
                        error -> {
                            log.error("reqMktData error.", error);
                        });

        Thread.sleep(10000);
    }
}
