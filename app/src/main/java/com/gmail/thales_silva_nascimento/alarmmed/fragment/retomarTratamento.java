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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.model.Alarme;

/**
 * Created by thales on 15/01/18.
 */

public class retomarTratamento extends DialogFragment {

    private Button btnMenos;
    private Button btnMais;
    private Button btnCancel;
    private Button btnDef;
    private EditText edNumero;
    private int numeroDias;
    private Alarme alarme;
    private CheckBox checkBox;
    private LinearLayout llContador;

    public retomarTratamento(){

    }

    public interface retomarTratamentoListener{
        public void onclickListenerPositivoRetomarTratamento(DialogFragment dialog, Alarme alarme);
        public void onclickListenerNegativoRetomarTratamento(DialogFragment dialog);
    }

    retomarTratamentoListener interfaceListener;

    //Sobreescreve o método Fragment.onAttach() para instanciar o objeto interfaceListener
    //O código está sobreescrevendo duas vezes, pois para dispositvos abaixo da API 23 o parâmetro de entrado do método
    //onAttach é uma Activity já para API > 23 utiliza-se um Context.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Verifica se a API do android instalado no aparelho é maior que a API 22 (Lollipop). Se for return para sair e entrar no onAttach seguinte.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof retomarTratamento.retomarTratamentoListener) {
            interfaceListener = (retomarTratamento.retomarTratamentoListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement diasEspecificosListener");
        }
    }

    //onAttach para API > 22
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof retomarTratamento.retomarTratamentoListener) {
            interfaceListener = (retomarTratamento.retomarTratamentoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement diasEspecificosListener");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Recebe argumentos da activity
        alarme = getArguments().getParcelable("alarme");
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Cria a Janela de Dialogo
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        //LayoutInflater para inflar o layout customizável Layout.diasEspecíficos
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View customizada do alertDialog
        View alertView = inflater.inflate(R.layout.fragment_retomar_tratamento, null);

        btnMenos = (Button) alertView.findViewById(R.id.btnMenosDias);
        btnMais = (Button) alertView.findViewById(R.id.btnMaisDias);
        btnCancel = (Button) alertView.findViewById(R.id.btnRetomarCancel);
        btnDef = (Button) alertView.findViewById(R.id.btnRetomarDef);
        edNumero = (EditText) alertView.findViewById(R.id.editNumeroDias);
        checkBox = (CheckBox) alertView.findViewById(R.id.checkboxRetomar);
        llContador = (LinearLayout) alertView.findViewById(R.id.llContadorRetomar);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    llContador.setVisibility(View.GONE);
                }else{
                    llContador.setVisibility(View.VISIBLE);
                }
            }
        });

        //Recupera o período do remédio
        numeroDias = alarme.getPeriodo();
        //Adiciona o período no dialogo;
        edNumero.setText(String.valueOf(numeroDias));

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

        btnDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numDiasFinal = Integer.parseInt(edNumero.getText().toString());
                if(numDiasFinal < 1){
                    edNumero.requestFocus();
                    return;
                }
                //interfaceListener.onClickListenerPositivoNumeroDias(fragNumeroDias.this, numDiasFinal);
                interfaceListener.onclickListenerPositivoRetomarTratamento(retomarTratamento.this, alarme);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceListener.onclickListenerNegativoRetomarTratamento(retomarTratamento.this);
            }
        });


        alert.setView(alertView);
        return  alert.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        interfaceListener.onclickListenerNegativoRetomarTratamento(retomarTratamento.this);
        //super.onCancel(dialog);
    }
}
