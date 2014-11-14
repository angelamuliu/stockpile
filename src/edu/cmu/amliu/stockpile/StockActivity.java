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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StockActivity extends Activity {
	
    private static final int REQUEST_CODE = 1234;
    private ListView wordsList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_stock);
		
		// Setup interactive elements, speak button and list that displays results
		Button speakButton = (Button) findViewById(R.id.speakbutton);
        wordsList = (ListView) findViewById(R.id.list);
        
     // Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
        	speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock, menu);
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
	
	// Also provide similar transitions for pressing the back button
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	public void switchActivity_Stockrecord(View view) {
		Intent intent = new Intent(this, StockrecordActivity.class);
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
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
		startActivityForResult(intent, REQUEST_CODE);
	}
	
	// Handle voice recognition results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
        	// i.e. "Milk" -> matches = [milk, Milk, Mielke]
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            
            Log.d("Hearing...", matches.toString());
            
            // ArrayAdapters convert an array into a set of views that can be loaded into a listview
            ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches);
            wordsList.setAdapter(wordAdapter);
            // Update wordsList with the new set of possible words from listener
            
            // The list has been generated. Now we can set a listener per list item (in this case,
            // android's best speech recognition guesses) and do something with them
            wordsList.setOnItemClickListener(new OnItemClickListener() {
                
            	public void onItemClick(AdapterView<?> parent, View view, int position,
                        long id) {
                    
                    String item = ((TextView)view).getText().toString();
                    // For now, just show a toast message of the tapped item
                    Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                    
                }
            });
            
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
	
}