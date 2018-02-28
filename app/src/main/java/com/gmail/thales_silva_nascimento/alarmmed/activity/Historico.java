package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.ListItemHistorico;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.adapter.HistoricoRecycleAdapter;
import com.gmail.thales_silva_nascimento.alarmmed.controller.HistoricoController;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.detalheHistorico;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.filtroHistorico;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarmeHistorico;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Historico extends AppCompatActivity implements HistoricoRecycleAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private HistoricoController historicoController;
    private Toolbar toolbar;
    private HistoricoRecycleAdapter adapter;
    private List<ListItemHistorico> historico;
    private TextView tvPeriodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        toolbar = (Toolbar) findViewById(R.id.tbHistorico);
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Histórico");

        //Onclick no botão de retonar da toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rvHistorico);
        recyclerView.setLayoutManager(new LinearLayoutManager(Historico.this));
        recyclerView.setHasFixedSize(true);

        //Controladora histórico
        historicoController = new HistoricoController(Historico.this);
        //Lista com os objetos do tipo ListItemHistorico que implemnta a interface ListItemhistorico
        //onde na lista possui o tipo HeaderHistorico e ItemAlarmeHistorico. A interface é utilizada
        //conseguir agrupar os medicamento com as datas.
        //Calcula a data inicial e final para ficar no período de 1 seman atrás.
        Calendar calFinal = Calendar.getInstance();
        String dataFinal = Utils.CalendarToStringData(calFinal);
        Log.v("dataFinal", dataFinal);
        dataFinal.replaceAll(" ","");

        Calendar calInical = Calendar.getInstance();
        calInical.add(Calendar.DATE, -7);
        String dataInicial = Utils.CalendarToStringData(calInical);
        Log.v("DataInicial", dataInicial);
        dataInicial.replaceAll(" ","");
        //Pesquisa no banco de dados
        historico = historicoController.listarHistoricoPeriodo(dataInicial, dataFinal);

        //Insere texto abaixo de Status
        SimpleDateFormat dia = new SimpleDateFormat("d");
        SimpleDateFormat mes = new SimpleDateFormat("MMM");
        SimpleDateFormat ano = new SimpleDateFormat("yyyy");
        String texto = mes.format(calInical.getTime())+" "+dia.format(calInical.getTime())
                +" - "+mes.format(calFinal.getTime())+" "+dia.format(calFinal.getTime())
                +", "+ano.format(calFinal.getTime());

        tvPeriodo = (TextView) findViewById(R.id.tvPeriodoHist);
        tvPeriodo.setText(texto);

        //adapater do recyclerview
        adapter = new HistoricoRecycleAdapter(Historico.this, historico);
        //OnClick do adapter
        adapter.setClickListener(this);

        //Adiciona o adapter no recyclerview
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adiciona um menu a ToolBar(ActonBar)
        getMenuInflater().inflate(R.menu.toolbar_historico, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Metodo que gerencia os itens do menu da Toolbar.
        switch (item.getItemId()) {
            case R.id.iTConfig:
                DialogFragment dialog = new filtroHistorico();
                dialog.show(getSupportFragmentManager(),"filtroHistorico");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view, int position) {
        DialogFragment dialog = new detalheHistorico();
        Bundle bundle = new Bundle();
        bundle.putParcelable("itemAlarmeHistorico", (ItemAlarmeHistorico)historico.get(position));
        //Manda o objeto com as informacoes do sistema
        dialog.setArguments(bundle);
        //Exibe o diálogo com as informação dos sistema.
        dialog.show(getSupportFragmentManager(), "itemAlarmeHistorico");
    }
}
