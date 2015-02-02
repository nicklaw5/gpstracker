package com.example.gpstrackerapp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	Button btnLogin;
	EditText etName;
	EditText etPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Instantiate layout
		etName = (EditText) findViewById(R.id.editTextName);
		etPassword = (EditText) findViewById(R.id.editTextPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
				
	}
	
	//Login button click action
	public void onClick_Login(View v) {
		String name =  etName.getText().toString();
		String password = etPassword.getText().toString();
		
		if(name.isEmpty() || password.isEmpty()) {
			//username and/or password fields empty
			Toast.makeText(getApplicationContext(), "Please enter a username and password.", Toast.LENGTH_LONG).show();
		} else {
			
			//attempt to login the user
			AsyncLogin postRequest = new AsyncLogin();
			String url = "http://www.boxial.com/tracker/loginScript.php"; 
			postRequest.execute(url, name, password);
		}
	}
	
	private class AsyncLogin extends AsyncTask <String, Void, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
						
			//setup post method
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(params[0]);
			
			//assign name value pairs
			List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
			pairs.add(new BasicNameValuePair("name", params[1]));
			pairs.add(new BasicNameValuePair("password", params[2]));
			
			try {
				post.setEntity(new UrlEncodedFormEntity(pairs));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				Log.v("TAG", "Failed to cast post entity: " + e);
			}
			
			//handle response
			try {
			    HttpResponse response = client.execute(post);
			    
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
			    
			    Log.d("Http Response:", responseText.toString());
			    
			    String[] returnData = new String[]{responseText.toString(), params[1]};
			    
			    
			    return returnData;
			    
			} catch (ClientProtocolException e) {
			    // writing exception to log
			    e.printStackTrace();
			         
			} catch (IOException e) {
			    // writing exception to log
			    e.printStackTrace();
			}
			
			return null;
		}
		
		protected void onPostExecute(String[] result) {
			
			if(!result[0].equals("success")) {
				
				//unsuccessful login
				Toast.makeText(getApplicationContext(), result[0], Toast.LENGTH_LONG).show();
		    } else {
		    	
		    	//successful login
		    	Toast.makeText(getApplicationContext(), "Logged in as " + result[1], Toast.LENGTH_LONG).show();
		    	
		    	//load MainActivity (map)
		    	Intent i = new Intent(getApplicationContext(), MainActivity.class); 
		    	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    	i.putExtra("username", result[1]);
		    	getApplicationContext().startActivity(i);
		    }
		}
	}
	
}
