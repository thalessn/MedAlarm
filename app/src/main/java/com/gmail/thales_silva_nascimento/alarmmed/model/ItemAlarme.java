package com.gmail.thales_silva_nascimento.alarmmed.model;

/**
 * Created by thales on 09/09/17.
 */

public class ItemAlarme {

    Medicamento med;
    String dataProgramada;
    Horario horario;
    String dataAdministrado;
    String horaAdministrado;
    long idAlarme;
    String status;


    public ItemAlarme(Medicamento med, String dataProgramada, Horario horario, long idAlarme){
        this.med = med;
        this.dataProgramada = dataProgramada;
        this.horario = horario;
        this.idAlarme = idAlarme;
    }

    public Medicamento getMed() {
        return med;
    }

    public Horario getHorario() {
        return horario;
    }

    public String getDataProgramada() {
        return dataProgramada;
    }

    public String getDataAdministrado() {
        return dataAdministrado;
    }

    public String getHoraAdministrado() {
        return horaAdministrado;
    }

    public long getIdAlarme() {
        return idAlarme;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDataAdministrado(String dataAdministrado) {
        this.dataAdministrado = dataAdministrado;
    }

    public void setHoraAdministrado(String horaAdministrado) {
        this.horaAdministrado = horaAdministrado;
    }
}