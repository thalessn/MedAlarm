package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarmeHistorico;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class detalheHistorico extends DialogFragment {

    private TextView tvDataProg;
    private TextView tvDataAdministrada;
    private TextView nomeMed;
    private ImageView img;

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
        nomeMed = (TextView) alertView.findViewById(R.id.tvnomeMedHistorico);
        img = (ImageView) alertView.findViewById(R.id.imgHistoricoItem);

        Bundle itemMedicamento = getArguments();
        if(itemMedicamento != null){
            ItemAlarmeHistorico item = itemMedicamento.getParcelable("itemAlarmeHistorico");
            //Adicona
            insereDados(item);
        }
        dialog.setView(alertView);
        return dialog.create();
    }

    public void insereDados(ItemAlarmeHistorico item){
        //Texto do primeiro parágrafo
        String texto = "Programado para "+item.getHorario().getHorario();

        SimpleDateFormat dia = new SimpleDateFormat("d");
        SimpleDateFormat mes = new SimpleDateFormat("MMM");
        SimpleDateFormat ano = new SimpleDateFormat("yy");
        Calendar calDataProg = Utils.DataStringToCalendar(item.getDataProgramada());
        texto+=", "+dia.format(calDataProg.getTime())+" "+mes.format(calDataProg.getTime())
                +" "+ano.format(calDataProg.getTime());
        tvDataProg.setText(texto);
        //Texto do segundo parágrafo
        Calendar calAd = Utils.DataStringToCalendar(item.getDataAdministrado());
        String texto2 = "Tomado às "+item.getHoraAdministrado()+", "+dia.format(calAd.getTime())
                +" "+mes.format(calAd.getTime())+" "+ano.format(calAd.getTime());
        tvDataAdministrada.setText(texto2);

        //Adiciona o nome do medicamento
        nomeMed.setText(item.getMed().getNome());

        //Adiciona foto no medicamento
        //Arquivo que contém o caminho da imagem no armazenamento interno
        File path = getContext().getFileStreamPath(item.getMed().getFoto());
        //Verifica se o caminho existe. Se existir carrega a imagem, se não carregue a imagem padrão que neste caso é o remédio.
        if(path.exists()){
            //existe
            Glide.with(getContext()).load(path).into(img);
        }else{
            //não existe
            Glide.with(getContext()).load(R.drawable.remedio1).into(img);
        }
    }
}
