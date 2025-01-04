package main.java.com.example;
import java.util.concurrent.*;
import java.util.Iterator;
import com.example.Package;
import com.example.Warehouse;

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
                    if (currentTime - p.getLastUpdateTime() > 60000) {  
                        p.setPriority("High");
                        warehouse.storePackage(p);
                        iterator.remove();
                        warehouse.logOperation("高优先包裹 " + p + " 已转移至高优先队列");
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
