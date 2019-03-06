package com.will.socketdemo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Crate Time:  2019/3/4
 * Author:      LiuHanwei
 * Email:       liuhanwei@xhg.com
 * Description:
 */
public class ClientHandler extends Thread {
    private Socket socket;
    private boolean flag = true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("新客户端连接：" + socket.getInetAddress() + ":" + socket.getPort());

        try {
            // 得到打印流，用于数据输出；服务器回送数据使用
            PrintStream socketOutput = new PrintStream(socket.getOutputStream());
            // 得到输入流，用于接收数据
            BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            do {
                // 客户端拿到第一条数据
                String str = socketInput.readLine();
                if ("bye".equalsIgnoreCase(str)) {
                    flag = false;
                    // 回送
                    socketOutput.println("bye");
                } else {
                    // 打印到屏幕，并回送数据长度
                    System.out.println(str);
                    socketOutput.println("回送：" + str.length());
                }
            } while (flag);
        } catch (IOException e) {
            System.out.println("连接异常断开");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("客户端已退出:" + socket.getInetAddress() + ":" + socket.getPort());
    }
}
