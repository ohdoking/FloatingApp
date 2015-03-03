package com.yapp.mycard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yapp.mycard.dto.Security;
import com.yapp.mycard.word.Word;

public class SecurityDbAdapter {

	
		private Context context;
		SQLiteDatabase db;
	
		SecurityDbHelper helper;
	
		
		public SecurityDbAdapter(Context context) {
			this.context = context;
			helper = new SecurityDbHelper(context,
					"ImageGroup.db", 
					null, 
					1); 
			db = helper.getWritableDatabase();
		}
	
		// insert
	
		private SQLiteDatabase getWritableDatabase() {
			return null;
		}
	
		public long insert(Security pwGroup) {
	
			db = helper.getWritableDatabase(); // db ��ü�� ���´�. ���� ����
			Long l;
			ContentValues values = new ContentValues();
			// db.insert�� �Ű������� values�� ContentValues �����̹Ƿ� �׿� ����
	
			values.put("password", pwGroup.getPw());
	
			l = db.insert(Word.SECURITY_DB, null, values); // ���̺�/���÷���/������(���÷���=����Ʈ)
	
			return l; // tip : ���콺�� db.insert�� �÷����� �Ű������� � ���� �;� �ϴ��� �� �� �ִ�.
	
		}
	
		// select
	
		public Security select(int pw) {
			Security se = new Security();
	
			// 1) db�� �����͸� �о�ͼ�, 2) ��� ����, 3)�ش� �����͸� ���� ���
			db = helper.getReadableDatabase(); // db��ü�� ���´�. �б� ���
			// Cursor c = db.query("image", null, null, null, null, null, null);
	
			String sql = "SELECT * FROM "+Word.SECURITY_DB+" WHERE password =" + pw;
			Cursor result = db.rawQuery(sql, null);
	
			// result(Cursor ��ü)�� ��� ������ false ����
			if (result.moveToFirst()) {
	
				int id = result.getInt(0);
				String pwTemp = result.getString(1);
	
				se.setId(id);
				se.setPw(pwTemp);
			}
			result.close();
	
			return se;
	
		}
	
	
		public boolean isPasswordEmpty() {
	
			boolean empty = true;
			Cursor cur = db.rawQuery("SELECT COUNT(*) FROM "+Word.SECURITY_DB, null);
			if (cur != null && cur.moveToFirst()) {
				empty = (cur.getInt(0) == 0);
			}
			cur.close();
	
			return empty;
		}
	

}
