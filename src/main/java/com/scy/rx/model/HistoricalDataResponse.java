package com.scy.rx.model;

import lombok.Data;

@Data
public class HistoricalDataResponse {
    private int reqId;
    private String date;
    private double open;
    private double high;
    private double low;
    private double close;
    private int volume;
    private int count;
    private double wap;
    private boolean hasGaps;

    public HistoricalDataResponse(int reqId, String date, double open, double high, double low, double close, int volume, int count, double wap, boolean hasGaps) {
        this.reqId = reqId;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.count = count;
        this.wap = wap;
        this.hasGaps = hasGaps;
    }
}
