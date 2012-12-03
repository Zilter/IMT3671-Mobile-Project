package com.project.gemswapper;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Achievements extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_achievements, menu);
        return true;
    }
}
