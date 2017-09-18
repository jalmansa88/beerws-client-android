package com.example.developmentvmachine.serviciosweb.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.developmentvmachine.serviciosweb.R;
import com.example.developmentvmachine.serviciosweb.impl.CervezasServiceImpl;

public class FindByIdActivity extends AppCompatActivity {

    private CervezasServiceImpl serviceImpl = new CervezasServiceImpl(this);
    EditText editTextBeerId;
    Button findButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_by_id);

        editTextBeerId = (EditText)findViewById(R.id.etIdActivity);
        findButton = (Button)findViewById(R.id.buttonFind);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextBeerId.getText() == null
                    || !validIdValue(Integer.parseInt(editTextBeerId.getText().toString()))){
                    Toast toast = Toast.makeText(getApplicationContext(), "Insert Beer ID", Toast.LENGTH_SHORT);
                }else{
                    serviceImpl.getById(Integer.parseInt(editTextBeerId.getText().toString()));
                }
            }
        });
    }

    private boolean validIdValue(Integer id) {
        return (id == null ? false : id > 0);
    }
}
