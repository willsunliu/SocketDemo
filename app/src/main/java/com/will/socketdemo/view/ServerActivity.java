package com.will.socketdemo.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.will.socketdemo.R;
import com.will.socketdemo.constants.TCPConstants;
import com.will.socketdemo.server.ServerProvider;
import com.will.socketdemo.server.TCPConnectionListener;

import java.io.IOException;

public class ServerActivity extends AppCompatActivity {
    TCPConnectionListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            // 开启TCP服务端监听
            listener = new TCPConnectionListener(TCPConstants.PORT_SERVER);
            listener.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 监听客户端通过UDP对服务端IP地址的搜索
        ServerProvider.start(TCPConstants.PORT_SERVER);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 停止对UDP搜索的监听
        ServerProvider.stop();
        // 断开TCP连接
        if (listener != null) {
            listener.exit();
        }
    }
}
