package com.yapp.mycard.function;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DbBackup {

	public static void saveBackup(Context c) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				File BackupDir = new File(sd, "membershipCardFolder");
				BackupDir.mkdir();

				File currentDB = new File(data,
						"//data//com.yapp.mycard//databases//ImageGroup.db");
				File backupDB = new File(sd, "membershipCardFolder/imageBackupDb.db");

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();

				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();

				Toast.makeText(c, "백업이 성공적으로 되었습니다.", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.i("ohdokingBackError",e.getMessage());
			Toast.makeText(c, "Fail", Toast.LENGTH_SHORT).show();
		}

	}

	public static void loadBackup(Context c) {

		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				File backupDB = new File(data,
						"//data//com.yapp.mycard//databases//ImageGroup.db");
				File currentDB = new File(sd, "membershipCardFolder/imageBackupDb.db");

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());

				src.close();
				dst.close();
				Toast.makeText(c, "복원 성공적으로 되었습니다.", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(c, "복원 실패", Toast.LENGTH_SHORT).show();
		}

	}
}
