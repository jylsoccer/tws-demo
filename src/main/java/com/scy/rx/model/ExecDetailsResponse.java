package com.scy.rx.model;

import com.ib.client.Contract;
import com.ib.client.Execution;
import lombok.Data;

@Data
public class ExecDetailsResponse {
    private int reqId;
    private Contract contract;
    private Execution execution;

    public ExecDetailsResponse() {
    }

    public ExecDetailsResponse(int reqId, Contract contract, Execution execution) {
        this.reqId = reqId;
        this.contract = contract;
        this.execution = execution;
    }
}
