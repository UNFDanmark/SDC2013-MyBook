package dk.unf.software.aar2013.gruppe7;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

//import dk.unf.example.camera.R;

public class EditPageActivity extends Activity {
	int currentpage;
	final int camerarequest = 42;
	int RESULT_LOAD_IMAGE = 12;
	EditText text;
	EditText title;
	DBhelper db;
	Cursor c;
	long newRowId;
	Uri currImageURI;
	ImageButton picturebutton;
	Entry entry;
	
	CharSequence[] picturechoices = { "Browse Gallery", "Take a Picture" };

	// private static final int TAKE_PHOTO_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edit_page);
		currentpage = getIntent().getIntExtra("currentpage", -1);
		Log.d("logafos", currentpage + "");

		if (currentpage == -1) {
			finish();
		} else {
			// tekst indtast felter
			title = (EditText) findViewById(R.id.editoverskrift2);
			text = (EditText) findViewById(R.id.edittekst);
			Button createpage = (Button) findViewById(R.id.editpage2);
			// camera ting
			picturebutton = (ImageButton) findViewById(R.id.editpicturebutton);

			// database ting
			db = new DBhelper(this);
			
			
			//hent fra currentpage (sidetallet) data og sæt ind i edittext feltter samt på sidste knap.
			// denne giver mig alt info i en entry dette skal så skrives til felterne
			entry = db.getEntry(currentpage-1);
			title.setText(entry.getTitle());
			text.setText(entry.getText());
			picturebutton.setImageURI(entry.getPhoto());
			

			// sætter knap listener på edit knap
			createpage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Lav intent til Database her!!!
					// her skal klades vores entry så vi får vores data

					
					// "her skal den tage uri'en fra billedet som gemmens når man har fået efter billed intenterne"
					if(currImageURI!=null){
						entry.setPhoto(currImageURI);
					}
					entry.setText(text.getText().toString());
					entry.setTitle(title.getText().toString());

//					Log.d("logafos", "Uri der bliver gemt er: "
//							+ entry.getPhoto().toString());

					db.updateEntry(entry, currentpage-1);
				

					Intent respons = new Intent(getApplicationContext(),
							MainActivity.class);
					setResult(RESULT_OK, respons);
					finish();
				}
			});
			picturebutton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							EditPageActivity.this);
					builder.setTitle("Add Image").setItems(picturechoices,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int picturechoice) {
									if (picturechoice == 0) {
										Intent galleryIntent = new Intent();
										galleryIntent.setType("image/*");
										galleryIntent
												.setAction(Intent.ACTION_GET_CONTENT);
										startActivityForResult(Intent
												.createChooser(galleryIntent,
														"Select Picture"), 1);

										// startActivityForResult(galleryIntent,
										// RESULT_LOAD_IMAGE);
									}
									if (picturechoice == 1) {
										Intent cameraIntent = new Intent(
												MediaStore.ACTION_IMAGE_CAPTURE);
										cameraIntent.putExtra(
												MediaStore.EXTRA_OUTPUT,
												Uri.fromFile(EditPageActivity.this
														.getTemporaryFile()));
										startActivityForResult(cameraIntent,
												camerarequest);
									}
								}
							});
					builder.create().show();

					// To open up a gallery browser

				}
				//
				//
				//
				// // To handle when an image is selected from the browser, add
				// the
				// // following to your Activity
				//
				// // And to convert the image URI to the direct file system
				// path
				// // of the image file

			});
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.d("logafuri", "Resultcode er " + resultCode + "");
		Log.d("logafuri", "requestCode er " + requestCode + "");

		if (resultCode == RESULT_OK) {

			if (requestCode == 1) {

				// currImageURI is the global variable I'm using to hold the
				// content:// URI of the image
				Log.d("logafuri", data.getData().toString());
				currImageURI = data.getData();
			}
			if (requestCode == camerarequest) {

				// Uri selectedImage = data.getData();
				InputStream imageStream = null;

				File file = getTemporaryFile();

				try {
					imageStream = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				Bitmap pic = BitmapFactory.decodeStream(imageStream);

				ImageButton imgBut = (ImageButton) findViewById(R.id.picturebutton);
				// imgBut.setImageBitmap(pic);

			}
		}
	}

	public String getRealPathFromURI(Uri contentUri) {

		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, // Which
																		// columns
																		// to
																		// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	protected File getTemporaryFile() {
		return new File(Environment.getExternalStorageDirectory(), "image.tmp");
	}

}
