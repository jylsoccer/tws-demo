package com.scy.rx.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPools {

    public static ExecutorService createFixedPool(String name, int size) {
        return createFixedPool(name, size, 1024);
    }

    public static ExecutorService createFixedPool(String name, int size, int queueSize) {
        return new ThreadPoolExecutor(size, size, 15, TimeUnit.MINUTES, new ArrayBlockingQueue<>(queueSize), new NamedThreadFactory(name));
    }

}
