package com.project.gemswapper;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HowToActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_how_to, menu);
        return true;
    }
}
