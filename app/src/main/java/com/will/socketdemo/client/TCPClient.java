package com.will.socketdemo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Crate Time:  2019/3/4
 * Author:      LiuHanwei
 * Email:       liuhanwei@xhg.com
 * Description:
 */
public class TCPClient {
    private static boolean done = false;

    public static void linkWith(client.bean.ServerInfo info) throws IOException {
        Socket socket = new Socket();
        // 超时时间
        socket.setSoTimeout(3000);

        // 连接服务器
        socket.connect(new InetSocketAddress(Inet4Address.getByName(info.getAddress()), info.getPort()), 3000);

        System.out.println("已发起服务器连接");
        System.out.println("客户端信息：" + socket.getLocalAddress() + ":" + socket.getLocalPort());
        System.out.println("服务器信息：" + socket.getInetAddress() + ":" + socket.getPort());

        try {
            running(socket);
        } catch (Exception e) {
            System.out.println("异常关闭");
        }

        socket.close();
        System.out.println("客户端已退出");
    }

    public static void disconnect() {
        done = true;
    }

    private static void running(Socket client) throws IOException {
        done = false;

        // 得到Socket输出流，并转换成打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);

        // 得到Socket输入流，并转换成BufferedReader
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        int i = 0;
        do {
            // 发送到服务器
            socketPrintStream.println(String.valueOf(i));

            // 从服务器读取一行
            String echo = socketBufferedReader.readLine();
            if (null == echo) {
                done = true;
            } else {
                System.out.println(echo);
            }
        } while (!done);

        socketPrintStream.close();
        socketBufferedReader.close();
    }
}
