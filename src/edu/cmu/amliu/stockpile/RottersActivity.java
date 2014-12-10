package edu.cmu.amliu.stockpile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import edu.cmu.amliu.stockpile.db.DBDataSource;
import edu.cmu.amliu.stockpile.db.Food;
import edu.cmu.amliu.stockpile.db.Stockrecord;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/*
 * This activity warns you what food is about to go bad, granted that the food has
 * not been fully consumed (only looks at the most recent stockrecord for now)
 */

public class RottersActivity extends Activity {
	
	private DBDataSource datasource;
	private Stockrecord mostRecent;
	private HashMap<String, ArrayList<String>> mostRecentFoodMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rotters);
		
		datasource = new DBDataSource(this);
	    datasource.open();
		mostRecent = datasource.getMostRecent_Stockrecord();
		
		// A record exists, go find the foods inside and populate 3 listviews depending on food location
		if (mostRecent != null) {
			TextView header = (TextView) findViewById(R.id.rot_header);
			header.setText("Check up on your foods that were stocked on " + mostRecent.get_date_made());
			 
			List<Food> mostRecentFood = datasource.getFood_forSR(mostRecent.get_id());
			mostRecentFoodMap = datasource.foods_toStrMap(mostRecentFood);
			 
			ListView out_listview = (ListView) findViewById(R.id.rot_outlist);
			ListView fridge_listview = (ListView) findViewById(R.id.rot_fridgelist);
			ListView freezer_listview = (ListView) findViewById(R.id.rot_freezelist);
			
			List<String> out_foodlist = mostRecentFoodMap.get("Outside");
			List<String> fridge_foodlist = mostRecentFoodMap.get("Fridge");
			List<String> freezer_foodlist = mostRecentFoodMap.get("Freezer");

			ArrayAdapter<String> out_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, out_foodlist);
			ArrayAdapter<String> fridge_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fridge_foodlist);
			ArrayAdapter<String> freezer_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, freezer_foodlist);
			
			out_listview.setAdapter(out_adapter);
			fridge_listview.setAdapter(fridge_adapter);
			freezer_listview.setAdapter(freezer_adapter);
		}
		datasource.close();
	}

	
	// If user wants to go back
	public void switchActivity_Main(View view) {
		datasource.close();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	// Also provide similar transitions for pressing the back button
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
}
