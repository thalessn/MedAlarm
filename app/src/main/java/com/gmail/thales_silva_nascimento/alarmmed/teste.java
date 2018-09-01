package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.gmail.thales_silva_nascimento.alarmmed.adapter.MedicamentoAutoCompleteAdapter;

public class teste extends AppCompatActivity {
    private MedicamentoAutoCompleteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        Button btn = (Button) findViewById(R.id.btnTeste);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(teste.this, OnBootService.class);
                startService(i);
            }
        });


    }

}
