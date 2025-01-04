package com.example;    

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

    public static void main(String[] args) throws IOError, IOException {
        int port = 8686;
        //先存入三个车辆信息
        var bus = new Bus("1234", "Beijing-Ningbo", 100, "active", true);
        var train = new Train("6543", "Beijing-Tokyo", 300, "active", 10, "electrical");
        var metro = new Metro("4561", "Ningbo-Hkong", 200, "active", true);
        Transport[] transportations = { bus, train, metro };

        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            System.out.println("Server is running on port " + port);
            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received message: " + message);
                String keys = getKeys(message);

                if (keys == null) {
                    System.out.println("Invalid message");
                    continue;
                }

                System.out.println("分割后的vehiclekey " + keys);       //调试key值
                boolean vehicleFound = false;
                for (var transportation : transportations) {
                    if (transportation.vehicleID.equals(keys)) {
                        System.out.println("找到了车辆");
                        transportation.status = "active";
                        System.out.println(transportation.toString());

                        //使用新线程更新日志文件
                        new UpdateLogThread(transportation).start();
                        vehicleFound = true;
                        break;
                    }
                }

                if (!vehicleFound) {
                    System.out.println("没找到车辆");
                }

                String response = "Server received your message: " + message;
                byte[] responseBytes = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length, packet.getAddress(), packet.getPort());
                serverSocket.send(responsePacket);
                System.out.println("Sent response: " + response);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static String getKeys(String message) {
        int startIndex = message.indexOf("vehicle-");
        if (startIndex == -1) {
            return null;
        }
        String temp = message.substring(startIndex);
        int endIndex = temp.length();
        for (int i = 8; i < endIndex; i++) {
            if (!Character.isDigit(temp.charAt(i))) {
                endIndex = i;
                break;
            }
        }
        return temp.substring(8, endIndex);
    }

    //文件操作
    static class UpdateLogThread extends Thread {
        private final Transport transportation;

        public UpdateLogThread(Transport transportation) {
            this.transportation = transportation;
        }

        @Override
        public void run() {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String logEntry = timestamp + ": " + transportation.vehicleID + " - " + transportation.status;

            synchronized (Server.class) {
                try (PrintWriter writer = new PrintWriter(new FileOutputStream("transportLogs.txt", true))) {
                    writer.println(logEntry); 
                    System.out.println("Logged to file: " + logEntry);      //调试文件输出
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

