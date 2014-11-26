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
import android.widget.ArrayAdapter;
import android.widget.ListView;

/*
 * Stockrecord Activity
 * Displays previously made stock records, user can go in and edit records as well
 * by tapping them and going to a more in depth food page associated with the record 
 */
public class StockrecordActivity extends ListActivity {

	private DBDataSource datasource;
	private List<Stockrecord> values;

	// ----------------------------------
	// Activity Lifecycle
	// ----------------------------------
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_stockrecord);
	    
	    // Attempt to receive string array from the creation process (stock activity)
	    // if said extras exist in the intent
	    Intent intent = getIntent();
	    if (intent.hasExtra("foodname array")) {
	    	Bundle bundle = intent.getExtras();
			String[] testarray = bundle.getStringArray("foodname array");
			Log.d("GOT", testarray[0]);
	    }

	    datasource = new DBDataSource(this);
	    datasource.open();

	    values = datasource.getall_Stockrecord();
	    List<String> stringvalues = datasource.stockrecords_toStrList(values);

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
	    	  sr = (Stockrecord) getListAdapter().getItem(0);
	    	  datasource.delete_Stockrecord(sr.get_id());
	    	  values.remove(sr);
	    	  adapter.remove(sr.format_Str());
	      }
	      break;
	    }
	    adapter.notifyDataSetChanged();
	  }
	  
	public void switchActivity_FoodView(View view) {
		Intent intent = new Intent(this, FoodActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	// Also provide similar transitions for pressing the back button
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	// TODO
	@Override
	protected void onListItemClick (ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id); 
		// We can get the string content of a tapped adapter!
		Object o = this.getListAdapter().getItem(position);
        String pen = o.toString();
		Log.d("Tapped", pen);
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
