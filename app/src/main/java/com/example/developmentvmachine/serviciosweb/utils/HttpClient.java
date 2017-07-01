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

public abstract class HttpClient extends AsyncTask<String, Void, String> {

s    String TAG = getClass().getSimpleName();

    @Override
    protected String doInBackground(String... params) {

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
