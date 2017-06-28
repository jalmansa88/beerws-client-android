package com.example.developmentvmachine.serviciosweb.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developmentvmachine.serviciosweb.R;
import com.example.developmentvmachine.serviciosweb.model.Cerveza;
import com.example.developmentvmachine.serviciosweb.model.WSCervezasResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
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
import java.util.List;

/**
 * Created by Development VMachine on 24/04/2017.
 */

public class HttpConnection extends AsyncTask<String, Void, WSCervezasResponse> {

    private Activity context;

    String TAG = getClass().getSimpleName();

    public HttpConnection(Activity context){
        this.context = context;
    }

    @Override
    protected WSCervezasResponse doInBackground(String... params) {

        String urlString = Constants.BASE_URL + params[0];
        URL url = null;
        WSCervezasResponse wsResponse = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        switch (params[0]){
            case Constants.GET_ALL:
                Log.i(TAG, "GetAll - ini");
                try {
                    url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    Integer responseCode = connection.getResponseCode();

                    if (responseCode != null && responseCode == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        reader = new BufferedReader(new InputStreamReader(in));

                        ObjectMapper mapper = new ObjectMapper();
                        wsResponse = mapper.readValue(IOUtils.toString(reader), WSCervezasResponse.class);

                        Log.i(TAG, "getAll response: " + wsResponse.toString());

                        String statusResponse = wsResponse.getStatus();
                        List<Cerveza> cervezas = wsResponse.getCervezas();

                        if(Constants.STATUS_OK.equals(statusResponse)){
                            //output = printCervezas(output, cervezas);
                            wsResponse.setMensaje("Mostrando lista completa de cervezas");
                        }else if(Constants.STATUS_NOK.equals(wsResponse.getStatus())) {
                            wsResponse.setMensaje("Error de conexion");
                            Log.i(TAG, wsResponse.getMensaje());
                        }
                    }

                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            Log.e(TAG, "Error closing stream", e);
                        }
                    }
                }
                Log.i(TAG, "GetAll - end");
                break;

            case Constants.GET_BY_ID:
                Log.i(TAG, "Get by id - ini");

                try {
                    url = new URL(urlString+params[1]);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    Integer httpResponseCode = connection.getResponseCode();

                    if (httpResponseCode != null && httpResponseCode == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        reader = new BufferedReader(new InputStreamReader(in));

                        ObjectMapper mapper = new ObjectMapper();
                        wsResponse = mapper.readValue(IOUtils.toString(reader), WSCervezasResponse.class);

                        if(Constants.STATUS_OK.equals(wsResponse.getStatus())){
                            List<Cerveza> cervezas = wsResponse.getCervezas();

                            if (cervezas != null && cervezas.size() > 1){
                                wsResponse.setMensaje("Mas de un registro para ID " + cervezas.get(0).getId());
                            }else{
                                wsResponse.setMensaje("Beer found!!");
                            }
                        } else if(Constants.STATUS_NOK.equals(wsResponse.getStatus())) {
                            wsResponse.setMensaje("Beear not found :(");
                            Log.i(TAG, wsResponse.getMensaje());
                        }

                    }else{
                        Log.e(TAG, "Connection error");
                    }

                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            Log.e(TAG, "Error closing stream", e);
                        }
                    }
                }

                Log.i(TAG, "Get by id - end");
                break;

            case Constants.INSERT:
                Log.i(TAG, "Insert - ini");
                try {
                    url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
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
                        reader = new BufferedReader(new InputStreamReader(in));

                        ObjectMapper mapper = new ObjectMapper();
                        wsResponse = mapper.readValue(IOUtils.toString(reader), WSCervezasResponse.class);

                        Log.i(TAG, "response: " + wsResponse.toString());

                        if(Constants.STATUS_OK.equals(wsResponse.getStatus())){
                            wsResponse.setMensaje("Cerveza insertada correctamente");
                        }else if(Constants.STATUS_NOK.equals(wsResponse.getStatus())){
                            wsResponse.setMensaje("Error insertando cerveza: " + wsResponse.getMensaje());
                            Log.i(TAG, wsResponse.getMensaje());
                        }
                    }

                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            Log.e(TAG, "Error closing stream", e);
                        }
                    }
                }

                Log.i(TAG, "Insert - end");
                break;

            case Constants.UPDATE:
                Log.i(TAG, "Update - ini");
                try {
                    url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    fillConnection(connection);
                    connection.connect();

                    JSONObject jsonParam = new JSONObject();
                    fillJsonParamsToUpdate(jsonParam, params);

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    Integer httpResponseCode = connection.getResponseCode();

                    if (httpResponseCode != null && httpResponseCode == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        reader = new BufferedReader(new InputStreamReader(in));

                        ObjectMapper mapper = new ObjectMapper();
                        wsResponse = mapper.readValue(IOUtils.toString(reader), WSCervezasResponse.class);

                        Log.i(TAG, "response: " + wsResponse.toString());

                        if(Constants.STATUS_OK.equals(wsResponse.getStatus())){
                            wsResponse.setMensaje("Cerveza actualizada correctamente");
                        }else if(Constants.STATUS_NOK.equals(wsResponse.getStatus())){
                            wsResponse.setMensaje("Error actualizando cerveza: " + wsResponse.getMensaje());
                            Log.i(TAG, wsResponse.getMensaje());
                        }
                    }

                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            Log.e(TAG, "Error closing stream", e);
                        }
                    }
                }

                Log.i(TAG, "Update - end");
                break;

            case Constants.DELETE:

                try {
                    url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
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
                        reader = new BufferedReader(new InputStreamReader(in));

                        ObjectMapper mapper = new ObjectMapper();
                        wsResponse = mapper.readValue(IOUtils.toString(reader), WSCervezasResponse.class);

                        String statusResponse = wsResponse.getStatus();

                        if(Constants.STATUS_OK.equals(statusResponse)){
                            wsResponse.setMensaje("Cerveza eliminada correctamente");
                        }else if (Constants.STATUS_NOK.equals(statusResponse)){
                            wsResponse.setMensaje("Error de conexion eliminando cerveza");
                        }
                    }

                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            Log.e(TAG, "Error closing stream", e);
                        }
                    }
                }
                break;
        }

        return wsResponse;
    }

    @Override
    protected void onPreExecute() {
        TextView txtView = (TextView) context.findViewById(R.id.tvResultado);
        txtView.setText("");
    }

    @Override
    protected void onPostExecute(WSCervezasResponse wsResponse){
        Toast toast = Toast.makeText(context, wsResponse.getMensaje(), Toast.LENGTH_SHORT);
        toast.show();

        if (wsResponse.getCervezas() != null && wsResponse.getCervezas().size() > 1){
            TextView txtView = (TextView) context.findViewById(R.id.tvResultado);
            txtView.setText(printCervezasTextView(wsResponse.getCervezas()));
        }else if(wsResponse.getCervezas() != null && wsResponse.getCervezas().size() > 0){
            fillBeerFields(wsResponse.getCervezas().get(0));
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(WSCervezasResponse s) {
        super.onCancelled(s);
    }

    private String printCervezasTextView(List<Cerveza> cervezas) {
        String output = "";
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

    private void fillConnection(HttpURLConnection connection) {
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("ContectType", "application/json");
        connection.setRequestProperty("Accept", "application/json");
    }

    private void fillBeerFields(Cerveza beer){
        EditText id = (EditText) context.findViewById(R.id.etId);
        EditText nombre = (EditText) context.findViewById(R.id.etNombre);
        EditText description = (EditText) context.findViewById(R.id.etDescription);
        EditText pais = (EditText) context.findViewById(R.id.etCountry);
        EditText familia = (EditText) context.findViewById(R.id.etFamily);
        EditText tipo = (EditText) context.findViewById(R.id.etType);
        EditText alc = (EditText) context.findViewById(R.id.etAlc);
        id.setText(Integer.toString(beer.getId()));
        nombre.setText(beer.getName());
        description.setText(beer.getDescription());
        pais.setText(beer.getCountry());
        familia.setText(beer.getFamily());
        tipo.setText(beer.getType());
        alc.setText(beer.getAlc().toString());
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
    private void fillJsonParamsToUpdate(JSONObject jsonParam, String[] params) throws JSONException {
        if(!TextUtils.isEmpty(params[1])) {
            jsonParam.put("id", params[1]);
        }
        if(!TextUtils.isEmpty(params[2])) {
            jsonParam.put("name", params[2]);
        }
        if(!TextUtils.isEmpty(params[3])) {
            jsonParam.put("description", params[3]);
        }
        if(!TextUtils.isEmpty(params[4])) {
            jsonParam.put("country", params[4]);
        }
        if(!TextUtils.isEmpty(params[5])) {
            jsonParam.put("type", params[5]);
        }
        if(!TextUtils.isEmpty(params[6])) {
            jsonParam.put("family", params[6]);
        }
        if(!TextUtils.isEmpty(params[7])) {
            jsonParam.put("alc", params[7]);
        }
    }

}
