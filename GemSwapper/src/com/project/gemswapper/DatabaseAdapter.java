package com.project.gemswapper;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAdapter {
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "Name";
    public static final String KEY_COMPLETED = "Completed";
    public static final String KEY_GOAL = "Goal";
    public static final String KEY_CURRENT = "Current";
    public static final String KEY_POINTVALUE = "PointValue";
    public static final String KEY_DESC = "Description";
    
    private static final String DATABASE_NAME = "GemswapperDb";
    private static final String DATABASE_TABLE = "Achievements";
    private static final int DATABASE_VERSION = 2;

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	
	/**
     * Database creation sql statement
     */
	
	private static final String DATABASE_CREATE =
			"CREATE TABLE Achievements (" +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"Name TEXT NOT NULL," +
					"Completed INTEGER NOT NULL DEFAULT '0'," +
					"Goal INTEGER NOT NULL," +
					"Current INTEGER NOT NULL DEFAULT '0'," +
					"Pointvalue INTEGER NOT NULL," +
					"Description TEXT NOT NULL)";
	
	private static final String DATABASE_FILL = "INSERT INTO Achievements (Name, Goal, PointValue, Description) VALUES" +
			" (" + R.string.three + ", 40, 20, " + R.string.threeDescription +")," +
			" (" + R.string.four + ", 25, 25, " + R.string.fourDescription +")," +
			" (" + R.string.five + ", 10, 20, " + R.string.fiveDescription +")," +
			" (" + R.string.corner + ", 15, 20, " + R.string.cornerDescription +")," +
			" (" + R.string.tShape + ", 15, 20, " + R.string.tShapeDescription +")," +
			" (" + R.string.score + ", 1000, 25, " + R.string.scoreDescription +")," +
			" (" + R.string.time + ", 60, 25, " + R.string.timeDescription +")";

	
	private final Context mCtx;
	
		
	private static class DatabaseHelper extends SQLiteOpenHelper {
	
		public DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate (SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE);
			db.execSQL(DATABASE_FILL);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
			//If the version is changed, the code in here will be run
		}
	}

	
	/**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
	
	public DatabaseAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	/**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DatabaseAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    
    
    /**
     * Return a Cursor over the list of all achievements in the database
     * 
     * @return Cursor over all achievements
     */
    
    public Cursor fetchAllNotes() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_COMPLETED, KEY_GOAL, KEY_CURRENT, KEY_POINTVALUE, KEY_DESC},
        		null, null, null, null, null);
    }
    
    
    
    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, 
            		new String[] {KEY_ROWID, KEY_NAME, KEY_COMPLETED, KEY_GOAL, KEY_CURRENT, KEY_POINTVALUE, KEY_DESC},
            		KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    
    public boolean updateNote(int rowId, String name, int completed, int goal, int current, int pointValue, String description) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_COMPLETED, completed);
        args.put(KEY_GOAL, goal);
        args.put(KEY_CURRENT, current);
        args.put(KEY_POINTVALUE, pointValue);
        args.put(KEY_DESC, description);
        
        
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
	
    
