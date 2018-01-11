package com.gmail.thales_silva_nascimento.alarmmed.model;

/**
 * Created by thales on 10/01/18.
 */

public class LembreteCompra {
    private long id;
    private long idMedicamento;
    private int qtd_alerta;
    private String horarioAlerta;

    public LembreteCompra(long idMedicamento, int qtd_alerta, String horarioAlerta){
        this.idMedicamento = idMedicamento;
        this.qtd_alerta = qtd_alerta;
        this.horarioAlerta = horarioAlerta;
    }

    public long getId() {
        return id;
    }

    public long getIdMedicamento() {
        return idMedicamento;
    }

    public int getQtd_alerta() {
        return qtd_alerta;
    }

    public String getHorarioAlerta() {
        return horarioAlerta;
    }
}
