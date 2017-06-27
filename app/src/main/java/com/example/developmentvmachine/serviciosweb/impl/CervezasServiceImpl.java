package com.example.developmentvmachine.serviciosweb.impl;

import android.app.Activity;

import com.example.developmentvmachine.serviciosweb.services.CervezasService;

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
