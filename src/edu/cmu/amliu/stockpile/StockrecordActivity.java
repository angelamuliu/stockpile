package edu.cmu.amliu.stockpile;

import java.util.List;

import edu.cmu.amliu.stockpile.db.DBDataSource;
import edu.cmu.amliu.stockpile.db.Stockrecord;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

/*
 * Stockrecord Activity
 * Displays previously made stock records, user can go in and edit records as well
 * by tapping them and going to a more in depth food page associated with the record 
 */
public class StockrecordActivity extends ListActivity {

	private DBDataSource datasource;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_stockrecord);

	    datasource = new DBDataSource(this);
	    datasource.open();

	    List<Stockrecord> values = datasource.getall_Stockrecord();

	    // use the SimpleCursorAdapter to show the
	    // elements in a ListView
	    ArrayAdapter<Stockrecord> adapter = new ArrayAdapter<Stockrecord>(this,
	        android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	  }

	  // Will be called via the onClick attribute
	  // of the buttons in main.xml
	  public void onClick(View view) {
	    @SuppressWarnings("unchecked")
	    ArrayAdapter<Stockrecord> adapter = (ArrayAdapter<Stockrecord>) getListAdapter();
	    Stockrecord sr = null;
	    switch (view.getId()) {
	    case R.id.add:
	    	sr = datasource.create_Stockrecord();
	    	adapter.add(sr);
	      break;
	    case R.id.delete:
	      if (getListAdapter().getCount() > 0) {
	    	  sr = (Stockrecord) getListAdapter().getItem(0);
	    	  datasource.delete_Stockrecord(sr.get_id());
	    	  adapter.remove(sr);
	      }
	      break;
	    }
	    adapter.notifyDataSetChanged();
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
	  
	public void switchActivity_FoodView(View view) {
		Intent intent = new Intent(this, FoodActivity.class);
		startActivity(intent);
	}

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
