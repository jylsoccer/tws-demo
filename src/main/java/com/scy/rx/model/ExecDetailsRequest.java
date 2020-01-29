package com.scy.rx.model;

import com.ib.client.ExecutionFilter;
import lombok.Data;

@Data
public class ExecDetailsRequest {
    private int reqId;
    private ExecutionFilter filter;

    public ExecDetailsRequest() {
    }

    public ExecDetailsRequest(int reqId, ExecutionFilter filter) {
        this.reqId = reqId;
        this.filter = filter;
    }
}
