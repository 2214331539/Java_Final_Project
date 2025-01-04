package com.example;
import com.example.Package;
import com.example.Warehouse;   
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse(10, 20);  //高优先容量10 普通优先容量20
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 启动卡车线程
        for (int i = 0; i < 5; i++) {
            executor.submit(new ArrivalTruck(warehouse));
            executor.submit(new DepartureTruck(warehouse));
        }

        // 启动包裹线程
        executor.submit(new Task(warehouse));

        //总共运行60秒
        try {
            Thread.sleep(60000);  
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
