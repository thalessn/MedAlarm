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
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;

public class fragNumeroDias extends DialogFragment {

    private Button btnMenos;
    private Button btnMais;
    private Button btnCancel;
    private Button btnDef;
    private EditText edNumero;
    private int numeroDias = 5;


    public fragNumeroDias(){

    }

    //Interface Utilizada para enviar as informações para a activity host (No caso a activity medicamentoCadastro)
    public interface numeroDiasListerner{
        public void onClickListenerPositivoNumeroDias(DialogFragment dialog, int numeroDias);
        public void onClickListenerNegativoNumeroDias(DialogFragment dialog);
    }

    //Instância do objeto da interface para a entrega das informacoes a activity host
    numeroDiasListerner interfaceListener;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Verifica se a API do android instalado no aparelho é maior que a API 22 (Lollipop). Se for return para sair e entrar no onAttach seguinte.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof fragNumeroDias.numeroDiasListerner) {
            interfaceListener = (fragNumeroDias.numeroDiasListerner) activity;
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
            interfaceListener = (fragNumeroDias.numeroDiasListerner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement diasIntervalosListerner");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Cria a Janela de Diálogo
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        //LayoutInflater para inflar o layout customizável Layout.diasIntervalos
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View para o layout customizado
        View alertView = inflater.inflate(R.layout.fragment_numero_dias, null);

        btnMenos = (Button) alertView.findViewById(R.id.btnMenosDias);
        btnMais = (Button) alertView.findViewById(R.id.btnMaisDias);
        btnCancel = (Button) alertView.findViewById(R.id.btnDiasCancel);
        btnDef = (Button) alertView.findViewById(R.id.btnDiasDef);
        edNumero = (EditText) alertView.findViewById(R.id.editNumeroDias);
        edNumero.setText(String.valueOf(numeroDias));

        //edNumero.setText(String.valueOf(numeroDias));

        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numeroDias = Integer.parseInt(edNumero.getText().toString());
                if(numeroDias<= 1){
                    numeroDias = 1;
                }else{
                    numeroDias -= 1;
                }
                edNumero.setText(String.valueOf(numeroDias));
            }
        });

        btnMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numeroDias = Integer.parseInt(edNumero.getText().toString());
                numeroDias++;
                edNumero.setText(String.valueOf(numeroDias));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceListener.onClickListenerNegativoNumeroDias(fragNumeroDias.this);
            }
        });

        btnDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numDiasFinal = Integer.parseInt(edNumero.getText().toString());
                if(numDiasFinal < 1){
                    edNumero.requestFocus();
                    return;
                }
                interfaceListener.onClickListenerPositivoNumeroDias(fragNumeroDias.this, numDiasFinal);
            }
        });

        //Adiciona o layout personalizado ao AlertDialog
        alert.setView(alertView);

        //Retorna o Dialogo criado
        return alert.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        interfaceListener.onClickListenerNegativoNumeroDias(fragNumeroDias.this);
        //super.onCancel(dialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
