package com.example.developmentvmachine.serviciosweb.services;

import java.util.HashMap;

/**
 * Created by Development VMachine on 24/04/2017.
 */

public interface CervezasService {

    public void getAll();

    public void getById(int id);

    public void updateCerveza(int id, HashMap<String, String> fieldsMap);

    public void deleteCerveza(int id);

    public void insertCerveza(HashMap<String, String> fieldsMap);


}
