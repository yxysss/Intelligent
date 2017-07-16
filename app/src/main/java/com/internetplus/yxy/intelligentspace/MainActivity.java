package com.internetplus.yxy.intelligentspace;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button AdministratorLabel, RoominformationLabel, OpendoorLabel;

    private RelativeLayout.LayoutParams AdministratorLabelLp, RoominformationLabelLp, OpendoorLabelLp, LogoLp;

    private ImageView Logo;

    public static boolean roomchanged = true;

    public static List<String> roomlist = new ArrayList<>();

    public static List<String> roominfolist = new ArrayList<>();

    public static String roomaddress = null;

    public static String roominfoaddress = null;

    public static String roomname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // APKUpdate.checkfotupdate(this);
        Logo = (ImageView) findViewById(R.id.Logo);
        AdministratorLabel = (Button) findViewById(R.id.AdministratorLabel);
        RoominformationLabel = (Button) findViewById(R.id.RoominfomationLabel);
        OpendoorLabel = (Button) findViewById(R.id.OpendoorLabel);

        WindowManager windowManager = this.getWindowManager();
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();

        LogoLp = new RelativeLayout.LayoutParams(height/10, height/10);
        Logo.setLayoutParams(LogoLp);
        Logo.setImageResource(R.mipmap.logo);

        AdministratorLabelLp = new RelativeLayout.LayoutParams(width/5, height/10);
        AdministratorLabelLp.addRule(RelativeLayout.LEFT_OF, R.id.RoominfomationLabel);
        AdministratorLabelLp.setMargins(0, 0, 5, 0);
        AdministratorLabel.setLayoutParams(AdministratorLabelLp);

        RoominformationLabelLp = new RelativeLayout.LayoutParams(width/5, height/10);
        RoominformationLabelLp.addRule(RelativeLayout.CENTER_IN_PARENT);
        RoominformationLabelLp.setMargins(0, 0, 5, 0);
        RoominformationLabel.setLayoutParams(RoominformationLabelLp);

        OpendoorLabelLp = new RelativeLayout.LayoutParams(width/5, height/10);
        OpendoorLabelLp.addRule(RelativeLayout.RIGHT_OF, R.id.RoominfomationLabel);
        OpendoorLabel.setLayoutParams(OpendoorLabelLp);

        /*
        viewPager = (ViewPager) findViewById(R.id.ViewPagerFragment);
        fragmentList.add(MainActivity.administratorLoginFragment);
        fragmentList.add(MainActivity.roomInformationFragment);
        fragmentList.add(MainActivity.openDoorFragment);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        */

        // transaction.addToBackStack(null);
        AdministratorLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                // startActivity(intent);

                // viewPager.setCurrentItem(0);

                AdministratorLabel.setBackgroundColor(Color.GRAY);
                RoominformationLabel.setBackgroundColor(Color.WHITE);
                OpendoorLabel.setBackgroundColor(Color.WHITE);

                AdministratorLoginFragment fragment = new AdministratorLoginFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.FrameFragment, fragment);
                transaction.commit();

            }
        });
        RoominformationLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viewPager.setCurrentItem(1);

                AdministratorLabel.setBackgroundColor(Color.WHITE);
                RoominformationLabel.setBackgroundColor(Color.GRAY);
                OpendoorLabel.setBackgroundColor(Color.WHITE);

                RoomInformationFragment fragment = new RoomInformationFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.FrameFragment, fragment);
                transaction.commit();
            }
        });
        OpendoorLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viewPager.setCurrentItem(2);

                AdministratorLabel.setBackgroundColor(Color.WHITE);
                RoominformationLabel.setBackgroundColor(Color.WHITE);
                OpendoorLabel.setBackgroundColor(Color.GRAY);

                OpenDoorFragment fragment = new OpenDoorFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.FrameFragment, fragment);
                transaction.commit();
            }
        });
    }
}
