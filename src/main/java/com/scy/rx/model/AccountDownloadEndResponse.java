package com.scy.rx.model;

import lombok.Data;

@Data
public class AccountDownloadEndResponse extends AccountUpdatesResponse {
    private String account;

    public AccountDownloadEndResponse() {
    }

    public AccountDownloadEndResponse(String account) {
        this.account = account;
    }
}
