package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.TelaAlarme;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class LembreteCompraRemedio extends AppCompatActivity {

    private LinearLayout llGps;
    private Medicamento medicamento;
    private MedicamentoController mc;
    private CircleImageView img;
    private TextView nomeMed;
    private TextView tvQtdMed;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("medicamento", medicamento);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembrete_compra_remedio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbLembreCompra);
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Onclick no botão de retonar da toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Inicia a controladora de Medicamento
        mc = new MedicamentoController(LembreteCompraRemedio.this);

        if(savedInstanceState == null){
            //Recupera as infomações passada da outra activity
            Intent i = getIntent();
            if(i != null){
                //Cria um medicamento através da idMedicamento que recebe da intent
                medicamento =  mc.buscarMedicamentoPorId(i.getExtras().getLong("idMedicamento"));
                if(medicamento == null){
                    finish();
                }
            }else{
                finish();
            }
        }else{
            medicamento = savedInstanceState.getParcelable("medicamento");
            Log.i("savedInstanceState else", "entrou");
        }


        llGps = (LinearLayout) findViewById(R.id.llGpsLembreteTela);
        llGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Adiciona o nome do medicamento
        nomeMed = (TextView) findViewById(R.id.nomeMedLembreteTela);
        nomeMed.setText(medicamento.getNome());


        //Imagem do medicamento
        img = (CircleImageView) findViewById(R.id.imgLembreteTela);
        loadImagem(medicamento);

        //Atribui o texto que informa a quantidade de remédios
        tvQtdMed = (TextView) findViewById(R.id.tvQtdRemedioEst);
        if(medicamento.getQuantidade() < 2){
            String qtd = String.valueOf(medicamento.getQuantidade());
            String texto = "Você possui " +qtd+ " remédio no momento.";
            tvQtdMed.setText(texto);
        }else{
            String qtd = String.valueOf(medicamento.getQuantidade());
            String texto = "Você possui " +qtd+ " remédios no momento.";
            tvQtdMed.setText(texto);
        }


    }

    private void loadImagem(Medicamento med){
        //Arquivo que contém o caminho da imagem no armazenamento interno
        File path = LembreteCompraRemedio.this.getFileStreamPath(med.getFoto());
        //Verifica se o caminho existe. Se existir carrega a imagem, se não, carregue a imagem padrão.
        if(path.exists()){
            //existe
            Glide.with(LembreteCompraRemedio.this).load(path).into(img);
            Log.v("Img Existe","Conseguiu ler");
        }else{
            //não existe
            Glide.with(LembreteCompraRemedio.this).load(R.drawable.remedio1).into(img);
            Log.v("Img NãoExiste","Não conseguiu ler");
        }
    }

}
