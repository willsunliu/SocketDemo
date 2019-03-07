package com.will.socketdemo.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.will.socketdemo.R;
import com.will.socketdemo.client.ClientSearcher;
import com.will.socketdemo.client.TCPClient;
import com.will.socketdemo.client.UDPReceiveListener;
import com.will.socketdemo.constants.TCPConstants;
import com.will.socketdemo.server.ServerProvider;
import com.will.socketdemo.server.TCPConnectionListener;

import java.io.IOException;

import client.bean.ServerInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServerActivity.class));
            }
        });

        findViewById(R.id.btn_client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, ClientActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}