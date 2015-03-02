package com.example.floatingbubble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
//import android.support.v7.app.ActionBarActivity;
//import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

public class FloatingBubbleActivity extends Activity {


 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	      getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	      getWindow().addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
	      getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, 
	                  WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
	      
	      setContentView(R.layout.activity_floating_bubble_example);
		Intent intent = new Intent(FloatingBubbleActivity.this, FloatingFaceBubbleService.class);
		startService(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

