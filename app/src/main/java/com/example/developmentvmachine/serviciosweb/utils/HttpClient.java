package com.example.developmentvmachine.serviciosweb.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.developmentvmachine.serviciosweb.services.CallBackService;

import java.util.HashMap;

/**
 * Created by Development VMachine on 28/06/2017.
 */

public class HttpClient extends AsyncTask<String, Void, String> {

    String TAG = getClass().getSimpleName();

    private CallBackService callBackService;
    private Bitmap bitmap = null;
    private HashMap<String, String> fieldsMap;
    private String requestMethod;

    public HttpClient(){
        super();
    }

    public HttpClient(CallBackService cb, HashMap<String, String> fieldData, String request){
        this.fieldsMap = fieldData;
        this.requestMethod = request;
        this.callBackService = cb;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = Constants.BASE_URL + params[0];
        if(Constants.HTTP_GET.equals(requestMethod)){
           return HttpUtility.GET(url);
        }else if(Constants.HTTP_POST.equals(requestMethod)){
            return HttpUtility.POST(url, fieldsMap);
        }else{
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        callBackService.callback(s);
    }
}
