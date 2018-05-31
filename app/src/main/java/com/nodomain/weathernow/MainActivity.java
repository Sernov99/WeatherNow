package com.nodomain.weathernow;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;


public class MainActivity extends AppCompatActivity {

    private WeatherApiHandler weatherApiHandler;
    private WeatherUpdateTask weatherUpdateTask;
    private AVLoadingIndicatorView loadAnim;

    static final int REQUEST_LOCATION = 101;
    static final int CURRENT_TIME_AND_POSITION = 1;

    LocationManager locationManager;
    double latitude =0;
    double longitude =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initializing weather api handler
        weatherApiHandler = new WeatherApiHandler();
        weatherApiHandler.setToken("83015247ed10c4b625e43e5b6168d356");

        //Default - get current location
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getCurrentLocation();

        //find loading animation to use in async task
        loadAnim = findViewById(R.id.loadAnim);

        //Update current weather data in async task
        weatherUpdateTask = new WeatherUpdateTask();
        weatherUpdateTask.execute();


    }

    private void updateScreenInfo(int requestCode){
        switch (requestCode) {
            case CURRENT_TIME_AND_POSITION:
                 TextView timeZoneTextView = (TextView) findViewById(R.id.timeZone);
                 timeZoneTextView.setText(weatherApiHandler.getTimeZone());
                 TextView tempTextView = (TextView) findViewById(R.id.temp);
                 tempTextView.setText(weatherApiHandler.getCurrentTemperature() + "°");
                 break;
        }

    }

    private void getCurrentLocation(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            } else {
                Log.e("Error:","Location N/A");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                getCurrentLocation();
                break;
        }
    }

    //Async Task to preform weather update with current longitude and latitude
    private class WeatherUpdateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadAnim.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            weatherApiHandler.WeatherRequest(latitude,longitude);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            loadAnim.hide();
            updateScreenInfo(CURRENT_TIME_AND_POSITION);
        }
    }


}


