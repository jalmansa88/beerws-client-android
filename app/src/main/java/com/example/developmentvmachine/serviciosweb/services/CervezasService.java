package com.example.developmentvmachine.serviciosweb.services;

import android.app.Activity;
import android.os.AsyncTask;

/**
 * Created by Development VMachine on 24/04/2017.
 */

public interface CervezasService {

    public void getAll();

    public void getById(int id);

    public void updateCerveza(int id);

    public void deleteCerveza(int id);

    public void insertCerveza(int id);


}
