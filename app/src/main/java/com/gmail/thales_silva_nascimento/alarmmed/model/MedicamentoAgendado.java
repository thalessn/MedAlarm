package com.gmail.thales_silva_nascimento.alarmmed.model;

/**
 * Created by Thales on 28/01/2018.
 */

public class MedicamentoAgendado {

    private Medicamento medicamento;
    private Alarme alarme;
    private String horario;


    public MedicamentoAgendado(Medicamento medicamento, Alarme alarme, String horario){
        this.medicamento = medicamento;
        this.alarme = alarme;
        this.horario = horario;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public Alarme getAlarme() {
        return alarme;
    }

    public String getHorario() {
        return horario;
    }

    @Override
    public String toString() {

        return "Medicamento: "+medicamento.getNome()+ " - Horario: "+horario;
    }
}
