package com.gmail.thales_silva_nascimento.alarmmed.model;

import com.gmail.thales_silva_nascimento.alarmmed.controller.LembreteCompraController;

/**
 * Created by Thales on 31/03/2018.
 */

public class ItemCompra {

    private Medicamento med;
    private LembreteCompra lembrete;

    public ItemCompra(Medicamento med, LembreteCompra lembrete){
        this.med = med;
        this.lembrete = lembrete;
    }

    public Medicamento getMedicamento(){
        return med;
    }

    public LembreteCompra getLembreteCompra(){
        return lembrete;
    }

}
