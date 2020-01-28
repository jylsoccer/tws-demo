package com.scy.rx.service.impl;

import com.scy.rx.client.EConnClient;
import com.scy.rx.model.HistoricalDataRequest;
import com.scy.rx.model.HistoricalDataResponse;
import com.scy.rx.model.MktDataRequest;
import com.scy.rx.model.TickResponse;
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
        if (FlowableEmitterMap.tryLock()) {
            try {
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
            } finally {
                FlowableEmitterMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");
    }

    @Override
    public Flowable<TickResponse> reqMktData(MktDataRequest request) {
        if (FlowableEmitterMap.tryLock()) {
            try {
                if (flowableEmitterMap.get(request.getTickerId()) != null) {
                    throw new RuntimeException("reqMktData is not available.");
                }
                return Flowable.<TickResponse>create(emitter -> {
                            flowableEmitterMap.put(request.getTickerId(), emitter);
                            eConnClient.getClientSocket().reqMktData(request.getTickerId(), request.getContract(), request.getGenericTickList(),
                                    request.isSnapshot(), request.getMktDataOptions());
                        },
                        BackpressureStrategy.BUFFER).cache();
            } finally {
                FlowableEmitterMap.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");
    }

    @Override
    public void cancelMktData(int tickerId) {
        eConnClient.getClientSocket().cancelOrder(tickerId);
    }

    @Override
    public void reqMarketDataType(int type) {
        eConnClient.getClientSocket().reqMarketDataType(type);
    }
}
