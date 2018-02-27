package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarmeHistorico;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Thales on 25/02/2018.
 */

public class detalheHistorico extends DialogFragment {

    private TextView tvDataProg;
    private TextView tvDataAdministrada;

    public detalheHistorico(){

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        //LayoutInflater para inflar o layout customizável Layout.diasEspecíficos
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View customizada do alertDialog
        View alertView = inflater.inflate(R.layout.dialog_item_historico_detalhe, null);

        tvDataProg = (TextView) alertView.findViewById(R.id.tvDataProgramada);
        tvDataAdministrada = (TextView) alertView.findViewById(R.id.tvDataAdministrado);

        Bundle itemMedicamento = getArguments();
        if(itemMedicamento != null){
            ItemAlarmeHistorico item = itemMedicamento.getParcelable("itemAlarmeHistorico");


        }
        dialog.setView(alertView);
        return dialog.create();
    }

    public String formataTexto(ItemAlarmeHistorico item){
        String texto = "Programado para "+item.getHorario().getHorario();

        SimpleDateFormat dia = new SimpleDateFormat("d");
        SimpleDateFormat mes = new SimpleDateFormat("MMM");
        Calendar calDataProg = Utils.DataStringToCalendar(item.getDataProgramada());



        return null;
    }
}
