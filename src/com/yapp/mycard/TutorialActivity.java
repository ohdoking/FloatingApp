package com.yapp.mycard;

/*
 * Copyright (c) 2012 Wireless Designs, LLC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
 
import com.example.floatingbubble.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
 
/**
 * PagerActivity: A Sample Activity for PagerContainer
 */
public class TutorialActivity extends Activity {
 
    PagerContainer mContainer;
    Intent intent;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        intent = getIntent(); // 값을 받아온다.
        
        if(getPreferences().equals("false") && intent.getStringExtra("checkTuto").equals("Bubble"))
        {
        	Intent i = new Intent(TutorialActivity.this,ListActivity.class);
        	startActivity(i);
        	finish();
        }
        
        setContentView(R.layout.activity_tutorial);
 
        mContainer = (PagerContainer) findViewById(R.id.pager_container);
 
        ViewPager pager = mContainer.getViewPager();
        PagerAdapter adapter = new MyPagerAdapter();
        pager.setAdapter(adapter);
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(adapter.getCount());
        //A little space between pages
        pager.setPageMargin(15);
 
        
        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.setClipChildren(false);
        
        

    
    }
 
    //Nothing special about this adapter, just throwing up colored views for demo
    private class MyPagerAdapter extends PagerAdapter {
 
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView view = new TextView(TutorialActivity.this);
            view.setText("Item "+position);
            view.setGravity(Gravity.CENTER);
            view.setTextColor(Color.BLUE);
            ImageView iv = new ImageView(TutorialActivity.this);
            
            
            LinearLayout l = new LinearLayout(TutorialActivity.this);
            l.setOrientation(LinearLayout.VERTICAL);
            
            
           
            
            if(position % 5 == 0 ){
            	
            	iv.setImageResource(R.drawable.greencard);
            	l.addView(iv,0);
            	l.addView(view,1);
            }
            else if(position % 5  == 1)
            {
            	iv.setImageResource(R.drawable.exit);
            	l.addView(iv,0);
            	l.addView(view,1);
            	
            }
            else if(position % 5  == 2)
            {
            	iv.setImageResource(R.drawable.floating_bubble);
            	l.addView(iv,0);
            	l.addView(view,1);
            	
            }
            else if(position % 5  == 3)
            {
            	iv.setImageResource(R.drawable.pinkcard);
            	l.addView(iv,0);
            	l.addView(view,1);
            }
            else
            {
            	iv.setImageResource(R.drawable.pinkcard);
            	 LinearLayout lChild = new LinearLayout(TutorialActivity.this);
                 TextView tvCb = new TextView(TutorialActivity.this);
                 Button btnOutTutorial = new Button(TutorialActivity.this);
            	 
            	if(!intent.getStringExtra("checkTuto").equals("AlertDialog")){
            		
            		final CheckBox cb = new CheckBox(TutorialActivity.this);
            		lChild.addView(cb,0);
            		 tvCb.setTextColor(Color.BLUE);
                	 tvCb.setText("튜토리얼을 그만 보시겠습니까?");
                	 
                	 lChild.setGravity(Gravity.CENTER);
                	 
                	 lChild.addView(tvCb,1);
                	  btnOutTutorial.setText("튜토리얼 종료");
                      btnOutTutorial.setOnClickListener(new OnClickListener() {
  						
  						@Override
  						public void onClick(View v) {
  							
  								if(cb.isChecked())
  								{
  									savePreferences("false");
  								}
  								
  								Intent i = new Intent(TutorialActivity.this,ListActivity.class);
  					        	startActivity(i);
  					        	finish();
  						}
  					});
              	 
            	} 
            	else
            	{
            		  btnOutTutorial.setText("튜토리얼 종료");
                      btnOutTutorial.setOnClickListener(new OnClickListener() {
  						
  						@Override
  						public void onClick(View v) {
  							
  								
  								Intent i = new Intent(TutorialActivity.this,ListActivity.class);
  					        	startActivity(i);
  					        	finish();
  						}
  						});
            	}
            	
            	 
            	 
            	 l.addView(iv,0);
            	 l.addView(lChild,1);
            	 l.addView(btnOutTutorial,1);
                
            	
            	
            }
            
//            view.setBackgroundColor(Color.argb(255, position * 50, position * 10, position * 50));
// 
//            container.addView(view);
            
            container.addView(l,0);
            return l;
        }
 
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
 
        @Override
        public int getCount() {
            return 5;
        }
 
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }
    
    // 값 저장하기
    private void savePreferences(String value){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("checkTutorial", value);
        editor.commit();
    }
    
    private String getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString("checkTutorial", "");
    }
}