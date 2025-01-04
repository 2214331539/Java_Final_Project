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
                    if (currentTime - p.getLastUpdateTime() > 60000) {  // 超过60秒
                        p.setPriority("High");
                        warehouse.storePackage(p);  // 存入高优先级队列
                        iterator.remove();  // 移除原来普通优先级队列的包裹
                        warehouse.logOperation("高优先级包裹 " + p + " 已转移至高优先队列");
                    }
                }

                Thread.sleep(1000);  // 每秒检查一次
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
