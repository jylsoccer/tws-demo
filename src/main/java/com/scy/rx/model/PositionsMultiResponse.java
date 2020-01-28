package com.scy.rx.model;

import com.ib.client.Contract;
import lombok.Data;

@Data
public class PositionsMultiResponse {
    private int reqId;
    private String account;
    private String modelCode;
    private Contract contract;
    private double pos;
    private double avgCost;

    public PositionsMultiResponse() {
    }

    public PositionsMultiResponse(int reqId, String account, String modelCode, Contract contract, double pos, double avgCost) {
        this.reqId = reqId;
        this.account = account;
        this.modelCode = modelCode;
        this.contract = contract;
        this.pos = pos;
        this.avgCost = avgCost;
    }
}
