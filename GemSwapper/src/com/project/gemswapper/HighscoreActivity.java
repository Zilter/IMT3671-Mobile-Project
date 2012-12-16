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

<<<<<<< HEAD
public class HighscoreActivity extends ListActivity implements AsyncTaskCompleteListener<JSONObject> {

	ArrayList<HashMap <String, String>> mylist; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar
		 //set app to full screen and keep screen on 
		 getWindow().setFlags(0xFFFFFFFF, LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_KEEP_SCREEN_ON);	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listplaceholder);
        
        mylist = new ArrayList<HashMap <String, String>>();
        
        if(isOnline())
        {
        	asyncGetJSONFromURL async = new asyncGetJSONFromURL(this);
        	async.execute("http://game-details.com/gemswapper/highscore.php?");
        }
        else
        {
        	// Alert the user? Open network config?
        }
    }
    
    // When the async task has completed and the JSON result has been filled (hopefully)
    // an ArrayList of HashMaps of highscores is filled with data, and the listview is drawn from this list. 
	public void onTaskComplete(JSONObject result) 
=======
public class HighscoreActivity extends TabActivity implements OnTabChangeListener 
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
>>>>>>> c2c95538597a707d22a88f17af2e0d62da9c6c4b
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
            v.setBackgroundColor(Color.parseColor("#33CCFF"));
            if(i == getTabHost().getCurrentTab())
            {
            	v.setBackgroundColor(Color.parseColor("#003DF5"));
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
            v.setBackgroundColor(Color.parseColor("#33CCFF"));
            if(i == getTabHost().getCurrentTab())
            {
            	v.setBackgroundColor(Color.parseColor("#003DF5"));
            }
    	}
    }
}
