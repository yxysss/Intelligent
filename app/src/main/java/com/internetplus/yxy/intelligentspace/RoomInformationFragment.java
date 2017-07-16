package com.internetplus.yxy.intelligentspace;

import android.app.ActionBar;
import android.media.ImageWriter;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import libcore.io.PictureLoader;
import org.w3c.dom.Text;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.RESTRICTIONS_SERVICE;

/**
 * Created by Y.X.Y on 2017/4/19 0019.
 */
public class RoomInformationFragment extends Fragment {

    private ImageView Imageofroom;

    private int Imagewidth, ImageHeight;

    private ImageView Qcode;

    private RelativeLayout.LayoutParams ImageLayoutParams, QcodeLayoutParams;

    private RelativeLayout square[][] = new RelativeLayout[4][7];

    private TableView text[][] = new TableView[4][7];

    private String roomname = null;

    private String Imageurl = "http://192.168.1.102/5.jpg";

    private String Texturl = "http://192.168.1.102/5.txt";

    private String Imagename = null;

    private String Text = null;

    private ImageRequest imageRequest;

    private StringRequest stringRequest;

    private String cachedir = null;

    private RequestQueue mQueue;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roominformation, container, false);
        loadview(container, view);
        filemkdirs();
        getSharedPreference();
        if(MainActivity.roomchanged == true) {
            roomchange();
        } else {
            roomkeep();
        }
        return view;
    }

    private void loadview(ViewGroup container, View view) {
        Log.d("RoomInformationFragment", container.getMeasuredWidth() + " , " + container.getMeasuredHeight());
        Log.d("RoomInformationFragment", container.getWidth() + " , " + container.getHeight());
        int width = container.getMeasuredWidth();
        int height = container.getMeasuredHeight();
        Imagewidth = width/3;
        ImageHeight = height/3;
        Imageofroom = (ImageView) view.findViewById(R.id.Imageofroom);
        ImageLayoutParams = new RelativeLayout.LayoutParams(width/3, height/3);
        Imageofroom.setLayoutParams(ImageLayoutParams);
        Qcode = (ImageView) view.findViewById(R.id.Qcode);
        QcodeLayoutParams = new RelativeLayout.LayoutParams(width/3, height/3);
        QcodeLayoutParams.addRule(RelativeLayout.BELOW, R.id.Imageofroom);
        Qcode.setLayoutParams(QcodeLayoutParams);
        /*
        square[0][0] = (RelativeLayout) view.findViewById(R.id.t0ll);
        square[0][1] = (RelativeLayout) view.findViewById(R.id.t1ll);
        square[0][2] = (RelativeLayout) view.findViewById(R.id.t2ll);
        square[0][3] = (RelativeLayout) view.findViewById(R.id.t3ll);
        square[0][4] = (RelativeLayout) view.findViewById(R.id.t4ll);
        square[0][5] = (RelativeLayout) view.findViewById(R.id.t5ll);
        square[1][0] = (RelativeLayout) view.findViewById(R.id.s0ll);
        square[1][1] = (RelativeLayout) view.findViewById(R.id.s1ll);
        square[1][2] = (RelativeLayout) view.findViewById(R.id.s2ll);
        square[1][3] = (RelativeLayout) view.findViewById(R.id.s3ll);
        square[1][4] = (RelativeLayout) view.findViewById(R.id.s4ll);
        square[1][5] = (RelativeLayout) view.findViewById(R.id.s5ll);
        square[2][0] = (RelativeLayout) view.findViewById(R.id.u0ll);
        square[2][1] = (RelativeLayout) view.findViewById(R.id.u1ll);
        square[2][2] = (RelativeLayout) view.findViewById(R.id.u2ll);
        square[2][3] = (RelativeLayout) view.findViewById(R.id.u3ll);
        square[2][4] = (RelativeLayout) view.findViewById(R.id.u4ll);
        square[2][5] = (RelativeLayout) view.findViewById(R.id.u5ll);
        */
        text[0][0] = (TableView) view.findViewById(R.id.t0);
        text[0][1] = (TableView) view.findViewById(R.id.t1);
        text[0][2] = (TableView) view.findViewById(R.id.t2);
        text[0][3] = (TableView) view.findViewById(R.id.t3);
        text[0][4] = (TableView) view.findViewById(R.id.t4);
        text[0][5] = (TableView) view.findViewById(R.id.t5);
        text[1][0] = (TableView) view.findViewById(R.id.s0);
        text[1][1] = (TableView) view.findViewById(R.id.s1);
        text[1][2] = (TableView) view.findViewById(R.id.s2);
        text[1][3] = (TableView) view.findViewById(R.id.s3);
        text[1][4] = (TableView) view.findViewById(R.id.s4);
        text[1][5] = (TableView) view.findViewById(R.id.s5);
        text[2][0] = (TableView) view.findViewById(R.id.u0);
        text[2][1] = (TableView) view.findViewById(R.id.u1);
        text[2][2] = (TableView) view.findViewById(R.id.u2);
        text[2][3] = (TableView) view.findViewById(R.id.u3);
        text[2][4] = (TableView) view.findViewById(R.id.u4);
        text[2][5] = (TableView) view.findViewById(R.id.u5);
        text[0][1].setText("08:30-10:00");
        text[0][2].setText("10:30-12:00");
        text[0][3].setText("14:00-15:30");
        text[0][4].setText("16:00-17:30");
        text[0][5].setText("19:00-20:30");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width/9, height/9);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        for (int i = 0; i <= 2; i ++) {
            for (int j = 0; j <= 5; j ++) {
                text[i][j].setLayoutParams(layoutParams);
                if(j != 0) {
                    text[i][j].setText("unknown");
                }
            }
        }
    }

    private void filemkdirs() {
        cachedir = getCacheDir(getActivity(), "bitmap");
        File file = new File(cachedir);
        if(!file.exists()) {
            file.mkdirs();
        }
        file = null;
    }

    private void getSharedPreference() {
        SharedPreferences preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        roomname = preferences.getString("roomname", "");
        Imagename = preferences.getString("Imagename", "");
        // Imageurl = preferences.getString("Imageurl", "");
        Text = preferences.getString("Text", "");
    }

    private void roomkeep() {
        if(Imagename.equals("")) {
            Imageofroom.setImageResource(R.mipmap.ic_launcher);
            // Textofroom.setText("房间尚未设置");
            return ;
        }
        try {
            File file = new File(Imagename);
            FileInputStream inputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Imageofroom.setImageBitmap(bitmap);
            // Textofroom.setText(Text);
        } catch (Exception e) {
            e.printStackTrace();
            roomchange();
        }
    }

    private void roomchange() {
        // mQueue = Volley.newRequestQueue(getActivity());
        // mQueue.add(loadTextofroom());
        // PictureLoader.getPictureLoader(getActivity()).ImageRequest(Imageurl, Imageofroom, Imagewidth, ImageHeight);
        Imageofroom.setImageResource(R.mipmap.p5);
        createQRImage("https://fresh.seceve.com/user?page=immApply&space_id=XXX&timeStamp=XXX");
    }

    /*
    private StringRequest loadTextofroom() {
        stringRequest = new StringRequest(Texturl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Textofroom.setText(response);
                        Text = response;
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        return stringRequest;
    }
    */

    /*
    private ImageRequest loadImageofroom() {
        imageRequest = new ImageRequest(
                Imageurl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Imageofroom.setImageBitmap(response);
                        File file = new File(cachedir + File.separator + "bitmap0");
                        Log.d("loadImageofroom", cachedir + File.separator + "bitmap0");
                        try {
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            FileOutputStream outputStream = new FileOutputStream(file);
                            response.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            // MainActivity.roomchanged = false;
                            Imagename = cachedir + File.separator + "bitmap0";
                            Log.d("File.pathSeparator", Imagename);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, Imagewidth, ImageHeight, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Imageofroom.setImageResource(R.mipmap.ic_launcher);
            }
        });
        return imageRequest;
    }
    */

    public String getCacheDir(Context context, String uniqueName) {
        String cachePath;
        // 如果SD卡被挂载了 或者 是内置SD卡， 那么使用/mnt/sdcard 路径
        // 否则使用内置存储 /data/data 路径
        // 内置存储一般安装系统固件，多出来的一些空间可以安装应用软件
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + uniqueName;
    }

    public void createQRImage(String url)
    {
        int QR_WIDTH = Imagewidth;

        int QR_HEIGHT = ImageHeight;

        try
        {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++)
            {
                for (int x = 0; x < QR_WIDTH; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            Qcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void onStop() {
        super.onStop();
        // mQueue.cancelAll(this);
    }

    public void onDestroy() {
        super.onDestroy();
        // mQueue.stop();
        stringRequest = null;
        imageRequest = null;
        PictureLoader.getPictureLoader(getActivity()).Destroy();
        MyApplication.getRefWatcher(getActivity()).watch(this);
    }

}
