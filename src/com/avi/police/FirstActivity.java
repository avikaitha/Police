package com.avi.police;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FirstActivity extends ListActivity {
	String menu[] = {"Inbox","Send Alerts","Criminals By Area","Verify Identity","Specific Period","Log out"};

	private static String getClassName(int position)
	{
		String result = "";
		switch(position)
		{
		case 0: result = "Inbox";
		break;
		case 1: result = "SendAlerts";
		break;
		case 2: result = "CriminalActivity";
		break;
		case 3: result = "VerifyIdentity";
		break;
		case 4: result = "SpecificPeriod";
		break;
		case 5: result = "Logout";
		break;
		
		}
		return result;
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		String item = getClassName(position);
		try {
			@SuppressWarnings("rawtypes")
			Class ourClass = Class.forName("com.avi.police."+item);
			Intent intent = new Intent(FirstActivity.this,ourClass);
			startActivity(intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_screen);
		setListAdapter(new ArrayAdapter<String>(FirstActivity.this,android. R.layout.simple_list_item_1, menu));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
