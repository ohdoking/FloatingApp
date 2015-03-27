package com.example.floatingbubble;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewDebug.FlagToString;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;


public class FloatingFaceBubbleService extends Service {
    private WindowManager windowManager;
    private ImageView floatingFaceBubble;
    
    public void onCreate() {
        super.onCreate();
        floatingFaceBubble = new ImageView(this);
        //a face floating bubble as imageView
        floatingFaceBubble.setImageResource(R.drawable.floating_img3); 

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        //here is all the science of params
        final LayoutParams myParams = new WindowManager.LayoutParams( 
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.TYPE_PHONE,
            LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);

        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x=0;
        myParams.y=100;
        windowManager.addView(floatingFaceBubble, myParams);

        try{
        	//for moving the picture on touch and slide
        	floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = myParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //remove face bubble on long press
//                	if(System.currentTimeMillis()-touchStartTime>ViewConfiguration.getLongPressTimeout() && initialTouchX== event.getX()){
//                		windowManager.removeView(floatingFaceBubble);
//                		stopSelf();
//                		return false;
//                		
//                	}
                	switch(event.getAction()){
                    
                    
                    case MotionEvent.ACTION_DOWN:
                    	touchStartTime = System.currentTimeMillis();
                        initialX = myParams.x;
                        initialY = myParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                    	
                        myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(v, myParams);
                        break;
                    }
                    return false;
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
        floatingFaceBubble.setOnClickListener(new OnClickListener() {
        	int mPublic;
        	
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(FloatingFaceBubbleService.this, "클릭됨~", Toast.LENGTH_SHORT).show();
				Context context = getApplicationContext();
				Intent intent = new Intent(context, ListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				context.startActivity(intent); 
			
			}
		});
        floatingFaceBubble.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
			
//				Toast.makeText(FloatingFaceBubbleService.this, "어플종료", Toast.LENGTH_SHORT).show();
				
				Bundle bun = new Bundle();

				Intent popupIntent = new Intent(getApplicationContext(), AlertDialogActivity.class);

				popupIntent.putExtras(bun);
				PendingIntent pie= PendingIntent.getActivity(getApplicationContext(), 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
				 try {
					pie.send();
				} catch (CanceledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
//				stopSelf();
//				android.os.Process.killProcess(android.os.Process.myPid()); //강제종료
				return true;
			}
		});
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
	
		return null;
	}
//	서비스 종료시 뷰를 제거 해야 한다.
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(floatingFaceBubble != null)        //서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
        {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(floatingFaceBubble);
            floatingFaceBubble = null;
        }
        this.stopSelf();
    	android.os.Process.killProcess(android.os.Process.myPid()); //강제종료

		
    }
    
}

	
