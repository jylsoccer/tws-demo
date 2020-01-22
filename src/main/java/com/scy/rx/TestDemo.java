package com.scy.rx;

import com.scy.rx.service.impl.MarketApiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jiangyuanlong
 */
@SpringBootApplication
public class TestDemo {
    @Autowired
    private MarketApiImpl marketApi;

    public static void main(String[] args) {
        SpringApplication.run(TestDemo.class, args);

//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.MONTH, -6);
//        SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
//        String formatted = form.format(cal.getTime());
//        HistoricalDataRequest request = new HistoricalDataRequest(4001, ContractSamples.EurGbpFx(), formatted, "1 M", "1 day", "MIDPOINT", 1, 1, null);
//
//        marketApi.historicalDataRequests(request)
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(historicalDataResponse -> {
//                    System.out.println("HistoricalData. "+reqId+" - Date: "+date+", Open: "+open+", High: "+high+", Low: "+low+", Close: "+close+", Volume: "+volume+", Count: "+count+", WAP: "+WAP+", HasGaps: "+hasGaps);
//                });
    }
}
