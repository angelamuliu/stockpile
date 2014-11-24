package edu.cmu.amliu.stockpile;

import edu.cmu.amliu.stockpile.db.Food;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/*
 * Similar to the Speechcraft Activity, but instead simply displays a text input box
 * and also displays buttons for selecting where the food item has gone. It receives the
 * listened results array from StockActivity. When a user presses the "ADD!" button,
 * a food item is created based on what the user has chosen and passed back to StockActivity
 */

public class TextcraftActivity extends Activity {
	
	private Food newFood;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		String[] testarray = bundle.getStringArray("test bundle");
		
		Log.d("Testing sending array between activities", testarray[0]);
		
		newFood = new Food();
		setContentView(R.layout.activity_textcraft);
	}

	public void addFood(View view) {
		Intent resultIntent = new Intent();
		resultIntent.putExtra("Add food", "From Add Food");
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.textcraft, menu);
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
