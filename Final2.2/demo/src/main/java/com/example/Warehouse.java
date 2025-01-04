package com.example;

import java.util.concurrent.*;
import java.util.*;
import java.io.*;

public class Warehouse {
    private final BlockingQueue<Package> highPriorityQueue;
    private final BlockingQueue<Package> regularPriorityQueue;
    private final List<String> operationLogs;

    public Warehouse(int highPriorityCapacity, int regularPriorityCapacity) {
        this.highPriorityQueue = new LinkedBlockingQueue<>(highPriorityCapacity);
        this.regularPriorityQueue = new LinkedBlockingQueue<>(regularPriorityCapacity);
        this.operationLogs = new ArrayList<>();
    }

    // 存储包裹到仓库
    public void storePackage(Package p) throws InterruptedException {
        if (p.getPriority().equals("High")) {
            highPriorityQueue.put(p);
            logOperation("高优先级包裹入库: " + p);
        } else {
            regularPriorityQueue.put(p);
            logOperation("普通优先级包裹入库: " + p);
        }
    }

    // 从仓库检索包裹
    public Package retrievePackage() throws InterruptedException {
        Package p = null;
        if (!highPriorityQueue.isEmpty()) {
            p = highPriorityQueue.take();
            logOperation("检索到高优先级包裹: " + p);
        } else if (!regularPriorityQueue.isEmpty()) {
            p = regularPriorityQueue.take();
            logOperation("检索到普通优先级包裹: " + p);
        }
        return p;
    }

    // 记录操作日志
    public void logOperation(String operation) {
        operationLogs.add(System.currentTimeMillis() + ": " + operation);
    }

    // 获取操作日志
    public List<String> getOperationLogs() {
        return operationLogs;
    }

    // 获取高优先级队列
    public BlockingQueue<Package> getHighPriorityQueue() {
        return highPriorityQueue;
    }

    // 获取普通优先级队列
    public BlockingQueue<Package> getRegularPriorityQueue() {
        return regularPriorityQueue;
    }

    // 获取仓库的当前容量
    public int getCurrentCapacity() {
        return highPriorityQueue.size() + regularPriorityQueue.size();
    }

    // 获取仓库的总容量
    public int getTotalCapacity() {
        return highPriorityQueue.remainingCapacity() + regularPriorityQueue.remainingCapacity();
    }
}
