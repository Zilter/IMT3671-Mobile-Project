package com.project.gemswapper;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class Achievements extends ListActivity  {
	
	private DatabaseAdapter mDbHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        
        mDbHelper = new DatabaseAdapter(this);
        mDbHelper.open();
        
        Cursor AchievementCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(AchievementCursor);

        String[] from = new String[] { DatabaseAdapter.KEY_NAME };
        int[] to = new int[] { R.id.text1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter achievements =
            new SimpleCursorAdapter(this, R.layout.achievements_row, AchievementCursor, from, to);
        setListAdapter(achievements);
    }
}
