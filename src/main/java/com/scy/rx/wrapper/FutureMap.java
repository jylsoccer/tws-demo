package com.scy.rx.wrapper;

import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class FutureMap {

    public static final Integer KEY_REQID = -1;

    public static final ReentrantLock lock = new ReentrantLock();

    private LinkedHashMap<Integer, CompletableFuture> supplierMap = new LinkedHashMap<Integer, CompletableFuture>() {
        private static final long serialVersionUID = 1L;
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, CompletableFuture> eldest) {
            return size() > 100;
        }
    };

    public CompletableFuture put(Integer key, CompletableFuture supplier) {
        return supplierMap.put(key, supplier);
    }

    public CompletableFuture get(Integer key) {
        return supplierMap.get(key);
    }

    public CompletableFuture remove(Integer key) {
        return supplierMap.remove(key);
    }

}
