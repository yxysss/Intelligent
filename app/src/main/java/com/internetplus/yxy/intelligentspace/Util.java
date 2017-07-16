package com.internetplus.yxy.intelligentspace;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Y.X.Y on 2017/5/10 0010.
 */
public class Util {

    private static Toast toast;

    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void deleteToast() {
        toast = null;
    }
}
