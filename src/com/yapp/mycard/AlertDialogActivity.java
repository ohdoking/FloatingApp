package com.yapp.mycard;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

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
		bookmarkItems.add(new SettingItem("종료", "앱을 종료합니다."));
		bookmarkItems.add(new SettingItem("튜토리얼", "튜토리얼을 다시 보고 싶으시면.."));
		bookmarkItems.add(new SettingItem("문의", "문의 사항이나 개선 했으면 하는 점을 알려주세요~"));
		bookmarkItems.add(new SettingItem("백업", "백업 또는 복구 할 수 있습니다."));
		bookmarkItems.add(new SettingItem("개발자 및 디자이너", "개발자 및 디자이너 정보를 볼 수 있습니다."));

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
				} else if (item.getName().toString().equals("개발자 및 디자이너")) {
					IntroduceFunction();
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

		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String userInfo = "Model Id : " + Build.MODEL+
				"\nOs ver : "+android.os.Build.VERSION.RELEASE+
				"\nApp Ver : " + pInfo.versionName +"\n(위의 데이터를 삭제하지마세요!)\n\n -------------------------------- \n\n ";
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("message/rfc822");

		emailIntent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "ohdoking@gmail.com" });
		 emailIntent.putExtra(Intent.EXTRA_SUBJECT,
		 "[문의]문의합니다.");
		 emailIntent.putExtra(Intent.EXTRA_TEXT , userInfo);
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
	
	public void IntroduceFunction(){
		
		final LinearLayout linear = (LinearLayout) View.inflate(
				AlertDialogActivity.this, R.layout.introduce_page, null);
		TextView tv = (TextView) linear.findViewById(R.id.introduce_me);
		String blog = "http://blog.naver.com/ohdoking";
		String mail = "ohdoking@gmail.com";
		String blog2 = "soolibar@gmail.com";
		String blog3 = "pon97206@gmail.com";
		tv.setText("오도근(Developer) \n블로그 : " + blog  +"\n메일 : "+mail +"\n\n김수린(Developer) \n메일 : "+ blog2 +" \n\n김영롱(Designer) \n메일 : " +blog3);
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		Animation anim = AnimationUtils
				.loadAnimation(this, R.anim.slide_in_top);
		tv.startAnimation(anim);
		
		
		tv.setMovementMethod(LinkMovementMethod.getInstance());


		Builder b = new AlertDialog.Builder(AlertDialogActivity.this).setView(
				linear).setCancelable(true);

		backupDialog = b.create();
		backupDialog.setCanceledOnTouchOutside(true);
		backupDialog.show();
		
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