package com.scy.rx.model;

import lombok.Data;

@Data
public class OrderStatusResponse {
    private int orderId;
    private String status;
    private double filled;
    private double remaining;
    private double avgFillPrice;
    private int permId;
    private int parentId;
    private double lastFillPrice;
    private int clientId;
    private String whyHeld;
}
