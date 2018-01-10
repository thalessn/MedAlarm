package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gmail.thales_silva_nascimento.alarmmed.R;

/**
 * Created by thales on 09/01/18.
 */

public class fragQtdLembrete extends DialogFragment {

    private int qtdRemedio;
    private EditText qtdEditText;
    private Button btnOk;
    private Button btnCancel;

    public fragQtdLembrete(){

    }

    //Interface Utilizada para enviar as informações para a activity host (No caso a activity medicamentoCadastro)
    public interface lembreteListerner{
        public void onClickListenerLembretePositivo(DialogFragment dialog, int qtdRemedio);
        public void onClickListenerLembreteNegativo(DialogFragment dialog);
    }

    //Instância do objeto da interface para a entrega das informacoes a activity host
    lembreteListerner lembreteListerner;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Verifica se a API do android instalado no aparelho é maior que a API 22 (Lollipop). Se for return para sair e entrar no onAttach seguinte.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof fragNumeroDias.numeroDiasListerner) {
            lembreteListerner = (fragQtdLembrete.lembreteListerner) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement diasIntervalosListerner");
        }
    }

    //onAttach para API > 22
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof fragNumeroDias.numeroDiasListerner) {
            lembreteListerner = (fragQtdLembrete.lembreteListerner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement diasIntervalosListerner");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Recebe as informações da activity
        qtdRemedio = getArguments().getInt("qtdLembreteMed");
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Cria a Janela de Diálogo
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        //LayoutInflater para inflar o layout customizável Layout.diasIntervalos
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View para o layout customizado
        View alertView = inflater.inflate(R.layout.fragment_qtd_lembrete_remedio, null);

        qtdEditText = (EditText) alertView.findViewById(R.id.edQtdRemedio);
        qtdEditText.setText(String.valueOf(qtdRemedio));
        qtdEditText.setSelection(qtdEditText.getText().length());

        btnOk = (Button) alertView.findViewById(R.id.btnOkLembrete);
        btnCancel = (Button) alertView.findViewById(R.id.btnCancelarLembrete);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = qtdEditText.getText().toString();
                if(numero.equals("")){
                    qtdEditText.requestFocus();
                    return;
                }
                else{
                    int num = Integer.parseInt(qtdEditText.getText().toString());
                    if(num < 1){
                        qtdEditText.requestFocus();
                        return;
                    }
                    lembreteListerner.onClickListenerLembretePositivo(fragQtdLembrete.this, num);
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lembreteListerner.onClickListenerLembreteNegativo(fragQtdLembrete.this);
            }
        });

        //Adiciona o layout personalizado ao AlertDialog
        alert.setView(alertView);

        return alert.create();
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        lembreteListerner.onClickListenerLembreteNegativo(fragQtdLembrete.this);
        super.onCancel(dialog);
    }
}
