package com.will.socketdemo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Crate Time:  2019/3/4
 * Author:      LiuHanwei
 * Email:       liuhanwei@xhg.com
 * Description:
 */
public class TCPConnectionListener extends Thread {
    private ServerSocket mServerSocket;
    private boolean done = false;

    public TCPConnectionListener(int port) throws IOException {
        mServerSocket = new ServerSocket(port);
        System.out.println("服务器信息：" + mServerSocket.getInetAddress() + ":" + mServerSocket.getLocalPort());
    }

    @Override
    public void run() {
        super.run();
        System.out.println("服务器准备就绪");
        // 等待客户端连接
        do {
            // 得到客户端
            Socket client;
            try {
                client = mServerSocket.accept();
            } catch (IOException e) {
                continue;
            }

            // 客户端构建异步线程
            ClientHandler clientHandler = new ClientHandler(client);
            // 启动线程
            clientHandler.start();
        } while (!done);

        System.out.println("服务器关闭");
    }

    void exit() {
        done = true;
        try {
            mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
