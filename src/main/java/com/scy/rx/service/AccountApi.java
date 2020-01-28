package com.scy.rx.service;

import com.scy.rx.model.PositionsMultiRequest;
import com.scy.rx.model.PositionsMultiResponse;
import io.reactivex.Flowable;

/**
 *  账户信息接口
 */
public interface AccountApi {
	/**
	 * 查持仓股票
	 */
	Flowable<PositionsMultiResponse> reqPositionsMulti(PositionsMultiRequest request);
}
