package com.example;

import java.util.concurrent.*;
import java.util.Iterator;

public class Task implements Runnable {
    private final Warehouse warehouse;

    public Task(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        while (true) {
            try {
                long currentTime = System.currentTimeMillis();
                BlockingQueue<Package> regularQueue = warehouse.getRegularPriorityQueue();
                Iterator<Package> iterator = regularQueue.iterator();
                while (iterator.hasNext()) {
                    Package p = iterator.next();
                    // 如果包裹超过10分钟未被处理，提升为高优先级
                    if (currentTime - p.getLastUpdateTime() > 600000) {
                        p.setPriority("High");
                        warehouse.storePackage(p);
                        iterator.remove();
                        warehouse.logOperation("高优先包裹 " + p + " 已转移至高优先队列");
                    }
                }
                Thread.sleep(60000);  // 每60秒检查一次
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
