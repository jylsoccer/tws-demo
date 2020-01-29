package com.scy.rx.model;

import com.ib.client.Contract;
import com.ib.client.TagValue;
import lombok.Data;

import java.util.List;

@Data
public class HistoricalDataRequest {
    private Contract contract;
    private String endDateTime;
    private String durationString;
    private String barSizeSetting;
    private String whatToShow;
    private int useRTH;
    private int formatDate;
    private List<TagValue> chartOptions;

    public HistoricalDataRequest() {
    }

    public HistoricalDataRequest(Contract contract, String endDateTime, String durationString, String barSizeSetting, String whatToShow, int useRTH, int formatDate, List<TagValue> chartOptions) {
        this.contract = contract;
        this.endDateTime = endDateTime;
        this.durationString = durationString;
        this.barSizeSetting = barSizeSetting;
        this.whatToShow = whatToShow;
        this.useRTH = useRTH;
        this.formatDate = formatDate;
        this.chartOptions = chartOptions;
    }
}
