package com.example.developmentvmachine.serviciosweb.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.developmentvmachine.serviciosweb.model.WSCervezasResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Development VMachine on 28/06/2017.
 */

public class HttpGet extends AsyncTask<String, Void, String> {


    private Activity context;

    String TAG = getClass().getSimpleName();

    @Override
    protected String doInBackground(String... params) {

        Log.i(TAG, "doInBackground - ini");

        String urlString = Constants.BASE_URL + params[0];
        URL url = null;
        String output = "";
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            url = new URL(urlString);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                    " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
            connection.connect();
            Integer responseCode = connection.getResponseCode();

            if (responseCode != null && responseCode == HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(connection.getInputStream());
                reader = new BufferedReader(new InputStreamReader(in));

                ObjectMapper mapper = new ObjectMapper();
                WSCervezasResponse wsResponse = mapper.readValue(IOUtils.toString(reader), WSCervezasResponse.class);
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (connection != null){
                connection.disconnect();
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        Log.i(TAG, "doInBackground - end");

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
