package com.scy.rx.wrapper;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class FutureMap {

    public static FutureMap INSTANCE = new FutureMap();

    public static final Integer KEY_REQID = -1;
    public static final Integer KEY_MANAGED_ACCOUNTS = -2;

    private static final ReentrantLock lock = new ReentrantLock();


    public static boolean tryLock() {
        return tryLock(1000);
    }

    private static boolean tryLock(long timeout) {
        try {
            return lock.tryLock(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("FutureMap.tryLock failed.", e);
            return false;
        }
    }

    public static void unlock() {
        lock.unlock();
    }

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
