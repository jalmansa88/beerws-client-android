package com.example.developmentvmachine.serviciosweb.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.example.developmentvmachine.serviciosweb.R;
import com.example.developmentvmachine.serviciosweb.impl.CervezasServiceImpl;
import com.example.developmentvmachine.serviciosweb.model.Cerveza;
import com.example.developmentvmachine.serviciosweb.utils.BeerAdapter;

import java.util.List;

public class GetAllActivity extends AppCompatActivity {

    private CervezasServiceImpl serviceImpl = new CervezasServiceImpl(this);
    ListView beerListView;
    EditText etFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all);

        serviceImpl.getAll();
    }
}
