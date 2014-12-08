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
import android.view.View;
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
	
	// If user wants to go back
	public void switchActivity_Main(View view) {
		datasource.close();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	
}


