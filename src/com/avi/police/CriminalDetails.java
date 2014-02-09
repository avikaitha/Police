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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CriminalDetails extends Activity {


	private static final String TAG = "Criminaldetails";
	TextView crim_nameTV;
	TextView akaTV,ageTV,religionTV,typeTV,addressTV,messageTV;
	String message,crim_name="com.avi.crim_name";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.criminal_details);
		akaTV = (TextView) findViewById(R.id.aka_textView);
		crim_nameTV = (TextView) findViewById(R.id.crim_name_textView);
		ageTV = (TextView) findViewById(R.id.age_textView);
		religionTV = (TextView) findViewById(R.id.religion_textView);
		typeTV = (TextView) findViewById(R.id.type_textView);
		addressTV = (TextView) findViewById(R.id.address_textView);
		messageTV = (TextView) findViewById(R.id.msg_textView);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		  crim_name =extras.getString(GCMIntentService.CRIM_NAME);
		message = intent.getStringExtra(GCMIntentService.MESSAGE);
		Log.i(TAG, crim_name);
		new GetDetails().execute(crim_name);
		getIntent().removeExtra(GCMIntentService.CRIM_NAME);
		getIntent().removeExtra(GCMIntentService.MESSAGE);
	}
	
	class GetDetails extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result ="";
	        InputStream isr = null;
	        Log.i(TAG, "Background task started");;
	        try{
	           HttpClient httpclient=new DefaultHttpClient();
	           HttpPost httppost= new HttpPost("http://testandroid.net46.net/getSpecificCriminal.php");
	           ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();           
			   nameValuePairs.add(new BasicNameValuePair("crimname",params[0]));
			   httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	           HttpResponse response=httpclient.execute(httppost);
	           HttpEntity entity = response.getEntity();
	           isr = entity.getContent();
	        }catch(Exception e){
	            Log.e("log_tag", "Eror at httpost "+e.toString());
	        }
	        try{        
	            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while((line = reader.readLine())!=null){
	                sb.append(line+"\n");
	            }
	            isr.close();
	            result=sb.toString();
	        }catch(Exception e){
	            Log.e("log_tag", "Error converting "+e.toString());
	        }
			return result;
		}
			
			

	   
	        

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
try{
				
				JSONArray jArray =new JSONArray(result);
				String  crim_nameTVS = "";
				String akaTVS="",ageTVS="",religionTVS="",typeTVS="",addressTVS="";
			
				for(int i=0; i<jArray.length();i++){
					JSONObject json = jArray.getJSONObject(i);
						crim_nameTVS = json.getString("NAME");
						akaTVS = json.getString("AKA"); 
						ageTVS= json.getString("AGE");
						religionTVS= json.getString("RELIGION");
						typeTVS= json.getString("TYPE");
						addressTVS= json.getString("H_NO");
						
					}
				crim_nameTV.setText("Name:"+crim_nameTVS);
				 akaTV.setText("AKA:"+akaTVS);
				 ageTV.setText("Age:"+ageTVS);
				 religionTV.setText("Religion:"+religionTVS);
				 typeTV.setText("Type of crime:"+typeTVS);
				 addressTV.setText("Address:"+addressTVS);
				 messageTV.setText("Message:"+message);

			}catch(Exception e) {
				
				Log.e("log_tag","Error Parsing Data"+e.toString());
			}
		
			
			
		}
		
	}
	
	

}
