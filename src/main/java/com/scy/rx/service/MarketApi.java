package com.scy.rx.service;

import com.scy.rx.model.MktDataRequest;
import com.scy.rx.model.TickResponse;
import io.reactivex.Flowable;
import com.scy.rx.model.HistoricalDataRequest;
import com.scy.rx.model.HistoricalDataResponse;

/**
 *  市场行情接口
 * @author Administrator
 *
 */
public interface MarketApi {

	/**
	 * 查询历史数据
	 */
	Flowable<HistoricalDataResponse> historicalDataRequests(HistoricalDataRequest request);

	/**
	 * 订阅行情
	 */
	Flowable<TickResponse> reqMktData(MktDataRequest request);

	/**
	 * 撤销行情订阅
	 */
	void cancelMktData(int tickerId);

	/**
	 *  Switch to live (1) frozen (2) delayed (3) or delayed frozen (4)
	 */
	void reqMarketDataType(int type);
}
