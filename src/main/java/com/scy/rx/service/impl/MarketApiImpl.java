package com.scy.rx.service.impl;

import com.scy.rx.client.EConnClient;
import com.scy.rx.model.HistoricalDataRequest;
import com.scy.rx.model.HistoricalDataResponse;
import com.scy.rx.service.MarketApi;
import com.scy.rx.wrapper.FlowableEmitterMap;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketApiImpl implements MarketApi {

    @Autowired
    private FlowableEmitterMap flowableEmitterMap;

    @Autowired
    private EConnClient eConnClient;

    @Override
    public synchronized Flowable<HistoricalDataResponse> historicalDataRequests(HistoricalDataRequest request) {
        if (flowableEmitterMap.get(request.getTickerId()) != null) {
            throw new RuntimeException("historicalDataRequests is not available.");
        }
        return Flowable.<HistoricalDataResponse>create(emitter -> {
                    flowableEmitterMap.put(request.getTickerId(), emitter);
                    eConnClient.getClientSocket().reqHistoricalData(request.getTickerId(), request.getContract(), request.getEndDateTime(),
                            request.getDurationString(), request.getBarSizeSetting(),
                            request.getWhatToShow(), request.getUseRTH(), request.getFormatDate(), request.getChartOptions());
                },
                BackpressureStrategy.BUFFER).cache();
    }
}
