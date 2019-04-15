package com.community.protectcommunity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by jingewang on 28/3/19.
 */

public class GameEndingActivity extends Activity implements View.OnClickListener{
    //Execute when the page is created
    Button backToMainScreenButton;
    View score;
    RelativeLayout endingActivityLayout;
    private SoundPool soundPool;//declare a SoundPool
    private int soundID;//Create an audio ID for a sound

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        backToMainScreenButton = (Button)findViewById(R.id.return_to_mainscreen_button_ending_activity);
        endingActivityLayout = (RelativeLayout)findViewById(R.id.ending_activity_layout);

        score = findViewById(R.id.ending_score);

        SoundUtil.initSound(this);

        //calculate the score from shared preference
        SharedPreferences sharedPref = getSharedPreferences("username_gender_choice", Context.MODE_PRIVATE);
        String question1 = sharedPref.getString("question1", null);
        String question2 = sharedPref.getString("question2", null);
        int finalScoreInt = 0;
        //I wrote it in a reverse order, so yes will plus 50 points
        if ("YES".equals(question1)) {
            finalScoreInt += 50;
        }
        if ("NO".equals(question2)) {
            finalScoreInt += 50;
        }

        System.out.println(finalScoreInt);
        Drawable drawable;
        Resources res = getResources();
        if(finalScoreInt == 100) {
            //set the score view background to 100
            drawable = res.getDrawable(R.drawable.number_100, null);
            score.setBackground(drawable);
            drawable = res.getDrawable(R.drawable.ending_activity_100_background, null);
            endingActivityLayout.setBackground(drawable);
        }
        if(finalScoreInt == 50) {
            //set the score view background to 50
            drawable = res.getDrawable(R.drawable.number_50, null);
            score.setBackground(drawable);
            drawable = res.getDrawable(R.drawable.ending_activity_50_background, null);
            endingActivityLayout.setBackground(drawable);
        }
        if(finalScoreInt == 0) {
            //set the score view background to 0
            drawable = res.getDrawable(R.drawable.number_0, null);
            score.setBackground(drawable);
            drawable = res.getDrawable(R.drawable.ending_activity_0_background, null);
            endingActivityLayout.setBackground(drawable);
        }

        //set up and start animation
        ObjectAnimator scoreViewAnimOn = AnimUtil.getAnimatorOn(score, this);
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scoreViewAnimOn);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatorSet.start();
            }
        });
        backToMainScreenButton.setOnClickListener(this);


        //Bind music service
        doBindService();
        Intent music = new Intent();
        music.setClass(this,MusicService_End.class);
        startService(music);

    }


    public void getHome(){
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.return_to_mainscreen_button_ending_activity:
                SoundUtil.playSound(soundID);
                getHome();
                break;
            default:
                break;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //Unbind music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService_End.class);
        stopService(music);
    }

    private boolean mIsBound = false;
    private MusicService_End mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService_End.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService_End.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mServ != null){
            mServ.resumeMusic();
        }
    }

}
