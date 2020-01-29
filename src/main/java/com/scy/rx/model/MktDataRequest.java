package com.scy.rx.model;

import com.ib.client.Contract;
import com.ib.client.TagValue;
import lombok.Data;

import java.util.List;

@Data
public class MktDataRequest {
    private int tickerId;
    private Contract contract;
    private String genericTickList;
    private boolean snapshot;
    private List<TagValue> mktDataOptions;

    public MktDataRequest() {
    }

    public MktDataRequest(int tickerId, Contract contract, String genericTickList, boolean snapshot, List<TagValue> mktDataOptions) {
        this.tickerId = tickerId;
        this.contract = contract;
        this.genericTickList = genericTickList;
        this.snapshot = snapshot;
        this.mktDataOptions = mktDataOptions;
    }
}
