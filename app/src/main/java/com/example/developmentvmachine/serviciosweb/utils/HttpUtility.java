package com.example.developmentvmachine.serviciosweb.utils;

import android.renderscript.ScriptGroup;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Almansa on 01/07/2017.
 */

public class HttpUtility {

    public static final String TAG = HttpUtility.class.getSimpleName();

    public static String GET(String urlString) {

        Log.i(TAG, "GET - ini");

        HttpURLConnection connection = null;
        String output = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                    " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

            Integer responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                output = inputstreamToString(connection.getInputStream());
            } else {
                throw new Exception("Error connecting to " + urlString);
            }

        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.i(TAG, "GET - end");

        return output;
    }

    public static String POST(String urlString, HashMap<String, String> params) {
        Log.i(TAG, "POST - ini");

        HttpURLConnection connection = null;
        String output = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("ContectType", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            JSONObject jsonParam = new JSONObject(params);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonParam.toString());
            writer.flush();
            writer.close();

            Integer httpResponseCode = connection.getResponseCode();

            if (httpResponseCode == HttpURLConnection.HTTP_OK) {
                output = inputstreamToString(connection.getInputStream());
            }else{
                throw new Exception("Error connecting to " + urlString);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.i(TAG, "POST - end");
        return output;
    }

    public static String inputstreamToString(InputStream input) throws IOException {
        InputStream in = new BufferedInputStream(input);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        return IOUtils.toString(reader);
    }

}
