package com.community.protectcommunity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.SoundPool;

public class SoundUtil {
    @SuppressLint("NewApi")
    static SoundPool soundPool = new SoundPool.Builder().build();
    public static int initSound(Activity activity) {
        System.out.print(activity);
        int soundID = soundPool.load(activity, R.raw.button_4, 1);
        return soundID;
    }

    public static void playSound(int soundID) {
        soundPool.play(
                soundID,
                1f,
                1f,
                0,
                0,
                1
        );
    }
}
