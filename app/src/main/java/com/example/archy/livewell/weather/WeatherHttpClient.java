/**
 * This is a tutorial source code 
 * provided "as is" and without warranties.
 *
 * For any question please visit the web site
 * http://www.survivingwithandroid.com
 *
 * or write an email to
 * survivingwithandroid@gmail.com
 *
 */
package com.example.archy.livewell.weather;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.location.Location;
/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class WeatherHttpClient {

    public static String lat = "";
    public static String lon= "";
    private static String IMG_URL = "http://openweathermap.org/img/w/";


    public String getWeatherData(Location loc) {
        HttpURLConnection con = null ;
        InputStreamReader isr = null;

        lat = String.valueOf(loc.getLatitude());
        lon = String.valueOf(loc.getLongitude());
        String base_url = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon;
        StringBuffer buffer = new StringBuffer();

        try {
            con = (HttpURLConnection) (new URL(base_url)).openConnection();
//            con.setRequestMethod("GET");
//            con.setInstanceFollowRedirects(false);
//            con.connect();

            // Let's read the response
            try {
                con.setDoInput(true);
                con.setDoOutput(true);
                isr = new InputStreamReader(con.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null)
                    buffer.append(line + "\r\n");
            }
            finally {
                con.disconnect();
            }
            return buffer.toString();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}