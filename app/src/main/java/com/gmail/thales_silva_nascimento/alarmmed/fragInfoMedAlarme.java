package com.gmail.thales_silva_nascimento.alarmmed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Thales on 23/01/2018.
 */

public class fragInfoMedAlarme extends Fragment {
    private RecyclerView recyclerView;

    public fragInfoMedAlarme(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_med_alarme, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rvAlarmeDetalhe);

        return view;
    }

}
