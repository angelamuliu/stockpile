package edu.cmu.amliu.stockpile;


import edu.cmu.amliu.stockpile.db.DBDataSource;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {
	
	private DBDataSource datasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		datasource = new DBDataSource(this);
//		datasource.clear_foodCountKV(this);
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
