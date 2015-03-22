package com.example.archy.livewell.weather;

import android.location.Location;

import com.example.archy.livewell.AccelService;
import com.example.archy.livewell.AppSQLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Archy on 3/21/15.
 */
public class JSONWeatherParser {

    public static AppEntry getWeather(String data, Location loc) throws JSONException {
        AppEntry entry = new AppEntry();

        // init data
        JSONObject jObj = new JSONObject(data);

        // coordinates
        JSONObject coords = getObject("coord", jObj);
        loc.setLatitude(getFloat("lat", coords));
        loc.setLongitude(getFloat("lon", coords));
        entry.loc = loc;

        // temp
        JSONObject temp = getObject("main", jObj);
        entry.temp = getFloat("temp_max", temp);

        // clouds
        JSONObject clouds = getObject("clouds", jObj);
        entry.clouds = getInt("all", clouds);

        return entry;
    }

    
    // helper methods
    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

}
