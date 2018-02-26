package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.gmail.thales_silva_nascimento.alarmmed.R;

/**
 * Created by Thales on 25/02/2018.
 */

public class detalheHistorico extends DialogFragment {

    public detalheHistorico(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        //LayoutInflater para inflar o layout customizável Layout.diasEspecíficos
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View customizada do alertDialog
        View alertView = inflater.inflate(R.layout.dialog_item_historico_detalhe, null);

        dialog.setView(alertView);
        return dialog.create();

    }
}
