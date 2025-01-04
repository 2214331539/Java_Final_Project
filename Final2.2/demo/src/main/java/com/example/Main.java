// package com.example;

// import java.util.concurrent.*;

// public class Main {
//     public static void main(String[] args) {
//         // 创建仓库对象，高优先级队列容量10，普通优先级队列容量20
//         Warehouse warehouse = new Warehouse(10, 20);

//         // 创建线程池，最多10个线程
//         ExecutorService executor = Executors.newFixedThreadPool(10);

//         // 启动多个卡车线程
//         for (int i = 0; i < 5; i++) {
//             executor.submit(new ArrivalTruck(warehouse));
//             executor.submit(new DepartureTruck(warehouse));
//         }

//         // 启动包裹优先级升级任务
//         executor.submit(new Task(warehouse));

//         // 运行60秒
//         try {
//             Thread.sleep(60000); // 模拟系统运行一段时间
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         } finally {
//             executor.shutdown(); // 关闭线程池
//         }
//     }
// }
