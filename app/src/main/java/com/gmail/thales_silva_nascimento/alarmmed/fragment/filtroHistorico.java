package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.gmail.thales_silva_nascimento.alarmmed.R;


public class filtroHistorico extends DialogFragment {


    public filtroHistorico(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        //LayoutInflater para inflar o layout customizável Layout.diasEspecíficos
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View customizada do alertDialog
        View alertView = inflater.inflate(R.layout.dialog_filtro_pedido, null);




        alert.setView(alertView);
        return alert.create();

    }
}
