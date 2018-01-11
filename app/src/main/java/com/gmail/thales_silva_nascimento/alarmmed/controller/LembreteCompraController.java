package com.gmail.thales_silva_nascimento.alarmmed.controller;

import android.content.Context;

import com.gmail.thales_silva_nascimento.alarmmed.dao.LembreteCompraDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.LembreteCompra;

/**
 * Created by thales on 10/01/18.
 */

public class LembreteCompraController {

    private LembreteCompraDAO lembreteCompraDAO;

    public LembreteCompraController(Context context){
        this.lembreteCompraDAO = LembreteCompraDAO.getInstance(context);
    }

    public void cadastrarLembreteCompra(LembreteCompra lembreteCompra){
        lembreteCompraDAO.cadastrarLembreteCompra(lembreteCompra);
    }

    public void excluirLembreteCompraPorIdMed(long idMedicamento){
        lembreteCompraDAO.deletarLembreteCompraPorIdMed(idMedicamento);
    }
}
