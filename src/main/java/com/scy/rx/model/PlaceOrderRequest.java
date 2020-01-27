package com.scy.rx.model;

import com.ib.client.Contract;
import com.ib.client.Order;
import lombok.Data;

@Data
public class PlaceOrderRequest {
    private int reqId;
    private Contract contract;
    private Order order;

    public PlaceOrderRequest() {
    }

    public PlaceOrderRequest(int reqId, Contract contract, Order order) {
        this.reqId = reqId;
        this.contract = contract;
        this.order = order;
    }
}
