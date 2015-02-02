package com.example.gpstrackerapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

	private GoogleMap map;
	private String statusConError = "Unable to update your status because you do not access to the internet.";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		//Instantiate Location services
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener ll = new myLocationListener(); 
		
		//update map location every 60 seconds and 
		//try to update database if user has Internet connection
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, ll);
		
		
	}
	
	private class myLocationListener implements LocationListener {
		
		
		public void onLocationChanged(Location location) {
			
			if(location != null) {
				
				final String username = getIntent().getExtras().getString("username");
				
				//instantiate map object
				map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				map.clear();
				
				//get current location
				double pLat = location.getLatitude();
				double pLong = location.getLongitude();
				LatLng currentLocation = new LatLng (pLat, pLong);
				
				//pan map to current location
				CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLocation, 15);
				map.animateCamera(update);
				Marker marker = map.addMarker(
						new MarkerOptions()
							.position(currentLocation)
							.visible(true)
							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
							.title(username)
						);
				marker.setVisible(true);
				marker.showInfoWindow();
								
				//if network available update database with current location
				if(isOnline()) { //(connectionService.getActiveNetworkInfo().isConnectedOrConnecting()) {
					// Device is online
					Toast.makeText(getApplicationContext(), "Network available. Updating server...", Toast.LENGTH_LONG).show();
										
					//update location in db
					AsyncUpdateLocation getRequest = new AsyncUpdateLocation();
					String params = "?name="+username+"&lat="+pLat+"&long="+pLong;
					String uri = "http://www.boxial.com/tracker/tracker.php"+params;
					
					//send get request						
					getRequest.execute(uri);
						
				} else {
					//no network connection
					Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_LONG).show();
				}
				
			}	
		}
	
		public void onProviderDisabled(String provider) {
			//GPS is disable
			Toast.makeText(getApplicationContext(), "GPS Disable", Toast.LENGTH_LONG).show();
		}
		
		public void onProviderEnabled(String provider) {
			//GPS enabled
			Toast.makeText(getApplicationContext(), "GPS Enable", Toast.LENGTH_LONG).show();
			
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
	
	} /** End class:: myLocationListener **/
	
	public class AsyncUpdateLocation extends AsyncTask <String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			
			try {
				//prepare request
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(params[0]);
				
				//execute request
				HttpResponse response = client.execute(request);
				
				//handle response
				BufferedReader br = new BufferedReader(new InputStreamReader(
										response.getEntity().getContent())
									);
				//String responseText = br.readLine();
				
				
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
		
		protected void onPostExecute(String result) {
			//display result to screen
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		    
		}
		
	} /** end class::AsyncUpdateLocation **/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            //do something
	        	
	            return true;
	        case R.id.action_exit:
	        	//close app
	        	finish();
	        	System.exit(0);
	          	        	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
		
	public void onClick_Ready(View v) {
		updateStatus(1);
		
		Button ready = (Button) findViewById(R.id.btnReady);
		Button delivering = (Button) findViewById(R.id.btnDelivering);
		Button returning = (Button) findViewById(R.id.btnReturning);
		
		ready.setBackgroundColor(Color.GREEN);
		delivering.setBackgroundColor(Color.GRAY);
		returning.setBackgroundColor(Color.GRAY);
	}
	
	public void onClick_Delivering(View v) {
		updateStatus(2);
		
		Button ready = (Button) findViewById(R.id.btnReady);
		Button delivering = (Button) findViewById(R.id.btnDelivering);
		Button returning = (Button) findViewById(R.id.btnReturning);
		
		ready.setBackgroundColor(Color.GRAY);
		delivering.setBackgroundColor(Color.RED);
		returning.setBackgroundColor(Color.GRAY);
	}
	
	public void onClick_Returning(View v) {
		updateStatus(3);
		Button ready = (Button) findViewById(R.id.btnReady);
		Button delivering = (Button) findViewById(R.id.btnDelivering);
		Button returning = (Button) findViewById(R.id.btnReturning);
		
		ready.setBackgroundColor(Color.GRAY);
		delivering.setBackgroundColor(Color.GRAY);
		returning.setBackgroundColor(Color.BLUE);
	}
	
	public void updateStatus(int status) {
		if(isOnline()) {
			String name = getIntent().getExtras().getString("username");
			String params = "?name="+name+"&status="+status;
			String uri = "http://www.boxial.com/tracker/tracker.php"+params;
			
			//send status update request
			AsyncUpdateStatus statusUpdate = new AsyncUpdateStatus(this);
			statusUpdate.execute(uri);
			
		} else {
			Toast.makeText(getApplicationContext(), statusConError, Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onBackPressed() {
	    Toast.makeText(getApplicationContext(), "Go to 'Menu' > Exit' to close application.", Toast.LENGTH_LONG).show();
	}
	
}
