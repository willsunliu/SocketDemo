package com.will.socketdemo.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.will.socketdemo.R;
import com.will.socketdemo.client.ClientSearcher;
import com.will.socketdemo.client.TCPClient;
import com.will.socketdemo.client.UDPReceiveListener;

import java.io.IOException;

public class ClientActivity extends AppCompatActivity {
    private static final String TAG = "ClientActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        findViewById(R.id.btn_connect_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClientSearcher.getInstance().isUDPReceiverSetup()) {
                    Log.e(TAG, "UDP接收器尚未完成初始化");
                    return;
                }
                ClientSearcher.getInstance().searchServer(10000);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ClientSearcher.getInstance().init(new UDPReceiveListener() {
            @Override
            public void onReceive(final client.bean.ServerInfo serverInfo) {
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
        TCPClient.disconnect();
    }
}
