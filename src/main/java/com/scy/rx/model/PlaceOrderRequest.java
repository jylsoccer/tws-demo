package com.scy.rx.model;

import com.ib.client.Contract;
import com.ib.client.Order;
import lombok.Data;

@Data
public class PlaceOrderRequest {
    private Contract contract;
    private Order order;

    public PlaceOrderRequest() {
    }

    public PlaceOrderRequest(Contract contract, Order order) {
        this.contract = contract;
        this.order = order;
    }
}
