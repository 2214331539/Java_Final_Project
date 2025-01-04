package com.example;

import java.net.*;
import java.io.*;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) {
        int port = 8686;
        String serverAddress = "localhost";
        try {
            DatagramSocket socket = new DatagramSocket();

            //String message = "Update request:Activate vehicle-1234";  //测试语句
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入车辆ID (vehicle-1234):");
            String vehicleID = scanner.nextLine();
    
            // 提示用户输入指令（如: 激活车辆、停用车辆）
            System.out.println("请输入指令 ( Activate/Deactivate):");
            String command = scanner.nextLine();
    
            // 构建消息
            String message = "Update request:" + command + " " + vehicleID;
            
            byte[] buffer = message.getBytes();
            InetAddress address = InetAddress.getByName(serverAddress);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet);

            byte[] readBuffer = new byte[1024];
            packet = new DatagramPacket(readBuffer, readBuffer.length);
            socket.receive(packet);
            String receivedMessage = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received message: " + receivedMessage);
            socket.close();
            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
