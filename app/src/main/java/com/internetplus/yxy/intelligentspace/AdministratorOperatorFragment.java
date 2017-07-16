package com.internetplus.yxy.intelligentspace;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Y.X.Y on 2017/4/18 0018.
 */
public class AdministratorOperatorFragment extends Fragment {

    private RecyclerView recyclerView;

    private RecyclerAdapter recyclerAdapter;

    private Button exit0;

    private Thread thread = null;

    private TextView messageToShow;

    private AlertDialog alertDialog;

    private AlertDialog.Builder builder;

    private Handler mhandler = new Handler() {
        public void handleMessage(final Message message) {
            switch (message.what) {
                case 0:
                    Log.d("OperatorError", "OperatorError");
                    break;
                case 1:
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                    recyclerView.setAdapter(recyclerAdapter = new RecyclerAdapter());
                    builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {
                            Log.d("onItemClick", "position"  + position);
                            builder.setTitle("选择房间")
                                    .setMessage("你确定选择" + MainActivity.roomlist.get(position) + "作为当前显示的房间吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            MainActivity.roominfoaddress = MainActivity.roominfolist.get(position);
                                            messageToShow.setText(MainActivity.roomlist.get(position));
                                            MainActivity.roomname = MainActivity.roomlist.get(position);
                                        }
                                    })
                                    .setNegativeButton("重新选择", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertDialog.dismiss();
                                        }
                                    });
                            alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_administratoroperator, container, false);
        MainActivity.roomlist.clear();
        messageToShow = (TextView) view.findViewById(R.id.messageToshow);
        messageToShow.setText(MainActivity.roomname);
        exit0 = (Button) view.findViewById(R.id.exit0);
        exit0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdministratorChooseFragment fragment = new AdministratorChooseFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.FrameFragment, fragment);
                transaction.commit();
            }
        });
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                updateinformation(MainActivity.roomaddress);
            }
        });
        thread.start();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        return view;
    }

    private void updateinformation(String address) {
        URL url = null;
        try {
            url = new URL(address);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url == null) {
            sendErrorMessage();
            return ;
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connection == null) {
            sendErrorMessage();
            return ;
        }
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(8000);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("charset", "utf-8");
        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
            sendErrorMessage();
            return ;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (reader == null) {
            sendErrorMessage();
            return ;
        }
        StringBuilder builder = new StringBuilder();
        String line = null;
        try {
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("OperatorFragment", builder.toString());
        parseJson(builder.toString());
        Message message = new Message();
        message.what = 1;
        mhandler.sendMessage(message);
    }

    private void sendErrorMessage() {
        Message message = new Message();
        message.what = 0;
        mhandler.sendMessage(message);
    }

    private void parseJson(String data) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray == null) {
            return ;
        }
        JSONObject jsonObject = null;
        for(int i = 0; i < jsonArray.length(); i ++) {
            jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObject == null) continue;
            String roomname = null;
            String roominfoaddress = null;
            try {
                roomname = jsonObject.getString("roomname");
                roominfoaddress = jsonObject.getString("roominfoaddress");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (roomname != null) {
                MainActivity.roomlist.add(roomname);
            }

            if (roominfoaddress != null) {
                MainActivity.roominfolist.add(roominfoaddress);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if(thread != null) {
            if(thread.isAlive()) {
                thread.interrupt();
            }
            thread = null;
        }
        MyApplication.getRefWatcher(getActivity()).watch(this);
    }

}
