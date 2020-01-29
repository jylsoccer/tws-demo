package com.scy.rx.model;

import com.ib.client.ExecutionFilter;
import lombok.Data;

@Data
public class ExecDetailsRequest {
    private ExecutionFilter filter;

    public ExecDetailsRequest() {
    }

    public ExecDetailsRequest(ExecutionFilter filter) {
        this.filter = filter;
    }
}
