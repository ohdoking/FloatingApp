package com.example.floatingbubble;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
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
	private TextView currentCardName;
	
	private ImageView editCarding;
	private EditText nameEdit;
	private EditText cardnumEdit;
	private CheckBox secureEdit;
	
	private ImageView showBigImg;
	private TextView result;

	public Image img;
	public ImageDbAdapter imageDb;

	ArrayList<Image> l;

	private PicAdapter imgAdapt;
	LinearLayout editLayer = null;
	// LinearLayout linear = null;

	public String gallaryImg;

	View linear;

	public AlertDialog al;
	public AlertDialog alAddDialog;
	public AlertDialog alShowDialog;
	
	Bitmap bitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Log.i("test","1");
		setContentView(R.layout.activity_list_acitivity);

		img = new Image();
		imageDb = new ImageDbAdapter(this);

		l = new ArrayList<Image>();

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

		picView = (ImageView) findViewById(R.id.picture);
		picGallery = (Gallery) findViewById(R.id.gallery);
		currentCardName = (TextView)findViewById(R.id.cuurentCardName);
		addCard = (Button) findViewById(R.id.addCard);
		closeWindows = (Button) findViewById(R.id.closeWindows);
		
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
			final TextView tempText = (TextView)linear2.findViewById(R.id.imgText);
			// set the larger image view to display the chosen bitmap calling
			// picView.setImageBitmap(imgAdapt.getPic(position));
			// result.setText( imgAdapt.getImage(position).getName());
			alAddDialog = new AlertDialog.Builder(ListActivity.this)
					.setView(linear2).setCancelable(true).show();
			
			final int pos = position;
			final Image imgTemp = null;
			

			final Image selectImg = imgAdapt.getImage(position);
			
//			currentCardName.setText(selectImg.getName());
			imgAdapt.notifyDataSetChanged();
			
              	tempImg.setImageBitmap(BitmapFactory.decodeFile(selectImg.getImg()));
              	tempText.setText(String.valueOf(selectImg.getCardName()));
              	
              	tempImg.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						final LinearLayout linear3 = (LinearLayout) View.inflate(
								ListActivity.this, R.layout.big_pop_up, null);
						
						alShowDialog = new AlertDialog.Builder(ListActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
						.setView(linear3).setCancelable(true).show();
						
						showBigImg= (ImageView)linear3.findViewById(R.id.bigImg);
						
						showBigImg.setImageBitmap(BitmapFactory.decodeFile(selectImg.getImg()));
						
						alShowDialog.setOnKeyListener(new Dialog.OnKeyListener() {

				            @Override
				            public boolean onKey(DialogInterface arg0, int keyCode,
				                    KeyEvent event) {
				                // TODO Auto-generated method stub
				                if (keyCode == KeyEvent.KEYCODE_BACK) {
				                    alShowDialog.dismiss();
				                    linear3.removeView(linear3);
//				                    linear3 = (LinearLayout) View.inflate(
//				        					ListActivity.this, R.layout.big_pop_up, null);
				                }
				                return true;
				            }
				        });
						
						
					}
				});
			

	        
		
		}
	};

	// x 버튼 누르면 종료 
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
					.setIcon(R.drawable.floating_img3)
					// .setMessage("hi")
					.setView(linear)
					.setCancelable(true)
					.setNegativeButton("추가",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									imgTemp.setName(name.getText().toString());
									imgTemp.setCardName(Long
											.parseLong(cardnum.getText()
													.toString()));
									imgTemp.setSecure(secure.isChecked());
									imgTemp.setImg(Word.IMG);
									
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
			editLayer = (LinearLayout) View.inflate(ListActivity.this,
					R.layout.edit_alert, null);
			editCarding = (ImageView) editLayer.findViewById(R.id.editCardimg);
			nameEdit = (EditText) editLayer.findViewById(R.id.edittextCardName);
			cardnumEdit = (EditText) editLayer.findViewById(R.id.edittextCardNum);
			secureEdit = (CheckBox) editLayer.findViewById(R.id.editcheckSecure);
			
			editCarding.setOnClickListener(editImage);


			al = new AlertDialog.Builder(ListActivity.this)
					.setTitle("수정 페이지")
					.setIcon(R.drawable.floating_img3)
					.setView(editLayer)
					.setCancelable(true)
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
									tempImg.setCardName(Long
											.valueOf(cardnumEdit.getText()
													.toString()));
									tempImg.setSecure(secureEdit.isChecked());
									//TODO 이미지 업데이트시 생각
//									tempImg.setImg(selectImg.getImg());
									imageDb.update(tempImg, Integer
											.valueOf(cardnumEdit.getText()
													.toString()));

									refresh();

									al.dismiss();
								}
							})

					.show();
			
			al.setOnKeyListener(new Dialog.OnKeyListener() {
	            @Override
	            public boolean onKey(DialogInterface arg0, int keyCode,
	                    KeyEvent event) {
	                // TODO Auto-generated method stub
	                if (keyCode == KeyEvent.KEYCODE_BACK) {
//	                    finish();
	                    al.dismiss();
	                }
	                return true;
	            }
	        });

			Log.d("carnum", String.valueOf(selectImg.isSecure()));

			nameEdit.setText(selectImg.getName());
			cardnumEdit.setText(String.valueOf(selectImg.getCardName()));
			secureEdit.setChecked(selectImg.isSecure());
			editCarding.setImageBitmap(BitmapFactory.decodeFile(selectImg.getImg()));

			return true;

		}

	};

	public OnClickListener addImage = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
/*			
 * 갤러리 불러오기
 * Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(i, RESULT_LOAD_IMAGE);
			
			
*/		
			Intent i = new Intent("com.google.zxing.client.android.SCAN");
//			Intent i = new Intent(ListActivity.this,CaptureActivity.class);
//			i.putExtra("SCAN_MODE", "QR_CODE_MODE");
			
			startActivityForResult(i, 0);
			
		
		}
	};
	
	public OnClickListener editImage = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent i = new Intent("com.google.zxing.client.android.SCAN");
			Log.i("??","여기요");
			startActivityForResult(i, 1);
			
		
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
		
		if (requestCode == 0 || requestCode == 1) {
		      if (resultCode == RESULT_OK) {
		         String contents = data.getStringExtra("SCAN_RESULT");
		         String format = data.getStringExtra("SCAN_RESULT_FORMAT");
		         BarcodeFormat barcodeFormat = BarcodeFormat.valueOf(format);
		         
		         String barcode_data = contents;
		         
		     	try {
		     		
					bitmap = encodeAsBitmap(barcode_data, barcodeFormat, 600,
							300);
					String temS= getDate(System.currentTimeMillis());
					String resultPath = null;
					resultPath = saveBitmaptoJpeg(bitmap,"barcode", "barcode_" + temS);
					Word.IMG = resultPath;
					Log.i("resultPathOhdoking",resultPath);
					
					
					if(requestCode == 0)
					{
						
						addCardimg.setImageBitmap(bitmap);
						cardnum.setText(contents);
					}
					else if(requestCode == 1)
					{
						editCarding.setImageBitmap(bitmap);
						cardnumEdit.setText(contents);
						
					}


				} catch (WriterException e) {
					e.printStackTrace();
				}
		         
		         
		         // Handle successful scan
		      } else if (resultCode == RESULT_CANCELED) {
		         // Handle cancel
		      }
		   }

		/*if (requestCode == RESULT_LOAD_IMAGE 
				&& resultCode == RESULT_OK
				&& null != data) 
		{
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
			addCardimg.setImageBitmap(BitmapFactory.decodeFile(Word.IMG));

		}*/

	}
	
	/*
	 *  bitmap을 jpeg 변환하여 갤러리에 저장
	 */
	
	public static String saveBitmaptoJpeg(Bitmap bitmap,String folder, String name){
    	String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                                   // Get Absolute Path in External Sdcard 
    	String foler_name = "/"+folder+"/";
    	String file_name = name+".jpg";
    	String string_path = ex_storage+foler_name;
    	String result_path = string_path+file_name;
    	
    	File file_path; 
    	try{
    		file_path = new File(string_path);
    		if(!file_path.isDirectory()){
    			file_path.mkdirs();
    		}
    		FileOutputStream out = new FileOutputStream(string_path+file_name);
    		
    		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
    		out.close(); 
    		
    		return result_path;
    
    	}catch(FileNotFoundException exception){
    		Log.e("FileNotFoundException", exception.getMessage());
    	}catch(IOException exception){
    		Log.e("IOException", exception.getMessage());
    	}
    	
    	return "error";
    	
    }
	
	
	private String getDate(long time) {
	    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
	    cal.setTimeInMillis(time);
	    String date = DateFormat.format("ssmmHHddMMyyyy", cal).toString();
	    Log.i("timeOhdoing",date);
	    return date;
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
				.setIcon(R.drawable.floating_img3).setView(setPasswordLinear)
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
				.setIcon(R.drawable.floating_img3).setView(password_linear)
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
									.setIcon(R.drawable.floating_img3)
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
				.setIcon(R.drawable.floating_img3).setView(linear)
				.setCancelable(true).setPositiveButton("닫기", null).show();
	}

	public OnClickListener imgClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			passwordChecked();
		}
	};


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
						galleryContext.getResources(),R.drawable.realcard);
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
			//이미지 크기 조절 및 위치
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
				
				currentCardName.setTextColor(Color.WHITE);
//				currentCardName.setText(getImage(position).getName());

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
	 *  뒤로가기 (back)
	 */

	
	/*	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
     // TODO Auto-generated method stub

     	if( keyCode == KeyEvent.KEYCODE_BACK){
     		
     		Toast.makeText(this, "Back키를 누르셨군요", Toast.LENGTH_SHORT).show();
     		return false;
     	}
     	return super.onKeyDown(keyCode, event);
}*/
	

	/**************************************************************
	 * getting from com.google.zxing.client.android.encode.QRCodeEncoder
	 * 
	 * See the sites below http://code.google.com/p/zxing/
	 * http://code.google.com
	 * /p/zxing/source/browse/trunk/android/src/com/google/
	 * zxing/client/android/encode/EncodeActivity.java
	 * http://code.google.com/p/zxing
	 * /source/browse/trunk/android/src/com/google/
	 * zxing/client/android/encode/QRCodeEncoder.java
	 * 
	 * 쉽게말해서 바코드 만들어주는 코드
	 */

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width,
			int img_height) throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contentsToEncode);
		if (encoding != null) {
			hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result;
		try {
			result = writer.encode(contentsToEncode, format, img_width,
					img_height, hints);
		} catch (IllegalArgumentException iae) {
			// Unsupported format
			return null;
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
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
