package com.nodomain.weathernow;

import android.util.Log;

import org.json.JSONObject;
import org.json.JSONException;


public class WeatherApiHandler {

    private String token;
    private JSONObject allData;
    private JSONObject currently;

    public WeatherApiHandler(){
        token = null;
        allData = null;
    }

    public void setToken(String token){
        this.token=token;
    }

    public void WeatherRequest(double latitude,double longitude) {

        if(token == null){
            Log.e("Error:","No token specified");
            return;
        }

        HTTPDataHandler hh = new HTTPDataHandler();
        String stream = null;
        String api_URL = "https://api.darksky.net/forecast/";

        stream = hh.GetHTTPData(api_URL + token + "/" + Double.toString(latitude) + "," + Double.toString(longitude));

        if (stream != null) {
            try {
                // Get the full HTTP Data as JSONObject
                allData = new JSONObject(stream);
                get_Currently_JSONObject();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.e("JSON Request Error:","http stream is null");
        }

    }

    public String getTimeZone(){
        String res = "N/A";
        try {
            res  = allData.getString("timezone");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
       return res;
    }

    private void get_Currently_JSONObject(){
        currently = null;
        try {
            currently = allData.getJSONObject("currently");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }


    public String getCurrentTemperature(){
        String res = "N/A";

        if(currently!=null) {
            double temp = 0;
            try {
                temp = currently.getDouble("temperature");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //convert to Celsius
            res = Integer.toString((int)Math.round((temp -32) *5/9));
        }//if statement end

        return res;
    }


}//WeatherApi class end
