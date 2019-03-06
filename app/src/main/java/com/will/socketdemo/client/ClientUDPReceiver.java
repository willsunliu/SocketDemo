package com.will.socketdemo.client;

import com.will.socketdemo.constants.UDPConstants;
import com.will.socketdemo.utils.ByteUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Crate Time:  2019/2/28
 * Author:      LiuHanwei
 * Email:       liuhanwei@xhg.com
 * Description:
 */
public class ClientUDPReceiver extends Thread {
    private final int listenPort;
    private final List<client.bean.ServerInfo> serverInfoList = new ArrayList<>();
    private final byte[] buffer = new byte[128];
    private final int minLen = UDPConstants.HEADER.length + 2 + 4;
    private boolean done = false;
    DatagramSocket ds = null;
    private UDPReceiveListener udpReceiveListener = null;
    private boolean isSetup = false;

    public ClientUDPReceiver(int listenPort) {
        this.listenPort = listenPort;
    }

    public void setUDPReceiveListener(UDPReceiveListener listener) {
        udpReceiveListener = listener;
    }

    public boolean isSetup() {
        return isSetup;
    }

    @Override
    public void run() {
        super.run();

        isSetup = true;
        try {
            // 监听回送端口
            ds = new DatagramSocket(listenPort);
            // 构建接收实体
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

            while (!done) {
                // 接收
                ds.receive(receivePacket);

                // 打印接收到得信息与发送者信息
                // 发送者得IP地址
                String ip = receivePacket.getAddress().getHostAddress();
                int port = receivePacket.getPort();
                int dataLen = receivePacket.getLength();
                byte[] data = receivePacket.getData();
                boolean isValid = dataLen >= minLen
                        && ByteUtils.startsWith(data, UDPConstants.HEADER);

                System.out.println("UDPSearcher receive form ip:" + ip
                        + "\tport:" + port + "\tdataValid:" + isValid);

                if (!isValid) {
                    // 接收到的数据无效，继续接收
                    continue;
                }

                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, UDPConstants.HEADER.length, dataLen);
                final short cmd = byteBuffer.getShort();
                final int serverPort = byteBuffer.getInt();
                if (cmd != 2 || serverPort <= 0) {
                    System.out.println("UDPSearcher receive cmd:" + cmd + "\tserverPort:" + serverPort);
                    continue;
                }

                String sn = new String(buffer, minLen, dataLen - minLen);
                client.bean.ServerInfo info = new client.bean.ServerInfo(sn, serverPort, ip);

                if (udpReceiveListener != null) {
                    udpReceiveListener.onReceive(info);
                }

                done = true;
                close();
            }
        } catch (Exception e) {
        } finally {
            close();
        }

        System.out.println("UDPSearcher listener finished.");
    }

    private void close() {
        if (ds != null) {
            ds.close();
            ds = null;
        }
    }
}
