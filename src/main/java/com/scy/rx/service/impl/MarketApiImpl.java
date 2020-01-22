package com.scy.rx.service.impl;

import com.ib.client.EClientSocket;
import com.scy.rx.client.EConnClientHolder;
import com.scy.rx.model.HistoricalDataRequest;
import com.scy.rx.model.HistoricalDataResponse;
import com.scy.rx.service.MarketApi;
import com.scy.rx.wrapper.MultiplexWrapperImpl;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import org.springframework.stereotype.Service;

@Service
public class MarketApiImpl implements MarketApi {

    @Override
    public Flowable<HistoricalDataResponse> historicalDataRequests(HistoricalDataRequest request) {
        EClientSocket clientSocket = EConnClientHolder.getClientSocket();
        clientSocket.reqHistoricalData(request.getTickerId(), request.getContract(), request.getEndDateTime(),
                request.getDurationString(), request.getBarSizeSetting(),
                request.getWhatToShow(), request.getUseRTH(), request.getFormatDate(), request.getChartOptions());

        MultiplexWrapperImpl wrapper = EConnClientHolder.getWrapper();
        FlowableOnSubscribe<HistoricalDataResponse> source = e -> wrapper.putFlowableEmitter(request.getTickerId(), e);

        return Flowable.create(source, BackpressureStrategy.BUFFER);
    }
}
