package com.yapp.mycard.db;

import com.yapp.mycard.word.Word;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SecurityDbHelper  extends SQLiteOpenHelper{

	public SecurityDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sql = "create table "+Word.SECURITY_DB+" (" +
				"_id integer primary key autoincrement, " +
				"password text);";
		
		db.execSQL(sql);
				
	        
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		String sql = "drop table if exists "+Word.SECURITY_DB+"";

		db.execSQL(sql);

		onCreate(db); // ���̺��� �������Ƿ� �ٽ� ���̺��� ������ִ� ����
	}

}
