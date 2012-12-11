package com.project.gemswapper;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.SimpleCursorAdapter;

public class Achievements extends ListActivity  {
	
	private DatabaseAdapter mDbHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide title
        getWindow().setFlags(0xFFFFFFFF,  LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_KEEP_SCREEN_ON);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        
        mDbHelper = new DatabaseAdapter(this);
        mDbHelper.open();
        
        Cursor AchievementCursor = mDbHelper.fetchAll();
        startManagingCursor(AchievementCursor); //DEPRECATED
        
        String[] from = new String[] { DatabaseAdapter.KEY_NAME, DatabaseAdapter.KEY_COMPLETED , DatabaseAdapter.KEY_CURRENT, DatabaseAdapter.KEY_GOAL, DatabaseAdapter.KEY_POINTVALUE, DatabaseAdapter.KEY_DESC };
        int[] to = new int[] { R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6};
        
        // The rest of this is also deprecated, but it works
        SimpleCursorAdapter achievements =
            new SimpleCursorAdapter(this, R.layout.achievements_row, AchievementCursor, from, to);
        setListAdapter(achievements);
    }
}
