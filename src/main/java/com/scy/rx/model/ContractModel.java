package com.scy.rx.model;

import com.ib.client.Contract;
import lombok.Data;

@Data
public class ContractModel {
    private String symbol;
    private String secType;
    private String lastTradeDateOrContractMonth;
    private String exchange;
    private String primaryExch;
    private String currency;
    private double strike;
    private String right;
    private String multiplier;

    public Contract toContract() {
        Contract contract = new Contract();
        contract.symbol(symbol);
        contract.secType(secType);
        contract.lastTradeDateOrContractMonth(lastTradeDateOrContractMonth);
        contract.exchange(exchange);
        contract.primaryExch(primaryExch);
        contract.currency(currency);
        contract.strike(strike);
        contract.right(right);
        contract.multiplier(multiplier);
        return contract;
    }
}
