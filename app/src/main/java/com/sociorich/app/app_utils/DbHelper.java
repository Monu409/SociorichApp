package com.sociorich.app.app_utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	static String DATABASE_NAME="usermedia";
	public static final String TABLE_NAME="media";
	public static final String KEY_PNAME="type";
	public static final String KEY_PRICE="url";
	public static final String KEY_QTY="relativeUrl";

	public static final String KEY_ID="id";


	SQLiteDatabase dataBase;
	public static final int DB_VERSION = 1;
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_PNAME+" TEXT, "+KEY_PRICE+" TEXT,"+KEY_QTY+" TEXT)";
		db.execSQL(CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
		onCreate(db);

	}


}
