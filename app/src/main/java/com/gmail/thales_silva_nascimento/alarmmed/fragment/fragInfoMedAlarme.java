package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.controller.InstanciaAlarmeController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoAgendadoController;
import com.gmail.thales_silva_nascimento.alarmmed.adapter.homeViewPagerAdapter;
import com.gmail.thales_silva_nascimento.alarmmed.adapter.medicamentosAgendadosAdapter;
import com.gmail.thales_silva_nascimento.alarmmed.model.MedicamentoAgendado;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Thales on 23/01/2018.
 */

public class fragInfoMedAlarme extends Fragment {
    private RecyclerView recyclerView;
    private int position;
    private int itemGridPosition;
    private String horaInicial, horaFinal;
    private TextView texto;

    public fragInfoMedAlarme(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");
        this.itemGridPosition = getArguments().getInt("ItemGridPosition");

        //configura a hora da pesquisa no banco de dados utilizados na controladora medicamentos agendados
        setupHoraInicialFinal(itemGridPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_med_alarme, container, false);
        //Textview - Texto informativo
        texto = (TextView) view.findViewById(R.id.textoInformativo);


        //Calcula o dia da semana de acordo com a posição do item do view adapter.
        Calendar diaFragment = Calendar.getInstance();
        if(!(position == ((homeViewPagerAdapter.NUM_ITEMS - 1)/2))){
            //Adiciona dia de acordo com a posição do viewpager do fragHome;
            int add = (position - ((homeViewPagerAdapter.NUM_ITEMS -1)/2));
            Log.v("DataCalendar", String.valueOf(add));
            diaFragment.add(Calendar.DAY_OF_YEAR, add);
            Log.v("DataCalendar", "Data e horario: "+ Utils.CalendarToStringData(diaFragment)+" - "+Utils.CalendarToStringHora(diaFragment));
        }

        diaFragment.set(Calendar.HOUR_OF_DAY, 3);
        diaFragment.set(Calendar.MINUTE, 59);
        Log.v("fragInfoMedAlarme", "Dia: "+ Utils.CalendarToStringData(diaFragment)+ " - Hora: "+ Utils.CalendarToStringHora(diaFragment));

        MedicamentoAgendadoController medAgendado = new MedicamentoAgendadoController(getContext());
        List<MedicamentoAgendado> medicamentoAgendados = medAgendado.buscarMedicamentoAgendados(horaInicial, horaFinal);

        //InstanciaController para verificar a instancia
        InstanciaAlarmeController iac = InstanciaAlarmeController.getInstanciaAlarmeController(getContext());
        for(int i = 0 ; i <medicamentoAgendados.size(); i++){
            MedicamentoAgendado med = medicamentoAgendados.get(i);
            //Verifica se a instancia para esse dia deve mostrar
            Calendar teste = iac.verificaInstancia(med.getAlarme(), med.getHorario(), diaFragment);
            if(teste == null){
                medicamentoAgendados.remove(i);
            }
        }

        //Verifica se existe medicamentos
        if(medicamentoAgendados.size() > 0){
            texto.setVisibility(View.GONE);
        }

        //Recy7clerView
        recyclerView = (RecyclerView) view.findViewById(R.id.rvAlarmeDetalhe);
        LinearLayoutManager ll = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(ll);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        medicamentosAgendadosAdapter adapter = new medicamentosAgendadosAdapter(getActivity(), medicamentoAgendados);

        //Add o adapter
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void setupHoraInicialFinal(int itemGridPosition){
        switch (itemGridPosition){
            case 0:
                horaInicial = "04:00";
                horaFinal = "11:59";
                break;
            case 1:
                horaInicial = "12:00";
                horaFinal = "17:59";
                break;
            case 2:
                horaInicial = "18:00";
                horaFinal = "23:59";
                break;
            case 3:
                horaInicial = "00:00";
                horaFinal = "03:59";
                break;
        }
    }

}
