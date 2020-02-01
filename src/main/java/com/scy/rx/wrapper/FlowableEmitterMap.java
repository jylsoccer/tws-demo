package com.scy.rx.wrapper;

import com.ib.controller.ConcurrentHashSet;
import com.scy.rx.model.OrderResponse;
import io.reactivex.FlowableEmitter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class FlowableEmitterMap {

    public final static FlowableEmitterMap INSTANCE = new FlowableEmitterMap();

    public static final Integer KEY_REQ_POSITIONS = -2;
    public static final Integer KEY_REQ_ACCOUNT_UPDATES = -3;


    private static final ReentrantLock lock = new ReentrantLock();


    public static boolean tryLock() {
        return tryLock(1000);
    }

    private static boolean tryLock(long timeout) {
        try {
            return lock.tryLock(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("FlowableEmitterMap.tryLock failed.", e);
            return false;
        }
    }

    public static void unlock() {
        lock.unlock();
    }

    private LinkedHashMap<Integer, FlowableEmitter> flowableEmitterMap = new LinkedHashMap<Integer, FlowableEmitter>() {
        private static final long serialVersionUID = 1L;
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, FlowableEmitter> eldest) {
            return size() > 500;
        }
    };
    private final ConcurrentHashSet<FlowableEmitter<OrderResponse>> orderEmitters = new ConcurrentHashSet<>();

    public ConcurrentHashSet<FlowableEmitter<OrderResponse>> getOrderEmitters() {
        return orderEmitters;
    }

    public boolean addOrderEmitters(FlowableEmitter<OrderResponse> emitter) {
        return orderEmitters.add(emitter);
    }

    public boolean removeOrderEmitters(FlowableEmitter<OrderResponse> emitter) {
        return orderEmitters.remove(emitter);
    }

    public void clearOrderEmitters() {
        orderEmitters.clear();
    }

    public FlowableEmitter put(Integer key, FlowableEmitter emitter) {
        log.debug("flowableEmitterMap put:{}", key);
        return flowableEmitterMap.putIfAbsent(key, emitter);
    }

    public FlowableEmitter get(Integer key) {
        return flowableEmitterMap.get(key);
    }

    public FlowableEmitter remove(Integer key) {
        log.debug("flowableEmitterMap remove:{}", key);
        return flowableEmitterMap.remove(key);
    }

}
