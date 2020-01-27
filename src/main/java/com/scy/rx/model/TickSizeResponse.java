package com.scy.rx.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TickSizeResponse extends TickResponse {
    private int field;
    private int size;

    public TickSizeResponse() {
    }

    public TickSizeResponse(int tickerId, int field, int size) {
        this.tickerId = tickerId;
        this.field = field;
        this.size = size;
    }
}
