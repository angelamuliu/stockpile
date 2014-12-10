package edu.cmu.amliu.stockpile;

import java.util.List;
import edu.cmu.amliu.stockpile.db.Food;
import edu.cmu.amliu.stockpile.db.DBDataSource;
import edu.cmu.amliu.stockpile.db.Stockrecord;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/*
 * This activity warns you what food is about to go bad, granted that the food has
 * not been fully consumed (only looks at the most recent stockrecord for now)
 */

public class RottersActivity extends Activity {
	
	private DBDataSource datasource;
	private Stockrecord mostRecent;
	private List<Food> mostRecentFood;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rotters);
		
		datasource = new DBDataSource(this);
	    datasource.open();
		mostRecent = datasource.getMostRecent_Stockrecord();
		if (mostRecent != null) {
			 TextView header = (TextView) findViewById(R.id.rot_header);
			 header.setText("HAS RECORD");
			 mostRecentFood = datasource.getFood_forSR(mostRecent.get_id());
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
