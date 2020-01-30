package com.scy.rx.model;

import lombok.Data;

@Data
public class AccountUpdatesRequest {
    private boolean subscribe;
    private String acctCode;

    public AccountUpdatesRequest(boolean subscribe, String acctCode) {
        this.subscribe = subscribe;
        this.acctCode = acctCode;
    }
}
