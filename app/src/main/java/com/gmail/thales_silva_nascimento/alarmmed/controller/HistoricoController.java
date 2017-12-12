package com.gmail.thales_silva_nascimento.alarmmed.controller;


import android.content.Context;

import com.gmail.thales_silva_nascimento.alarmmed.dao.HistoricoDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarme;

public class HistoricoController {

    private HistoricoDAO historicoDAO;

    public HistoricoController(Context context){
        this.historicoDAO = HistoricoDAO.getInstance(context);
    }


    public void cadastrarHistoricoMedicamento(ItemAlarme itemAlarme){
        historicoDAO.cadastrarHistoricoMedicamento(itemAlarme);
    }

    public void deletarHistoricoMedicamento(long idMedicamento){
        historicoDAO.deletarHistoricoMedicamento(idMedicamento);
    }
}
