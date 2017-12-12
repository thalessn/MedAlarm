package com.gmail.thales_silva_nascimento.alarmmed.controller;

import android.content.Context;

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
    }

    public static HorarioController getInstance(Context context){
        if(instance == null){
            return new HorarioController(context);
        }
        return instance;
    }


    public long buscarIdHorario(String horario){
        Long id = horarios.get(horario);

        if(!(id == null)){
            return id;
        }
        //Cadastrar o horário no banco pois não exite e retorna a id
        id = horarioDAO.cadastrarHorario(horario);
        //Adiciona o novo horario no hashmap
        horarios.put(horario, id);

        return id;
    }

    public Horario buscarHorario(long id){
        Horario novo = horarioDAO.buscarHorario(id);
        return novo;
    }

}
