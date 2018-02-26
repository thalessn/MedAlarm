package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.ListItemHistorico;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.adapter.HistoricoRecycleAdapter;
import com.gmail.thales_silva_nascimento.alarmmed.controller.HistoricoController;

import java.util.List;

public class Historico extends AppCompatActivity implements HistoricoRecycleAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private HistoricoController historicoController;
    private Toolbar toolbar;

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
        List<ListItemHistorico> historico = historicoController.listarTodosItensHistorico();
        //adapater do recyclerview
        HistoricoRecycleAdapter adapter = new HistoricoRecycleAdapter(Historico.this, historico);

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
                Toast.makeText(Historico.this, "Teste do botão", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view, int position) {
        Log.v("Teste", "Fucnionou");
        Toast.makeText(Historico.this, "Olá sou a posicao:"+String.valueOf(position), Toast.LENGTH_LONG).show();
    }
}
