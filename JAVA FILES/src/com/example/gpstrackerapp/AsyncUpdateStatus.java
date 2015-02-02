package com.example.gpstrackerapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncUpdateStatus extends AsyncTask <String, Void, String> {
	
	Context c;
	
    public AsyncUpdateStatus(Context context)
    {
         c= context;
     }
	
	@Override
	protected String doInBackground(String... uri) {
		
		try {
			//prepare request
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(uri[0]);
			
			//execute request
			HttpResponse response = client.execute(request);
			
			//handle response
			BufferedReader br = new BufferedReader(new InputStreamReader(
									response.getEntity().getContent())
								);
						
			//get response value using StringBuilder
		    StringBuilder responseText = new StringBuilder();
		    String line;
		    while ((line = br.readLine()) != null) {
		    	responseText.append(line);
		    }
		    
		    //close buffer
		    br.close();
			
			
			Log.d("TAG", responseText.toString());
			
			return responseText.toString();
			
		}catch (Exception e) {
			Log.v("TAG", "Error: " + e);
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Toast.makeText(c, result, Toast.LENGTH_LONG).show();
	}

}


