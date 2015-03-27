package com.example.floatingbubble;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AlertDialogActivity extends Activity {

	private String notiMessage;
	private BroadcastReceiver   locationChangereceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Bundle bun = getIntent().getExtras();
		notiMessage = bun.getString("notiMessage");

		setContentView(R.layout.alertdialog);

		TextView adMessage = (TextView) findViewById(R.id.message);

		Button submitButton = (Button) findViewById(R.id.submit);
		Button cancelButton = (Button) findViewById(R.id.cancel);

		submitButton.setOnClickListener(new SubmitOnClickListener());
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private class SubmitOnClickListener implements OnClickListener {

		public void onClick(View v) {
			Log.i("die","rrrrrrr죽음!!");
			
			Intent intent  = new Intent(getApplicationContext(), ListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			startActivity(intent);
			
			finish();
	        stopService(new Intent(getApplicationContext(), FloatingFaceBubbleService.class));
			android.os.Process.killProcess(android.os.Process.myPid()); // 강제종료
		}
	}
}