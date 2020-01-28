package com.scy.rx.service.impl;


import com.scy.rx.client.EConnClient;
import com.scy.rx.model.PositionsMultiRequest;
import com.scy.rx.model.PositionsMultiResponse;
import com.scy.rx.service.AccountApi;
import com.scy.rx.wrapper.FlowableEmitterMap;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountApiImpl implements AccountApi {

    @Autowired
    private FlowableEmitterMap flowableEmitterMap;

    @Autowired
    private EConnClient eConnClient;

    @Override
    public Flowable<PositionsMultiResponse> reqPositionsMulti(PositionsMultiRequest request) {
        if (FlowableEmitterMap.lock.tryLock()) {
            try {
                if (flowableEmitterMap.get(request.getRequestId()) != null) {
                    throw new RuntimeException("reqPositionsMulti is not available.");
                }
                return Flowable.<PositionsMultiResponse>create(emitter -> {
                            flowableEmitterMap.put(request.getRequestId(), emitter);
                            eConnClient.getClientSocket().reqPositionsMulti(request.getRequestId(), request.getAccount(), request.getModelCode());
                        },
                        BackpressureStrategy.BUFFER).cache();
            } finally {
                FlowableEmitterMap.lock.unlock();
            }
        }
        throw new RuntimeException("try lock failed.");
    }
}
