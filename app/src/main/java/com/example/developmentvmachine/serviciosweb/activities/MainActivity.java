package com.example.developmentvmachine.serviciosweb.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developmentvmachine.serviciosweb.R;
import com.example.developmentvmachine.serviciosweb.impl.CervezasServiceImpl;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    Button consultar;
    Button constarId;
    Button insertar;

    Button actualizar;
    Button borrar;
    EditText id;
    EditText nombre;
    EditText description;
    EditText pais;
    EditText tipo;
    EditText familia;
    EditText alc;
    TextView resultado;

    CervezasServiceImpl cervezasImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        consultar = (Button) findViewById(R.id.consultar);
        constarId = (Button) findViewById(R.id.consultarid);
        insertar = (Button) findViewById(R.id.insertar);
        actualizar = (Button) findViewById(R.id.actualizar);
        borrar = (Button) findViewById(R.id.borrar);
        id = (EditText) findViewById(R.id.etId);
        nombre = (EditText) findViewById(R.id.etNombre);
        description = (EditText) findViewById(R.id.etDescription);
        pais = (EditText) findViewById(R.id.etCountry);
        familia = (EditText) findViewById(R.id.etFamily);
        tipo = (EditText) findViewById(R.id.etType);
        alc = (EditText) findViewById(R.id.etAlc);
        resultado = (TextView) findViewById(R.id.tvResultado);
        resultado.setMovementMethod(new ScrollingMovementMethod());

        consultar.setOnClickListener(this);
        constarId.setOnClickListener(this);
        insertar.setOnClickListener(this);
        actualizar.setOnClickListener(this);
        borrar.setOnClickListener(this);

        cervezasImpl = new CervezasServiceImpl(this);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, R.string.floating_button_message, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onClick(View v) {
        HashMap<String, String> fields = null;
        Integer beerId = !TextUtils.isEmpty(id.getText().toString()) ? Integer.valueOf(id.getText().toString()) : null;

        switch (v.getId()) {
            case R.id.consultar:
                cervezasImpl.getAll();
                break;
            case R.id.consultarid:
                if (checkIdValue(beerId)) {
                    cervezasImpl.getById(beerId);
                } else {
                    Toast toast = Toast.makeText(this, R.string.id_not_null, Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.insertar:
                if (!TextUtils.isEmpty(nombre.getText().toString())
                        && !TextUtils.isEmpty(description.getText().toString())) {
                    fields = preProcessFieldValues();
                    cervezasImpl.insertCerveza(fields);
                } else {
                    Toast toast = Toast.makeText(this, R.string.name_and_desctiption_not_empty, Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.actualizar:
                if (checkValuesForUpdate(beerId, nombre.getText().toString(), description.getText().toString())) {
                    fields = preProcessFieldValues();
                    cervezasImpl.updateCerveza(fields);
                } else {
                    Toast toast = Toast.makeText(this, R.string.id_name_and_desc_not_null, Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.borrar:
                if (checkIdValue(beerId)) {
                    cervezasImpl.deleteCerveza(beerId);
                } else {
                    Toast toast = Toast.makeText(this, R.string.id_not_null, Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            default:

                break;
        }
    }

    private HashMap<String, String> preProcessFieldValues() {
        HashMap<String, String> fieldsMap = new HashMap<>();

        fieldsMap.put("id", id.getText().toString());
        fieldsMap.put("name", nombre.getText().toString());
        fieldsMap.put("description", description.getText().toString());
        fieldsMap.put("country", pais.getText().toString());
        fieldsMap.put("country", pais.getText().toString());
        fieldsMap.put("type", tipo.getText().toString());
        fieldsMap.put("family", familia.getText().toString());
        fieldsMap.put("alc", alc.getText().toString());

        return fieldsMap;
    }

    private boolean checkIdValue(Integer id) {
        return (id == null ? false : id > 0);
    }

    private boolean checkValuesForUpdate(Integer id, String name, String desc) {
        return checkIdValue(id) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(desc);
    }

}
