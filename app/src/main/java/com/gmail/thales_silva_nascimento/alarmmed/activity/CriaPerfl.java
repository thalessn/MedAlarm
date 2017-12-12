package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.TimePickerFragment;

import java.util.ArrayList;
import java.util.List;

public class CriaPerfl extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView hManha;
    private TextView hAlmoco;
    private TextView hJanta;
    private TextView hDormir;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cria_perfl);

        toolbar = (Toolbar) findViewById(R.id.tBCriaPeril);
        toolbar.setTitle("Criar Perfil");
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24px);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Adiciona o spinner do tipo sanguineo
        setaSpinner();

        //Seta onclicklistener no textview dos horários
        setaOnclickListenerHorarioTextView();

    }

    private void setaSpinner(){
        spinner = (Spinner) findViewById(R.id.tpSanguineoSpinner);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("A +");
        categories.add("A -");
        categories.add("AB +");
        categories.add("AB -");
        categories.add("B +");
        categories.add("B -");
        categories.add("O +");
        categories.add("O -");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private void setaOnclickListenerHorarioTextView()
    {

        hManha = (TextView) findViewById(R.id.hCafeManha);
        hManha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker1");

            }
        });

        hAlmoco = (TextView) findViewById(R.id.hAlmoco);
        hAlmoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker2");
            }
        });

        hJanta = (TextView) findViewById(R.id.hJanta);
        hJanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker3");
            }
        });

        hDormir = (TextView) findViewById(R.id.hDormi);
        hDormir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker4");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adiciona uma menu a ToolBar(ActonBar)
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_cria_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Metodo que gerencia os itens do menu da Toolbar.
        switch (item.getItemId()) {
            case R.id.iTPerfilSave:
                Toast.makeText(CriaPerfl.this, "Apertou o botão Salvar", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
