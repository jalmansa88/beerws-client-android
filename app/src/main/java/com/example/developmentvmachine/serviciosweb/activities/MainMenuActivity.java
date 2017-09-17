package com.example.developmentvmachine.serviciosweb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.developmentvmachine.serviciosweb.R;
import com.example.developmentvmachine.serviciosweb.impl.CervezasServiceImpl;

public class MainMenuActivity extends AppCompatActivity {

    private CervezasServiceImpl serviceImpl = new CervezasServiceImpl(this);

    Button showAllButton;
    Button findByIdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        findByIdButton = (Button)findViewById(R.id.btFindById);
        showAllButton = (Button)findViewById(R.id.btShowAll);

        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), GetAllActivity.class));
            }
        });

        findByIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), FindByIdActivity.class));
            }
        });
    }
}