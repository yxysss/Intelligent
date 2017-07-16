package com.internetplus.yxy.intelligentspace;

import android.content.Context;
import android.support.multidex.MultiDex;
import com.liulishuo.filedownloader.FileDownloader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import dalvik.system.PathClassLoader;
import android.support.multidex.MultiDexApplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Y.X.Y on 2017/4/19 0019.
 */
public class MyApplication extends MultiDexApplication {

    private static RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        //MyApplication application = (MyApplication) context.getApplicationContext();
        return refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        FileDownloader.init(this);
        MultiDex.install(this);
        /*
        boolean hasBaseDexClassLoader = true;
        try {
            Class.forName("dalvik.system.BaseDexClassLoader");
        } catch (ClassNotFoundException e) {
            hasBaseDexClassLoader = false;
            e.printStackTrace();
        }
        if (hasBaseDexClassLoader) {
            PathClassLoader pathClassLoader = (PathClassLoader) this.getClassLoader();
        }
        */
    }

}
