package com.internetplus.yxy.intelligentspace;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.internetplus.yxy.intelligentspace.MainActivity.roomaddress;

/**
 * Created by Y.X.Y on 2017/4/18 0018.
 */
public class AdministratorChooseFragment extends Fragment {

    private Button chooseroom, opendoor, exit;

    private Thread getroomdata = null;

    private String roomdata;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 201:
                    // MainActivity.viewPager.setVisibility(0);
                    AdministratorOperatorFragment fragment = new AdministratorOperatorFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.FrameFragment, fragment);
                    transaction.commit();
                    break;
                case 202:
                    break;
                default:
                    break;
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_administratorchoose, container, false);
        chooseroom = (Button) view.findViewById(R.id.chooseroom);
        chooseroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                getroomdata = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getroomdata();
                    }
                });
                */

                // getroomdata.run();

                // MainActivity.viewPager.setVisibility(0);
                AdministratorOperatorFragment fragment = new AdministratorOperatorFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.FrameFragment, fragment);
                transaction.commit();
            }
        });
        opendoor = (Button) view.findViewById(R.id.opendoor);
        opendoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDoor.opendoor(getActivity());
            }
        });
        exit = (Button) view.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdministratorLoginFragment fragment = new AdministratorLoginFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.FrameFragment, fragment);
                transaction.commit();
            }
        });
        return view;
    }

    public void getroomdata() {
        int mark = 0;
        HttpURLConnection connection = null;
        //Log.d(TAG, "Thread is running");
        try {
            //Log.d(TAG, "Thread is running 2");
            URL url = new URL(roomaddress);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("Accept-Encoding", "identity");
            //Log.d(TAG, "Thread is running 3");
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            //Log.d(TAG, "Thread is running 4");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                response.append(line);
            }
            in.close();
            roomdata = response.toString();
            //Log.d(TAG, jsonobj);
        } catch (Exception e) {
            Message message = new Message();
            message.what = 201;
            handler.sendMessage(message);
            mark = 1;
            // sessionid = null;
            e.printStackTrace();
        }
        if(mark == 0) {
            Log.d("setroom", "信息获取成功");
            Message message = new Message();
            message.what = 202;
            handler.sendMessage(message);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (getroomdata != null) {
            if (getroomdata.isAlive()) getroomdata.interrupt();
            getroomdata = null;
        }
        MyApplication.getRefWatcher(getActivity()).watch(this);
    }
}
