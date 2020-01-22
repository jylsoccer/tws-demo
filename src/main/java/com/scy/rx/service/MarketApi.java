package com.scy.rx.service;

import io.reactivex.Flowable;
import com.scy.rx.model.HistoricalDataRequest;
import com.scy.rx.model.HistoricalDataResponse;

/**
 *  市场行情接口
 * @author Administrator
 *
 */
public interface MarketApi {
	
	Flowable<HistoricalDataResponse> historicalDataRequests(HistoricalDataRequest request);

}
