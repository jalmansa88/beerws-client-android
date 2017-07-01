package com.example.developmentvmachine.serviciosweb.impl;

import android.app.Activity;
import android.util.Log;

import com.example.developmentvmachine.serviciosweb.model.WSCervezasResponse;
import com.example.developmentvmachine.serviciosweb.services.CervezasService;
import com.example.developmentvmachine.serviciosweb.utils.Constants;
import com.example.developmentvmachine.serviciosweb.utils.HttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Development VMachine on 26/06/2017.
 */

public class CervezasServiceImpl implements CervezasService {

    Activity context;

    public CervezasServiceImpl(Activity context) {
        this.context = context;
    }

    public CervezasServiceImpl() {
    }

    @Override
    public void getAll() {
        HttpClient httpClient = new HttpClient(context, null, "GET");
        httpClient.execute(Constants.BASE_URL+Constants.GET_ALL);


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

    @Override
    public void getById(int id) {

    }

    @Override
    public void updateCerveza(int id) {

    }

    @Override
    public void deleteCerveza(int id) {

    }

    @Override
    public void insertCerveza(int id) {

    }


}
