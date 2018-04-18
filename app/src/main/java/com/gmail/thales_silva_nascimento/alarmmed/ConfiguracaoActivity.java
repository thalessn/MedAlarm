package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gmail.thales_silva_nascimento.alarmmed.fragment.ConfiguracaoFragment;

public class ConfiguracaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbConfiguracao);
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Configurações");

        //Onclick no botão de retonar da toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cria um intent para retorna o valor
                Intent i = new Intent();
                //Adiciona o resultado a ser comparado e a intent
                setResult(1, i);
                finish();
            }
        });

        getFragmentManager().beginTransaction().replace(R.id.rlConfiguracao, new ConfiguracaoFragment()).commit();
    }
}
