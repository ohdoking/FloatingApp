package com.yapp.mycard;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import com.yapp.mycard.function.DbBackup;
import com.yapp.mycard.function.SettingAdapter;
import com.yapp.mycard.function.SettingItem;

public class AlertDialogActivity extends ListActivity {

	private List<SettingItem> bookmarkItems;
	private SettingAdapter bookmarkAdapter;

	private String notiMessage;
	private BroadcastReceiver locationChangereceiver;

	public AlertDialog backupDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("BOOKMARKS");
		setFinishOnTouchOutside(true);

		closeBroadcast();
		
		// 북마크 리스트에 아이템을 추가
		bookmarkItems = new ArrayList<SettingItem>();
		bookmarkItems.add(new SettingItem("종료", "앱을 종료시킬 수 있습니다."));
		bookmarkItems.add(new SettingItem("튜토리얼", "튜토리얼을 다시 보고 싶으시면.."));
		bookmarkItems.add(new SettingItem("문의", "문의 사항이나 개선 했으면 하는 점을 알려주세요~"));
		bookmarkItems.add(new SettingItem("백업", "백업 또는 복구 할 수 있습니다."));
		bookmarkItems.add(new SettingItem("개발자", "개발자 정보를 볼 수 있습니다."));

		// 북마크 Adapter 작성
		bookmarkAdapter = new SettingAdapter(this, R.layout.list_item,
				bookmarkItems);

		setListAdapter(bookmarkAdapter);

		// 이벤트 리스너 설정
		// 이미 슈퍼클래스에서 onListItemClick 메소드가 준비되어 있으므로, 아래 리스너의 설정은 꼭 해야하는건 아니다.
		ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListView listView = (ListView) parent;
				SettingItem item = (SettingItem) listView
						.getItemAtPosition(position);
				Log.d("JO", "clicked item => " + item.getName());

				if (item.getName().toString().equals("종료")) {
					exitApplicationFunction();
				} else if (item.getName().toString().equals("튜토리얼")) {
					tutorialFunction();
				} else if (item.getName().toString().equals("문의")) {
					questionFuntion();
				} else if (item.getName().toString().equals("백업")) {
					backUpFunction();
				} else if (item.getName().toString().equals("개발자")) {

				}
			}

		});

	}


	
	
	/*
	 * 
	 * 
	 *  backup / 복원
	 */
	void backUpFunction() {

		final LinearLayout linear = (LinearLayout) View.inflate(
				AlertDialogActivity.this, R.layout.backup_check, null);
		final Button saveBackup = (Button) linear.findViewById(R.id.saveBackup);
		final Button loadBackup = (Button) linear.findViewById(R.id.loadBackup);

		loadBackup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(AlertDialogActivity.this)
						.setTitle("복구")
						.setMessage(
								"기존 데이터는 모두 사라지고 백업된 파일로 복구됩니다. 정말 복구하시겠습니까?")
						// .setView(editLayer)
						.setPositiveButton("아니오", null)
						.setNegativeButton("네",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										DbBackup.loadBackup(getApplicationContext());

									}
								})

						.setCancelable(true).show();

			}
		});

		saveBackup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DbBackup.saveBackup(getApplicationContext());

			}
		});

		Builder b = new AlertDialog.Builder(AlertDialogActivity.this).setView(
				linear).setCancelable(true);

		backupDialog = b.create();
		backupDialog.setCanceledOnTouchOutside(true);
		backupDialog.show();

	}
	
	
	void tutorialFunction(){
		Intent i = new Intent(AlertDialogActivity.this,
				TutorialActivity.class);
		i.putExtra("checkTuto", "AlertDialog");
		startActivity(i);
		finish();
	}
	
	void questionFuntion(){
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
	
	
	void exitApplicationFunction(){
		
		Log.i("die", "rrrrrrr죽음!!");
		
		sendBroadcast(new Intent("listview"));
		
		finish();
		stopService(new Intent(getApplicationContext(),
				FloatingFaceBubbleService.class));
		android.os.Process.killProcess(android.os.Process.myPid()); // 강제종료
	}
	
	
	public void closeBroadcast() {
		final BroadcastReceiver br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				unregisterReceiver(this);
				finish();
			}

		};
		registerReceiver(br, new IntentFilter("listview"));
	}

}