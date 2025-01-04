package com.example;

import java.util.concurrent.*;
import java.io.*;
import java.util.concurrent.locks.*;

public class Warehouse {
    private final BlockingQueue<Package> highPriorityQueue;
    private final BlockingQueue<Package> regularPriorityQueue;
    private final ReentrantLock lock;
    private final Condition notEmpty;

    public Warehouse(int highPriorityCapacity, int regularPriorityCapacity) {
        highPriorityQueue = new LinkedBlockingQueue<>(highPriorityCapacity);
        regularPriorityQueue = new LinkedBlockingQueue<>(regularPriorityCapacity);
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
    }

    public void storePackage(Package p) throws InterruptedException {
        lock.lock();
        try {
            if ("High".equals(p.getPriority())) {
                highPriorityQueue.put(p);
                logOperation("Stored high priority package: " + p);
            } else {
                regularPriorityQueue.put(p);
                logOperation("Stored regular priority package: " + p);
            }
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Package retrievePackage() throws InterruptedException {
        lock.lock();
        try {
            while (highPriorityQueue.isEmpty() && regularPriorityQueue.isEmpty()) {
                notEmpty.await();
            }

            Package p = null;
            if (!highPriorityQueue.isEmpty()) {
                p = highPriorityQueue.take();
                logOperation("Retrieved high priority package: " + p);
            } else if (!regularPriorityQueue.isEmpty()) {
                p = regularPriorityQueue.take();
                logOperation("Retrieved regular priority package: " + p);
            }
            return p;
        } finally {
            lock.unlock();
        }
    }

    public void logOperation(String operation) {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream("PackageLog.txt", true))) {
            writer.println(System.currentTimeMillis() + ": " + operation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BlockingQueue<Package> getHighPriorityQueue() {
        return highPriorityQueue;
    }

    public BlockingQueue<Package> getRegularPriorityQueue() {
        return regularPriorityQueue;
    }
}
