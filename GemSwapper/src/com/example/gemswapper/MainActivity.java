package com.example.gemswapper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	
	public static Typeface typeface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide title
        getWindow().setFlags(0xFFFFFFFF,  LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_KEEP_SCREEN_ON);
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        typeface = Typeface.createFromAsset(getAssets(), "IMPACT:TTF");
        
        TextView startGame = (TextView) findViewById(R.id.start_game);
        TextView howToPlay = (TextView) findViewById(R.id.how_to);
        TextView achievements = (TextView) findViewById(R.id.achievements);
        TextView highScore = (TextView) findViewById(R.id.high_score);
        TextView credits = (TextView) findViewById(R.id.credits);
        TextView quitGame = (TextView) findViewById(R.id.quit_game);
        
        startGame.setTypeface(typeface);
        howToPlay.setTypeface(typeface);
        achievements.setTypeface(typeface);
        highScore.setTypeface(typeface);
        credits.setTypeface(typeface);
        quitGame.setTypeface(typeface);
        
        startGame.setOnClickListener(this);
        howToPlay.setOnClickListener(this);
        achievements.setOnClickListener(this);
        highScore.setOnClickListener(this);
        credits.setOnClickListener(this);
        quitGame.setOnClickListener(this);
    }
    
    public void onClick(View v)
    {
    	switch(v.getId())
    	{
//    	case R.id.start_game:
//    		Intent startGameIntent = new Intent(this, ....);
//    		startActivity(startGameIntent);
//    		break;
//    		
//    	case R.id.how_to:
//    		Intent howToIntent = new Intent(this, .....);
//    		startActivity(howToIntent);
//    		break;
//    		
//    	case R.id.achievements:
//    		Intent achievementIntent = new Intent(this, ....);
//    		startActivity(achievementIntent);
//    		break;
//    		
//    	case R.id.high_score:
//    		Intent highScoreIntent = new Intent(this, ....);
//    		startActivity(highScoreIntent);
//    		break;
//    		
//    	case R.id.credits:
//    		Intent creditsIntent = new Intent(this, ....);
//    		startActivity(creditsIntent);
//    		break;
//    		
//    	case R.id.quit_game:
//    		finish();
//    		break;
    	}
    }

    
    
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
