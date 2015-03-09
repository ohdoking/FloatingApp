package com.example.floatingbubble;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yapp.mycard.db.ImageDbAdapter;
import com.yapp.mycard.db.SecurityDbAdapter;
import com.yapp.mycard.dto.Image;
import com.yapp.mycard.dto.Security;
import com.yapp.mycard.word.Word;

public class ListActivity extends Activity {
	static int temppwd;
	// variable for selection intent
	private final int PICKER = 1;
	// variable to store the currently selected image
	private int currentPic = 0;
	// gallery object
	private Gallery picGallery;
	// image view for larger display

	private static int RESULT_LOAD_IMAGE = 1;

	private ImageView picView;
	private Button addCard;
	private Button closeWindows;
	private EditText name;
	private EditText cardnum;
	private CheckBox secure;
	private ImageView addCardimg;

	private ImageView EditCarding;
	private EditText nameEdit;
	private EditText cardnumEdit;
	private CheckBox secureEdit;
	private TextView result;

	public Image img;
	public ImageDbAdapter imageDb;
	public SecurityDbAdapter securityDb;

	ArrayList<Image> l;

	private PicAdapter imgAdapt;
	LinearLayout editLayer = null;
	// LinearLayout linear = null;

	public String gallaryImg;

	View linear;

	public AlertDialog al;
	public AlertDialog alAddDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, 
//                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		
		
		setContentView(R.layout.activity_list_acitivity);

		img = new Image();
		imageDb = new ImageDbAdapter(this);
		securityDb = new SecurityDbAdapter(this);

		l = new ArrayList<Image>();
		// 수린
		ifCardListIsEmpty();

		ids();
		listUpdate("selectAll");
		// Add Card
		addCard.setOnClickListener(addImageClicked);

		// Edit Card
		picGallery.setAdapter(imgAdapt);
		picGallery.setOnItemLongClickListener(editImageClicked);

		// set the click listener for each item in the thumbnail gallery
		picGallery.setOnItemClickListener(showCard);

		closeWindows.setOnClickListener(historyBack);

	}

	public void ids() {
		// linear = (LinearLayout) View.inflate(
		// ListActivity.this, R.layout.add_alert, null);
		editLayer = (LinearLayout) View.inflate(ListActivity.this,
				R.layout.edit_alert, null);
		// get the large image view

		// LayoutInflater linf = LayoutInflater.from(this);
		// linear = linf.inflate(R.layout.add_alert, null);

		// get the large image view
		picView = (ImageView) findViewById(R.id.picture);
		// get the gallery view
		picGallery = (Gallery) findViewById(R.id.gallery);

		addCard = (Button) findViewById(R.id.addCard);
		closeWindows = (Button) findViewById(R.id.closeWindows);

		// result = (TextView) findViewById(R.id.result);

		EditCarding = (ImageView) editLayer.findViewById(R.id.editCardimg);
		nameEdit = (EditText) editLayer.findViewById(R.id.edittextCardName);
		cardnumEdit = (EditText) editLayer.findViewById(R.id.edittextCardNum);
		secureEdit = (CheckBox) editLayer.findViewById(R.id.editcheckSecure);
	}

	/*
	 * Click
	 */

	//카드보기
	public OnItemClickListener showCard = new OnItemClickListener() {
		// handle clicks
		
		
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {

			
			
			final LinearLayout linear2 = (LinearLayout) View.inflate(
					ListActivity.this, R.layout.pop_up, null);

			final ImageView tempImg = (ImageView) linear2.findViewById(R.id.imgTest);
			// set the larger image view to display the chosen bitmap calling
			// picView.setImageBitmap(imgAdapt.getPic(position));
			// result.setText( imgAdapt.getImage(position).getName());
			alAddDialog = new AlertDialog.Builder(ListActivity.this)
					.setTitle("추가 페이지").setIcon(R.drawable.floating_img2)
					// .setMessage("hi")
					.setView(linear2).setCancelable(false).show();
			
			final int pos = position;
			final Image imgTemp = null;
			

			final Image selectImg = imgAdapt.getImage(position);
			
              	tempImg.setImageBitmap(BitmapFactory.decodeFile(selectImg.getImg()));
			
//			final Handler handler = new Handler() {
//	            @Override
//	            public void handleMessage(Message message) {
//	            	Image img = (Image)message.obj;
//	            	Drawable drawable = getResources().getDrawable(R.drawable.cancelbtn);
//	                	tempImg.setImageBitmap(BitmapFactory.decodeFile(img.getImg()));
//				Log.i("pic 주소!!!!!! : ",img.getImg());
//	                	
////	                	tempImg.setImageDrawable(drawable);
//	            }
//	        };
//
//	        Thread thread = new Thread() {
//	            @Override
//	            public void run() {
//	                //TODO : set imageView to a "pending" image
//	                Message message = handler.obtainMessage(1, imageDb.select(pos));
//	                handler.sendMessage(message);
//	            }
//	        };
//	        thread.start();
	        
		
		}
	};

	// ���� ��Ƽ��Ƽ�� ���� ��ư�̺�Ʈ
	public OnClickListener historyBack = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	// 이미지를 추가하는 이벤트
	public OnClickListener addImageClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final LinearLayout linear = (LinearLayout) View.inflate(
					ListActivity.this, R.layout.add_alert, null);
			final Image imgTemp = new Image();

			addCardimg = (ImageView) linear.findViewById(R.id.addCardimg);
			name = (EditText) linear.findViewById(R.id.addtextCardName);
			cardnum = (EditText) linear.findViewById(R.id.addtextCardNum);
			secure = (CheckBox) linear.findViewById(R.id.addcheckSecure);

			addCardimg.setOnClickListener(addImage);
			// 키 얼럴트 창이 떠오른다!!!!
			alAddDialog = new AlertDialog.Builder(ListActivity.this)
					.setTitle("추가 페이지")
					.setIcon(R.drawable.floating_img2)
					// .setMessage("hi")
					.setView(linear)
					.setCancelable(false)
					.setNegativeButton("추가",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									imgTemp.setName(name.getText().toString());
									// img.setCardName(Integer.valueOf(cardnum.getText().toString()));
									imgTemp.setCardName(Integer
											.parseInt(cardnum.getText()
													.toString()));
									// imgTemp.setCardName(11);
									imgTemp.setSecure(secure.isChecked());
									
									// TODO 이미지 추가 해줘야함
									 imgTemp.setImg(Word.IMG);
									// l.add(img);
									Log.i("img주소",Word.IMG);
									Log.d("nnnnnn", name.getText().toString()
											+ "!!");
									// result.setText());

									// setImageBitmap(BitmapFactory.decodeFile(picturePath));

									String.valueOf(imageDb.insert(imgTemp));

									refresh();
									alAddDialog.dismiss();

								}
							}).show();

		}
	};
	public OnItemLongClickListener editImageClicked = new OnItemLongClickListener() {

		// handle long clicks
		public boolean onItemLongClick(AdapterView<?> parent, View v,
				int position, long id) {
			//
			// editLayer = (LinearLayout) View.inflate(
			// ListActivity.this, R.layout.edit_alert, null);

			final int pos = position;

			final Image selectImg = imgAdapt.getImage(pos);

			// Context context = getApplicationContext();
			// CharSequence text = "Hello toast!";
			// int duration = Toast.LENGTH_SHORT;
			//
			// Toast.makeText(context, String.valueOf(id), duration).show();
			// update the currently selected position so that we assign the
			// imported bitmap to correct item
			/*
			 * currentPic = position;
			 * 
			 * //take the user to their chosen image selection app (gallery or
			 * file manager) Intent pickIntent = new Intent();
			 * pickIntent.setType("image/*");
			 * pickIntent.setAction(Intent.ACTION_GET_CONTENT); //we will handle
			 * the returned data in onActivityResult
			 * startActivityForResult(Intent.createChooser(pickIntent,
			 * "Select Picture"), PICKER);
			 * 
			 * return true;
			 */

			// 키 얼럴트 창이 떠오른다!!!!

			al = new AlertDialog.Builder(ListActivity.this)
					.setTitle("수정 페이지")
					.setIcon(R.drawable.floating_img2)
					.setView(editLayer)
					.setCancelable(false)
					.setPositiveButton("삭제",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									imageDb.delete(selectImg);

									listUpdate("selectAll");

									refresh();

									al.dismiss();

								}
							})
					.setNegativeButton("수정",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Toast.makeText(ListActivity.this,
											"modify!!", Toast.LENGTH_SHORT)
											.show();

									Image tempImg = new Image();
									tempImg.setName(nameEdit.getText()
											.toString());
									tempImg.setCardName(Integer
											.valueOf(cardnumEdit.getText()
													.toString()));
									tempImg.setSecure(secureEdit.isChecked());
									//TODO 이미지 업데이트시 생각
//									tempImg.setImg(selectImg.getImg());
//									tempImg.setImg(R.drawable.temp_id);
									imageDb.update(tempImg, Integer
											.valueOf(cardnumEdit.getText()
													.toString()));

									refresh();

									al.dismiss();
								}
							})

					.show();

			Log.d("carnum", String.valueOf(selectImg.isSecure()));

			nameEdit.setText(selectImg.getName());
			cardnumEdit.setText(String.valueOf(selectImg.getCardName()));
			secureEdit.setChecked(selectImg.isSecure());
			Log.i("!!!!! 알려줘 주소",selectImg.getImg());
			EditCarding.setImageBitmap(BitmapFactory.decodeFile(selectImg.getImg()));

			return true;

		}

	};

	public OnClickListener addImage = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(i, RESULT_LOAD_IMAGE);
		}
	};

	public void listUpdate(String name) {

		if (name.equals("selectAll")) {
			l = imageDb.selectAll();
			// create a new adapter

			if (!imageDb.isEmpty()) {
				imgAdapt = new PicAdapter(this, l);
			} else {
				picGallery.setBackgroundResource(R.drawable.temp_id);
			}
		} else if (name.equals("delete")) {

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);

			// gallaryImg = picturePat;
			Word.IMG = picturePath;
			cursor.close();
//			tempImg.setImageBitmap(BitmapFactory.decodeFile(Word.IMG));
			addCardimg.setImageBitmap(BitmapFactory.decodeFile(Word.IMG));
			// picView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

		}

	}

	/*
	 * protected void onActivityResult(int requestCode, int resultCode, Intent
	 * data) { Uri pickedUri = data.getData(); //declare the bitmap Bitmap pic =
	 * null;
	 * 
	 * //declare the path string String imgPath = "";
	 * 
	 * if (resultCode == RESULT_OK) { //check if we are returning from picture
	 * selection
	 * 
	 * 
	 * 
	 * if (requestCode == PICKER) { //retrieve the string using media data
	 * String[] medData = { MediaStore.Images.Media.DATA }; //query the data
	 * Cursor picCursor = managedQuery(pickedUri, medData, null, null, null);
	 * if(picCursor!=null) { //get the path string int index =
	 * picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	 * picCursor.moveToFirst(); imgPath = picCursor.getString(index); } else
	 * imgPath = pickedUri.getPath();
	 * 
	 * } } super.onActivityResult(requestCode, resultCode, data);
	 * 
	 * //if we have a new URI attempt to decode the image bitmap
	 * if(pickedUri!=null) { //set the width and height we want to use as
	 * maximum display int targetWidth = 600; int targetHeight = 400;
	 * 
	 * //create bitmap options to calculate and use sample size
	 * BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
	 * 
	 * //first decode image dimensions only - not the image bitmap itself
	 * bmpOptions.inJustDecodeBounds = true; BitmapFactory.decodeFile(imgPath,
	 * bmpOptions);
	 * 
	 * //image width and height before sampling int currHeight =
	 * bmpOptions.outHeight; int currWidth = bmpOptions.outWidth;
	 * 
	 * //variable to store new sample size int sampleSize = 1;
	 * 
	 * //calculate the sample size if the existing size is larger than target
	 * size if (currHeight>targetHeight || currWidth>targetWidth) { //use either
	 * width or height if (currWidth>currHeight) sampleSize =
	 * Math.round((float)currHeight/(float)targetHeight); else sampleSize =
	 * Math.round((float)currWidth/(float)targetWidth); }
	 * 
	 * //use the new sample size bmpOptions.inSampleSize = sampleSize;
	 * 
	 * //now decode the bitmap using sample options
	 * bmpOptions.inJustDecodeBounds = false;
	 * 
	 * //get the file as a bitmap pic = BitmapFactory.decodeFile(imgPath,
	 * bmpOptions); }
	 * 
	 * //pass bitmap to ImageAdapter to add to array imgAdapt.addPic(pic);
	 * //redraw the gallery thumbnails to reflect the new addition
	 * picGallery.setAdapter(imgAdapt);
	 * 
	 * //display the newly selected image at larger size
	 * picView.setImageBitmap(pic); //scale options
	 * picView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	 * 
	 * }
	 */

	/*
	 * refresh
	 */
	public void refresh() {
		finish();
		startActivity(getIntent());
	}

	/*
	 * ID
	 */
	public void setPassword() {
		final LinearLayout setPasswordLinear = (LinearLayout) View.inflate(
				ListActivity.this, R.layout.set_pass_word, null);
		new AlertDialog.Builder(ListActivity.this).setTitle("비밀번호 설정")
				.setIcon(R.drawable.floating_img2).setView(setPasswordLinear)
				.setCancelable(true).setPositiveButton("닫기", null)
				.setNegativeButton("저장", new DialogInterface.OnClickListener() {
					EditText pwd1 = (EditText) setPasswordLinear
							.findViewById(R.id.passwordfirst);
					EditText pwd2 = (EditText) setPasswordLinear
							.findViewById(R.id.passwordsecond);

					public void onClick(DialogInterface dialog, int which) {
						// 비밀번호가 정상적으로 입력됐을 경우.
						if (Integer.parseInt(pwd1.getText().toString()) == Integer
								.parseInt(pwd2.getText().toString())) {
							Security securityTemp = new Security();
							securityTemp.setPw(pwd1.getText().toString());
							Toast.makeText(ListActivity.this,
									"비밀번호가 정상적으로 입력됐습니다.", Toast.LENGTH_SHORT)
									.show();
							temppwd = Integer.parseInt(pwd1.getText()
									.toString());
							securityDb.insert(securityTemp);
							// 비밀번호가 저장되는 부분!
							// setContentView(R.layout.activity_list_acitivity);
							// ImageView img =
							// (ImageView)findViewById(R.id.cardimg);
							// img.setOnClickListener(imgClicked);
						} else {
							Toast.makeText(ListActivity.this,
									"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT)
									.show();
							setPassword();
						}
					}
				}).show();
	}

	public void passwordChecked() {
		// CheckBox check = (CheckBox)findViewById(R.id.ch);
		// 비밀번호 체크여부 체크되어있을경우.
		if (imageDb.isCheckPassword()) {
			ischecked();
		}
		// 비밀번호 유무여부 체크 xxx
		else {
			isUnchecked();
		}

	}

	public void ischecked() {
		final LinearLayout password_linear = (LinearLayout) View.inflate(
				ListActivity.this, R.layout.pass_word, null);
		new AlertDialog.Builder(ListActivity.this).setTitle("추가페이지")
				.setIcon(R.drawable.floating_img2).setView(password_linear)
				.setCancelable(true).setPositiveButton("닫기", null)
				.setNegativeButton("확인", new DialogInterface.OnClickListener() {
					EditText pwd = (EditText) password_linear
							.findViewById(R.id.editText1);

					public void onClick(DialogInterface dialog, int which) {
						// 암호화가 풀려 카드 바코드가 출력되는 부분.
						if (temppwd == Integer.parseInt(pwd.getText()
								.toString())) {
							final LinearLayout linear = (LinearLayout) View
									.inflate(ListActivity.this,
											R.layout.final_page, null);
							new AlertDialog.Builder(ListActivity.this)
									.setTitle("추가페이지")
									.setIcon(R.drawable.floating_img)
									.setView(linear).setCancelable(true)
									.setPositiveButton("닫기", null).show();
						}
						// 비밀번호가 다른 경우!
						else
							Toast.makeText(ListActivity.this, "비밀번호가 틀렸습니다.",
									Toast.LENGTH_SHORT).show();
					}
				}).show();

	}

	public void isUnchecked() {
		final LinearLayout linear = (LinearLayout) View.inflate(
				ListActivity.this, R.layout.final_page, null);
		new AlertDialog.Builder(ListActivity.this).setTitle("추가페이지")
				.setIcon(R.drawable.floating_img2).setView(linear)
				.setCancelable(true).setPositiveButton("닫기", null).show();
	}

	public OnClickListener imgClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			passwordChecked();
		}
	};

	public void ifCardListIsEmpty() {
		// if (securityDb.isPasswordEmpty()) {
		// setPassword();
		// }
	}

	/*
	 * Base Adapter
	 */

	class PicAdapter extends BaseAdapter {

		// use the default gallery background image
		int defaultItemBackground;

		// gallery context
		private Context galleryContext;

		// array to store bitmaps to display
		private Bitmap[] imageBitmaps;

		// placeholder bitmap for empty spaces in gallery
		Bitmap placeholder;
		public ArrayList<Image> image;

		public PicAdapter(Context c, ArrayList<Image> l) {

			// instantiate context
			galleryContext = c;
			image = l;

			// create bitmap array
			imageBitmaps = new Bitmap[l.size()];

			// decode the placeholder image

			// more processing

			// set placeholder as all thumbnail images in the gallery initially
			for (int i = 0; i < imageBitmaps.length; i++) {

//				placeholder = BitmapFactory.decodeResource(
//						galleryContext.getResources(), l.get(i).getImg());
//				placeholder = BitmapFactory.decodeFile(l.get(i).getImg());
				placeholder = BitmapFactory.decodeResource(
						galleryContext.getResources(),R.drawable.redcard);
				imageBitmaps[i] = placeholder;

			}

			// get the styling attributes - use default Andorid system resources
			TypedArray styleAttrs = galleryContext
					.obtainStyledAttributes(R.styleable.PicGallery);

			// get the background resource
			defaultItemBackground = styleAttrs.getResourceId(
					R.styleable.PicGallery_android_galleryItemBackground, 0);

			// recycle attributes
			styleAttrs.recycle();

		}

		@Override
		public int getCount() {
			return imageBitmaps.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// create the view
			ImageView imageView = new ImageView(galleryContext);
			// specify the bitmap at this position in the array
			imageView.setImageBitmap(imageBitmaps[position]);
			// set layout options
			imageView.setLayoutParams(new Gallery.LayoutParams(600, 450));
			// scale type within view area
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			// set default gallery item background
			// imageView.setBackgroundResource(defaultItemBackground);

			// return the view
			return imageView;
		}

		// helper method to add a bitmap to the gallery when the user chooses
		// one
		public void addPic(Bitmap newPic) {
			// set at currently selected index
			imageBitmaps[currentPic] = newPic;
		}

		// return bitmap at specified position for larger display
		public Bitmap getPic(int posn) {
			// return bitmap at posn index
			return imageBitmaps[posn];
		}

		public Image getImage(int id) {
			return image.get(id);
		}

	}

	/*
	 * ETC
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
