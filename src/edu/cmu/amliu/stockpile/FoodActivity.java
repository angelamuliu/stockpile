package edu.cmu.amliu.stockpile;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import edu.cmu.amliu.stockpile.db.DBDataSource;
import edu.cmu.amliu.stockpile.db.Food;

/*
 * Food Activity
 * A more in depth view listing all foods associated with a certain
 * stockrecord. Displayed right after making the list or when user presses
 * a stockrecord in the stockrecord activity. Edit/Update/Delete/Create more foods
 */
public class FoodActivity extends ListActivity {

	private DBDataSource datasource;
	private List<Food> values; 

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

	    // First load in food values as list, then convert to strings for display
	    values = datasource.getFood_forSR(sr_id);
	    List<String> stringvalues = datasource.foods_toStrList(values);
	    Log.d("stringvalues", ""+stringvalues.size());

	    // use the SimpleCursorAdapter to show the
	    // elements in a ListView
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
	    		android.R.layout.simple_list_item_1, stringvalues);
	    setListAdapter(adapter);
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
