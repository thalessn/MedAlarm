package com.gmail.thales_silva_nascimento.alarmmed.controller;

import android.content.Context;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.dao.HorarioDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.Horario;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thales on 06/09/17.
 */

public class HorarioController {

    private Map<String, Long> horarios = new HashMap<>();
    private HorarioDAO horarioDAO;

    private static HorarioController instance = null;

    private HorarioController(Context context){
        horarioDAO = HorarioDAO.getInstance(context);
        horarios = horarioDAO.listarTodosHorarios();
        Log.v("HorarioConstrutor", String.valueOf(horarios.size()));
    }

    public static HorarioController getInstance(Context context){
        if(instance == null){
            return new HorarioController(context);
        }
        return instance;
    }

    public long buscarIdHorario(String horario){
        //Retira os espaços em branco
        String h = horario.replace(" ", "");

        long id = horarioDAO.buscarIdHorario(h);
        Log.v("Horario-IDFuncNova", String.valueOf(id));
        return id;
    }

    public Horario buscarHorarioPorId(long id){
        Horario novo = horarioDAO.buscarHorario(id);
        Log.v("HorarioPorId", novo.toString());
        return novo;
    }

    public Horario buscarHorario(String horario){
       //Retira os espaços em branco
       String h = horario.replace(" ", "");
       long id = horarioDAO.buscarIdHorario(h);

       //Adiciona espaço em branco no texto para ficar formatado
       h = h.substring(0,2) +" "+ h.substring(2,3) +" "+ h.substring(3,5);
       Log.v("HorarioFormatado", h);
       Horario horario1 = new Horario(id, horario);

       return horario1;
    }

}
