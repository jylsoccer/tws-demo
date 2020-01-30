package com.scy.rx.model;

import com.ib.client.Contract;
import lombok.Data;

@Data
public class PositionsResponse {
    private String account;
    private Contract contract;
    private double pos;
    private double avgCost;

    public PositionsResponse() {
    }

    public PositionsResponse(String account, Contract contract, double pos, double avgCost) {
        this.account = account;
        this.contract = contract;
        this.pos = pos;
        this.avgCost = avgCost;
    }
}
