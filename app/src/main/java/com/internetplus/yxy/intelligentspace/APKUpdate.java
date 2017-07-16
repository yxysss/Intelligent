package com.internetplus.yxy.intelligentspace;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Config;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.toolbox.StringRequest;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Y.X.Y on 2017/4/21 0021.
 */
public class APKUpdate {

    private static String checkupdateaddress = "192.168.1.1";

    private static int versioncode = 0;

    public static long reference;

    public static void checkfotupdate(Context context) {
        new queryappversiontask(context).execute();
    }

    private static void getappversion(Context context) {

    }

    public static String getversion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);;
            return info.versionName + info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    static class queryappversiontask extends AsyncTask<Void, Integer, Boolean> {

        private Context context;

        private StringBuilder builder;

        public queryappversiontask(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {

        }

        protected Boolean doInBackground(Void... params) {
            try {
                URL url = new URL(checkupdateaddress);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(8000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("connection", "keep-alive");
                connection.setRequestProperty("charset", "utf-8");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onProgressUpdate(Integer... values) {

        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                Toast.makeText(context, "获取成功", Toast.LENGTH_LONG).show();
                jsonresolve(context, builder.toString());
            } else {
                Toast.makeText(context, "获取失败", Toast.LENGTH_LONG).show();
            }
            context = null;
        }

    }

    private static void jsonresolve(Context context, String data) {
        String versionname = "1.1";
        int newversioncode = 1;
        String path = "192.168.1.1";
        if (newversioncode > versioncode) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.show();
            downloadnewapk(context, path);
        }
    }

    private static void installAPK(Context context) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        //install.setDataAndType(downloaduri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }

    private static void downloadnewapk(final Context context, String path) {
        /*
        FileDownloader.getImpl().create(path).setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void blockComplete(BaseDownloadTask task) {

            }

            @Override
            protected void completed(BaseDownloadTask task) {
                installAPK(context);
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                Toast.makeText(context, "下载失败", Toast.LENGTH_LONG).show();
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        });
        */
    }

}
