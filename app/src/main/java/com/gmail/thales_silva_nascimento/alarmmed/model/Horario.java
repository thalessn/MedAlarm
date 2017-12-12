package com.gmail.thales_silva_nascimento.alarmmed.model;

/**
 * Created by thales on 06/09/17.
 */

public class Horario {

    private long id;
    private String horario;

    public Horario(String horario){
        this.horario = horario;
    }

    public Horario(long id, String horario){
        this.id = id;
        this.horario = horario;
    }

    public String getHorario(){
        return horario;
    }

    public void setHorario(String horario){
        this.horario = horario;
    }

    public long getId(){
        return id;
    }

    @Override
    public String toString() {
        return horario;
    }

}
