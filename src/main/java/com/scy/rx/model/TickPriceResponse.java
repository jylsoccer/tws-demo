package com.scy.rx.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TickPriceResponse extends TickResponse {
    private int field;
    private double price;
    private int canAutoExecute;

    public TickPriceResponse() {
    }

    public TickPriceResponse(int tickerId, int field, double price, int canAutoExecute) {
        this.tickerId = tickerId;
        this.field = field;
        this.price = price;
        this.canAutoExecute = canAutoExecute;
    }
}
