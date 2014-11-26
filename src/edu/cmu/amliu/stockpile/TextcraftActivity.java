package edu.cmu.amliu.stockpile;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.Toast;


/*
 * Similar to the Speechcraft Activity, but instead simply displays a text input box
 * and also displays buttons for selecting where the food item has gone. It receives the
 * listened results array from StockActivity. When a user presses the "ADD!" button,
 * a food item is created based on what the user has chosen and passed back to StockActivity
 */

public class TextcraftActivity extends Activity {
	
	private String location = "";
	private String foodName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		String[] testarray = bundle.getStringArray("test bundle");
		Log.d("Testing sending array between activities", testarray[0]);
		
		setContentView(R.layout.activity_textcraft);
	}

	// When the user is all done, press the addfood button which calls this
	// which packages info to send back to stock activity
	public void addFood(View view) {
		Context context = getApplicationContext();
		if (foodName.isEmpty()) {
			CharSequence text = "Type in food";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} else if (location.isEmpty()) {
			CharSequence text = "Select a location";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} else {
			// Valid input, package name and location and send back array to
			// stockactivity
			Intent resultIntent = new Intent();
			resultIntent.putExtra("foodName", foodName);
			resultIntent.putExtra("location", location);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		}
	}
	
	// Clicking OK next to the text input sets the name
	public void setFoodName(View view) {
		EditText foodInput = (EditText) findViewById(R.id.foodInput);
		foodName = foodInput.getText().toString();
		
		Context context = getApplicationContext();
		// Remove focus from keyboard
		InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(foodInput.getWindowToken(), 0);
	}
	
	// Setting location and styling the choosen button
	public void setOutside(View view) {
		location = "Outside";
		resetButtonStyles();
		Button outsideButton = (Button) findViewById(R.id.outside);
		outsideButton.setBackgroundColor(Color.BLUE);	
	}
	
	public void setFridge(View view) {
		location = "Fridge";
		resetButtonStyles();
		Button fridgeButton = (Button) findViewById(R.id.fridge);
		fridgeButton.setBackgroundColor(Color.BLUE);	
	}
	
	public void setFreezer(View view) {
		location = "Freezer";
		resetButtonStyles();
		Button freezerButton = (Button) findViewById(R.id.freezer);
		freezerButton.setBackgroundColor(Color.BLUE);	
	}
	
	// Resets all button styles to the default
	public void resetButtonStyles() {
//		TODO: Create own style, set a drawable, and reset buttons to said drawable
//		Drawable d = getResources().getDrawable(android.R.drawable.btn_default);
//		
//		Button outsideButton = (Button) findViewById(R.id.outside);
//		outsideButton.setBackground(d);
//		Button fridgeButton = (Button) findViewById(R.id.fridge);
//		fridgeButton.setBackground(d);
//		Button freezerButton = (Button) findViewById(R.id.freezer);
//		freezerButton.setBackground(d);
		
		// For now I'm just going to set the bg colors
		Button outsideButton = (Button) findViewById(R.id.outside);
		outsideButton.setBackgroundColor(Color.GRAY);
		Button fridgeButton = (Button) findViewById(R.id.fridge);
		fridgeButton.setBackgroundColor(Color.GRAY);
		Button freezerButton = (Button) findViewById(R.id.freezer);
		freezerButton.setBackgroundColor(Color.GRAY);
	}
	
	
	// ------------------------------------------
	// Default stuff, remove if necessary
	
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
