package com.scy.rx.service;

import ctp.thostapi.*;
import io.reactivex.Flowable;
import org.saturn.ctp.CTP.TradeContext;
import org.saturn.ctp.CtpFuture;
import org.saturn.ctp.CtpQuery;
import org.saturn.ctp.CtpQuery.OrderQry;

import java.util.List;

/**
 *  ctprx交易接口
 * @author chenbangxin
 *
 */
public interface TraderApi {

	
	//注册前置机地址
	public void RegisterFront(String pszFrontAddress);
	
	//接口初始化
	public void Init();
	
	//释放交易对象，将不再接受任何服务端反馈
	public void Release();
	
	public String getVersion();
	
	public boolean isConnected();
	
	//订阅网络状态信息
	public Flowable<Integer> getNetStates();
	
	//订阅订单有关消息
	public Flowable<CThostFtdcOrderField> getOrdersMessage();	
	
	//订阅成交信息
	public Flowable<CThostFtdcTradeField> getTradesMessage();
	
	//获取登录上下文
	public TradeContext getTradeContext();
	
	
	//登录操作
	public CtpFuture<CThostFtdcRspUserLoginField> reqUserLogin(CThostFtdcReqUserLoginField pReqUserLoginField, int nRequestID);

	//结算单确认
	public CtpFuture<List<CThostFtdcSettlementInfoConfirmField>> reqSettlementInfoConfirm(CThostFtdcSettlementInfoConfirmField field, int nRequestID);

	//报单
	public CtpFuture<CThostFtdcOrderField> reqOrderInsert(CThostFtdcInputOrderField field, int nRequestID);

	//撤单
	public CtpFuture<CThostFtdcOrderField> reqOrderAction(CThostFtdcInputOrderActionField pInputOrderAction, int nRequestID);

	public CtpFuture<List<CThostFtdcSettlementInfoField>> qrySettlementInfo(CThostFtdcQrySettlementInfoField field, int nRequestID);

	public CtpFuture<List<CThostFtdcSettlementInfoConfirmField>> qrySettlementInfoConfirm(CThostFtdcQrySettlementInfoConfirmField field, int nRequestID);

	public CtpFuture<CThostFtdcTradingAccountField> qryTradingAccount(CThostFtdcQryTradingAccountField field, int nRequestID);

	public CtpFuture<List<CThostFtdcInvestorPositionField >> qryInverstorPosition(CThostFtdcQryInvestorPositionField field, int nRequestID);

	public CtpFuture<List<CThostFtdcOrderField>> qryOrder(CThostFtdcQryOrderField field, int nRequestID);

	public CtpFuture<List<CThostFtdcTradeField>> qryTrade(CThostFtdcQryTradeField field, int nRequestID);

	//查询交易所
	public CtpFuture<List<CThostFtdcExchangeField>> qryExchange(CThostFtdcQryExchangeField pQryExchange, int nRequestID);

	public CtpFuture<List<CThostFtdcInstrumentField>> qryInstrument(CThostFtdcQryInstrumentField field, int nRequestID);
	
	public CtpFuture<List<CThostFtdcDepthMarketDataField>>  qryDepthMarketData(CThostFtdcQryDepthMarketDataField pQryDepthMarketData, int nRequestID);

    //设置查询接口的起始requestID
	public org.saturn.ctp.TraderApi setQryStartID(int startRequestID);
	
	//创建查询接口， 不用输入显式的请求编号。
	public CtpQuery createQry();
	
	//创建订单查询接口，支持链式编程
	public OrderQry createOrderQuery();

	public void Join();
	
}
