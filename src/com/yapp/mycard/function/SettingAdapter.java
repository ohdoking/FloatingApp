package com.yapp.mycard.function;

import java.util.List;

import com.yapp.mycard.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingAdapter extends ArrayAdapter<SettingItem> {

    private List<SettingItem> items;
    private LayoutInflater inflater;

    public SettingAdapter(Context context, int textViewResourceId,
            List<SettingItem> items) {
        super(context, textViewResourceId, items);
        this.items = items;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, null);
        }
        
        // 현재 position 의 내용을 View 로 작성하여 리턴
        SettingItem item = (SettingItem) items.get(position);

        if (item != null) {
            
            TextView bookmarkName = (TextView) view.findViewById(R.id.bookmark_name);
            bookmarkName.setSelected(true);
            bookmarkName.setTypeface(Typeface.DEFAULT_BOLD);
//            bookmarkName.setTextScaleX(2.0f);
            bookmarkName.setTextSize(20.0f);
            bookmarkName.setText(item.getName());
            
            TextView bookmarkUrl = (TextView) view.findViewById(R.id.bookmark_url);
            bookmarkUrl.setSelected(true);
            bookmarkUrl.setText(item.getUrl());

            ImageView img = (ImageView) view.findViewById(R.id.icon);
            if(item.getName().toString().equals("종료"))
            {
            	img.setImageResource(R.drawable.close_icon_white);
            }
            else if(item.getName().toString().equals("튜토리얼"))
            {
            	img.setImageResource(R.drawable.tutorial_icon_white);
            	
            }
            else if(item.getName().toString().equals("문의"))
            {
            	img.setImageResource(R.drawable.mail_icon_white);
            	
            }
            else if(item.getName().toString().equals("백업"))
            {
            	img.setImageResource(R.drawable.backup_icon_white);
            	
            }
            else if(item.getName().toString().equals("개발자 및 디자이너"))
            {
            	img.setImageResource(R.drawable.we_icon_white);
            	
            }
        }
        return view;
    }

}