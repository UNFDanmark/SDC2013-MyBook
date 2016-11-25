package dk.unf.software.aar2013.gruppe7;

import android.provider.BaseColumns;

public class FeedEntry implements BaseColumns {

	public static final String TABLE_NAME = "noteDatabase";
	public static final String COLUM_NAME_TITLE = "title";
	public static final String COLUM_NAME_TEXT = "text";
	public static final String COLUM_NAME_FOTO = "foto_id";

	private static final String TEXT_TYPE = " TEXT";
	private static final String INT_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";

	public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ COLUM_NAME_TITLE + TEXT_TYPE + COMMA_SEP + COLUM_NAME_TEXT
			+ TEXT_TYPE + COMMA_SEP + COLUM_NAME_FOTO + TEXT_TYPE + " )";
	
	public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	protected static final String COLUMN_NAME_NULLABLE = null;
	
	private FeedEntry(){
		
	}

	
	
}
