package edu.cmu.amliu.stockpile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import edu.cmu.amliu.stockpile.db.DBDataSource;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * This activity is what generates a shopping list for you
 * based on your previous stock habits.
 */
public class MakelistActivity extends Activity {

	// We need to access the saved K-V pairs of food and count
	private DBDataSource datasource;
	
	private ListView foodlist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_makelist);
		
		datasource = new DBDataSource(this);
	    datasource.open();
	    
	    HashMap<Integer, ArrayList<String>> foodmap = datasource.process_foodCountKV(getApplicationContext());
	    ArrayList<String> orderedFoods = datasource.foodCountKV_toList(foodmap);
	    
	    foodlist = (ListView) findViewById(R.id.list);
	    ArrayAdapter<String> displayedFoodsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderedFoods);
	    foodlist.setAdapter(displayedFoodsAdapter);	
    	
    	datasource.close();
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
	
	
	// Also provide similar transitions for pressing the back button
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	// ---- defaults
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.makelist, menu);
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
		if (id == android.R.id.home) { 
			// When clicking to go back to main activity/home, also transition
			finish();
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}


