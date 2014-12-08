package edu.cmu.amliu.stockpile;

import java.util.List;

import edu.cmu.amliu.stockpile.db.DBDataSource;
import edu.cmu.amliu.stockpile.db.Food;
import edu.cmu.amliu.stockpile.db.Stockrecord;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/*
 * Stockrecord Activity
 * Displays previously made stock records, user can go in and edit records as well
 * by tapping them and going to a more in depth food page associated with the record 
 */
public class StockrecordActivity extends ListActivity {

	// Allows CRUD access to our SQLITE db
	private DBDataSource datasource;
	
	// Displayed stockrecords in this activity
	private List<Stockrecord> values;
	
	// Converted to strings for display purposes
	List<String> stringvalues;

	// ----------------------------------
	// Activity Lifecycle
	// ----------------------------------
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_stockrecord);
	    
	    datasource = new DBDataSource(this);
	    datasource.open();
	    
	    // Attempt to receive string array from the creation process (stock activity)
	    // if said extras exist in the intent. Create a stock record!
	    Intent intent = getIntent();
	    if (intent.hasExtra("foodname array")) {
	    	Bundle bundle = intent.getExtras();
			String[] foodname_Array = bundle.getStringArray("foodname array");
			String[] foodlocation_Array = bundle.getStringArray("foodlocation array");
			create_SR(foodname_Array, foodlocation_Array);
	    }

	    values = datasource.getall_Stockrecord();
	    stringvalues = datasource.stockrecords_toStrList(values);

	    
	    if (stringvalues.size() > 0) {
	    	// use the SimpleCursorAdapter to show the
		    // elements in a ListView (except the most recently created SR)
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, stringvalues.subList(1, stringvalues.size()));
		    setListAdapter(adapter);
		    
		    // Have most recent SR in it's own little container
		    TextView mostRecent = (TextView) findViewById(R.id.newSR);
		    mostRecent.setText(stringvalues.get(0));
	    } else {
	    	TextView mostRecent = (TextView) findViewById(R.id.newSR);
		    mostRecent.setText("There are no records!");
	    }
	  }
	  
	  // Ensuring the DB is properly handled and not left open
	  @Override
	  protected void onResume() {
	    datasource.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
	    datasource.close();
	    super.onPause();
	  }
	  
	  @Override
	  protected void onStop() {
		  datasource.close();
		  super.onStop();
	  }
	  
	  @Override
	  protected void onDestroy() {
		  datasource.close();
		  super.onDestroy();
	  }

	// ----------------------------------
	// CRUD - connect with layout
	// ----------------------------------
	  
	  /**
	   * Usually called after stock activity passes stock information to this activity.
	   * Inserts a stockrecord into the DB that has the foods attached to it as well.
	   * Food is also inserted into the DB and a foodCount via SharedPreferences is
	   * updated as well in this step.
	   * @param foodname_Array
	   * @param foodlocation_Array
	   */
	  public void create_SR(String[] foodname_Array, String[] foodlocation_Array) {
		  Stockrecord sr = datasource.create_Stockrecord();
		  int sr_id = sr.get_id();
		  for (int i=0; i<foodname_Array.length; i++) {
			  String foodname = foodname_Array[i];
			  String foodlocation = foodlocation_Array[i];
			  Log.d("Food", foodname + foodlocation);
			  datasource.create_Food(sr_id, foodname, foodlocation);
			  datasource.addOne_tofood(getApplicationContext(), foodname);
		  }
	  }
	
	/**
	 * Responds to each list item being tapped. We extract the string value of the tapped
	 * list item and parse it to get the stock record id, then start the food view activity
	 * based on this id
	 */
	@Override
	protected void onListItemClick (ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id); 
		// We can get the string content of a tapped adapter!
		Object o = this.getListAdapter().getItem(position);
        String itemStr = o.toString();
		
		switchActivity_FoodView(extract_srID(itemStr), itemStr);
	}
	
	// When the most recent SR is tapped
	public void getNewest(View view) {
		String itemStr = stringvalues.get(0);
		switchActivity_FoodView(extract_srID(stringvalues.get(0)), itemStr);
	}
	
	/**
	 * Given a string of a stockrecord, parses it and extracts the stock record's id
	 * @param itemStr
	 * @return
	 */
	protected int extract_srID(String itemStr) {
		Pattern pattern =  Pattern.compile("\\|");
		int sr_id = Integer.parseInt(pattern.split(itemStr.toString())[0].trim());
		return sr_id;
	}
	
	
	private static final int FOODVIEW_REQCODE = 5000;
	
	/**
	 * Given the stock record id, start a food activity view that corresponds
	 * @param sr_id
	 */
	public void switchActivity_FoodView(int sr_id, String itemStr) {
		datasource.close();
		Intent intent = new Intent(this, FoodActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("sr id", sr_id);
		bundle.putString("itemStr", itemStr);
		intent.putExtras(bundle);
		startActivityForResult(intent, FOODVIEW_REQCODE);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	// Also provide similar transitions for pressing the back button
	@Override
	public void onBackPressed() {
		datasource.close();
	    super.onBackPressed();
	    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	// If user wants to go back
	public void switchActivity_Main(View view) {
		datasource.close();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	// Handle the response from a foodview activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("Activity arguments", "ReqCode:" + requestCode + " Result:"+resultCode);
		
		if (resultCode == RESULT_OK) { // Deleted stockrecord and food items
			Log.d("Need to delete", "" +data.getExtras().getInt("sr id"));
			Log.d("Need to delete", data.getExtras().getString("itemStr"));
			
			datasource.open();
			int sr_id = data.getExtras().getInt("sr id");
			String itemStr = data.getExtras().getString("itemStr");
			
			  // Need to remove one per food in foodcount k-v file (since sharedpreferences file
	    	  // is not the db, we need this step)
	    	  List<Food> sr_foods = datasource.getFood_forSR(sr_id);
	    	  for (int i=0; i<sr_foods.size(); i++) {
	    		  Food viewedfood = sr_foods.get(i);
	    		  String foodname = viewedfood.get_name();
	    		  datasource.removeOne_fromfood(getApplicationContext(), foodname);
	    	  }
	    	  
	    	  // Deleting the stockrecord also removes all associated foods from db
	    	  datasource.delete_Stockrecord(sr_id);
	    	  
	    	  // Update the view now
	    	  stringvalues.remove(itemStr);
			  if (stringvalues.size() > 0) {
				  // Reset the adapter to the updated string
				  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				        android.R.layout.simple_list_item_1, stringvalues.subList(1, stringvalues.size()));
				  setListAdapter(adapter);
				    
				  // Reset the 'most recent' record in case user deleted most recent
				  TextView mostRecent = (TextView) findViewById(R.id.newSR);
				  mostRecent.setText(stringvalues.get(0));
			  } else {
				TextView mostRecent = (TextView) findViewById(R.id.newSR);
			    mostRecent.setText("There are no records!");
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					        android.R.layout.simple_list_item_1, stringvalues);
				setListAdapter(adapter);
			  }
		}
	}
	
	
	
	
	
	
	
}
