package com.example;

class ArrivalTruck implements Runnable {
    private final Warehouse warehouse;

    public ArrivalTruck(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 随机生成优先级
                String priority = Math.random() > 0.5 ? "High" : "Regular";
                Package p = new Package(priority);
                warehouse.storePackage(p);
                Thread.sleep(200);  
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class DepartureTruck implements Runnable {
    private final Warehouse warehouse;

    public DepartureTruck(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Package p = warehouse.retrievePackage();
                p.setStatus("Shipped");
                p.updateLastUpdateTime();
                Thread.sleep(300);  
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

