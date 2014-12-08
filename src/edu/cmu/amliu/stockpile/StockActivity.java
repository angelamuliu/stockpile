package edu.cmu.amliu.stockpile;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/*
 * This activity shows the user two methods of input (speech-to-text and keyboard)
 * and the stock list so far, as well as a done button that will save the record to the DB
 */

public class StockActivity extends Activity {
	
	// Request codes for starting certain activities and responding to their
	// different results (given back in bundles!)
    private static final int SPEECH_REQCODE = 1111;
    private static final int TEXTCRAFT_REQCODE = 2222;
    private static final int SPEECHCRAFT_REQCODE = 3333;
    
    // List views we're updating
    private ListView foodList;
    
    // We'll be keeping track of foods inputted, so that when finished we can
    // process the list and create a stock record from it
    private ArrayList<String> foodNameList = new ArrayList<String>();
    private ArrayList<String> foodlocationList = new ArrayList<String>();
    
    // Used to display the foods in the foodList
    public ArrayList<String> displayedFoods = new ArrayList<String>();
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_stock);
		
		// Setup interactive elements, speak button and list that displays results
		Button speakButton = (Button) findViewById(R.id.speakbutton);
        foodList = (ListView) findViewById(R.id.foodList);
        
     // Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
        	speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }
	}
	
	// Also provide similar transitions for pressing the back button
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	// Done, switch views and get ready to save on DB
	public void switchActivity_Stockrecord(View view) {
		Intent intent = new Intent(this, StockrecordActivity.class);
		
		// Package a bundle of an array to stockrecord activity
		Bundle bundle =new Bundle();
		String[] finished_foodNameArray = foodNameList.toArray(new String[foodNameList.size()]);
		String[] finished_foodlocationArray = foodlocationList.toArray(new String[foodlocationList.size()]);
 		bundle.putStringArray("foodname array", finished_foodNameArray);
 		bundle.putStringArray("foodlocation array", finished_foodlocationArray);
 		intent.putExtras(bundle);
 		
 		finish();
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	// If user wants to go back
	public void switchActivity_Main(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	// ------------------------------------------
	// SPEECH TO TEXT MANAGEMENT
	// ------------------------------------------
	
	// Called on speak button click
	public void speakbutton_clicked(View v) {
		startVoiceRecognitionActivity();
	}
   
	// Fire intent to start voice recognition
	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What food are you stocking right now?");
		startActivityForResult(intent, SPEECH_REQCODE);
	}
	
    
	// ------------------------------------------
	// TEXT BACKUP MANAGEMENT (alternative to speech-to-text)
	// ------------------------------------------
    
    // Called on text button click
 	public void textbutton_clicked(View v) {
 		Intent intent = new Intent(this, TextcraftActivity.class);
 		
 		Bundle bundle =new Bundle();
 		bundle.putStringArray("test bundle", new String[]{"Hello", "World"});
 		
 		intent.putExtras(bundle);
 		
 		// We use startActivityForResult so that when the textcraft activity finishes, we can detect
 		// WHAT finished and get back specialized results
		startActivityForResult(intent, TEXTCRAFT_REQCODE);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
 	}
 	
 	// ------------------------------------------
 	
 	// This function handles getting back results from the built in speech recognition activity,
 	// the textcraft activity, and the speechcraft activity
 	// We can respond to each by checking what the requestcode is!
 	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.d("Activity", "Got back activity");
    	Log.d("Activity arguments", "ReqCode:" + requestCode + " Result:"+resultCode);
    	
    	// Didn't get anything useful back from either textcraft or speechcraft
    	if ((requestCode == SPEECHCRAFT_REQCODE || requestCode == TEXTCRAFT_REQCODE) && resultCode == RESULT_CANCELED) {
    		Log.d("Activity", "Didn't get anything back");
    	} else {
        	// Respond to built in speech recognition activity
            if (requestCode == SPEECH_REQCODE && resultCode == RESULT_OK) {
                // Populate the wordsList with the String values the recognition engine thought it heard
            	// i.e. "Milk" -> matches = [milk, Milk, Mielke]
                ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Log.d("Hearing...", matches.toString());
                
                // Start the speechcraft activity and passing heard words
                Intent intent = new Intent(this, SpeechcraftActivity.class);
         		Bundle bundle =new Bundle();
         		bundle.putStringArray("matches", matches.toArray(new String[matches.size()]));
         		intent.putExtras(bundle);
        		startActivityForResult(intent, SPEECHCRAFT_REQCODE);
        		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
            // Respond to a successfully finished textcraft or speechcraft activity, we'll get back
            // two strings - the food's name, and the food's location
            if ((requestCode == TEXTCRAFT_REQCODE || requestCode == SPEECHCRAFT_REQCODE) && resultCode == RESULT_OK) {
            	// Update the two arrays that store values and the one that displays it on this activity
            	String foodName = data.getExtras().getString("foodName");
            	foodNameList.add(foodName);
            	
            	String location = data.getExtras().getString("location");
            	foodlocationList.add(location);

            	displayedFoods.add(foodName + " | " + location) ;
            	ArrayAdapter<String> displayedFoodsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayedFoods);
            	foodList.setAdapter(displayedFoodsAdapter);	
            }
            super.onActivityResult(requestCode, resultCode, data);
    	}
    }
 
    
	
}
