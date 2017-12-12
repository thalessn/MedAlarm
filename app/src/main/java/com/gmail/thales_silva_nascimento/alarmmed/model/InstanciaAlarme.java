package com.gmail.thales_silva_nascimento.alarmmed.model;

import com.gmail.thales_silva_nascimento.alarmmed.Utils;

import java.util.Calendar;


public class InstanciaAlarme {

    private long id;
    private Calendar data;
    private float qtd_tomar;

    private long id_horario;
    private long id_alarme;


    public InstanciaAlarme(Calendar calendar, float qtd_tomar, long id_horario, long id_alarme){
        this.data = calendar;
        this.qtd_tomar = qtd_tomar;
        this.id_horario = id_horario;
        this.id_alarme = id_alarme;
    }

    public InstanciaAlarme(long id, Calendar calendar, float qtd_tomar, long id_horario, long id_alarme){
        this.id = id;
        this.data = calendar;
        this.qtd_tomar = qtd_tomar;
        this.id_horario = id_horario;
        this.id_alarme = id_alarme;
    }

    public Calendar getData(){
        return data;
    }

    public String getDataString(){
        String data = Utils.CalendarToStringData(getData());
        return data;
    }

    public long getId_horario(){
        return id_horario;
    }

    public long getId_alarme(){
        return id_alarme;
    }
    public float getQTD_Tomar(){
        return qtd_tomar;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Data = " +Utils.CalendarToStringData(data)+ " ID_horario = " +String.valueOf(id_horario)+ " ID_alarme = " +String.valueOf(id_alarme);
    }

    //Retonar o hashCode da 'id' do
    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

}
