package com.example.developmentvmachine.serviciosweb.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.developmentvmachine.serviciosweb.R;
import com.example.developmentvmachine.serviciosweb.impl.CervezasServiceImpl;

public class GetAllActivity extends AppCompatActivity {

    private CervezasServiceImpl serviceImpl = new CervezasServiceImpl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all);

        serviceImpl.getAll();

    }
}
