package com.gmail.thales_silva_nascimento.alarmmed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.thales_silva_nascimento.alarmmed.controller.InstanciaAlarmeController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoAgendadoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.MedicamentoAgendado;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Thales on 23/01/2018.
 */

public class fragInfoMedAlarme extends Fragment {
    private RecyclerView recyclerView;
    private int position;

    public fragInfoMedAlarme(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_med_alarme, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rvAlarmeDetalhe);

        //Calcula o dia da semana de acordo com a posição do item do view adapter.
        Calendar diaFragment = Calendar.getInstance();
        if(!(position == ((homeViewPagerAdapter.NUM_ITEMS - 1)/2))){
            //Adiciona dia de acordo com a posição do viewpager do fragHome;
            int add = (position - ((homeViewPagerAdapter.NUM_ITEMS -1)/2));
            Log.v("DataCalendar", String.valueOf(add));
            diaFragment.add(Calendar.DAY_OF_YEAR, add);
        }
        diaFragment.set(Calendar.HOUR_OF_DAY, 3);
        diaFragment.set(Calendar.MINUTE, 59);
        Log.v("fragInfoMedAlarme", "Dia: "+ Utils.CalendarToStringData(diaFragment)+ " - Hora: "+ Utils.CalendarToStringHora(diaFragment));
        MedicamentoAgendadoController medAgendado = new MedicamentoAgendadoController(getContext());
        List<MedicamentoAgendado> medicamentoAgendados = medAgendado.buscarMedicamentoAgendados("03:59", "11:59");

        InstanciaAlarmeController iac = InstanciaAlarmeController.getInstanciaAlarmeController(getContext());

        for(int i = 0 ; i <medicamentoAgendados.size(); i++){
            MedicamentoAgendado med = medicamentoAgendados.get(i);
            Calendar teste = iac.verificaInstancia(med.getAlarme(), med.getHorario(), diaFragment);
            if(teste == null){
                medicamentoAgendados.remove(i);
            }
        }

        for(MedicamentoAgendado result : medicamentoAgendados){
            Log.v("Resultado: ", "Medicamento agendado - "+ result.toString());
        }

        return view;
    }

}
