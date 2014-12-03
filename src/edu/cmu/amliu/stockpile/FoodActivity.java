package edu.cmu.amliu.stockpile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import edu.cmu.amliu.stockpile.db.DBDataSource;
import edu.cmu.amliu.stockpile.db.Food;
import edu.cmu.amliu.stockpile.db.FoodExpandableListAdapter;

/*
 * Food Activity
 * A more in depth view listing all foods associated with a certain
 * stockrecord. Displayed right after making the list or when user presses
 * a stockrecord in the stockrecord activity. Edit/Update/Delete/Create more foods
 */
public class FoodActivity extends ListActivity {

	private DBDataSource datasource;
	private List<Food> values;  // Food values for a certain SR
	
	FoodExpandableListAdapter listAdapter; // Adapter for expListView
    ExpandableListView expListView; // The view we're inserting into
    ArrayList<String> listDataHeader; // Used to give list it's headers/expandable sections
    HashMap<String, ArrayList<String>> listDataChild; // The list item children, separated per header/section by key
    
	// ----------------------------------
	// Activity Lifecycle
	// ----------------------------------
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_food);
	    
	    // Receive the stock record's id
	    Intent intent = getIntent();
	    Bundle bundle = intent.getExtras();
	    int sr_id = bundle.getInt("sr id");
	 
	    datasource = new DBDataSource(this);
	    datasource.open();

	    // Load in food values as list
	    values = datasource.getFood_forSR(sr_id);
	    // Prepare data into a header array and a values map so that it can be displayed in exp list
	    preparedata(values);
	    
	    // Setting up the expandable food list adapter and list
	    expListView = (ExpandableListView) findViewById(R.id.lvExp);
        listAdapter = new FoodExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
	  }
	  
	  /**
	   * Load and prepare data from the sql db so that it can
	   * be displayed in the expandable list
	   * @param foods
	   */
	  private void preparedata(List<Food> foods) {
		  	listDataHeader = new ArrayList<String>();
	        // Add headers
	        listDataHeader.add("Outside");
	        listDataHeader.add("Fridge");
	        listDataHeader.add("Freezer");
	        
	        // Load in the data from the SQL db (already mapped to keys outside, fridge, freezer
	        // and returns values as an ArrayList of strings
	        listDataChild = datasource.foods_toStrMap(foods);
	  }
	  
	  @Override
	  protected void onResume() {
	    datasource.open();
	    super.onResume();
	    Log.d("Resume", "Food Activity");
	  }

	  @Override
	  protected void onPause() {
	    datasource.close();
	    super.onPause();
	    Log.d("Pause", "Food Activity");
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

	  // Will be called via the onClick attribute
	  // of the buttons in main.xml
	  public void onClick(View view) {
	    @SuppressWarnings("unchecked")
	    ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
	    Food food = null;
	    switch (view.getId()) {
	    case R.id.add:
//	    	food = datasource.create_Food(1); //Create a food associated with stockrecord id 1
//	    	values.add(food);
//	    	Log.d("Food values", food.format_Str());
//	    	adapter.add(food.format_Str());
	      break;
	    case R.id.delete:
	      if (getListAdapter().getCount() > 0) {
	    	  Log.d("Delete", "Delete food");
	    	  
	    	  food = values.get(0);
	    	  datasource.delete_Food(food.get_id());
	    	  values.remove(0);
	    	  adapter.remove(food.format_Str());
	      }
	      break;
	    }
	    adapter.notifyDataSetChanged();
	  }
	  
	// Also provide similar transitions for pressing the back button
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	  
	// ----------------------------------
	// Misc
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
