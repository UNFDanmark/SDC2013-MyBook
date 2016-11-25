package dk.unf.software.aar2013.gruppe7;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class DBhelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "noteDatabase.db";

	public DBhelper(Context con) {
		super(con, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(FeedEntry.SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// pas på alt bliver slettet
		db.execSQL(FeedEntry.SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	public List<String> getTitles() {
		List<String> titles = new ArrayList<String>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM noteDatabase", null);

		if (c.moveToFirst()) {
			do {
				titles.add(c.getString(c
						.getColumnIndexOrThrow(FeedEntry.COLUM_NAME_TITLE)));
			} while (c.moveToNext());

		}
		c.close();
		db.close();
		return titles;
	}

	public int getCount() {

		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM noteDatabase", null);

		int count = c.getCount() + 1;

		c.close();
		db.close();

		return count;
	}

	public void addEntry(Entry entry) {
		// her mangler at at komme det med Uri ind.
		SQLiteDatabase db = getWritableDatabase();
		Uri uri = entry.getPhoto();

		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUM_NAME_TITLE, entry.getTitle());
		values.put(FeedEntry.COLUM_NAME_TEXT, entry.getText());
		if (uri != null) {
			Log.d("logafuri", uri.toString());
			values.put(FeedEntry.COLUM_NAME_FOTO, entry.getPhoto().toString());
		}

		db.insert(FeedEntry.TABLE_NAME, FeedEntry.COLUMN_NAME_NULLABLE, values);

		db.close();
	}

	public Entry getEntry(int id) {

		// hent fra db
		SQLiteDatabase db = getReadableDatabase();

		String[] projection = { FeedEntry._ID, FeedEntry.COLUM_NAME_TITLE };
		String where = FeedEntry._ID;
		String[] whereArgs = { id + "" };
		// db.query(FeedEntry.TABLE_NAME, projection, where, whereArgs,
		// null, null, null);
		Cursor c = db.rawQuery("SELECT * FROM " + FeedEntry.TABLE_NAME, null);

		Entry entry = new Entry();

		if (c.moveToPosition(id)) {
			// gem i entry objekt
			// "her skal den tage uri'en fra billedet som gemmens når man har fået efter billed intenterne"
			String img = c.getString(c
					.getColumnIndexOrThrow(FeedEntry.COLUM_NAME_FOTO));
			if (img != null) {
				entry.setPhoto(Uri.parse(img));
				Log.d("photo_uri", img);
			}

			entry.setText(c.getString(c
					.getColumnIndexOrThrow(FeedEntry.COLUM_NAME_TEXT)));
			entry.setTitle(c.getString(c
					.getColumnIndexOrThrow(FeedEntry.COLUM_NAME_TITLE)));
		} else {
			Log.d("Tag", "No working");
		}

		// luk database
		c.close();
		db.close();
		// og retrun objekt
		return entry;

	}

	public void deleteEntry(int id) {

		Log.d("logafos", id + "");

		String where;
		// hent fra db
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM " + FeedEntry.TABLE_NAME, null);

		if (c.moveToPosition(id)) {
			int nummeridb = c.getInt(c.getColumnIndexOrThrow(FeedEntry._ID));
			String[] whereArgs = { String.valueOf(nummeridb) };

			int result = db.delete(FeedEntry.TABLE_NAME, "_ID=?", whereArgs);
			Log.d("logafos", "har slettet" + result);
		} else {
			Log.d("logafos", "No working");
		}

		// luk database
		db.close();
	}

	public void updateEntry(Entry entry, int id) {

		// New value's for one column
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUM_NAME_TITLE, entry.getTitle()); // title
		values.put(FeedEntry.COLUM_NAME_TEXT, entry.getText()); // text
		//values.put(FeedEntry.COLUM_NAME_FOTO, entry.getPhoto().toString()); // billed
																			// uri

		// find ID
		SQLiteDatabase db = getWritableDatabase();

		Cursor c = db.rawQuery("SELECT * FROM " + FeedEntry.TABLE_NAME, null);

		if (c.moveToPosition(id)) {
			int nummeridb = c.getInt(c.getColumnIndexOrThrow(FeedEntry._ID));

			// Which row to update, based on the ID
			String selection = FeedEntry._ID + "=?";
			String[] selectionArgs = { String.valueOf(nummeridb) };

			db.update(FeedEntry.TABLE_NAME, values, selection, selectionArgs);

		} else {
			Log.d("logafos", "No working");
		}

	}
}
