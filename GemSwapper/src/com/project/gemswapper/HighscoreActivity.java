package com.project.gemswapper;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

public class HighscoreActivity extends TabActivity implements OnTabChangeListener 
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide title
		getWindow().setFlags(0xFFFFFFFF, LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_KEEP_SCREEN_ON);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscore);

		TabHost tabHost = getTabHost();

		Resources ressources = getResources();

		Intent intentScore = new Intent().setClass(this, HighScoreScore.class);
		TabSpec tabScore = tabHost
				.newTabSpec("Score")
				.setIndicator("", ressources.getDrawable(R.drawable.highscore_score_01))
				.setContent(intentScore);

		Intent intentAchievement = new Intent().setClass(this,
				HighScoreAchievements.class);
		TabSpec tabAchievements = tabHost
				.newTabSpec("Achievement")
				.setIndicator("", ressources.getDrawable(R.drawable.highscore_achievements_01))
				.setContent(intentAchievement);

		// add all tabs
		tabHost.addTab(tabScore);
		tabHost.addTab(tabAchievements);

		tabHost.getTabWidget().setStripEnabled(false);
		// set Windows tab as default (zero based)

		tabHost.setCurrentTab(0);
    	TabWidget tw = getTabWidget();

    	for (int i = 0; i < tw.getChildCount(); i++) 
    	{
            View v = tw.getChildAt(i);
            v.setBackgroundColor(Color.parseColor("#FFFFFF"));
            if(i == getTabHost().getCurrentTab())
            {
            	v.setBackgroundColor(Color.parseColor("#000000"));
            }
    	}
    	
		tabHost.setOnTabChangedListener(this);
	}

    public void onTabChanged(String tabId) 
    {
    	TabWidget tw = getTabWidget();

    	for (int i = 0; i < tw.getChildCount(); i++) 
    	{
            View v = tw.getChildAt(i);
            v.setBackgroundColor(Color.parseColor("#FFFFFF"));
            if(i == getTabHost().getCurrentTab())
            {
            	v.setBackgroundColor(Color.parseColor("#000000"));
            }
    	}
    }
}