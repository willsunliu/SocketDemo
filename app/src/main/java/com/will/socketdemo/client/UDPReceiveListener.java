package com.will.socketdemo.client;

/**
 * Crate Time:  2019/2/28
 * Author:      LiuHanwei
 * Email:       liuhanwei@xhg.com
 * Description:
 */
public interface UDPReceiveListener {
    void onReceive(client.bean.ServerInfo serverInfo);
}
