package com.yapp.mycard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class SflashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		setContentView(R.layout.activity_sflash);
		
		ImageView iv = (ImageView)findViewById(R.id.splashView);

		
		// Scaling
		Animation scale = new ScaleAnimation(3.0f, 1.0f, 3.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(3000);
		// Moving up
		Animation slideUp = new TranslateAnimation(
              Animation.RELATIVE_TO_SELF, 3.0f,
              Animation.RELATIVE_TO_SELF, 0.0f, 
              Animation.RELATIVE_TO_SELF, 3.0f,
              Animation.RELATIVE_TO_SELF, 0.9f);
		
		slideUp.setDuration(3000);

			// Animation set to join both scalig and moving
		AnimationSet animSet = new AnimationSet(true);
		animSet.setFillEnabled(true);
		animSet.addAnimation(scale);
		animSet.addAnimation(slideUp);
		
		
		// Launching animation set
		iv.startAnimation(animSet);


		Animation animation = AnimationUtils.loadAnimation(this, R.anim.flash_view_anim);
//		iv.startAnimation(ani);
		
		Thread background = new Thread() {
            public void run() {
                 
                try {
                    // Thread will sleep for 5 seconds
                    sleep(3*1000);
                     
                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),FloatingBubbleActivity.class);
                    startActivity(i);
                     
                    //Remove activity
                    finish();
                     
                } catch (Exception e) {
                 
                }
            }
        };
         
        // start thread
        background.start();
		
	}
	
	
	

}
