package dk.unf.software.aar2013.gruppe7;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class DeleteDummy extends Activity {
int sidetal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	
//		setContentView(R.layout.activity_delete_dummy);
//		sidetal = getIntent().getExtras().getInt("sidetal");
//		
//		Intent returnvalue =	new Intent(getApplicationContext(), MainActivity.class);
//		returnvalue.putExtra("returnvalue",returnvalue);
//		startActivityForResult(returnvalue, 5);
//		
//		
//		returnvalue.putExtra("sidetalretur", sidetal);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete_dummy, menu);
		return true;
	}
	
}