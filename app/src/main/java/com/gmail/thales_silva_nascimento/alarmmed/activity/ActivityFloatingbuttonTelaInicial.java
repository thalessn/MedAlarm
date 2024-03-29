package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.SelecionarMedicamentoDose;

public class ActivityFloatingbuttonTelaInicial extends AppCompatActivity implements SelecionarMedicamentoDose.SelecionaMedicamentoDoseListerner{

    private FloatingActionMenu fam;
    private FloatingActionButton fabDose, fabAdd;
    private static final int CODIGO_RESULT_ACTIVITY = 1;
    private boolean verificaMenuDoseAberto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floatingbutton_tela_inicial);

        fabAdd = (FloatingActionButton) findViewById(R.id.fab1);
        fabDose = (FloatingActionButton) findViewById(R.id.fab2);
        fam = (FloatingActionMenu) findViewById(R.id.fab_menu);

        //handling menu status (open or close)
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {

                } else {
                    //Encerra a atividade caso o usuário clique fora do menu ou o menu se feche
                    //Verifica se o menu selecionar medicamento foi aberto, se sim não finalize a atividade
                    if(!verificaMenuDoseAberto)
                        finish();
                }
            }
        });

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });


        //handling each floating action button clicked
        fabDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new SelecionarMedicamentoDose();
                dialogFragment.show(getSupportFragmentManager(), "fragSelecionaMedicamentoDose");
                verificaMenuDoseAberto = true;
                fam.hideMenu(false);
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityFloatingbuttonTelaInicial.this, MedicamentoCadastro.class);
                startActivityForResult(i, CODIGO_RESULT_ACTIVITY);
            }
        });
    }



    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        fam.toggle(true);
        super.onStart();
    }

    @Override
    protected void onActivityResult(int codigo, int resultado, Intent intent) {
        super.onActivityResult(codigo, resultado, intent);
        if(codigo == CODIGO_RESULT_ACTIVITY){
            if(intent != null){
                if(resultado == 1){
                    Log.v("Terminou", " e salvou");
                    finish();
                }else{
                    Log.v("Terminou", " e não salvou");
                    finish();
                }
            }
        }
        //Encerra a activity caso o usuário cancele o cadastro do medicamento
        finish();
    }

    /**
     * Método reponsável por tratar o evento de clicar fora do DialogFragment
     * @param dialog
     */
    @Override
    public void onCancelFragSelecionaMedicamentoDose(DialogFragment dialog) {
        //Fecha o dialog que exibe os remédios
        dialog.dismiss();
        //Encerra a atividade
        finish();
    }
}
