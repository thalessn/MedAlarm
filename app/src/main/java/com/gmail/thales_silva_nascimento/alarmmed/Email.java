package com.gmail.thales_silva_nascimento.alarmmed;

import android.app.DatePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.EnumMap;

public class Email extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvDataIni, tvDataFinal;
    private TextInputEditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        toolbar = (Toolbar) findViewById(R.id.tbEmail);
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Enviar Relatório de Tratamento");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvDataIni = (TextView) findViewById(R.id.dInicialHis);
        tvDataFinal = (TextView) findViewById(R.id.dFinalHis);

        //Calendário para preencher os textviews dos períodos
        Calendar cal = Calendar.getInstance();
        String dataFinal = Utils.formataDataBrasil(Utils.CalendarToStringData(cal));
        //Retira 30 dias do dia atual
        cal.add(Calendar.DATE, -30);
        String dataInicial = Utils.formataDataBrasil(Utils.CalendarToStringData(cal));

        tvDataIni.setText(dataInicial);
        tvDataFinal.setText(dataFinal);


        tvDataIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int dia = cal.get(Calendar.DAY_OF_WEEK);
                int mes = cal.get(Calendar.MONTH);
                int ano = cal.get(Calendar.YEAR);
                DatePickerDialog date = new DatePickerDialog(Email.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String novaData = Utils.formataDataBrasil(i2,i1,i);
                        tvDataIni.setText(novaData);
                    }
                }, ano, mes, dia);

                date.show();
            }
        });

        tvDataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int dia = cal.get(Calendar.DAY_OF_WEEK);
                int mes = cal.get(Calendar.MONTH);
                int ano = cal.get(Calendar.YEAR);
                DatePickerDialog date = new DatePickerDialog(Email.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String novaData = Utils.formataDataBrasil(i2,i1,i);
                        tvDataIni.setText(novaData);
                    }
                }, ano, mes, dia);

                date.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adiciona um menu a ToolBar(ActonBar)
        getMenuInflater().inflate(R.menu.toolbar_enviar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Metodo que gerencia os itens do menu da Toolbar.
        switch (item.getItemId()) {
            case R.id.iTEnviar:
                Toast.makeText(Email.this, "Enviar", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
