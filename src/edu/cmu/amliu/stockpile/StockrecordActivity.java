package edu.cmu.amliu.stockpile;

import java.util.List;

import edu.cmu.amliu.stockpile.db.DBDataSource;
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

	    // use the SimpleCursorAdapter to show the
	    // elements in a ListView (except the most recently created SR)
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, stringvalues.subList(1, stringvalues.size()));
	    setListAdapter(adapter);
	    
	    // Have most recent SR in it's own little container
	    TextView mostRecent = (TextView) findViewById(R.id.newSR);
	    mostRecent.setText(stringvalues.get(0));
	    
	  }
	  
	  // When the most recent SR is tapped
	  public void getNewest(View view) {
		  switchActivity_FoodView(extract_srID(stringvalues.get(0)));
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
	   * Inserts a stockrecord into the DB that has the foods attached to it as well
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
		  }
	  }
	  
	  // Will be called via the onClick attribute
	  // of the buttons in main.xml
	  public void onClick(View view) {
	    @SuppressWarnings("unchecked")
	    ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
	    Stockrecord sr = null;
	    switch (view.getId()) {
	    case R.id.add:
	    	sr = datasource.create_Stockrecord();
	    	values.add(sr);
	    	adapter.add(sr.format_Str());
	      break;
	    case R.id.delete:
	      if (getListAdapter().getCount() > 0) {
	    	  
	    	  String itemStr = (String) getListAdapter().getItem(0);
	    	  datasource.delete_Stockrecord(extract_srID(itemStr));
//	    	  values.remove();
	    	  adapter.remove(itemStr);
	      }
	      break;
	    }
	    adapter.notifyDataSetChanged();
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
		
		switchActivity_FoodView(extract_srID(itemStr));
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
	
	/**
	 * Given the stock record id, create a food activity view that corresponds
	 * @param sr_id
	 */
	public void switchActivity_FoodView(int sr_id) {
		Intent intent = new Intent(this, FoodActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("sr id", sr_id);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	// Also provide similar transitions for pressing the back button
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	// ----------------------------------
	// MISC
	// ----------------------------------
	
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
