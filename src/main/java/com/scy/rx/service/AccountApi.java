package com.scy.rx.service;

import com.scy.rx.model.AccountSummaryRequest;
import com.scy.rx.model.AccountSummaryResponse;
import com.scy.rx.model.PositionsMultiRequest;
import com.scy.rx.model.PositionsMultiResponse;
import io.reactivex.Flowable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 *  账户信息接口
 */
public interface AccountApi {

	/**
	 * 查当前登录用户的账户列表
	 */
	CompletableFuture<List<String>> reqManagedAccts();

	/**
	 * 查持仓股票
	 */
	Flowable<PositionsMultiResponse> reqPositionsMulti(PositionsMultiRequest request);

	/**
	 * 查账户信息
	 */
	Flowable<AccountSummaryResponse> reqAccountSummary(AccountSummaryRequest request);

	/**
	 * 取消账户信息查询
	 */
	void cancelAccountSummary(int reqId);
}
