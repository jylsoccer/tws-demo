package com.scy.rx.model;

import lombok.Data;

@Data
public class PositionsMultiRequest {
    private String account;
    private String modelCode;

    public PositionsMultiRequest() {
    }

    public PositionsMultiRequest(String account, String modelCode) {
        this.account = account;
        this.modelCode = modelCode;
    }
}
