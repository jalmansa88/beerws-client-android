package com.example.developmentvmachine.serviciosweb.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Development VMachine on 24/04/2017.
 *
 * Http connection class to get the Beer data from DB
 *
 */

public class HttpConnection extends AsyncTask<HashMap, Void, WSCervezasResponse> {

    private Activity context;
    Bitmap bitmap = null;


    private final String TAG = getClass().getSimpleName();

    public HttpConnection(Activity context){
        this.context = context;
    }

    @Override
    protected WSCervezasResponse doInBackground(HashMap... params) {

        HashMap<String, String> fieldsMap = params[0];

        String urlString = Constants.BASE_URL + fieldsMap.get(Constants.OPERATION);
        URL url;
        WSCervezasResponse wsResponse = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        switch (fieldsMap.get(Constants.OPERATION)){
            case Constants.GET_ALL:
                Log.i(TAG, "GetAll - ini");
                try {
                    url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    Integer responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        reader = new BufferedReader(new InputStreamReader(in));

                        ObjectMapper mapper = new ObjectMapper();
                        wsResponse = mapper.readValue(IOUtils.toString(reader), WSCervezasResponse.class);

                        Log.i(TAG, "getAll response: " + wsResponse.toString());

                        String statusResponse = wsResponse.getStatus();

                        if(Constants.STATUS_OK.equals(statusResponse)){
                            //output = printCervezas(output, cervezas);
                            wsResponse.setMensaje("Mostrando lista completa de cervezas");
                        }else if(Constants.STATUS_NOK.equals(wsResponse.getStatus())) {
                            wsResponse.setMensaje("Error de conexion");
                            Log.i(TAG, wsResponse.getMensaje());
                        }
                    }
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
                    url = new URL(urlString+fieldsMap.get("id"));
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    Integer httpResponseCode = connection.getResponseCode();

                    if (httpResponseCode == HttpURLConnection.HTTP_OK){

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
                                if (wsResponse.getCervezas().get(0).getImagePath() != null){
                                    URL imageUrl = new URL(Constants.BASE_URL + "/" + wsResponse.getCervezas().get(0).getImagePath());
                                    HttpURLConnection imageConnection = (HttpURLConnection) imageUrl.openConnection();
                                    imageConnection.connect();
                                    bitmap = BitmapFactory.decodeStream(imageConnection.getInputStream());
                                } else {
                                    bitmap = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_delete);
                                }
                            }
                        } else if(Constants.STATUS_NOK.equals(wsResponse.getStatus())) {
                            wsResponse.setMensaje("Connection Error");
                            Log.i(TAG, wsResponse.getMensaje());
                        }

                    }else{
                        Log.e(TAG, "Connection error");
                    }

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

                    fieldsMap.remove(Constants.OPERATION);
                    JSONObject jsonParam = new JSONObject(fieldsMap);

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    Integer httpResponseCode = connection.getResponseCode();

                    if (httpResponseCode == HttpURLConnection.HTTP_OK){

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

                Log.i(TAG, "Insert - end");
                break;

            case Constants.UPDATE:
                Log.i(TAG, "Update - ini");
                try {
                    url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    fillConnection(connection);
                    connection.connect();

                    fieldsMap.remove(Constants.OPERATION);
                    JSONObject jsonParam = new JSONObject(fieldsMap);

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    Integer httpResponseCode = connection.getResponseCode();

                    if (httpResponseCode == HttpURLConnection.HTTP_OK){

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

                Log.i(TAG, "Update - end");
                break;

            case Constants.DELETE:

                try {
                    url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    fillConnection(connection);
                    connection.connect();

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("id", fieldsMap.get("id"));

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    Integer httpResponseCode = connection.getResponseCode();

                    if (httpResponseCode == HttpURLConnection.HTTP_OK){

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

                } catch (IOException | JSONException e) {
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
        ImageView imageView = (ImageView) context.findViewById(R.id.ivImagen);

        id.setText(String.valueOf(beer.getId()));
        nombre.setText(beer.getName());
        description.setText(beer.getDescription());
        pais.setText(beer.getCountry());
        familia.setText(beer.getFamily());
        tipo.setText(beer.getType());
        alc.setText(String.valueOf(beer.getAlc()));
        imageView.setImageBitmap(bitmap);
    }

//    private void fillJsonParams(JSONObject jsonParam, HashMap params) throws JSONException {
//        if(!TextUtils.isEmpty(params[1])) {
//            jsonParam.put("name", params[1]);
//        }
//        if(!TextUtils.isEmpty(params[2])) {
//            jsonParam.put("description", params[2]);
//        }
//        if(!TextUtils.isEmpty(params[3])) {
//            jsonParam.put("country", params[3]);
//        }
//        if(!TextUtils.isEmpty(params[4])) {
//            jsonParam.put("type", params[4]);
//        }
//        if(!TextUtils.isEmpty(params[5])) {
//            jsonParam.put("family", params[5]);
//        }
//        if(!TextUtils.isEmpty(params[6])) {
//            jsonParam.put("alc", params[6]);
//        }
//    }

//    private void fillJsonParamsToUpdate(JSONObject jsonParam, HashMap params) throws JSONException {
//        if (params.get("id") != null) {
//            jsonParam.put("id", params.get("id"));
//        }
//        if (params.get("nombre") != null) {
//            jsonParam.put("name", params.get("name"));
//        }
//        if (params.get("id") != null) {
//            jsonParam.put("id", params.get("id"));
//        }
//        if (params.get("id") != null) {
//            jsonParam.put("id", params.get("id"));
//        }
//        if (params.get("id") != null) {
//            jsonParam.put("id", params.get("id"));
//        }
//        if (params.get("id") != null) {
//            jsonParam.put("id", params.get("id"));
//        }
//        if (params.get("id") != null) {
//            jsonParam.put("id", params.get("id"));
//        }
//        if (params.get("id") != null) {
//            jsonParam.put("id", params.get("id"));
//        }
//        if (!TextUtils.isEmpty(nombre.getText().toString())) {
//            jsonParam.put("name", nombre.getText().toString());
//        }
//        if (!TextUtils.isEmpty(description.getText().toString())) {
//            jsonParam.put("description", description.getText().toString());
//        }
//        if (!TextUtils.isEmpty(pais.getText().toString())) {
//            jsonParam.put("country", pais.getText().toString());
//        }
//        if (!TextUtils.isEmpty(tipo.getText().toString())) {
//            jsonParam.put("type", tipo.getText().toString());
//        }
//        if (!TextUtils.isEmpty(familia.getText().toString())) {
//            jsonParam.put("family", familia.getText().toString());
//        }
//        if (!TextUtils.isEmpty(alc.getText().toString())) {
//            jsonParam.put("alc", alc.getText().toString());
//        }
//    }

}
