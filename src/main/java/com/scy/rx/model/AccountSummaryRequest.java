package com.scy.rx.model;

import lombok.Data;

@Data
public class AccountSummaryRequest {
    private String group;
    private String tags;

    public AccountSummaryRequest() {
    }

    public AccountSummaryRequest(String group, String tags) {
        this.group = group;
        this.tags = tags;
    }
}
