package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.controller.AlarmeController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.HistoricoController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.HorarioController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.Horario;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarmeHistorico;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdicionarDose extends AppCompatActivity {

    private TextView tvdata;
    private TextView tvhora;
    private TextInputEditText qtdMed;
    private Toolbar toolbar;
    private int dia, mes,ano, hora, minuto;
    private long idMedicamento;
    private MedicamentoController medicamentoController;
    private HistoricoController historicoController;
    private AlarmeController alarmeController;
    private Medicamento medicamento;
    private HorarioController horarioController;
    private Calendar cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_dose);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Adicionar Medicamento");
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Adicionar Dose");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //Cria um intent para retorna o valor
//                Intent i = new Intent();
//                //Adiciona o resultado a ser comparado e a intent
//                setResult(1, i);
                finish();
            }
        });

        //Recupera o medicamaneto da tela 'pai'
        if(savedInstanceState == null){
            //Recupera as infomações passada da outra activity
            Intent i = getIntent();
            if(i != null){
                //Cria um médico que recebe um medico da activity anterior
                medicamento = i.getExtras().getParcelable("medicamento");
            }
        }else{
            medicamento = savedInstanceState.getParcelable("medicamento");
            Log.v("savedInstanceState else", "entrou");
        }

        //Controladoras
        medicamentoController = new MedicamentoController(AdicionarDose.this);
        historicoController = new HistoricoController(AdicionarDose.this);
        alarmeController = new AlarmeController(AdicionarDose.this);
        horarioController = HorarioController.getInstance(AdicionarDose.this);

        //Associa os TextViews
        tvdata = (TextView) findViewById(R.id.tvDataDose);
        tvhora = (TextView) findViewById(R.id.tvHorarioDose);
        qtdMed = (TextInputEditText) findViewById(R.id.qtdDose);

        //Cria o calendário para preencher os textviews
        cal = Calendar.getInstance();
        dia = cal.get(Calendar.DAY_OF_MONTH);
        mes = cal.get(Calendar.MONTH);
        ano = cal.get(Calendar.YEAR);
        hora = cal.get(Calendar.HOUR_OF_DAY);
        minuto = cal.get(Calendar.MINUTE);

        //Formata a hora
        String h = hora < 10? "0"+String.valueOf(hora): String.valueOf(hora);
        String mm = minuto < 10?"0"+String.valueOf(minuto):String.valueOf(minuto);
        String timeTexto = h +" : "+ mm;
        tvhora.setText(timeTexto);

        //Formata a data
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yy");
        String sDia = dia < 10 ? "0"+String.valueOf(dia) : String.valueOf(dia);
        String texto = "Hoje, "+ sDia+ " " +sdf.format(cal.getTime());
        tvdata.setText(texto);

        tvdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdicionarDose.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        formataData(i2, i1, i);
                    }
                }, ano, mes, dia);
                datePickerDialog.show();
            }
        });

        tvhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hora = cal.get(Calendar.HOUR_OF_DAY);
                minuto = cal.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(AdicionarDose.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        formataHora(i, i1);
                    }
                }, hora, minuto, true);
                time.show();
            }
        });

        //Mantém o teclado escondido quando a activity inicia. Mantendo o foco no Editext
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("medicamento", medicamento);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adiciona uma menu a ToolBar(ActonBar)
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_salvar, menu);
        return true;
    }

    // Metodo que gerencia o menu da Toolbar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iTMedSave:
                salvarDose();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void formataData(int dia, int mes, int year){
        Log.v("Parametros", String.valueOf(dia)+"-"+String.valueOf(mes)+"-"+String.valueOf(ano)+"-");
        //Declara o objeto para formata a data
        SimpleDateFormat sdf = null;
        //Atualiza a data do calendário da Activit
        cal.set(year, mes, dia);
        //Verifica se o dia escolhido foi ontem ou amanhã. Para formatar o texto de forma diferente.
        if(this.dia == dia){
            return;
        }
        if((this.dia - 1) == dia){
            sdf =  new SimpleDateFormat("MMM yy");
            String texto = "Ontem, "+ dia+ " "+ sdf.format(cal.getTime());
            tvdata.setText(texto);
            return;
        }
        if((this.dia + 1) == dia){
            sdf = new SimpleDateFormat("MMM yy");
            String texto = "Amanhã, "+ dia + " " +sdf.format(cal.getTime());
            tvdata.setText(texto);
            return;
        }
        //Insere o texto no textview
        sdf =  new SimpleDateFormat("EEE, d MMM yy");
        Log.v("Calendar", cal.toString());
        String data = sdf.format(cal.getTime());
        tvdata.setText(data);
    }

    private void formataHora(int h, int min){
        //Atualiza o calendário da Activity
        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);
        //Formata a hora a ser inserida no textview
        Log.v("FormataHora", "Entrou na função");
        String hora = h < 10? "0"+String.valueOf(h) : String.valueOf(h);
        String minuto = min < 10? "0"+String.valueOf(min) : String.valueOf(min);
        String texto = hora +" : "+ minuto;
        Log.v("FormataHora", texto);
        tvhora.setText(texto);
    }

    private void salvarDose(){
        //Verifica se o campo quantidade está vazio
        if(qtdMed.getText().toString().equals("")){
            qtdMed.setFocusable(true);
            qtdMed.requestFocus();
            return;
        }
        // Transforma em inteiro a qtd digitada
        int qtd = Integer.parseInt(qtdMed.getText().toString());
        //Verifica se a quantidade é negativa
        if(qtd <= 0){
            qtdMed.setFocusable(true);
            qtdMed.requestFocus();
            return;
        }else{
            SalvarDose salvarDose = new SalvarDose(AdicionarDose.this, medicamento, cal, qtd);
            salvarDose.execute();

            Intent i = new Intent();
            setResult(1, i);
            finish();
        }
    }

    private static class SalvarDose extends AsyncTask<Void,Void,Void>{
        private final WeakReference<AdicionarDose> mActivityRef;
        private Medicamento medicamento;
        private Calendar cal;
        private int qtd;
        ProgressDialog dialog;


        public SalvarDose(AdicionarDose activity, Medicamento medicamento, Calendar cal, int qtd){
            mActivityRef = new WeakReference<>(activity);
            this.medicamento = medicamento;
            this.cal = cal;
            this.qtd = qtd;
            this.dialog = new ProgressDialog(mActivityRef.get());
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Salvando Dose...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AlarmeController alarmeController = new AlarmeController(mActivityRef.get());
            MedicamentoController medicamentoController = new MedicamentoController(mActivityRef.get());
            HistoricoController historicoController = new HistoricoController(mActivityRef.get());
            HorarioController horarioController = HorarioController.getInstance(mActivityRef.get());

            long idAlarme = alarmeController.buscarIdAlarmePorMedID(medicamento.getId());
            String dataProgramada = Utils.CalendarToStringData(cal);
            String dataAdministrado = Utils.CalendarToStringData(cal);
            String horaAdministrado = Utils.CalendarToStringHora(cal);

            Horario horario = horarioController.buscarHorario(Utils.CalendarToStringHora(cal));
            //Instancia o objeto que salvará as informações do estoque;
            ItemAlarmeHistorico itemAlarmeHistorico = new ItemAlarmeHistorico(medicamento,dataProgramada,horario,idAlarme, dataAdministrado, horaAdministrado);
            itemAlarmeHistorico.setStatus(ItemAlarmeHistorico.STATUS_TOMADO);
            //Cadastra no histório a data e hora que a dose foi tomada
            historicoController.cadastrarHistoricoMedicamento(itemAlarmeHistorico);
            //Só por preocaução de o alarme te toca e a pessoa está na tela de adicionar dose
            medicamento = medicamentoController.buscarMedicamentoPorId(medicamento.getId());

            int qtdAtual = medicamento.getQuantidade();
            if(qtdAtual != -1){
                int qtdNova = qtdAtual - qtd;
                if(qtdNova <= 0) qtdNova = 0;
                medicamento.setQuantidade(qtdNova);
                medicamentoController.atualizarMedicamento(medicamento);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(dialog!= null){
                dialog.dismiss();
                Log.v("SalvarDose", "Terminou de executar");
            }
        }
    }

}
