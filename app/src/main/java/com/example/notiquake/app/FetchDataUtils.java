package com.example.notiquake.app;

import android.text.TextUtils;
import android.util.Log;

import com.example.notiquake.app.model.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FetchDataUtils {
    private static final String LOG_TAG =  FetchDataUtils.class.getSimpleName();

    public  static List<Earthquake> fetchEarthquakeData(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractEarthquakeFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static  String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if(url == null){
            return  jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG,"Error response code :"+ urlConnection.getResponseCode());
            }
        }catch(IOException io){
            Log.e(LOG_TAG,"Problem retrieving the earthquake JSON results",io);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private  static  List<Earthquake> extractEarthquakeFromJson(String jsonData){
        if(TextUtils.isEmpty(jsonData)){
            return null;
        }
        List<Earthquake> earthquakes = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonData);
            JSONArray earthquakeArray = root.getJSONArray("features");

            for (int i =0 ; i < earthquakeArray.length(); i++){
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject earthquakeFeature = currentEarthquake.getJSONObject("properties");

                double magnitude = earthquakeFeature.getDouble("mag");
                String location = earthquakeFeature.getString("place");
                long time = earthquakeFeature.getLong("time");
                String url = earthquakeFeature.getString("url");
                /*String noPeople = earthquakeFeature.getString("felt");
                String cdi = earthquakeFeature.getString("cdi");
                double strength = Double.parseDouble(cdi);
                int tsunamiAlert = earthquakeFeature.getInt("tsunami");
                String title = earthquakeFeature.getString("title");

                //Extracting the coordinates from the "coordinates" array in the geometry object
                JSONObject earthquakeGeo = currentEarthquake.getJSONObject("geometry");
                JSONArray  coordinates = earthquakeGeo.getJSONArray("coordinates");
                double longitude = coordinates.getDouble(0);
                double latitude  = coordinates.getDouble(1);
                double depth = coordinates.getDouble(2);*/

                Earthquake earthquake = new Earthquake(magnitude,location,time,url);
                earthquakes.add(earthquake);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG,"Problem parsing the earthquake JSON results",e);
        }

        return earthquakes;
    }

}
