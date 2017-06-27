package com.example.developmentvmachine.serviciosweb.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.developmentvmachine.serviciosweb.R;
import com.example.developmentvmachine.serviciosweb.model.Cerveza;
import com.example.developmentvmachine.serviciosweb.model.WSCervezasResponse;
import com.fasterxml.jackson.core.io.DataOutputAsStream;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Development VMachine on 24/04/2017.
 */

public class HttpConnection extends AsyncTask<String, Void, String> {

//    private static final String GET_ALL = "getAll";
//    private static final String GET_BY_ID = "getById";
//    private static final String INSERT = "insert";
//    private static final String UPDATE = "update";
//    private static final String DELETE = "delete";

//    private static final String STATUS_OK = "1";
//    private static final String STATUS_NOK = "2";

    private Activity context;

    String TAG = getClass().getSimpleName();

    public HttpConnection(Activity context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        String urlString = Constants.BASE_URL + params[0];
        URL url = null;
        DataOutputAsStream printOutput;
        DataInputStream input;
        String output = "";

        switch (params[0]){
            case Constants.GET_ALL:
                Log.i(TAG, "GetAll - ini");
                try {
                    url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    Integer responseCode = connection.getResponseCode();

                    if (responseCode != null && responseCode == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        ObjectMapper mapper = new ObjectMapper();
                        WSCervezasResponse wsResponse = mapper.readValue(IOUtils.toString(reader), WSCervezasResponse.class);

                        Log.i(TAG, "getAll response: " + wsResponse.toString());

                        String statusResponse = wsResponse.getStatus();
                        List<Cerveza> cervezas = wsResponse.getCervezas();

                        if(Constants.STATUS_OK.equals(statusResponse)){

                            output = printCervezas(output, cervezas);

                        }else if (Constants.STATUS_NOK.equals(statusResponse)){
                            output = wsResponse.getMensaje();
                        }

                        output = "Resultados: " + output;
                    }

                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                Log.i(TAG, "GetAll - end");
                break;

            case Constants.GET_BY_ID:
                Log.i(TAG, "Get by id - ini");
                try {
                    url = new URL(urlString+params[1]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    Integer httpResponseCode = connection.getResponseCode();

                    if (httpResponseCode != null && httpResponseCode == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        ObjectMapper mapper = new ObjectMapper();
                        WSCervezasResponse wsResponse = mapper.readValue(IOUtils.toString(reader), WSCervezasResponse.class);

                        if(Constants.STATUS_OK.equals(wsResponse.getStatus())){
                            List<Cerveza> cervezas = wsResponse.getCervezas();

                            if (cervezas != null && cervezas.size() > 1){
                                return "Mas de un registro para ID " + cervezas.get(0).getId();
                            }

                            output = printCervezas(output, cervezas);

                        }else if (Constants.STATUS_NOK.equals(wsResponse.getStatus())){
                            output = wsResponse.getMensaje();
                            Log.e(TAG, "Error: " + output);
                        }

                    }else{
                        Log.e(TAG, "Connection error");
                    }

                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage(), e);
                    output = "Error : " + e.getMessage();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                    output = "Error : " + e.getMessage();
                }
                Log.i(TAG, "Get by id - end");
                break;

            case Constants.INSERT:
                Log.i(TAG, "Insert - ini");
                try {
                    url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    fillConnection(connection);
                    connection.connect();

                    JSONObject jsonParam = new JSONObject();
                    fillJsonParams(jsonParam, params);

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    Integer httpResponseCode = connection.getResponseCode();

                    if (httpResponseCode != null && httpResponseCode == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        ObjectMapper mapper = new ObjectMapper();
                        WSCervezasResponse wsResponse = mapper.readValue(IOUtils.toString(reader), WSCervezasResponse.class);

                        Log.i(TAG, "response: " + wsResponse.toString());

                        if(Constants.STATUS_OK.equals(wsResponse.getStatus())){
                            output = "Cerveza insertada correctamente";
                        }else if(Constants.STATUS_NOK.equals(wsResponse.getStatus())){
                            output = "Error insertando alumno: " + wsResponse.getMensaje();
                            Log.i(TAG, wsResponse.getMensaje());
                        }
                    }

                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage(), e);
                    output = "Error : " + e.getMessage();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                    output = "Error : " + e.getMessage();
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                    output = "Error : " + e.getMessage();
                }

                Log.i(TAG, "Insert - end");
                break;

            case Constants.UPDATE:
                break;

            case Constants.DELETE:

                try {
                    url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    fillConnection(connection);
                    connection.connect();

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("id", params[1]);

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    Integer httpResponseCode = connection.getResponseCode();

                    if (httpResponseCode != null && httpResponseCode == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        ObjectMapper mapper = new ObjectMapper();
                        WSCervezasResponse wsResponse = mapper.readValue(IOUtils.toString(reader), WSCervezasResponse.class);

                        String statusResponse = wsResponse.getStatus();

                        if(Constants.STATUS_OK.equals(statusResponse)){
                           output = "Cerveza eliminada correctamente";
                        }else if (Constants.STATUS_NOK.equals(statusResponse)){
                            output = wsResponse.getMensaje();
                        }
                    }

                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage(), e);
                    output = "URLException: " + e.getMessage();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                    output = "IOException: " + e.getMessage();
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                    output = "JSONException: " + e.getMessage();
                }
                break;
        }

        return output;
    }

    private void fillConnection(HttpURLConnection connection) {
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("ContectType", "application/json");
        connection.setRequestProperty("Accept", "application/json");
    }

    private void fillJsonParams(JSONObject jsonParam, String[] params) throws JSONException {

        if(!TextUtils.isEmpty(params[1])) {
            jsonParam.put("name", params[1]);
        }
        if(!TextUtils.isEmpty(params[2])) {
            jsonParam.put("description", params[2]);
        }
        if(!TextUtils.isEmpty(params[3])) {
            jsonParam.put("country", params[3]);
        }
        if(!TextUtils.isEmpty(params[4])) {
            jsonParam.put("type", params[4]);
        }
        if(!TextUtils.isEmpty(params[5])) {
            jsonParam.put("family", params[5]);
        }
        if(!TextUtils.isEmpty(params[6])) {
            jsonParam.put("alc", params[6]);
        }
    }

    @Override
    protected void onPreExecute() {
        TextView txtView = (TextView) context.findViewById(R.id.tvResultado);
        txtView.setText("");
        //super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s){
        TextView txtView = (TextView) context.findViewById(R.id.tvResultado);
        txtView.setText(s);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    private String printCervezas(String output, List<Cerveza> cervezas) {
        for (Cerveza cerveza : cervezas) {
            output += "ID: " + cerveza.getId() + "\n" +
                    "Nombre: " + cerveza.getName() + "\n" +
                    "Description: " +cerveza.getDescription() + "\n" +
                    "Familia: " + cerveza.getFamily() + "\n" +
                    "Pais: " + cerveza.getCountry() + "\n" +
                    "Alcohol: " + cerveza.getAlc() + "%" + "\n";
        }
        return output;
    }

}
