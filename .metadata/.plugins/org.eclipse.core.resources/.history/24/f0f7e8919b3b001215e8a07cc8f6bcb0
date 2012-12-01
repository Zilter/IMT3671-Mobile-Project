package com.example.gemswapper;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TouchListener implements View.OnTouchListener {

	public boolean onTouch(View view, MotionEvent motionEvent)
	{
		switch(motionEvent.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			((TextView) view).setTextColor(0xCCFFFFFF);
			break;
			
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			((TextView) view).setTextColor(0xFF000000);
			break;
		}
		
		return false;
	}
}
