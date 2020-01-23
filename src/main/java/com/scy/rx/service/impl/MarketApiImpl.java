package com.scy.rx.service.impl;

import com.ib.client.EClientSocket;
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
    public Flowable<HistoricalDataResponse> historicalDataRequests(HistoricalDataRequest request) {
        EClientSocket client = eConnClient.getClientSocket();
        client.reqHistoricalData(request.getTickerId(), request.getContract(), request.getEndDateTime(),
                request.getDurationString(), request.getBarSizeSetting(),
                request.getWhatToShow(), request.getUseRTH(), request.getFormatDate(), request.getChartOptions());

        return Flowable.create(emitter -> flowableEmitterMap.put(request.getTickerId(), emitter),
                BackpressureStrategy.BUFFER);
    }
}
