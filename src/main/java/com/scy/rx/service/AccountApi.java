package com.scy.rx.service;

import com.scy.rx.model.*;
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
	 * 查持仓
	 */
	Flowable<PositionsMultiResponse> reqPositionsMulti(PositionsMultiRequest request);

	Flowable<PositionsResponse> reqPositions();

	void cancelPositions();

	/**
	 * 查账户信息
	 */
	Flowable<AccountSummaryResponse> reqAccountSummary(AccountSummaryRequest request);

	/**
	 * 取消账户信息查询
	 */
	void cancelAccountSummary(int reqId);
}
