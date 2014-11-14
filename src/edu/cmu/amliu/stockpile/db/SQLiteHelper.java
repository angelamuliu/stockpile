package edu.cmu.amliu.stockpile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * SQLiteHelper
 * Creates the SQL DB
 * SCHEMA:
 * stockrecord > _id, date_made
 * food > _id, stockrecord_id, location, name
 */
public class SQLiteHelper extends SQLiteOpenHelper {
	// SQLiteOpenHelper is a abstract class that manages DB creation 
	
	// Set up some database settings
	private static final String DATABASE_NAME = "foodstocks.db";
	private static final int DATABASE_VERSION = 1;
	
	// Setting up strings that create our tables
	private static final String DATABASE_TBLCREATE_stockrecord = "CREATE TABLE " +
			"stockrecord(_id INTEGER PRIMARY KEY AUTOINCREMENT, date_made TEXT)";
	private static final String DATABASE_TBLCREATE_food = "CREATE TABLE " +
			"food(_id INTEGER PRIMARY KEY AUTOINCREMENT, stockrecord_id INTEGER, " +
			"location TEXT, name TEXT, FOREIGN KEY(stockrecord_id) REFERENCES stockrecord(_id))";
	
	public SQLiteHelper(Context context) {
		// Call SQLiteOpenHelper constructor
		// Params: Context, name, cursorfactory, version
		// Setting cursorfactory to null to use default SQLiteCursor behavior 
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Initialize our database's tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data
		db.execSQL(DATABASE_TBLCREATE_stockrecord);
		db.execSQL(DATABASE_TBLCREATE_food);
	}

	// Drop tables and reset
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("Upgrade", "Upgrading db from version " + oldVersion + " to " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS stockrecord");
		db.execSQL("DROP TABLE IF EXISTS food");
		onCreate(db);
	}
	
}
