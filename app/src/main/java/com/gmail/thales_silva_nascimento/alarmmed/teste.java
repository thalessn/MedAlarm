package com.gmail.thales_silva_nascimento.alarmmed;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class teste extends AppCompatActivity {
    private MedicamentoAutoCompleteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        AutoCompleteTextView ac = findViewById(R.id.acteste);
        ac.setThreshold(3);
        adapter = new MedicamentoAutoCompleteAdapter(teste.this);
        ac.setAdapter(adapter);

        ac.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RetroMedicamento retroMed = (RetroMedicamento) adapter.getItem(position);
                Toast.makeText(teste.this, retroMed.getNomeGen(), Toast.LENGTH_LONG).show();
            }
        });



    }

}
