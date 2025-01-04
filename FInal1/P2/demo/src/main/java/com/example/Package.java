package java.com.example;

import java.util.concurrent.atomic.AtomicLong;

public class Package {
    private static final AtomicLong counter = new AtomicLong();
    
    private final long id;
    private String status;
    private String priority;
    private long lastUpdateTime;
    
    public Package(String priority) {
        this.id = counter.incrementAndGet();
        this.status = "In Storage";
        this.priority = priority;
        this.lastUpdateTime = System.currentTimeMillis();
    }
    
    public long getId() {
        return id;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    public void updateLastUpdateTime() {
        this.lastUpdateTime = System.currentTimeMillis();
    }
    
    @Override
    public String toString() {
        return "Package ID: " + id + ", Status: " + status + ", Priority: " + priority;
    }
}
