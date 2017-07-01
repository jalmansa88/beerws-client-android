package com.example.developmentvmachine.serviciosweb.utils;

import android.app.Activity;
import android.graphics.Bitmap;
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
import java.util.HashMap;

/**
 * Created by Development VMachine on 28/06/2017.
 */

public class HttpClient extends AsyncTask<String, Void, InputStream> {

    String TAG = getClass().getSimpleName();

    private Bitmap bitmap = null;
    private Activity context;
    private HashMap<String, String> fieldsMap;
    private String requestMethod;
    String urlToConnect;

    public HttpClient(){
        super();
    }

    public HttpClient(Activity context, HashMap<String, String> fieldData, String request){
        this.context = context;
        this.fieldsMap = fieldData;
        this.requestMethod = request;

        if(Constants.HTTP_GET.equals(requestMethod)){

        }

        if(Constants.HTTP_POST.equals(requestMethod)){

        }
    }

    @Override
    protected InputStream doInBackground(String... params) {

        if(Constants.HTTP_GET.equals(requestMethod)){
           return HttpUtility.GET(params[0]);
        }
        if(Constants.HTTP_POST.equals(requestMethod)){
            return HttpUtility.POST(params[0], fieldsMap);
        }
        return null;
    }

    @Override
    protected void onPostExecute(InputStream s) {
        super.onPostExecute(s);
    }
}
