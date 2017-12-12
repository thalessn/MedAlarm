package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class editaPerfil extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView hManha;
    private TextView hAlmoco;
    private TextView hJanta;
    private TextView hDormir;
    private Spinner spinner;
    private Button btnEcluir;
    private Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edita_perfil);

        toolbar = (Toolbar) findViewById(R.id.tBEditaPeril);
        toolbar.setTitle("Editar Perfil");
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24px);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        hManha = (TextView) findViewById(R.id.editaHCafeManha);
        hAlmoco = (TextView) findViewById(R.id.editaHAlmoco);
        hJanta = (TextView) findViewById(R.id.editaHJanta);
        hDormir = (TextView) findViewById(R.id.editaHDormi);

        btnEcluir = (Button) findViewById(R.id.btnExcluirPerfil);
        btnEcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(editaPerfil.this, "Excluir", Toast.LENGTH_LONG).show();
            }
        });

        setOnClickListenerTextViews();

        setaSpinner();



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
                Toast.makeText(editaPerfil.this, "Apertou o botão Salvar Edita Perfil", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setaSpinner(){
        spinner = (Spinner) findViewById(R.id.editaTpSangSpinner);

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


    private void setOnClickListenerTextViews(){

        hManha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 
                int hora = c.get(Calendar.HOUR_OF_DAY);
                int minuto = c.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(editaPerfil.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hora = String.valueOf(hourOfDay);
                        String minuto = String.valueOf(minute);
                        if(hourOfDay < 10){
                            hora = "0" + hora;
                        }
                        if(minute < 10){
                            minuto = "0" + minuto;
                        }
                        hManha.setText(hora + " : "+ minuto);
                    }
                }, hora, minuto, true);
                time.show();
            }
        });

        hAlmoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 
                int hora = c.get(Calendar.HOUR_OF_DAY);
                int minuto = c.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(editaPerfil.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hora = String.valueOf(hourOfDay);
                        String minuto = String.valueOf(minute);
                        if(hourOfDay < 10){
                            hora = "0" + hora;
                        }
                        if(minute < 10){
                            minuto = "0" + minuto;
                        }
                        hAlmoco.setText(hora + " : "+ minuto);
                    }
                }, hora, minuto, true);
                time.show();
            }
        });

        hJanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 
                int hora = c.get(Calendar.HOUR_OF_DAY);
                int minuto = c.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(editaPerfil.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hora = String.valueOf(hourOfDay);
                        String minuto = String.valueOf(minute);
                        if(hourOfDay < 10){
                            hora = "0" + hora;
                        }
                        if(minute < 10){
                            minuto = "0" + minuto;
                        }
                        hJanta.setText(hora + " : "+ minuto);
                    }
                }, hora, minuto, true);
                time.show();
            }
        });

        hDormir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 
                int hora = c.get(Calendar.HOUR_OF_DAY);
                int minuto = c.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(editaPerfil.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hora = String.valueOf(hourOfDay);
                        String minuto = String.valueOf(minute);
                        if(hourOfDay < 10){
                            hora = "0" + hora;
                        }
                        if(minute < 10){
                            minuto = "0" + minuto;
                        }
                        hDormir.setText(hora + " : "+ minuto);
                    }
                }, hora, minuto, true);
                time.show();
            }
        });
    }

}
