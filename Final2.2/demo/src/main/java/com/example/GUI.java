package com.example;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.chart.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GUI extends Application {
    private Warehouse warehouse;
    private ListView<String> operationLog;
    private TextArea statusArea;
    private PieChart priorityChart;
    private Label capacityLabel;
    private TextField searchField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        warehouse = new Warehouse(10, 20);  // 高优先10 低优先20

        
        operationLog = new ListView<>();        // 操作日志列表
        statusArea = new TextArea();            //仓库状态显示
        statusArea.setEditable(false);
        capacityLabel = new Label("当前仓库容量: 0 / 30");
        searchField = new TextField();
        searchField.setPromptText("输入包裹ID查询");

        // 初始化图表
        priorityChart = createPieChart();

        // 控制按钮
        Button startButton = new Button("Start Trucks");
        Button stopButton = new Button("Stop Trucks");

        // 布局
        VBox statusPanel = new VBox(10, new Label("仓库状态"), capacityLabel, statusArea, priorityChart);
        VBox controlPanel = new VBox(10, startButton, stopButton, new Label("包裹查询"), searchField);
        VBox logPanel = new VBox(10, new Label("操作日志"), operationLog);

        // 主面板
        HBox mainLayout = new HBox(20, statusPanel, logPanel, controlPanel);

        // 定时更新操作
        Runnable statusUpdater = () -> Platform.runLater(() -> {
            updateWarehouseStatus();
            updateLogs();
            updateChart();
        });

        Thread statusThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);  // 每秒更新一次
                    statusUpdater.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        statusThread.setDaemon(true);
        statusThread.start();

        // 启动卡车线程
        startButton.setOnAction(e -> startTrucks());

        // 停止所有线程
        stopButton.setOnAction(e -> stopTrucks());

        // 搜索包裹
        searchField.setOnAction(e -> searchPackageById());

        // 设置场景并显示
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setTitle("仓库管理系统");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 创建饼图
    private PieChart createPieChart() {
        PieChart chart = new PieChart();
        chart.setTitle("包裹优先级比例");
        return chart;
    }

    // 更新仓库状态
    private void updateWarehouseStatus() {
        String statusText = "当前仓库容量: " + warehouse.getCurrentCapacity() + " / " + warehouse.getTotalCapacity();
        capacityLabel.setText(statusText);
    }

    // 更新操作日志
    private void updateLogs() {
        List<String> logs = warehouse.getOperationLogs();
        operationLog.getItems().clear();
        operationLog.getItems().addAll(logs);
    }

    // 更新优先级图表
    private void updateChart() {
        PieChart.Data highPriority = new PieChart.Data("高优先级", warehouse.getHighPriorityQueue().size());
        PieChart.Data regularPriority = new PieChart.Data("普通优先级", warehouse.getRegularPriorityQueue().size());
        priorityChart.getData().setAll(highPriority, regularPriority);
    }

    // 启动卡车线程
    private void startTrucks() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(new ArrivalTruck(warehouse));
        executor.submit(new DepartureTruck(warehouse));
    }

    // 停止所有线程
    private void stopTrucks() {
        System.exit(0);
    }

    // 通过包裹ID查询
    private void searchPackageById() {
        String queryId = searchField.getText().trim();
        if (!queryId.isEmpty()) {
            // 实现包裹ID的查询逻辑
            System.out.println("查询包裹ID: " + queryId);
        }
    }
}
