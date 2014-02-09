package com.avi.police;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;



import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

public class Inbox extends ListActivity implements SearchView.OnQueryTextListener {

	public ArrayList<String> detailsList=new ArrayList<String>();
	public ArrayList<String> refinedList;
	public final static String DETAIL = "com.avi.police.DETAIL";
	public final static String EXTRA_DETAIL = "com.avi.police.EXTRA_DETAIL";
	public final static int RESULT_DETAIL=0;
	private SearchView mSearchView;
	ListView list;
	int count = 0,flag = 0;
	String crimname =  "com.avi.police.CRIM_NAME" ;
	String message = "com.avi.message";
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String detail;
		if(flag==1){
			 detail = refinedList.get(position);
		}
		else
		{
			detail= detailsList.get(position);
		}
		Intent intent = new Intent(Inbox.this,AreaActivity.class);
		intent.putExtra(EXTRA_DETAIL, crimname);
		intent.putExtra(DETAIL,detail);
	
		
	}

	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.criminals);
		
		list = (ListView) findViewById(android.R.id.list);
		Intent intent = getIntent();
		 crimname = intent.getStringExtra(GCMIntentService.CRIM_NAME_INBOX);
		 message = intent.getStringExtra(GCMIntentService.MESSAGE_INBOX);
		 detailsList.add("About: "+crimname+" Message: "+message);
		 setListAdapter(new ArrayAdapter<String>(Inbox.this,android. R.layout.simple_list_item_1, detailsList));
		 getIntent().removeExtra(GCMIntentService.CRIM_NAME_INBOX);
			getIntent().removeExtra(GCMIntentService.MESSAGE_INBOX);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search_in_menu, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) searchItem.getActionView();
		setupSearchView(searchItem);
		return true;
	}
	 protected boolean isAlwaysExpanded() {
	        return false;
	    }
	private void setupSearchView(MenuItem searchItem) {
		
		if(isAlwaysExpanded()) {
			mSearchView.setIconifiedByDefault(false);
			}else {
				searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
			}
		mSearchView.setOnQueryTextListener(this);
		}
	
	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		

		 count = 0;
		 flag = 1;
		 refinedList = new ArrayList<String>();

		

		
		for(int i=0;i<detailsList.size();i++) {
			if(detailsList.get(i).toLowerCase().contains(newText.toLowerCase()))
			{
				refinedList.add(detailsList.get(i));
				count++;
				
			}
			
		}
		if(count>0)
		{
			list.setVisibility(View.VISIBLE);
			setListAdapter(new ArrayAdapter<String>(Inbox.this,android. R.layout.simple_list_item_1,refinedList));

		}
		else
		{
			list.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "No Results", Toast.LENGTH_SHORT).show();
		}
		

		

		return false;
	}


	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}
	
}


