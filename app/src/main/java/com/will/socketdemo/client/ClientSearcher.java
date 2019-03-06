package com.will.socketdemo.client;

import com.will.socketdemo.constants.UDPConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ClientSearcher {
    private static final String TAG = "ClientSearcher";
    private static ClientSearcher instance;
    private ClientUDPReceiver receiver;

    private ClientSearcher() {}

    public static ClientSearcher getInstance() {
        if (instance == null) {
            instance = new ClientSearcher();
        }

        return instance;
    }

    public boolean isUDPReceiverSetup() {
        return receiver.isSetup();
    }

    public void init(UDPReceiveListener listener) {
        receiver = new ClientUDPReceiver(UDPConstants.PORT_CLIENT_RESPONSE);
        receiver.setUDPReceiveListener(listener);
        receiver.start();
    }

    public void searchServer(int timeout) {
        System.out.println("UDPSearcher Started.");

        try {
            sendBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendBroadcast() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run(){
                System.out.println("UDPSearcher sendBroadcast started.");

                // 作为搜索方，让系统自动分配端口
                DatagramSocket ds = null;
                try {
                    ds = new DatagramSocket();
                } catch (SocketException e) {
                    e.printStackTrace();
                }

                // 构建一份请求数据
                ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                // 头部
                byteBuffer.put(UDPConstants.HEADER);
                // CMD命令
                byteBuffer.putShort((short) 1);
                // 回送端口信息
                byteBuffer.putInt(UDPConstants.PORT_CLIENT_RESPONSE);
                // 直接构建packet
                DatagramPacket requestPacket = new DatagramPacket(byteBuffer.array(), byteBuffer.position() + 1);
                // 广播地址
                try {
                    requestPacket.setAddress(InetAddress.getByName("255.255.255.255"));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                // 设置服务器端口
                requestPacket.setPort(UDPConstants.PORT_SERVER);

                // 发送
                try {
                    ds.send(requestPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ds.close();

                //完成
                System.out.println("UDPSearcher sendBroadcast finished.");
            }
        }).start();

    }
}
