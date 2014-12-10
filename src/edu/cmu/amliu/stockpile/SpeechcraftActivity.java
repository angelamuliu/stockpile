package edu.cmu.amliu.stockpile;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This activity displays the heard results of speech-to-text to the user, and
 * also displays buttons for selecting where the food item has gone. It receives the
 * listened results array from StockActivity. When a user presses the "ADD!" button,
 * a food item is created based on what the user has chosen and passed back to StockActivity
 */

public class SpeechcraftActivity extends Activity {
	
	private ListView wordsList;
	
	private String location = "";
	private String foodName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Recieving the bundle's array of heard words from stock activity
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		String[] matches = bundle.getStringArray("matches");
		Log.d("Testing sending array between activities", matches[0]);
		
		setContentView(R.layout.activity_speechcraft);

		 // ArrayAdapters convert an array into a set of views that can be loaded into a listview
		// Update wordList with strings within matches array
		 ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches);
		 wordsList = (ListView) findViewById(R.id.speechmatches);
		 wordsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		 wordsList.setAdapter(wordAdapter);
		 
		 // Tapping an item sets it bg color to pink and sets foodName to it
		 wordsList.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				 resetListStyles();
				 TextView tappedItem = ((TextView)view);
				 int pink = Color.parseColor("#D75A5D");
				 tappedItem.setBackgroundColor(pink);
				 String item = tappedItem.getText().toString();
				 foodName = item;
		      }
			 
			 // Clear the style of any other list items first
			 public void resetListStyles() {
				 for (int i=0; i<wordsList.getChildCount(); i++) {
					 View child = wordsList.getChildAt(i);
					 child.setBackgroundColor(Color.TRANSPARENT);
				 }
			 }
		 });
	}
	
	
	// A back button within the page
	public void backHome(View view) {
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();
	    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	// When the user is all done, press the addfood button which calls this
		// which packages info to send back to stock activity
	public void addFood(View view) {
		Context context = getApplicationContext();
		if (foodName.isEmpty()) {
			CharSequence text = "Select a food";
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
	
	// Setting location and styling the choosen button
	public void setOutside(View view) {
		location = "Outside";
		resetButtonStyles();
		Button outsideButton = (Button) findViewById(R.id.outside);
		int pink = Color.parseColor("#D75A5D");
		outsideButton.setBackgroundColor(pink);	
	}
	
	public void setFridge(View view) {
		location = "Fridge";
		resetButtonStyles();
		Button fridgeButton = (Button) findViewById(R.id.fridge);
		int pink = Color.parseColor("#D75A5D");
		fridgeButton.setBackgroundColor(pink);	
	}
	
	public void setFreezer(View view) {
		location = "Freezer";
		
		// TODO: Quick fix for now, maybe in future figure out issue
		int drawable_id = R.drawable.stockbutton;
		Drawable stockbutton = getResources().getDrawable(drawable_id);
		Button outsideButton = (Button) findViewById(R.id.outside);
		outsideButton.setBackground(stockbutton);
		Button fridgeButton = (Button) findViewById(R.id.fridge);
		fridgeButton.setBackground(stockbutton);
		
		
		Button freezerButton = (Button) findViewById(R.id.freezer);
		int pink = Color.parseColor("#D75A5D");
		freezerButton.setBackgroundColor(pink);	
	}

	/**
	 * Resets all button option styles to the default
	 */
	public void resetButtonStyles() {
		int drawable_id = R.drawable.stockbutton;
		Drawable stockbutton = getResources().getDrawable(drawable_id);
		
		// For now I'm just going to set the bg colors
		Button outsideButton = (Button) findViewById(R.id.outside);
		outsideButton.setBackground(stockbutton);
		Button fridgeButton = (Button) findViewById(R.id.fridge);
		fridgeButton.setBackground(stockbutton);
		Button freezerButton = (Button) findViewById(R.id.freezer);
		freezerButton.setBackground(stockbutton);
	}
	
}
