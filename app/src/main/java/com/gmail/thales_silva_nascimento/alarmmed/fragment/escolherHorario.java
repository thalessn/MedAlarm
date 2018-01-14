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
import android.widget.TimePicker;

import com.gmail.thales_silva_nascimento.alarmmed.R;

/**
 * Created by thales on 10/01/18.
 */

public class escolherHorario extends DialogFragment {

    private TimePicker timePicker;
    private Button btnOK;
    private Button btnCancelar;
    private String horario;

    public escolherHorario(){

    }

    public interface escolherHorarioListener{
        public void onClickListenerPositivoEscolherHorario(DialogFragment dialog, String horario);
        public void onClickListenerNegativoEscolherHorario(DialogFragment dialog);
    }

    escolherHorarioListener interfaceListener;

    //Sobreescreve o método Fragment.onAttach() para instanciar o objeto interfaceListener
    //O código está sobreescrevendo duas vezes, pois para dispositvos abaixo da API 23 o parâmetro de entrado do método
    //onAttach é uma Activity já para API > 23 utiliza-se um Context.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Verifica se a API do android instalado no aparelho é maior que a API 22 (Lollipop). Se for return para sair e entrar no onAttach seguinte.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof escolherHorario.escolherHorarioListener) {
            interfaceListener = (escolherHorario.escolherHorarioListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement diasEspecificosListener");
        }
    }

    //onAttach para API > 22
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof escolherHorario.escolherHorarioListener) {
            interfaceListener = (escolherHorario.escolherHorarioListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement diasEspecificosListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        horario = getArguments().getString("horario");
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
        View alertView = inflater.inflate(R.layout.fragment_escolher_horario, null);

        btnOK = (Button) alertView.findViewById(R.id.btnOkHorario);
        btnCancelar = (Button) alertView.findViewById(R.id.btnCancelarHorario);
        timePicker = (TimePicker) alertView.findViewById(R.id.pickerHorario);
        timePicker.setIs24HourView(true);

        String []hh = horario.split(":");
        String h = hh[0].trim();
        String m = hh[1].trim();

        timePicker.setCurrentHour(Integer.parseInt(h));
        timePicker.setCurrentMinute(Integer.parseInt(m));

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hora = timePicker.getCurrentHour() < 10? "0"+timePicker.getCurrentHour().toString() : timePicker.getCurrentHour().toString();
                String minuto = timePicker.getCurrentMinute() <10? "0"+timePicker.getCurrentMinute().toString() : timePicker.getCurrentMinute().toString();
                String horario = hora +" : "+ minuto;
               interfaceListener.onClickListenerPositivoEscolherHorario(escolherHorario.this, horario);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceListener.onClickListenerNegativoEscolherHorario(escolherHorario.this);
            }
        });
        alert.setView(alertView);
        return alert.create();
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        interfaceListener.onClickListenerNegativoEscolherHorario(escolherHorario.this);
        super.onCancel(dialog);
    }
}
