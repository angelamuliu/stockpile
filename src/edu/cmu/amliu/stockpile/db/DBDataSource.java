package edu.cmu.amliu.stockpile.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * DBDataSource
 * A class that manages CRUD access to the SQL DB for stockrecords and food.
 * In addition, this class also manages SharedPreference saved K-V pairs specifically for
 * keeping count of food.
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
		// Insert values into table stockrecord,
		// 2nd argument simply states that IF value is null, then it won't insert a row
		// Insert returns the id assigned to new row
		long newID = database.insert("stockrecord", null, values);

		String[] cols = {"_id", "date_made"}; // What cols we're extracting from stockrecord table
		// Query the database to get the just made stockrecord
	    Cursor cursor = database.query("Stockrecord", cols, "_id = " + (int) newID, null, null, null, null);
	    
	    Log.d("Number found", ""+cursor.getCount());
	    
	    // If we SOMEHOW got more than one returned row, move it to the first row
	    cursor.moveToFirst();
	    Stockrecord sr = cursorto_Stockrecord(cursor);
	    cursor.close();
	    return sr;
	}
	
	/**
	 * Deletes a stockrecord and any associated food records from the db
	 * @param id
	 */
	public void delete_Stockrecord(int id) {
		database.delete("stockrecord", "_id = " + id, null);
		// Delete all foods who were associated with the stock record
		database.delete("food", "stockrecord_id = " + id, null);
	}
	
	/**
	 * Gets all stockrecords ordered by id (so by which was most recently created)
	 * @return
	 */
	public List<Stockrecord> getall_Stockrecord() {
		List<Stockrecord> all_stockrecords = new ArrayList<Stockrecord>();
		
		String[] cols = {"_id", "date_made"}; // What cols we're extracting
		Cursor cursor = database.query("stockrecord", cols, null, null, null, null, "_id DESC");
		
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
	
	// Given a list of foods, converts all foods into a list of strings
	public List<String> stockrecords_toStrList(List<Stockrecord> stockrecords) {
		List<String> stockrecord_strlist = new ArrayList<String>();
		for (int i=0; i<stockrecords.size(); i++) {
			Stockrecord sr = stockrecords.get(i);
			stockrecord_strlist.add(sr.format_Str());
		}
		return stockrecord_strlist;
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
	
	/**
	 * Inserts a food object into the database, given a stockrecord to attach it to
	 * @param stockrecord_id
	 * @return
	 */
	public Food create_Food(int stockrecord_id, String name, String location) {
		ContentValues values = new ContentValues();

		values.put("stockrecord_id", stockrecord_id);
		values.put("name", name);
		values.put("location", location);
		
		// 2nd argument simply states that if value is null, then it won't insert a row
		// Insert returns the id assigned to new row
		long newID = database.insert("food", null, values);

		String[] cols = {"_id", "stockrecord_id", "location", "name"}; // What cols we're extracting
	    Cursor cursor = database.query("food", cols, "_id = " + (int) newID, null, null, null, null);
	    cursor.moveToFirst();
	    Food food = cursorto_Food(cursor);
	    cursor.close();
	    Log.d("Created food", food.get_name() + food.get_location() + food.get_stockrecord_id());
	    return food;
	}
	
	public void delete_Food(int id) {
		database.delete("food", "_id = " + id, null);
	}
	
	/**
	 * Returns all food items associated with a certain stock record
	 * @param stockrecord_id
	 * @return
	 */
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
			Log.d("Loading food", food.format_Str());
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
	
	/**
	 * Given a list of foods, returns a dictionary whose keys are outside, fridge, and freezer
	 * and the values are arraylists of foods who correspond to each location
	 * @param foods
	 * @return
	 */
	public HashMap<String, ArrayList<String>> foods_toStrMap(List<Food> foods) {
		HashMap<String, ArrayList<String>> foodMap = new HashMap<String, ArrayList<String>>();
		
		ArrayList<String> outside = new ArrayList<String>();
		ArrayList<String> fridge = new ArrayList<String>();
		ArrayList<String> freezer = new ArrayList<String>();
		
		for (int i=0; i<foods.size(); i++) {
			Food viewedfood = foods.get(i);
			
			if (viewedfood.get_location().equals("Outside")) {
				outside.add(viewedfood.get_name());
			}
			if (viewedfood.get_location().equals("Fridge")) {
				fridge.add(viewedfood.get_name());
			}
			if (viewedfood.get_location().equals("Freezer")) {
				freezer.add(viewedfood.get_name());
			}
		}
		foodMap.put("Outside", outside);
		foodMap.put("Fridge", fridge);
		foodMap.put("Freezer", freezer);
		return foodMap;
	}
	
	// Can translate cursor query results into a Stockrecord obj
	private Food cursorto_Food(Cursor cursor) {
		Food food = new Food();
		food.set_ID(cursor.getInt(0));
		food.set_stockrecord_id(cursor.getInt(1));
		food.set_location(cursor.getString(2));
		food.set_name(cursor.getString(3));
		return food;
	}
	
	
	// ----------------------------------
	// SharedPreferences K-V Food Count Storage
	// ----------------------------------
	// Shared Preferences allows simple saving of key value pairs which can then be accessed by any activity
	// We'll be using it to save counts of types of food so that in producing the recommended shopping list, we
	// dont iterate over all foods in the sql db and take forever (also inefficient)
	
	/**
	 * Opens our SharedPreferences data storage area to add one to a food (i.e. "Eggs" was 5, now 6)
	 * If a food is new, it deals with this situation as well.
	 * @param context
	 * @param foodname
	 */
	public void addOne_tofood(Context context, String foodname) {
		// First we check if this is a new food
		SharedPreferences foodCount = context.getSharedPreferences("foodCount", context.MODE_PRIVATE);
		// We search for the value for key foodname, IF it doesn't exist then 0 is returned by getInt()
		int foodname_count = foodCount.getInt(foodname, 0); 
		
		// Open up the SharedPreferences Editor to make changes to our data
		SharedPreferences.Editor foodCount_editor = foodCount.edit();
		
		if (foodname_count > 0) {
			foodCount_editor.putInt(foodname,  foodname_count+1);
		} else {
			foodCount_editor.putInt(foodname, 1);
		}
		foodCount_editor.commit();
	}
	
	/**
	 * Opens our SharedPreferences data storage area to remove one to a food.
	 * If the food is new/doesn't exist, this won't do anything
	 * @param context
	 * @param foodname
	 */
	public void removeOne_fromfood(Context context, String foodname) {
		SharedPreferences foodCount = context.getSharedPreferences("foodCount", context.MODE_PRIVATE);
		// We search for the value for key foodname, IF it doesn't exist then 0 is returned by getInt()
		int foodname_count = foodCount.getInt(foodname, 0); 
		
		if (foodname_count > 0) {
			// Open up the SharedPreferences Editor to make changes to our data
			SharedPreferences.Editor foodCount_editor = foodCount.edit();
			foodCount_editor.putInt(foodname,  foodname_count-1);
			foodCount_editor.commit();
		}
	}

	/**
	 * Takes the K-V sharedpreferences file and reverses it so that keys are count and values are
	 * arraylists of the foods contained per count. This allows us to quickly figure out which foods
	 * are in the higher counts later. We return a hashmap
	 * @return
	 */
	public HashMap<Integer, ArrayList<String>> process_foodCountKV(Context context) {
		HashMap<Integer, ArrayList<String>> reversedMap = new HashMap<Integer, ArrayList<String>>();
		
		SharedPreferences foodCount = context.getSharedPreferences("foodCount", context.MODE_PRIVATE);
		Set<String> foodnamekeys = foodCount.getAll().keySet();
		Iterator<String> iterator = foodnamekeys.iterator();
		while (iterator.hasNext()) {
			String foodname = iterator.next();
			int foodvalue = foodCount.getInt(foodname, 0);
			if (reversedMap.get(foodvalue) == null) {
				ArrayList<String> foodnames = new ArrayList<String>();
				foodnames.add(foodname);
				reversedMap.put(foodvalue, foodnames);
			} else {
				reversedMap.get(foodvalue).add(foodname);
			}
		}
		return reversedMap;
	}
	
	/**
	 * Meant to take what comes out of processing the foodcount map. The process -> Start with shared
	 * preferences, k=name of food, v=count. When we use process, we reverse it so that k=count, and
	 * v=array of all foods with that count. This then creates an ordered ArrayList.
	 * Not sure if really optimal.
	 * @param foodmap
	 * @return
	 */
	public ArrayList<String> foodCountKV_toList(HashMap<Integer, ArrayList<String>> foodmap) {
		ArrayList<String> sortedFoods = new ArrayList<String>();
		Integer[] keys = (Integer[]) foodmap.keySet().toArray(new Integer[0]);
	    // Start from end and go to begining
		for (int i=keys.length-1; i>=0; i--) {
	    	Log.d("Key is", ""+keys[i]);
	    	ArrayList<String> foodsInCount = foodmap.get(keys[i]);
	    	sortedFoods.addAll(foodsInCount);
	    }
		for (int i=0; i<sortedFoods.size(); i++) {
			Log.d("FOOD", sortedFoods.get(i));
		}
		return sortedFoods;
	}
	

}
