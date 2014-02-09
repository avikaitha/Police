package com.avi.police;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SendAlerts extends Activity{
	
	EditText crimnameET;
	EditText polnameET;
	EditText descET;
	AlertDialogManager alert = new AlertDialogManager();
	AsyncTask<Void, Void, Void> mRegisterTask;
	static final String EXTRA_STRING  = "com.avi.SendAlerts.EXTRA_STRING";
	static final String SENDER_ID = "1089043749159"; 
	String regId;
	String polname;
	String crimname;
	String desc;
	String crim_name;
	String pol_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendalerts);
		crimnameET = (EditText) findViewById(R.id.crimnameEditText);
		polnameET = (EditText) findViewById(R.id.toEditText);
		descET = (EditText) findViewById(R.id.descriptionEditText);
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		 regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			  GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.				
				Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
			}else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, MainActivity.USER_NAME, "dummy place", regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
	
		
	}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		
		if(requestCode==0)
		{
			if(resultCode==DetailsActivity.RESULT_DETAIL)
			{
				if(intent.hasExtra(DetailsActivity.EXTRA_DETAIL)){
					if(intent.getStringExtra(DetailsActivity.EXTRA_DETAIL).equals("Criminal")){
						 crim_name = intent.getStringExtra(DetailsActivity.DETAIL);
						crimnameET.setText(crim_name);
						}
					else if(intent.getStringExtra(DetailsActivity.EXTRA_DETAIL).equals("Police"))
					{
						 pol_name = intent.getStringExtra(DetailsActivity.DETAIL); 
						polnameET.setText(pol_name);
					
				}
			}
			}
		}
	}
	public void searchCrim(View v)
	{
		Intent intent = new Intent(SendAlerts.this,DetailsActivity.class);
		intent.putExtra(EXTRA_STRING, "Criminal");
		startActivityForResult(intent, 0);
	}
	
	public void searchPol(View v)
	{
		Intent intent = new Intent(SendAlerts.this,DetailsActivity.class);
		intent.putExtra(EXTRA_STRING, "Police");
		startActivityForResult(intent,0);
	}
	
	public void sendFunc(View v)
	{
		 polname = polnameET.getText().toString();
		 crimname = crimnameET.getText().toString();
		 desc = descET.getText().toString();
		if(polname.trim().length()>0 && crimname.trim().length()>0)
		{
			new SendMessage().execute();
		}
		else{
			
			alert.showAlertDialog(SendAlerts.this, "Error!", "Please enter Police Name and Criminal Name", false);
		}
		
	}
	
	class SendMessage extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			
			

	        try{
	           HttpClient httpclient=new DefaultHttpClient();
	           HttpPost httppost= new HttpPost("http://testandroid.net46.net/index2.php");
	           ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
	           nameValuePairs.add(new BasicNameValuePair("name",pol_name));
	           nameValuePairs.add(new BasicNameValuePair("crim_name",crim_name));
			   nameValuePairs.add(new BasicNameValuePair("message",desc));
			   httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	           HttpResponse response=httpclient.execute(httppost);
	           HttpEntity entity = response.getEntity();
	          
	        }catch(Exception e){
	            Log.e("log_tag", "Error at httpost "+e.toString());
	        }
	        
	        return regId;
		}
	        

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Toast.makeText(getApplicationContext(), "Message: " + desc+"to:"+pol_name+"about:"+crim_name, Toast.LENGTH_LONG).show();
			
		}
		
	}
	

}
