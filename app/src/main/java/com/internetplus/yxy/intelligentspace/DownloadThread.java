package com.internetplus.yxy.intelligentspace;

import android.app.DownloadManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Y.X.Y on 2017/5/5 0005.
 */
public class DownloadThread extends Thread {

    String url = null;

    public DownloadThread(String url) {
        this.url = url;
    }

    public void run() {
        URL url = null;
        try {
            url = new URL(this.url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url == null) return ;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connection == null) return;
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setReadTimeout(8*1000);
        connection.setConnectTimeout(8*1000);
        connection.setRequestProperty("charset", "utf-8");
        int mark = 0;
        try {
            connection.connect();
        } catch (IOException e) {
            mark = 1;
            e.printStackTrace();
        }
        if (mark == 0) return ;
    }
}
