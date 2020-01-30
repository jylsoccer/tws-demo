package com.scy.rx.model;

import com.ib.client.Contract;
import lombok.Data;

@Data
public class PortfolioResponse extends AccountUpdatesResponse {
    private Contract contract;
    private double positionIn;
    private double marketPrice;
    private double marketValue;
    private double averageCost;
    private double unrealizedPNL;
    private double realizedPNL;
    private String account;

    public PortfolioResponse() {
    }

    public PortfolioResponse(Contract contract, double positionIn, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String account) {
        this.contract = contract;
        this.positionIn = positionIn;
        this.marketPrice = marketPrice;
        this.marketValue = marketValue;
        this.averageCost = averageCost;
        this.unrealizedPNL = unrealizedPNL;
        this.realizedPNL = realizedPNL;
        this.account = account;
    }

}
