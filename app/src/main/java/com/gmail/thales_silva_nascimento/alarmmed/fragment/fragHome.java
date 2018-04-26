package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.gmail.thales_silva_nascimento.alarmmed.adapter.GridViewAdapter;
import com.gmail.thales_silva_nascimento.alarmmed.activity.InfoMedAlarmes;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemGridView;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.adapter.homeViewPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class fragHome extends Fragment {
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private ArrayList<ItemGridView> itemGridList;
    private int posicao;

    public fragHome(){

    }

    public static fragHome newInstance(int posicao){
        fragHome fragHome = new fragHome();
        Bundle args = new Bundle();
        args.putInt("posicao", posicao);
        fragHome.setArguments(args);
        return fragHome;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.posicao = getArguments().getInt("posicao");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        //Gridview que conterá as imagens dos tempos
        gridView = (GridView) view.findViewById(R.id.gridView);

        //Arraylist contedo os objetos com a Imagem e seu respectivo texto.
        itemGridList = new ArrayList<>();
        itemGridList.add(new ItemGridView(R.drawable.ic_sunset, "Manhã"));
        itemGridList.add(new ItemGridView(R.drawable.ic_sun, "Tarde"));
        itemGridList.add(new ItemGridView(R.drawable.ic_sundown, "Noite"));
        itemGridList.add(new ItemGridView(R.drawable.ic_moon, "Madrugada"));

        //Adapter que gerencia os ícones
        gridViewAdapter = new GridViewAdapter(getContext(), itemGridList);
        //Adiciona o Adapter ao GridView
        gridView.setAdapter(gridViewAdapter);

        //Onclick listner no grid view
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int ItemGridPosition, long l) {
                Intent i = new Intent(getContext(), InfoMedAlarmes.class);
                i.putExtra("posicao", posicao);
                i.putExtra("ItemGridPosition", ItemGridPosition);
                startActivity(i);
            }
        });

        //Adiciona texto no TextView da Data
        TextView tvData = (TextView) view.findViewById(R.id.tvDataFragHome);
        SimpleDateFormat dia = new SimpleDateFormat("d");
        SimpleDateFormat mes = new SimpleDateFormat("MMM");
        Calendar cal = Calendar.getInstance();
        if(posicao == ((homeViewPagerAdapter.NUM_ITEMS - 1)/2)){
            String texto = "Hoje, "+ dia.format(cal.getTime()) +" de "+ mes.format(cal.getTime());
            tvData.setText(texto);
        }else{
            //Adiciona dia de acordo com a posição;
            int add = (posicao - ((homeViewPagerAdapter.NUM_ITEMS -1)/2));
            cal.add(Calendar.DAY_OF_YEAR, add);
            SimpleDateFormat diaSemana = new SimpleDateFormat("EEEE");
            String texto = diaSemana.format(cal.getTime()) +", "+ dia.format(cal.getTime()) +" de "+ mes.format(cal.getTime()) ;
            tvData.setText(texto);
        }
        return view;
    }
}
