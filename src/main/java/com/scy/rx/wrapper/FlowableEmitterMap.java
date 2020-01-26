package com.scy.rx.wrapper;

import io.reactivex.FlowableEmitter;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class FlowableEmitterMap {
    private LinkedHashMap<Integer, FlowableEmitter> flowableEmitterMap = new LinkedHashMap<Integer, FlowableEmitter>() {
        private static final long serialVersionUID = 1L;
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, FlowableEmitter> eldest) {
            return size() > 100;
        }
    };

    public FlowableEmitter put(Integer key, FlowableEmitter emitter) {
        return flowableEmitterMap.put(key, emitter);
    }

    public FlowableEmitter get(Integer key) {
        return flowableEmitterMap.get(key);
    }

    public FlowableEmitter remove(Integer key) {
        return flowableEmitterMap.remove(key);
    }

}
