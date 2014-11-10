package edu.cmu.amliu.stockpile;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	
	//View rl = findViewById(R.id.info);
////LinearLayout layout = (LinearLayout) findViewById(R.id.info);
//
//
//// Adding a textview programatically
//TextView valueTV = new TextView(this);
//valueTV.setText("Hello WorldS!!!");
//RelativeLayout.LayoutParams textviewparams = new RelativeLayout.LayoutParams(
//		RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//((RelativeLayout) rl).addView(valueTV, textviewparams);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
	
	// Switching activities
	// -----------------------------------------------------------------
	
	public void switchActivity_Stock(View view) {
		Intent intent = new Intent(this, Stock.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	public void switchActivity_Makelist(View view) {
		Intent intent = new Intent(this, Makelist.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	public void switchActivity_Rotters(View view) {
		Intent intent = new Intent(this, Rotters.class);
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
