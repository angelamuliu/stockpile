package edu.cmu.amliu.stockpile;

import edu.cmu.amliu.stockpile.db.Food;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/*
 * This activity displays the heard results of speech-to-text to the user, and
 * also displays buttons for selecting where the food item has gone. It receives the
 * listened results array from StockActivity. When a user presses the "ADD!" button,
 * a food item is created based on what the user has chosen and passed back to StockActivity
 */

public class SpeechcraftActivity extends Activity {
	
	private Food newFood;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		newFood = new Food();
		setContentView(R.layout.activity_speechcraft);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.speechcraft, menu);
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
