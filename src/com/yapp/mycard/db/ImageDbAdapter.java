package com.yapp.mycard.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.floatingbubble.R;
import com.yapp.mycard.dto.Image;

public class ImageDbAdapter {

	private Context context;
	SQLiteDatabase db;

	ImageDbHelper helper;

	ArrayList<Image> tempList;

	public ImageDbAdapter(Context context) {
		this.context = context;
		helper = new ImageDbHelper(context, // ���� ȭ���� context
				"ImageGroup.db", // ���ϸ�
				null, // Ŀ�� ���丮
				1); // ���� ��ȣ
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
		values.put("card_name", img.getCardName());
		values.put("etc", "test");
		values.put("secure", img.isSecure());
		values.put("img", img.getImg());

		l = db.insert("image", null, values); 

		return l; // tip : ���콺�� db.insert�� �÷����� �Ű������� � ���� �;� �ϴ��� �� �� �ִ�.

	}

	// update

	public void update(Image img, Integer cardNum) {

		db = helper.getWritableDatabase(); // db ��ü�� ���´�. ���Ⱑ��

		ContentValues values = new ContentValues();

		values.put("name", img.getName());
		values.put("card_name", img.getCardName());
		values.put("etc", img.getEtc());
		values.put("secure", img.isSecure());
//		values.put("img", R.drawable.mintcard);

		db.update("image", values, "card_name=" + cardNum, null);

	}

	// delete

	public void delete(Image img) {

		db = helper.getWritableDatabase();
		db.delete("image", "card_name=" + img.getCardName(), null);

	}

	// select

	public Image select(int select_id) {
		Image img = new Image();

		// 1) db�� �����͸� �о�ͼ�, 2) ��� ����, 3)�ش� �����͸� ���� ���
		db = helper.getReadableDatabase(); // db��ü�� ���´�. �б� ���
		// Cursor c = db.query("image", null, null, null, null, null, null);

		String sql = "SELECT * FROM image WHERE _id =" + select_id;
		Cursor result = db.rawQuery(sql, null);

		// result(Cursor ��ü)�� ��� ������ false ����
		if (result.moveToFirst()) {

			int id = result.getInt(0);
			String name = result.getString(1);
			Long cardName = Long.parseLong(result.getString(2));
			String img2 = result.getString(3);
			// String img2 = result.getString(3);
			boolean secure = Boolean.parseBoolean(result.getString(4));
			String etc = result.getString(5);

			img.setId(id);
			img.setName(name);
			img.setCardName(cardName);
			img.setSecure(secure);
			img.setEtc(etc);
			img.setImg(img2);
		}
		result.close();

		return img;

	}

	public ArrayList<Image> selectAll() {

		String sql = "SELECT * FROM image";
		Cursor result = db.rawQuery(sql, null);

		if (result != null && result.moveToFirst()) {
			tempList = new ArrayList<Image>();
			while (result.isAfterLast() == false) {
				Image img = new Image();
				img.setId(result.getInt(0));
				img.setName(result.getString(1));
				img.setCardName(Long.parseLong(result.getString(2)));
				img.setImg(result.getString(3));
				Log.i("dbohdoking",result.getString(3));
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
