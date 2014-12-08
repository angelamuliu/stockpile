package edu.cmu.amliu.stockpile;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	
	// Switching activities
	// -----------------------------------------------------------------
	
	public void switchActivity_Stock(View view) {
		Intent intent = new Intent(this, StockActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	public void switchActivity_Makelist(View view) {
		Intent intent = new Intent(this, MakelistActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	public void switchActivity_Rotters(View view) {
		Intent intent = new Intent(this, RottersActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	public void switchActivity_StockRecord(View view) {
		Intent intent = new Intent(this, StockrecordActivity.class);
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
