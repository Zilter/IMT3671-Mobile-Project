package com.project.gemswapper;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class CreditsActivity extends Activity {
	
	public static Typeface typeface;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(0xFFFFFFFF, LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credits);
		
		typeface = Typeface.createFromAsset(getAssets(), "IMPACT.TTF");
		
		TextView creditsMain = (TextView) findViewById(R.id.creditsMain);
		TextView creators = (TextView) findViewById(R.id.creators);
		
		creditsMain.setTypeface(typeface);
		creators.setTypeface(typeface);
	}
}
