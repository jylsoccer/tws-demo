package com.scy.rx.model;

import lombok.Data;

@Data
public class PositionsMultiRequest {
    private int requestId;
    private String account;
    private String modelCode;

    public PositionsMultiRequest() {
    }

    public PositionsMultiRequest(int requestId, String account, String modelCode) {
        this.requestId = requestId;
        this.account = account;
        this.modelCode = modelCode;
    }
}
