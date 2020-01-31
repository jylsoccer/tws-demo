package com.scy.rx.model;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import lombok.Data;

@Data
public class OpenOrderResponse extends OrderResponse {
    private Contract contract;
    private Order order;
    private OrderState orderState;

    public OpenOrderResponse() {
    }

    public OpenOrderResponse(int orderId, Contract contract, Order order, OrderState orderState) {
        this.orderId = orderId;
        this.contract = contract;
        this.order = order;
        this.orderState = orderState;
    }
}
