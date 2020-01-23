package com.scy.rx.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    public static final String NAME_THREAD_PREFIX = "scy-";

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private String namePrefix;
    private int priority;

    public NamedThreadFactory(String name) {
        this(name, 0);
    }

    public NamedThreadFactory(String name, int priority) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = NAME_THREAD_PREFIX + poolNumber.getAndIncrement() + "-" + name + "-thread-";
        this.priority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (priority < Thread.MIN_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        } else {
            t.setPriority(priority);
        }

        return t;
    }
}
