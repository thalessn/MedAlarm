package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.adapter.ItemAlarmeAdapter;
import com.gmail.thales_silva_nascimento.alarmmed.controller.AlarmeController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.HorarioController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.Horario;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarmeHistorico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TelaAlarme extends AppCompatActivity {

    public static final String TELA_ALARME_ATUALIZAR = "ATUALIZAR_LISTA";
    public static final String TELA_ENCERRAR = "ENCERRAR_ATIVIDADE";
    private Button btn;
    private MedicamentoController medController;
    private RecyclerView recyclerView;
    private HorarioController horarioController;
    private Toolbar toolbar;
    private ItemAlarmeAdapter adapter;


    /**
     * BroadcastReceiver utilizado para a cominucação da classe AlarmeService com esta Activity
     * Com ele a TelaAlarme sabe se deve atualizar sua lista de alarme.
     */
    private BroadcastReceiver message = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("LocalBroadcast", "Tela-Alarme Localbroadcast");
            String action = intent.getAction();


            switch (action) {
                case TELA_ALARME_ATUALIZAR:
                    Log.v("LocalBroadcat", "Tela Atualizar");
                    long horario = intent.getLongExtra("horario",0);
                    Log.v("Atualiza", "LocalBroadcast");
                    atualizaItensAlarme(horario);
                    break;

                case TELA_ENCERRAR:
                    Log.v("LocalBroadcast", "Tela Encerrar");
                    finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Atribui a view a activity
        setContentView(R.layout.activity_tela_alarme);

        toolbar = (Toolbar) findViewById(R.id.tBTelaAlarme);
        setSupportActionBar(toolbar);

        medController = new MedicamentoController(TelaAlarme.this);
        Intent info = getIntent();

        /**
         * Pega a data atual do celular para realizar a pesquisa no banco de dados instancias nesta data.
         */

        String data = Utils.CalendarToStringData(Calendar.getInstance());

        /**
         * Faz uma verificação no horario para saber se é zero
         */
        long idHorario = info.getLongExtra("horario",0);
        if(idHorario <=0) {
            Log.v("Tela Alarme", "Horario inválido");
            finish();
        }

        /**
         * Verifica se existem medicamentos para essa data e horário
         * Se não houver encerra a atividade
         */
        List<Medicamento> medicamentos = medController.medicamentosPorDataEHora(idHorario,data);
        if(medicamentos!=null && medicamentos.size()<=0){
            Log.v("Tela Alarme", "Não há medicamento para este horario");
            finish();
        }

        //Busca o objeto horario no banco
        horarioController = HorarioController.getInstance(TelaAlarme.this);
        Horario horario = horarioController.buscarHorarioPorId(idHorario);

        //AlarmeController
        AlarmeController alarmeController = new AlarmeController(TelaAlarme.this);

        //Cria a lista de itemAlames a ser exibida.
        List<ItemAlarmeHistorico> itemAlarmeHistoricos = new ArrayList<>();
        for(Medicamento med : medicamentos){
            long idAlarme = alarmeController.buscarIdAlarmePorMedID(med.getId());
            ItemAlarmeHistorico novo = new ItemAlarmeHistorico(med, data, horario, idAlarme);
            itemAlarmeHistoricos.add(novo);
        }

        //RecycleView
        recyclerView = (RecyclerView) findViewById(R.id.rvalarme);
        LinearLayoutManager ll = new LinearLayoutManager(TelaAlarme.this);
        recyclerView.setLayoutManager(ll);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        //Adapter RecycleView
        adapter = new ItemAlarmeAdapter(itemAlarmeHistoricos, TelaAlarme.this);
        recyclerView.setAdapter(adapter);


        //Registra o LocalBroadcastReceiver e suas ações
        IntentFilter intentFilter = new IntentFilter(TELA_ALARME_ATUALIZAR);
        intentFilter.addAction(TELA_ENCERRAR);
        LocalBroadcastManager.getInstance(this).registerReceiver(message, intentFilter);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        //Inicia o som
        AlarmKlaxon.start(TelaAlarme.this);
        //Contador para parar a música
        CountDownTimer timer = new CountDownTimer(15000,1000) {
            @Override
            public void onTick(long l) {

            }
            @Override
            public void onFinish() {
                AlarmKlaxon.stop(TelaAlarme.this);
            }
        };
        timer.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(message);
        Intent i = new Intent(getApplicationContext(), AlarmeService.class);
        stopService(i);
        Log.v("OnDestroy", "Desativou o serviço e fechou a tela");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adiciona uma menu a ToolBar(ActonBar)
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_tela_alarme, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Metodo que gerencia os itens do menu da Toolbar.
        switch (item.getItemId()) {
            case R.id.telaAlarmeSnooze:
                Log.v("TelaAlarme", "Apertou o Soneca");
                adapter.adiarTodosMedicamentos();
                AlarmKlaxon.stop(TelaAlarme.this);
                finish();
                return true;
            case R.id.telaAlarmeTomar:
                Log.v("TelaAlarme", "Apertou o botão tomar todos");
                adapter.tomarTodosMedicamentos();
                AlarmKlaxon.stop(TelaAlarme.this);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void atualizaItensAlarme(long idHorario){
        String data = Utils.CalendarToStringData(Calendar.getInstance());
        if(idHorario <=0) {
            Log.v("Tela Alarme - Atualizar", "Horario inválido");
            return;
        }

        /**
         * Verifica se existem medicamentos para essa data e horário
         * Se não houver encerra a atividade
         */
        List<Medicamento> medicamentos = medController.medicamentosPorDataEHora(idHorario,data);
        if(medicamentos!=null && medicamentos.size()<=0){
            Log.v("Tela Alarme - Atualizar", "Não há medicamento para este horario");
            return;
        }

        //Busca o objeto horario no banco
        horarioController = HorarioController.getInstance(TelaAlarme.this);
        Horario horario = horarioController.buscarHorarioPorId(idHorario);

        //AlarmeController
        AlarmeController alarmeController = new AlarmeController(TelaAlarme.this);


        for(Medicamento med : medicamentos){
            long idAlarme = alarmeController.buscarIdAlarmePorMedID(med.getId());
            ItemAlarmeHistorico novo = new ItemAlarmeHistorico(med, data, horario, idAlarme);
            adapter.adicionarItemAlarme(novo);
        }
        Log.v("Tela Alarme - Atualizar", "FIM FUNÇÃO");
    }
}
