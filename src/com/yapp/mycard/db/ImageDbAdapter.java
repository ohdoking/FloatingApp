package com.yapp.mycard.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yapp.mycard.dto.Image;

public class ImageDbAdapter {

	private Context context;
	SQLiteDatabase db;

	ImageDbHelper helper;

	ArrayList<Image> tempList;

	public ImageDbAdapter(Context context) {
		this.context = context;
		helper = new ImageDbHelper(context, 
				"ImageGroup.db", 
				null, 
				1); 
		db = helper.getWritableDatabase();
	}

	// insert

	private SQLiteDatabase getWritableDatabase() {
		return null;
	}

	public long insert(Image img) {

		db = helper.getWritableDatabase();
		Long l;
		ContentValues values = new ContentValues();
		values.put("name", img.getName());
		values.put("card_name", img.getCardNum());
		values.put("etc", "test");
		values.put("secure", img.isSecure());
		values.put("img", img.getImg());

		l = db.insert("image", null, values); 

		return l; 

	}

	// update

	public void update(Image img, String cardNum) {

		db = helper.getWritableDatabase(); 

		ContentValues values = new ContentValues();

		values.put("name", img.getName());
		values.put("card_name", img.getCardNum());
		values.put("etc", img.getEtc());
		values.put("secure", img.isSecure());
		values.put("img", img.getImg());

		Log.i("ohdoking",String.valueOf(cardNum));
		db.update("image", values, "card_name=" + cardNum, null);

	}

	// delete

	public void delete(Image img) {

		db = helper.getWritableDatabase();
		db.delete("image", "card_name=" + img.getCardNum(), null);

	}

	// select

	public ArrayList<Image> select(String name) {
		ArrayList<Image> imgList = new ArrayList<Image>();
		int i = 0;
		db = helper.getReadableDatabase(); 

//		String sql = "SELECT * FROM image WHERE name =" + name +"";
		
		if (name.length() != 0) {

	        name = "%"+ name + "%";
	    }
		
		String[] columns={"name", "card_name","img"};
		String[] parms={name};
		Cursor result=db.query("image", columns, "name LIKE ?", parms, null, null, null);
		

		
		
		if (result != null && result.moveToFirst()) {
			while (result.isAfterLast() == false) {

			String cardName = result.getString(0);
			String cardNum = result.getString(1);
			String img2 = result.getString(2);
			Image img = new Image();
			img.setName(cardName);
			img.setCardNum(cardNum);
			img.setImg(img2);
			
			imgList.add(img);
			result.moveToNext();
			}
		}
		result.close();

		return imgList;

	}

	public ArrayList<Image> selectAll(String arrange) {

		String sql = null ;
		if(arrange.equals("asc"))
		{
		
			sql = "SELECT * FROM image order by name asc";
		}
		else if(arrange.equals("desc"))
		{
			
			sql = "SELECT * FROM image order by name desc";
			
		}
		Cursor result = db.rawQuery(sql, null);

		if (result != null && result.moveToFirst()) {
			tempList = new ArrayList<Image>();
			while (result.isAfterLast() == false) {
				Image img = new Image();
				img.setId(result.getInt(0));
				img.setName(result.getString(1));
				img.setCardNum(result.getString(2));
				img.setImg(result.getString(3));
				img.setSecure(Boolean.parseBoolean(result.getString(4)));
				img.setEtc(result.getString(5));

				tempList.add(img);
				result.moveToNext();
			}
		}
		result.close();

		return tempList;

	}

	public boolean isEmpty() {

		boolean empty = true;
		Cursor cur = db.rawQuery("SELECT COUNT(*) FROM image", null);
		if (cur != null && cur.moveToFirst()) {
			empty = (cur.getInt(0) == 0);
		}
		cur.close();

		return empty;
	}

	
	
	public boolean checkedPassword(){
		return true;
	}

	public boolean isCheckPassword() {
		return false;
	}
	
}
