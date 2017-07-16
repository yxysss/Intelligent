package com.internetplus.yxy.intelligentspace;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Y.X.Y on 2017/4/18 0018.
 */
public class OpenDoor {

    // private static SoundPool soundPool;

    private static MediaPlayer mediaPlayer;

    private static boolean access = true;

    public static void opendoor(FragmentActivity context) {

        if (!access) {
            Log.d("Fragment", "Door is openning");
            Util.showToast(context, "门已打开，请勿重复点击按钮");
            return;
        }

        access = false;

        mediaPlayer = MediaPlayer.create(context, R.raw.flow);
        mediaPlayer.setVolume(1, 1);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp = null;
                access = true;
            }
        });
        mediaPlayer.start();

        /*
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
        final int soundId = soundPool.load(context, R.raw.flow, 100);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
        });
        */
    }
}
