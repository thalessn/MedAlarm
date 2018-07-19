package com.gmail.thales_silva_nascimento.alarmmed;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.bumptech.glide.Glide;

public class teste extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        AutoCompleteTextView ac = findViewById(R.id.acteste);
        ac.setThreshold(3);
        MedicamentoAutoCompleteAdapter adapter = new MedicamentoAutoCompleteAdapter(teste.this);
        ac.setAdapter(adapter);





    }

}
