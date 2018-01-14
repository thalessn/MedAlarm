package com.gmail.thales_silva_nascimento.alarmmed.controller;

import android.content.Context;

import com.gmail.thales_silva_nascimento.alarmmed.dao.MedicamentoDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.io.File;
import java.util.List;

/**
 * Created by thales on 08/09/17.
 */

public class MedicamentoController {

    private MedicamentoDAO medicamentoDAO;
    public MedicamentoController(Context context){
        this.medicamentoDAO = MedicamentoDAO.getInstance(context);
    }


    public long cadastrarMedicamento(Medicamento medicamento){
        long id = medicamentoDAO.cadastrarMedicamento(medicamento);
        return id;
    }

    public void atualizarMedicamento(Medicamento medicamaento){
        medicamentoDAO.atualizarMedicamento(medicamaento);
    }

    public void excluirMedicamento(Medicamento medicamento, Context context){
        File foto = context.getFileStreamPath(medicamento.getFoto());
        if(foto.exists()){
            foto.delete();
        }
        medicamentoDAO.excluirMedicamento(medicamento.getId());

    }

    public List<Medicamento> listarTodosMedicamentos(){
        List<Medicamento> medicamentos = medicamentoDAO.listarTodosMedicamentos();
        if(medicamentos == null){
            return null;
        }

        return medicamentos;
    }

    public Medicamento buscarMedicamentoPorId(long idMedicamento){
        if(idMedicamento <= 0)
            return null;

        Medicamento novo = medicamentoDAO.buscarMedicamentoID(idMedicamento);
        if (novo == null)
            return null;

        return novo;
    }

    public List<Medicamento> medicamentosPorDataEHora(long idHorario, String data){
        if(idHorario <= 0)
            return null;

        List<Medicamento> medicamentos = medicamentoDAO.medicamentosPorHorarioEData(idHorario, data);
        if(medicamentos == null)
            return null;
        return medicamentos;
    }

    public void updateQtdMedicamento(long idMedicamento, int qtd){
        medicamentoDAO.updateQtdMedicamento(idMedicamento, qtd);
    }

}
