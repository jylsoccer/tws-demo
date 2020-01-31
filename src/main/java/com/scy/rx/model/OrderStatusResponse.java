package com.scy.rx.model;

import lombok.Data;

@Data
public class OrderStatusResponse extends OrderResponse {
    private String status;
    private double filled;
    private double remaining;
    private double avgFillPrice;
    private int permId;
    private int parentId;
    private double lastFillPrice;
    private int clientId;
    private String whyHeld;

    public OrderStatusResponse() {
    }

    public OrderStatusResponse(int orderId, String status, double filled, double remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
        this.orderId = orderId;
        this.status = status;
        this.filled = filled;
        this.remaining = remaining;
        this.avgFillPrice = avgFillPrice;
        this.permId = permId;
        this.parentId = parentId;
        this.lastFillPrice = lastFillPrice;
        this.clientId = clientId;
        this.whyHeld = whyHeld;
    }
}
