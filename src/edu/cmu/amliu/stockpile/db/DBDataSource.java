package edu.cmu.amliu.stockpile.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*
 * DBDataSource
 * A class that manages CRUD access to the SQL DB
 */
public class DBDataSource {
	
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	
	public DBDataSource(Context context) {
		// The helper also initializes the database
		dbHelper = new SQLiteHelper(context);
	}
	
	// Open up the SQL db, primed for read/writes
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	// Close the db so you stop read/writes
	public void close() {
		dbHelper.close();
	}
	
	// ----------------------------------
	// CRUD Stockrecords
	// ----------------------------------
	
	public Stockrecord create_Stockrecord() {
		ContentValues values = new ContentValues(); // Used to store values you want to insert

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date today = new Date();
		System.out.println(dateFormat.format(today));
		
		values.put("date_made", dateFormat.format(today).toString());
		// 2nd argument simply states that if value is null, then it won't insert a row
		// Insert returns the id assigned to new row
		long newID = database.insert("stockrecord", null, values);

		String[] cols = {"_id", "date_made"}; // What cols we're extracting
	    Cursor cursor = database.query("Stockrecord", cols, "_id = " + (int) newID, null, null, null, null);
	    cursor.moveToFirst();
	    Stockrecord sr = cursorto_Stockrecord(cursor);
	    cursor.close();
	    return sr;
	}
	
	public void delete_Stockrecord(int id) {
		database.delete("stockrecord", "_id = " + id, null);
	}
	
	public List<Stockrecord> getall_Stockrecord() {
		List<Stockrecord> all_stockrecords = new ArrayList<Stockrecord>();
		
		String[] cols = {"_id", "date_made"}; // What cols we're extracting
		Cursor cursor = database.query("stockrecord", cols, null, null, null, null, null);
		
		// Move cursor to first returned row. We'll iterate over them
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Stockrecord sr = cursorto_Stockrecord(cursor);
			all_stockrecords.add(sr);
			cursor.moveToNext();
		}
		cursor.close();
		return all_stockrecords;
	}
	
	// Can translate cursor query results into a Stockrecord obj
	private Stockrecord cursorto_Stockrecord(Cursor cursor) {
		Stockrecord sr = new Stockrecord();
		sr.set_ID(cursor.getInt(0));
		sr.set_date_made(cursor.getString(1));
		return sr;
	}
	
	// ----------------------------------
	// CRUD Food
	// ----------------------------------
	
	public Food create_Food(int stockrecord_id) {
		ContentValues values = new ContentValues();

		values.put("stockrecord_id", stockrecord_id);
		values.put("location", "fillerlocation");
		values.put("name", "fillerfoodname");
		
		// 2nd argument simply states that if value is null, then it won't insert a row
		// Insert returns the id assigned to new row
		long newID = database.insert("food", null, values);

		String[] cols = {"_id", "name", "location", "stockrecord_id"}; // What cols we're extracting
	    Cursor cursor = database.query("food", cols, "_id = " + (int) newID, null, null, null, null);
	    cursor.moveToFirst();
	    Food food = cursorto_Food(cursor);
	    cursor.close();
	    return food;
	}
	
	public void delete_Food(int id) {
		database.delete("food", "_id = " + id, null);
	}
	
	// Returns all the foods underneath a certain stockrecord
	public List<Food> getFood_forSR(int stockrecord_id) {
		Log.d("Get Food for SR", ""+stockrecord_id);
		List<Food> foods = new ArrayList<Food>();
		
		String[] cols = {"_id", "stockrecord_id", "location", "name"};
		String[] whereArgs = new String[] {""+stockrecord_id};
		Log.d("WhereArgs", whereArgs[0]);
		Cursor cursor = database.query("food", cols, "stockrecord_id = ?", whereArgs, null, null, null);
		
		// Move cursor to first returned row. We'll iterate over them
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Food food = cursorto_Food(cursor);
			foods.add(food);
			cursor.moveToNext();
		}
		cursor.close();
		return foods;
	}
	
	// Given a list of foods, converts all foods into a list of strings
	public List<String> foods_toStrList(List<Food> foods) {
		List<String> food_strList = new ArrayList<String>();
		for (int i=0; i<foods.size(); i++) {
			Food viewedfood = foods.get(i);
			food_strList.add(viewedfood.format_Str());
		}
		return food_strList;
	}
	
	// Can translate cursor query results into a Stockrecord obj
	private Food cursorto_Food(Cursor cursor) {
		Food food = new Food();
		food.set_ID(cursor.getInt(0));
		food.set_stockrecord_id(cursor.getInt(1));
		food.set_name(cursor.getString(2));
		food.set_location(cursor.getString(3));
		return food;
	}



}
