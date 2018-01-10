package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class receitaCadastro extends AppCompatActivity {
    private Toolbar toolbar;
    private Button medicoCad;
    private Button medicamentoCad;
    private Spinner medicos;
    private TextView dataRec;
    private Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita_cadastro);

        dataRec = (TextView) findViewById(R.id.dataRec);
        medicos = (Spinner) findViewById(R.id.medicoSpinner);
        medicoCad = (Button) findViewById(R.id.btnMedCad);
        medicamentoCad = (Button) findViewById(R.id.btnMedicamentoRec);
        toolbar = (Toolbar) findViewById(R.id.tBReceitaCad);

        toolbar.setTitle("Adicionar Receita");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(receitaCadastro.this, "Fechou Receita", Toast.LENGTH_LONG).show();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(receitaCadastro.this);
                alertDialog.setTitle("Tem Certeza?");
                alertDialog.setMessage("Você têm certeza de sair sem salvar a receita?");
                alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Toast.makeText(receitaCadastro.this, "Apertou SIM", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(receitaCadastro.this, "Apertou Cancelar", Toast.LENGTH_LONG).show();
                    }
                });

                alertDialog.show();


            }
        });

        dataRec.setText(c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR));
        dataRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog date = new DatePickerDialog(receitaCadastro.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dataRec.setText(dayOfMonth + "/" + (month +1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                date.show();
            }
        });


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Selecione");
        categories.add("Manuel");
        categories.add("Fabrícion Nascimento");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Adiciona o adapter no spinner
        medicos.setAdapter(dataAdapter);

        //Botão cadastrar médico
        medicoCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(receitaCadastro.this, activity_medicoCadastro.class);
                i.putExtra("tipoTela", 1);
                startActivityForResult(i, 1);
            }
        });

        //Botão Cadastrar Medicamento
        medicamentoCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(receitaCadastro.this, MedicamentoCadastro.class);
                startActivity(m);
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
                Toast.makeText(receitaCadastro.this, "Apertou o botão Salvar", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
