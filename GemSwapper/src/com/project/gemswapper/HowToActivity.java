package com.project.gemswapper;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class HowToActivity extends Activity 
{

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(0xFFFFFFFF,  LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_how_to);
    }
}
