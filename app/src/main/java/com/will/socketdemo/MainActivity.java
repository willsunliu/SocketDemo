package com.will.socketdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.will.socketdemo.client.ClientSearcher;
import com.will.socketdemo.client.TCPClient;
import com.will.socketdemo.client.UDPReceiveListener;
import com.will.socketdemo.constants.TCPConstants;
import com.will.socketdemo.server.ServerProvider;
import com.will.socketdemo.server.TCPConnectionListener;

import java.io.IOException;

import client.bean.ServerInfo;

public class MainActivity extends AppCompatActivity {

    boolean isServer = false;

    private TextView tvServerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isServer = true;
                try {
                    TCPConnectionListener listener = new TCPConnectionListener(TCPConstants.PORT_SERVER);
                    listener.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ServerProvider.start(TCPConstants.PORT_SERVER);
            }
        });

        findViewById(R.id.btn_client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClientSearcher.getInstance().isUDPReceiverSetup()) {
                    Toast.makeText(MainActivity.this, "UDP数据接收器还未初始化", Toast.LENGTH_LONG).show();
                    return;
                }
                ClientSearcher.getInstance().searchServer(10000);
            }
        });

        tvServerInfo = findViewById(R.id.server_info);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ClientSearcher.getInstance().init(new UDPReceiveListener() {
            @Override
            public void onReceive(final ServerInfo serverInfo) {
                try {
                    TCPClient.linkWith(serverInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isServer) {
            ServerProvider.stop();
        } else {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}