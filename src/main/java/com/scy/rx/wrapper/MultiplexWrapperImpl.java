package com.scy.rx.wrapper;

import com.ib.client.*;
import com.scy.rx.model.*;
import io.reactivex.FlowableEmitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.scy.rx.wrapper.FlowableEmitterMap.KEY_REQ_ALL_OPEN_ORDERS;
import static com.scy.rx.wrapper.FutureMap.KEY_REQID;

@Slf4j
@Service
public class MultiplexWrapperImpl implements EWrapper {

	private EReaderSignal readerSignal;
	private EClientSocket clientSocket;
	protected int currentOrderId = -1;

	@Autowired
	private FlowableEmitterMap flowableEmitterMap;

	@Autowired
	private FutureMap futureMap;

	public MultiplexWrapperImpl() {
		readerSignal = new EJavaSignal();
		clientSocket = new EClientSocket(this, readerSignal);
	}

	public EClientSocket getClientSocket() {
		return clientSocket;
	}
	
	public EReaderSignal getSignal() {
		return readerSignal;
	}



	public int getCurrentOrderId() {
		return currentOrderId;
	}
	
	 //! [tickprice]
	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		System.out.println("Tick Price. Ticker Id:"+tickerId+", Field: "+field+", Price: "+price+", CanAutoExecute: "+canAutoExecute);
		TickPriceResponse response = new TickPriceResponse(tickerId, field, price, canAutoExecute);
		FlowableEmitter<TickPriceResponse> emitter = flowableEmitterMap.get(tickerId);
		if (emitter != null) {
			emitter.onNext(response);
			return;
		}
		log.warn("tickPrice, emitter not registered. tickerId:{}", tickerId);
	}
	//! [tickprice]
	
	//! [ticksize]
	@Override
	public void tickSize(int tickerId, int field, int size) {
		System.out.println("Tick Size. Ticker Id:" + tickerId + ", Field: " + field + ", Size: " + size);
		TickSizeResponse response = new TickSizeResponse(tickerId, field, size);
		FlowableEmitter<TickSizeResponse> emitter = flowableEmitterMap.get(tickerId);
		if (emitter != null) {
			emitter.onNext(response);
			return;
		}
		log.warn("tickSize, emitter not registered. tickerId:{}", tickerId);
	}
	//! [ticksize]
	
	//! [tickoptioncomputation]
	@Override
	public void tickOptionComputation(int tickerId, int field,
			double impliedVol, double delta, double optPrice,
			double pvDividend, double gamma, double vega, double theta,
			double undPrice) {
		System.out.println("TickOptionComputation. TickerId: "+tickerId+", field: "+field+", ImpliedVolatility: "+impliedVol+", Delta: "+delta
                +", OptionPrice: "+optPrice+", pvDividend: "+pvDividend+", Gamma: "+gamma+", Vega: "+vega+", Theta: "+theta+", UnderlyingPrice: "+undPrice);
	}
	//! [tickoptioncomputation]
	
	//! [tickgeneric]
	@Override
	public void tickGeneric(int tickerId, int tickType, double value) {
		System.out.println("Tick Generic. Ticker Id:" + tickerId + ", Field: " + TickType.getField(tickType) + ", Value: " + value);
	}
	//! [tickgeneric]
	
	//! [tickstring]
	@Override
	public void tickString(int tickerId, int tickType, String value) {
		System.out.println("Tick string. Ticker Id:" + tickerId + ", Type: " + tickType + ", Value: " + value);
	}
	//! [tickstring]
	@Override
	public void tickEFP(int tickerId, int tickType, double basisPoints,
			String formattedBasisPoints, double impliedFuture, int holdDays,
			String futureLastTradeDate, double dividendImpact,
			double dividendsToLastTradeDate) {
		System.out.println("TickEFP. "+tickerId+", Type: "+tickType+", BasisPoints: "+basisPoints+", FormattedBasisPoints: "+
			formattedBasisPoints+", ImpliedFuture: "+impliedFuture+", HoldDays: "+holdDays+", FutureLastTradeDate: "+futureLastTradeDate+
			", DividendImpact: "+dividendImpact+", DividendsToLastTradeDate: "+dividendsToLastTradeDate);
	}
	//! [orderstatus]
	@Override
	public void orderStatus(int orderId, String status, double filled,
			double remaining, double avgFillPrice, int permId, int parentId,
			double lastFillPrice, int clientId, String whyHeld) {
		System.out.println("OrderStatus. Id: "+orderId+", Status: "+status+", Filled"+filled+", Remaining: "+remaining
                +", AvgFillPrice: "+avgFillPrice+", PermId: "+permId+", ParentId: "+parentId+", LastFillPrice: "+lastFillPrice+
                ", ClientId: "+clientId+", WhyHeld: "+whyHeld);
		OrderStatusResponse response = new OrderStatusResponse(orderId, status, filled, remaining, avgFillPrice, permId, parentId,
				lastFillPrice, clientId, whyHeld);

		CompletableFuture<OrderStatusResponse> future = futureMap.get(orderId);
		if (future == null) {
			log.warn("orderStatus, orderId:{} not found.", orderId);
		}
		if (future != null) {
			future.complete(response);
			futureMap.remove(orderId);
		}
	}
	//! [orderstatus]
	
	//! [openorder]
	@Override
	public void openOrder(int orderId, Contract contract, Order order,
			OrderState orderState) {
		log.info("OpenOrder. ID: "+orderId+", "+contract.symbol()+", "+contract.secType()+" @ "+contract.exchange()+": "+
			order.action()+", "+order.orderType()+" "+order.totalQuantity()+", "+orderState.status());
		OpenOrderResponse response = new OpenOrderResponse(orderId, contract, order, orderState);
		FlowableEmitter<OpenOrderResponse> emitter = flowableEmitterMap.get(KEY_REQ_ALL_OPEN_ORDERS);
		if (emitter != null) {
			emitter.onNext(response);
			return;
		}
		log.warn("openOrder, no registered listener. orderId:{}", orderId);
	}
	//! [openorder]
	
	//! [openorderend]
	@Override
	public void openOrderEnd() {
		System.out.println("OpenOrderEnd");
		FlowableEmitter<OpenOrderResponse> emitter = flowableEmitterMap.get(KEY_REQ_ALL_OPEN_ORDERS);
		if (emitter != null) {
			emitter.onComplete();
			flowableEmitterMap.remove(KEY_REQ_ALL_OPEN_ORDERS);
			log.info("openOrderEnd, KEY_REQ_ALL_OPEN_ORDERS removed.");
		}
	}

	//! [openorderend]
	
	//! [updateaccountvalue]
	@Override
	public void updateAccountValue(String key, String value, String currency,
			String accountName) {
		System.out.println("UpdateAccountValue. Key: " + key + ", Value: " + value + ", Currency: " + currency + ", AccountName: " + accountName);
	}
	//! [updateaccountvalue]
	
	//! [updateportfolio]
	@Override
	public void updatePortfolio(Contract contract, double position,
			double marketPrice, double marketValue, double averageCost,
			double unrealizedPNL, double realizedPNL, String accountName) {
		System.out.println("UpdatePortfolio. "+contract.symbol()+", "+contract.secType()+" @ "+contract.exchange()
                +": Position: "+position+", MarketPrice: "+marketPrice+", MarketValue: "+marketValue+", AverageCost: "+averageCost
                +", UnrealisedPNL: "+unrealizedPNL+", RealisedPNL: "+realizedPNL+", AccountName: "+accountName);
	}
	//! [updateportfolio]
	
	//! [updateaccounttime]
	@Override
	public void updateAccountTime(String timeStamp) {
		System.out.println("UpdateAccountTime. Time: " + timeStamp+"\n");
	}
	//! [updateaccounttime]
	
	//! [accountdownloadend]
	@Override
	public void accountDownloadEnd(String accountName) {
		System.out.println("Account download finished: "+accountName+"\n");
	}
	//! [accountdownloadend]
	
	//! [nextvalidid]
	@Override
	public void nextValidId(int orderId) {
		log.info("Next Valid Id: [{}]", orderId);
		CompletableFuture<Integer> future = futureMap.get(KEY_REQID);
		if (future != null) {
			future.complete(orderId);
		}
		currentOrderId = orderId;
	}
	//! [nextvalidid]
	
	//! [contractdetails]
	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) {
		System.out.println("ContractDetails. ReqId: ["+reqId+"] - ["+contractDetails.contract().symbol()+"], ["+contractDetails.contract().secType()+"], ConId: ["+contractDetails.contract().conid()+"] @ ["+contractDetails.contract().exchange()+"]");
	}
	//! [contractdetails]
	@Override
	public void bondContractDetails(int reqId, ContractDetails contractDetails) {
		System.out.println("bondContractDetails");
	}
	//! [contractdetailsend]
	@Override
	public void contractDetailsEnd(int reqId) {
		System.out.println("ContractDetailsEnd. "+reqId+"\n");
	}
	//! [contractdetailsend]
	
	//! [execdetails]
	@Override
	public void execDetails(int reqId, Contract contract, Execution execution) {
		System.out.println("ExecDetails. "+reqId+" - ["+contract.symbol()+"], ["+contract.secType()+"], ["+contract.currency()+"], ["+execution.execId()+"], ["+execution.orderId()+"], ["+execution.shares()+"]");
		ExecDetailsResponse response = new ExecDetailsResponse(reqId, contract, execution);
		FlowableEmitter<ExecDetailsResponse> emitter = flowableEmitterMap.get(reqId);
		if (emitter != null) {
			emitter.onNext(response);
			return;
		}
		log.warn("execDetails, no registered listener. reqId:{}", reqId);
	}
	//! [execdetails]
	
	//! [execdetailsend]
	@Override
	public void execDetailsEnd(int reqId) {
		System.out.println("ExecDetailsEnd. "+reqId+"\n");
		FlowableEmitter<ExecDetailsResponse> emitter = flowableEmitterMap.get(reqId);
		if (emitter != null) {
			emitter.onComplete();
			flowableEmitterMap.remove(reqId);
			log.info("execDetailsEnd, {} removed.", reqId);
		}
	}
	//! [execdetailsend]
	
	//! [updatemktdepth]
	@Override
	public void updateMktDepth(int tickerId, int position, int operation,
			int side, double price, int size) {
		System.out.println("UpdateMarketDepth. "+tickerId+" - Position: "+position+", Operation: "+operation+", Side: "+side+", Price: "+price+", Size: "+size+"");
	}
	//! [updatemktdepth]
	@Override
	public void updateMktDepthL2(int tickerId, int position,
			String marketMaker, int operation, int side, double price, int size) {
		System.out.println("updateMktDepthL2");
	}
	//! [updatenewsbulletin]
	@Override
	public void updateNewsBulletin(int msgId, int msgType, String message,
			String origExchange) {
		System.out.println("News Bulletins. "+msgId+" - Type: "+msgType+", Message: "+message+", Exchange of Origin: "+origExchange+"\n");
	}
	//! [updatenewsbulletin]
	
	//! [managedaccounts]
	@Override
	public void managedAccounts(String accountsList) {
		System.out.println("Account list: " +accountsList);
	}
	//! [managedaccounts]

	//! [receivefa]
	@Override
	public void receiveFA(int faDataType, String xml) {
		System.out.println("Receing FA: "+faDataType+" - "+xml);
	}
	//! [receivefa]
	
	@Override
	public void historicalData(int reqId, String date, double open,
			double high, double low, double close, int volume, int count,
			double WAP, boolean hasGaps) {
		System.out.println("HistoricalData. "+reqId+" - Date: "+date+", Open: "+open+", High: "+high+", Low: "+low+", Close: "+close+", Volume: "+volume+", Count: "+count+", WAP: "+WAP+", HasGaps: "+hasGaps);
		HistoricalDataResponse response = new HistoricalDataResponse(reqId, date, open, high, low, close, volume, count, WAP, hasGaps);
		FlowableEmitter<HistoricalDataResponse> emitter = flowableEmitterMap.get(reqId);
		if (emitter == null) {
			log.warn("historicalData, emitter not registered. tickerId:{}", reqId);
			return;
		}
		emitter.onNext(response);
		if (date.startsWith("finished-")) {
			emitter.onComplete();
			flowableEmitterMap.remove(reqId);
			log.info("historicalData finished, {} removed.", reqId);
		}
	}

	//! [scannerparameters]
	@Override
	public void scannerParameters(String xml) {
		System.out.println("ScannerParameters. "+xml+"\n");
	}
	//! [scannerparameters]
	
	//! [scannerdata]
	@Override
	public void scannerData(int reqId, int rank,
			ContractDetails contractDetails, String distance, String benchmark,
			String projection, String legsStr) {
		System.out.println("ScannerData. "+reqId+" - Rank: "+rank+", Symbol: "+contractDetails.contract().symbol()+", SecType: "+contractDetails.contract().secType()+", Currency: "+contractDetails.contract().currency()
                +", Distance: "+distance+", Benchmark: "+benchmark+", Projection: "+projection+", Legs String: "+legsStr);
	}
	//! [scannerdata]
	
	//! [scannerdataend]
	@Override
	public void scannerDataEnd(int reqId) {
		System.out.println("ScannerDataEnd. "+reqId);
	}
	//! [scannerdataend]
	
	//! [realtimebar]
	@Override
	public void realtimeBar(int reqId, long time, double open, double high,
			double low, double close, long volume, double wap, int count) {
		System.out.println("RealTimeBars. " + reqId + " - Time: " + time + ", Open: " + open + ", High: " + high + ", Low: " + low + ", Close: " + close + ", Volume: " + volume + ", Count: " + count + ", WAP: " + wap);
	}
	//! [realtimebar]
	@Override
	public void currentTime(long time) {
		System.out.println("currentTime");
	}
	//! [fundamentaldata]
	@Override
	public void fundamentalData(int reqId, String data) {
		System.out.println("FundamentalData. ReqId: ["+reqId+"] - Data: ["+data+"]");
	}
	//! [fundamentaldata]
	@Override
	public void deltaNeutralValidation(int reqId, DeltaNeutralContract underComp) {
		System.out.println("deltaNeutralValidation");
	}
	//! [ticksnapshotend]
	@Override
	public void tickSnapshotEnd(int reqId) {
		System.out.println("TickSnapshotEnd: "+reqId);
	}
	//! [ticksnapshotend]
	
	//! [marketdatatype]
	@Override
	public void marketDataType(int reqId, int marketDataType) {
		System.out.println("MarketDataType. ["+reqId+"], Type: ["+marketDataType+"]\n");
	}
	//! [marketdatatype]
	
	//! [commissionreport]
	@Override
	public void commissionReport(CommissionReport commissionReport) {
		System.out.println("CommissionReport. ["+commissionReport.m_execId+"] - ["+commissionReport.m_commission+"] ["+commissionReport.m_currency+"] RPNL ["+commissionReport.m_realizedPNL+"]");
	}
	//! [commissionreport]
	
	//! [position]
	@Override
	public void position(String account, Contract contract, double pos,
			double avgCost) {
		System.out.println("Position. "+account+" - Symbol: "+contract.symbol()+", SecType: "+contract.secType()+", Currency: "+contract.currency()+", Position: "+pos+", Avg cost: "+avgCost);
	}
	//! [position]
	
	//! [positionend]
	@Override
	public void positionEnd() {
		System.out.println("PositionEnd \n");
	}
	//! [positionend]
	
	//! [accountsummary]
	@Override
	public void accountSummary(int reqId, String account, String tag,
			String value, String currency) {
		System.out.println("Acct Summary. ReqId: " + reqId + ", Acct: " + account + ", Tag: " + tag + ", Value: " + value + ", Currency: " + currency);
	}
	//! [accountsummary]
	
	//! [accountsummaryend]
	@Override
	public void accountSummaryEnd(int reqId) {
		System.out.println("AccountSummaryEnd. Req Id: "+reqId+"\n");
	}
	//! [accountsummaryend]
	@Override
	public void verifyMessageAPI(String apiData) {
		System.out.println("verifyMessageAPI");
	}

	@Override
	public void verifyCompleted(boolean isSuccessful, String errorText) {
		System.out.println("verifyCompleted");
	}

	@Override
	public void verifyAndAuthMessageAPI(String apiData, String xyzChallange) {
		System.out.println("verifyAndAuthMessageAPI");
	}

	@Override
	public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
		System.out.println("verifyAndAuthCompleted");
	}
	//! [displaygrouplist]
	@Override
	public void displayGroupList(int reqId, String groups) {
		System.out.println("Display Group List. ReqId: "+reqId+", Groups: "+groups+"\n");
	}
	//! [displaygrouplist]
	
	//! [displaygroupupdated]
	@Override
	public void displayGroupUpdated(int reqId, String contractInfo) {
		System.out.println("Display Group Updated. ReqId: "+reqId+", Contract info: "+contractInfo+"\n");
	}
	//! [displaygroupupdated]
	@Override
	public void error(Exception e) {
		System.out.println("Exception: "+e.getMessage());
	}

	@Override
	public void error(String str) {
		System.out.println("Error STR");
	}
	//! [error]
	@Override
	public void error(int id, int errorCode, String errorMsg) {
		System.out.println("Error. Id: " + id + ", Code: " + errorCode + ", Msg: " + errorMsg + "\n");
		FlowableEmitter emitter = flowableEmitterMap.remove(id);
		if (emitter != null) {
			emitter.onError(new RuntimeException(String.format("code:%d, msg:%s.", errorCode, errorMsg)));
			log.warn("flowableEmitterMap key:{} removed.", id);
		}

		CompletableFuture future = futureMap.remove(id);
		if (future != null) {
			future.completeExceptionally(new RuntimeException(String.format("code:%d, msg:%s.", errorCode, errorMsg)));
			log.warn("futureMap key:{} removed.", id);
		}
	}
	//! [error]
	@Override
	public void connectionClosed() {
		System.out.println("Connection closed");
	}

	//! [connectack]
	@Override
	public void connectAck() {
		if (clientSocket.isAsyncEConnect()) {
			System.out.println("Acknowledging connection");
			clientSocket.startAPI();
		}
	}
	//! [connectack]
	
	//! [positionmulti]
	@Override
	public void positionMulti(int reqId, String account, String modelCode,
			Contract contract, double pos, double avgCost) {
		System.out.println("Position Multi. Request: " + reqId + ", Account: " + account + ", ModelCode: " + modelCode + ", Symbol: " + contract.symbol() + ", SecType: " + contract.secType() + ", Currency: " + contract.currency() + ", Position: " + pos + ", Avg cost: " + avgCost + "\n");
	}
	//! [positionmulti]
	
	//! [positionmultiend]
	@Override
	public void positionMultiEnd(int reqId) {
		System.out.println("Position Multi End. Request: " + reqId + "\n");
	}
	//! [positionmultiend]
	
	//! [accountupdatemulti]
	@Override
	public void accountUpdateMulti(int reqId, String account, String modelCode,
			String key, String value, String currency) {
		System.out.println("Account Update Multi. Request: " + reqId + ", Account: " + account + ", ModelCode: " + modelCode + ", Key: " + key + ", Value: " + value + ", Currency: " + currency + "\n");
	}
	//! [accountupdatemulti]
	
	//! [accountupdatemultiend]
	@Override
	public void accountUpdateMultiEnd(int reqId) {
		System.out.println("Account Update Multi End. Request: " + reqId + "\n");
	}
	//! [accountupdatemultiend]
	
	//! [securityDefinitionOptionParameter]
	@Override
	public void securityDefinitionOptionalParameter(int reqId, String exchange,
			int underlyingConId, String tradingClass, String multiplier,
			Set<String> expirations, Set<Double> strikes) {
		System.out.println("Security Definition Optional Parameter. Request: "+reqId+", Trading Class: "+tradingClass+", Multiplier: "+multiplier+" \n");
	}
	//! [securityDefinitionOptionParameter]
	@Override
	public void securityDefinitionOptionalParameterEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {
		for (SoftDollarTier tier : tiers) {
			System.out.print("tier: " + tier + ", ");
		}
		
		System.out.println();
	}

}
