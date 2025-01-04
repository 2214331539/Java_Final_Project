package com.example;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse(10, 20);    //优先级10 普通级20
        
        // 线程池
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 启动卡车线程
        for (int i = 0; i < 5; i++) {
            executor.submit(new ArrivalTruck(warehouse));  // 启动到货卡车线程
            executor.submit(new DepartureTruck(warehouse));  // 启动发货卡车线程
        }
        // 优先级线程
        executor.submit(new Task(warehouse));

        try {
            Thread.sleep(60000);  // 主线程睡眠60秒，来观察线程结果
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
