package com.scy.rx.model;

import lombok.Data;

@Data
public class AccountSummaryRequest {
    private int reqId;
    private String group;
    private String tags;

    public AccountSummaryRequest() {
    }

    public AccountSummaryRequest(int reqId, String group, String tags) {
        this.reqId = reqId;
        this.group = group;
        this.tags = tags;
    }
}
