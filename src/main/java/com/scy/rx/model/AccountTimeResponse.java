package com.scy.rx.model;

import lombok.Data;

@Data
public class AccountTimeResponse extends AccountUpdatesResponse {
    private String timeStamp;

    public AccountTimeResponse() {
    }

    public AccountTimeResponse(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
