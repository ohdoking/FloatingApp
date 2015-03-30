package com.yapp.mycard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.floatingbubble.R;

public class AlertDialogActivity extends Activity {

	private String notiMessage;
	private BroadcastReceiver locationChangereceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Bundle bun = getIntent().getExtras();
		notiMessage = bun.getString("notiMessage");

		setContentView(R.layout.alertdialog);
		closeBroadcast();
		TextView tvMaker = (TextView) findViewById(R.id.maker);
		TextView tvMaker2 = (TextView) findViewById(R.id.maker2);
		

		Button questionButton = (Button) findViewById(R.id.question);
		Button submitButton = (Button) findViewById(R.id.submit);
		Button cancelButton = (Button) findViewById(R.id.cancel);
		Button tutorialButton = (Button) findViewById(R.id.tutorial);
		
		String blog = "http://blog.naver.com/ohdoking";
		String blog2 = "수린이 블로그";
		String blog3 = "영롱이 블로그";
		tvMaker.setText("만든이 \n오도근(Developer) / " + blog  +"\n김수린(Developer) / "+ blog2 +" \n김영롱(Designer) /" +blog3);

		tvMaker.setMovementMethod(LinkMovementMethod.getInstance());
		Animation anim = AnimationUtils
				.loadAnimation(this, R.anim.slide_in_top);
		tvMaker.startAnimation(anim);
		
		
		tvMaker2.setMovementMethod(LinkMovementMethod.getInstance());


		tutorialButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent goTutorial = new Intent(AlertDialogActivity.this,
						TutorialActivity.class);
				startActivity(goTutorial);
			}
		});

		submitButton.setOnClickListener(new SubmitOnClickListener());
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		tutorialButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AlertDialogActivity.this,
						TutorialActivity.class);
				i.putExtra("checkTuto", "AlertDialog");
				startActivity(i);
				finish();
			}
		});

		questionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setData(Uri.parse("mailto:"));
				emailIntent.setType("text/plain");

				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "ohdoking@gmail.com" });
				 emailIntent.putExtra(Intent.EXTRA_SUBJECT,
				 "subject of email");
				 emailIntent.putExtra(Intent.EXTRA_TEXT , "body of email");
				startActivity(Intent.createChooser(emailIntent, "Send Mail"));
				finish();
			}
		});

	}

	private class SubmitOnClickListener implements OnClickListener {

		public void onClick(View v) {
			Log.i("die", "rrrrrrr죽음!!");

			sendBroadcast(new Intent("listview"));

			finish();
			stopService(new Intent(getApplicationContext(),
					FloatingFaceBubbleService.class));
			android.os.Process.killProcess(android.os.Process.myPid()); // 강제종료
		}
	}
	
	public void closeBroadcast() {
		final BroadcastReceiver br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		};
		registerReceiver(br, new IntentFilter("listview"));
	}
}