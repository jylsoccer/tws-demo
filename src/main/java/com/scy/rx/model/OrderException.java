package com.scy.rx.model;

import lombok.Data;

@Data
public class OrderException extends RuntimeException {
    private int orderId;
    private int errorCode;
    private String errorMsg;

    public OrderException(int orderId, int errorCode, String errorMsg) {
        this.orderId = orderId;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
