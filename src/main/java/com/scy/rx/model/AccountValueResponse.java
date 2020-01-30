package com.scy.rx.model;

import lombok.Data;

@Data
public class AccountValueResponse extends AccountUpdatesResponse {
    private String tag;
    private String value;
    private String currency;
    private String account;

    public AccountValueResponse() {
    }

    public AccountValueResponse(String tag, String value, String currency, String account) {
        this.tag = tag;
        this.value = value;
        this.currency = currency;
        this.account = account;
    }
}
