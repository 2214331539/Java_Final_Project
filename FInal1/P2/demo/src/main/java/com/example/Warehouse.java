package main.java.com.example;
import java.util.concurrent.*;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.locks.*;
import java.io.*;

public class Warehouse {
    private final BlockingQueue<Package> highPriorityQueue;
    private final BlockingQueue<Package> regularPriorityQueue;
    private final ReentrantLock lock;
    private final Condition notEmpty;

    public Warehouse(int highPriorityCapacity, int regularPriorityCapacity) {
        this.highPriorityQueue = new LinkedBlockingQueue<>(highPriorityCapacity);
        this.regularPriorityQueue = new LinkedBlockingQueue<>(regularPriorityCapacity);
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
    }


    public void storePackage(Package p) throws InterruptedException {
        lock.lock();
        try {
            if (p.getPriority().equals("High")) {
                highPriorityQueue.put(p);
                logOperation("高优先级包入库: " + p);
            } else {
                regularPriorityQueue.put(p);
                logOperation("普通优先包入库: " + p);
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

    // 文件写入
    void logOperation(String operation) {
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

