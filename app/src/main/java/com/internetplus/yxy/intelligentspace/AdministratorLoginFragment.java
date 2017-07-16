package com.internetplus.yxy.intelligentspace;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.squareup.leakcanary.LeakCanary;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by Y.X.Y on 2017/4/18 0018.
 */
public class AdministratorLoginFragment extends Fragment implements View.OnTouchListener{

    private EditText userid, passwd;

    private Button submit;

    private final String IP = "https://fresh.seceve.com/api/client?do=getRooms";

    private boolean isLogin = false;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    passwd.setText("");
                    Util.showToast(getActivity(), "用户名或密码错误");
                    break;
                case 102:
                    passwd.setText("");
                    // MainActivity.viewPager.setVisibility(0);
                    AdministratorChooseFragment fragment = new AdministratorChooseFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.FrameFragment, fragment);
                    transaction.commit();
                    break;
                case 103:
                    passwd.setText("");
                    Util.showToast(getActivity(), "连接服务器失败");
                    break;
            }
        }
    };

    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Fragment", "onAttach");
    }

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        Log.d("Fragment", "onCreate");
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment", "onActivityCreated");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Fragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_administratorlogin, container, false);
        // view.setOnTouchListener(this);
        // view.setBackgroundColor(Color.GRAY);
        Log.d("LoginFragment", view.getId()+"");
        view.setTag(0);
        userid = (EditText) view.findViewById(R.id.userid);
        passwd = (EditText) view.findViewById(R.id.passwd);
        submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Login();
                    }
                }).start();
            }
        });
        return view;
    }

    private void LoginError() {
        Message message = new Message();
        message.what = 103;
        handler.sendMessage(message);
        Log.d(TAG, "连接服务器失败");
    }

    private void Login() {
        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL(IP);
        } catch (MalformedURLException e) {
            LoginError();
            e.printStackTrace();
        }
        if (url == null) return ;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            LoginError();
            e.printStackTrace();
        }
        if (connection == null) return ;
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            LoginError();
            e.printStackTrace();
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setReadTimeout(8000);
        connection.setConnectTimeout(10000);
        connection.setRequestProperty("connection", "keep-alive");
        connection.setRequestProperty("accept-charset", "utf-8");
        int mark = 0;
        try {
            connection.connect();
        } catch (IOException e) {
            LoginError();
            e.printStackTrace();
            mark = 1;
        }
        if (mark == 1) return ;
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedOutputStream(connection.getOutputStream()));
        } catch (IOException e) {
            LoginError();
            e.printStackTrace();
        }
        if (pw == null) return ;
        pw.write(userid.getText().toString() + "+" + passwd.getText().toString());
        pw.flush();
        pw.close();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            LoginError();
            e.printStackTrace();
        }
        if (br == null) return ;
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.substring(0, 1).equals("A")) {
                    isLogin = true;
                    MainActivity.roomaddress = line.substring(1);
                } else {
                    //Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                    Message message = new Message();
                    message.what = 101;
                    handler.sendMessage(message);
                    Log.d(TAG, "用户名或密码错误");
                }
                break;
            }
        } catch (IOException e) {
            LoginError();
            e.printStackTrace();
        }

        /*
        System.out.println(cookieval + " , " + data);
        if ("denied".equals(data)) sessionid = null;
        else {
            if (cookieval != null) sessionid = cookieval.substring(0, cookieval.indexOf(";"));
         }
         */
        // passwd.setText("");
        if(isLogin == true) {
            Log.d(TAG, "登录成功");
            //logindialog.dismiss();
            //setroom();
            Message message = new Message();
            message.what = 102;
            handler.sendMessage(message);
        }
        Log.d("login", userid.getText().toString() + passwd.getText().toString());
        if (connection != null) {
            connection.disconnect();
        }
    }

    private float downx, downy;

    private float upx, upy;

    private float lastx, lasty;

    private float curx, cury;

    private float distance = 100;

    public boolean onTouch(View v, MotionEvent e1) {

        Log.d("LoginFragment", "onTouch" + "," + e1.getAction());
        switch (e1.getAction()) {
            case MotionEvent.ACTION_DOWN :
                downx = e1.getX();
                downy = e1.getY();
                lastx = e1.getX();
                lasty = e1.getY();
                break;
            case MotionEvent.ACTION_UP :
                upx = e1.getX();
                upy = e1.getY();
                Log.d("LoginFragment", "(" + downx + "," + downy + ")" + "-" + "(" + upx + "," + upy + ")");
                if (upx - downx > distance) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(AdministratorLoginFragment.this.getView(), "translationX", 0f, 100f);
                    objectAnimator.setDuration(1000);
                    objectAnimator.start();
                }
                break;
            case MotionEvent.ACTION_MOVE :
                curx = e1.getX();
                cury = e1.getY();
                float x = AdministratorLoginFragment.this.getView().getX();
                float dx = curx - lastx;
                lastx = curx;
                lasty = cury;
                AdministratorLoginFragment.this.getView().setX(x+dx);

        }

        return true;
    }

    public void onDetach() {
        super.onDetach();
        Log.d("Fragment", "onDetach");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Fragment", "onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment", "onDestroy");
        MyApplication.getRefWatcher(getActivity()).watch(this);
    }

}
