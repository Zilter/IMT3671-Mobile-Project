package com.project.gemswapper;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class EndgameActivity extends Activity {
	
	Intent finishedGame = getIntent();
	String scoreString;
	Typeface typeface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar
    	getWindow().setFlags(0xFFFFFFFF, LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);
        
        
        typeface = Typeface.createFromAsset(getAssets(), "IMPACT.TTF");
        scoreString = finishedGame.getStringExtra(GameView.SCORE);
        
        
        TextView highscore_main = (TextView) findViewById(R.id.highscore_main);
        TextView highscore_score = (TextView) findViewById(R.id.highscore_score);
	    
	    highscore_main.setTypeface(typeface);
	    highscore_score.setTypeface(typeface);
        
        highscore_main.setText(R.string.highscore_main);
        highscore_score.setText(scoreString);
    }

 // Does the networking in a background thread. 
 	private class DataSend extends AsyncTask <Void, Void, Void> 
 	{
 		@Override
 		protected Void doInBackground(Void... params) {
 			send_data();
 			return null;
 		}
 	}
 	
 	// creates a get request and executes it. 
 	private void send_data()
 	{
     	DefaultHttpClient httpClient = new DefaultHttpClient();
     	
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        String name = preferences.getString("Name","");
        String id = preferences.getString("Id","");
        
        int formerScore = preferences.getInt("Score",0);
        int currentScore = Integer.parseInt(scoreString);
        
        if(currentScore > formerScore)
        {
	        SharedPreferences.Editor editor = preferences.edit();
	        editor.putInt("Score",currentScore);
	        editor.commit();
        }
        
     	String coreUrl = "http://game-details.com/gemswapper/insertscore.php?";
        String urlToSend = coreUrl + "id=" + id + "&" + "name=" + name + "&" + "score" + scoreString;
     	
//     	System.out.println(urlToSend);
     	HttpGet getScore = new HttpGet(urlToSend);
     	
     	coreUrl = "http://game-details.com/gemswapper/insertachievements.php?";
     	////!!!!!!!!!!!!!UNCOMMENT WHEN ACHIEVEMENTPOINTS ARE CALCULATED!!!!!!!!!!!!!!!!!////
        //urlToSend = coreUrl + "id=" + id + "&" + "name=" + name + "&" + "achievements" + achievementPoints;
     	
     	HttpGet getAchievement = new HttpGet(urlToSend);
     	
     	try 
     	{
 			httpClient.execute(getScore);
 			httpClient.execute(getAchievement);
 		} 
     	catch (Exception e) 
     	{
 			e.printStackTrace();
 		}
     	
 	}
 	
 	// Checks to see if the user is connected to the Internet
 	public boolean isOnline()
 	{
 		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
 		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
 			
 		return (networkInfo != null && networkInfo.isConnected());
 	}
 	
 	public void Submit(View view)
 	{
 		updateDB();
 		
 		if(isOnline())
 		{
 			new DataSend().execute();
 			
 			Intent startGameIntent = new Intent(this, Game.class);
    		startActivity(startGameIntent);
 		}
 	}
 	
 	public void Quit(View view)
 	{
 		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
 		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 		startActivity(intent);
 	}
 	
 	public void updateDB()
 	{
 		int oldCurrents[] = new int[7];
 		int oldCompleteds[] = new int[7];
 		int newCurrents[] = new int[7];
 		int newCompleteds[] = new int[7];
 		int progressCurrents[];
 		
 		DatabaseAdapter mDbHelper;
 		mDbHelper = new DatabaseAdapter(this);
 		mDbHelper.open();
 		
 		Cursor numberOfAchievements = mDbHelper.fetchAll();
 	
 		progressCurrents = finishedGame.getIntArrayExtra(GameView.COUNTERS);
 		
 		for (int i = 0; i < numberOfAchievements.getCount(); i++)
 		{
 			Cursor achievement = mDbHelper.fetch(i);
 			int goal = achievement.getInt(achievement.getColumnIndexOrThrow("Goal"));

 			oldCurrents[i] = achievement.getInt(achievement.getColumnIndexOrThrow("Current"));
 			oldCompleteds[i] = achievement.getInt(achievement.getColumnIndexOrThrow("Completed"));
 			newCompleteds[i] = (oldCompleteds[i] * goal + progressCurrents[i]) / goal;
 			newCurrents[i] = oldCurrents[i] + progressCurrents[i] % goal;
 			
 			mDbHelper.update(i, newCompleteds[i], newCurrents[i]);
 			
 		}
 		
 	}
 	
}
