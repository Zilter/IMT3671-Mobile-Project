package com.project.gemswapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class HighScoreAchievements extends ListActivity implements AsyncTaskCompleteListener<JSONObject> {

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
        	async.execute("http://www.game-details.com/gemswapper/highscoreachievements.php?");
        }
    }
    
    // When the async task has completed and the JSON result has been filled (hopefully)
    // an ArrayList of HashMaps of highscores is filled with data, and the listview is drawn from this list. 
	public void onTaskComplete(JSONObject result) 
	{
		try
    	{
            JSONArray highscores = result.getJSONArray("highscore");
            
            String name = getString(R.string.name);
            String score = getString(R.string.highscore);
            
	        for(int i = 0; i < highscores.length(); i++)
	        {
				HashMap<String, String> map = new HashMap<String, String>();	
				JSONObject e = highscores.getJSONObject(i);
				
				
				map.put("placement", String.valueOf( i + 1 ) + ".");
	        	map.put("name", name + e.getString( "name" ));
	        	map.put("achievements", score + e.getString( "achievements" ));
	        	mylist.add(map);
			}        
    	} 
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
        		
    	ListAdapter adapter = new SimpleAdapter(this, mylist , R.layout.activity_high_score_achievements, 
                new String[] {"placement", "name", "achievements" }, 
                new int[] { R.id.placement, R.id.name, R.id.score});

        setListAdapter(adapter);
	}	
	
	// Creates a new thread and retrieves the JSON string in that new thread
	// This is done so networking is avoided on the main thread which would cause
	// an exception on newer android version. 
    
    // It runs the function to retrieve the JSONObject from the page in background
	// When the object has been parsed, converted and packed into an object it is sent to
	// onPostExecute, and in turn to the callback onTaskComplete. 
	
	private class asyncGetJSONFromURL extends AsyncTask <String, Void, JSONObject> 
	{
		private AsyncTaskCompleteListener<JSONObject> callback;
		
		public asyncGetJSONFromURL(AsyncTaskCompleteListener<JSONObject> cb)
		{
			this.callback = cb;
		}
		
		@Override
		protected JSONObject doInBackground(String... url) 
		{
			JSONObject json = getJSONfromURL(url[0]);
			return json;
		}
		
		protected void onPostExecute(JSONObject result)
		{
			callback.onTaskComplete(result);
		}
	}

	// This functions takes the url of our target page, retrives the postResponse
	// converts it to string and then packs the result into a JSONObject which is
	// returned for further use. 
	
	public static JSONObject getJSONfromURL(String url)
	{
		InputStream inputStream = null;
		String result = "";
		JSONObject jArray = null;
		
		
		inputStream = getPostResponse(url);
		result = convertToString(inputStream);
		
	    try
	    {
            jArray = new JSONObject(result);            
	    }
	    catch(JSONException e)
	    {
	            Log.e("log_tag", "Error parsing data " + e.toString());
	    }
    
	    return jArray;
	}
	
	// connects to the Internet and returns the get response as an inputstream. 
	public static InputStream getPostResponse(String url)
	{
		InputStream result = null;
		
	    try
	    {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            result = entity.getContent();
	    }
		catch(Exception e)
		{
	            Log.e("log_tag", "Error in http connection "+e.toString());
	    }
	    
	    return result;
	}
	
	// Converts the get response from an input stream to a string. 
	public static String convertToString(InputStream postResponse)
	{
		String result = "";
		
	    try
	    {
    		BufferedReader reader = new BufferedReader(new InputStreamReader(postResponse, "iso-8859-1"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            
            while ((line = reader.readLine()) != null) 
            {
                    stringBuilder.append(line + "\n");
            }
            
            result = stringBuilder.toString();
	    }
	    catch(Exception e)
	    {
	            Log.e("log_tag", "Error converting result "+e.toString());
	    }
	    
	    return result;
	}
	
	// Checks to see if the device has an Internet connection
	public boolean isOnline()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager)
						getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			
		return (networkInfo != null && networkInfo.isConnected());
	}
}
