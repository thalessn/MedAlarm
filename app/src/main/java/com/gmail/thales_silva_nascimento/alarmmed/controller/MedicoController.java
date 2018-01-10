package com.gmail.thales_silva_nascimento.alarmmed.controller;
import android.content.Context;
import com.gmail.thales_silva_nascimento.alarmmed.dao.MedicoDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medico;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thales on 10/02/2017.
 */

public class MedicoController {
    private List<Medico> medicos;
    private MedicoDAO medicoDAO;

    private static MedicoController instance = null;

    private MedicoController(Context context){
        this.medicos = new ArrayList<>();
        this.medicoDAO = MedicoDAO.getInstance(context);
    }

    public static MedicoController getInstance(Context context){
        if(instance == null ){
            instance = new MedicoController(context);
        }
        return instance;
    }

    public void salvarMedico(Medico medico){
        medicoDAO.salvar(medico);
    }

    public void editarMedico(Medico medico){
        medicoDAO.editar(medico);
    }

    public void excluirMedico(Medico medico){
        medicoDAO.excluir(medico);
    }

    public List<Medico> listarTodosMedicos(){
        medicos = medicoDAO.listarTodos();
        return medicos;
    }

    public Medico buscarMedicoPorId(long id){
        Medico medico = medicoDAO.buscarMedicoPorId(id);
        return medico;
    }

}
