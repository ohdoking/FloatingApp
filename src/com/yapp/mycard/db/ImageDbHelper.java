package com.yapp.mycard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageDbHelper extends SQLiteOpenHelper{

	public ImageDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sql = "create table image (" +
				"_id integer primary key autoincrement, " +
				"name text, " +
				"card_name text, " +
				"img text, " +
				"secure numeric, " +
				"etc text);";

		db.execSQL(sql);
				
	        
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		String sql = "drop table if exists image";

		db.execSQL(sql);

		onCreate(db); 
	}

}
