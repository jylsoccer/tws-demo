package com.scy.rx.model;

import lombok.Data;

@Data
public class AccountSummaryResponse {
    private int reqId;
    private String account;
    private String tag;
    private String value;
    private String currency;

    public AccountSummaryResponse() {
    }

    public AccountSummaryResponse(int reqId, String account, String tag, String value, String currency) {
        this.reqId = reqId;
        this.account = account;
        this.tag = tag;
        this.value = value;
        this.currency = currency;
    }
}
