package com.internetplus.yxy.intelligentspace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.InputStream;

/**
 * Created by Y.X.Y on 2017/6/18 0018.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        Intent mBootIntent = new Intent(context, MainActivity.class);
        mBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mBootIntent);
    }
}
