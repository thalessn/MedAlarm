package com.gmail.thales_silva_nascimento.alarmmed.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.dao.AlarmeDAO;
import com.gmail.thales_silva_nascimento.alarmmed.dao.AlarmeInfoDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.Alarme;
import com.gmail.thales_silva_nascimento.alarmmed.model.AlarmeInfo;

import java.util.List;

/**
 * Created by thales on 06/09/17.
 */

public class AlarmeController {

    private AlarmeDAO alarmeDAO;
    private InstanciaAlarmeController instanciaAlarmeController;
    private Context context;
    private AlarmeInfoDAO alarmeInfoDAO;

    public AlarmeController(Context context) {
        this.context = context;
        this.alarmeDAO = AlarmeDAO.getInstance(context);
        this.instanciaAlarmeController = InstanciaAlarmeController.getInstanciaAlarmeController(context);
        this.alarmeInfoDAO = AlarmeInfoDAO.getInstance(context);
    }




    public void registrarAlarmeAsync(final Alarme alarme) {
        final AsyncTask<Void, Void, Void> registrarTask =
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        /*Refazer considerando não conseguir salvar no banco.*/
                        /*SALVAR OS ALARME INFOS NO BANCO DE DADOS*/
                        if (alarme != null)
                        {
                            Log.v("registrarAlarme", "iniciou");

                            if(alarme.getId() == Alarme.ID_INVALIDA){
                                //Salva o alarme no banco e retorna sua respectiva id
                                long idAlarme = cadastrarAlarme(alarme);
                                Log.v("Id_Alarme", String.valueOf(idAlarme));
                                //Adiciona a id ao objeto alarme
                                alarme.setId(idAlarme);
                                Log.v("IF - Id_Alarme", "Entrou");

                                for (AlarmeInfo ai : alarme.getAlarmeInfo()) {
                                    //Adiciona a id do alarme
                                    ai.setIdAlarme(alarme.getId());
                                    //Salva alarmeInfo no banco
                                    alarmeInfoDAO.cadastrarAlarmeInfo(ai);
                                    //Registra a instancia no AlarmManager do Sistema
                                    instanciaAlarmeController.resgistraInstanciaAlarme(context, alarme, ai);
                                }

                            }else{
                                for (AlarmeInfo ai : alarme.getAlarmeInfo()) {
                                    Log.v("RegistraAlarmeInfo", "Horario: "+ ai.getHorario());
                                    Log.v("RegistraAlarmeInfo", "Id Alarme: "+ String.valueOf(alarme.getId()));
                                    //Adiciona a id do alarme
                                    ai.setIdAlarme(alarme.getId());
                                    //Salva alarmeInfo no banco
                                    alarmeInfoDAO.cadastrarAlarmeInfo(ai);
                                    //Registra a instancia no AlarmManager do Sistema
                                    instanciaAlarmeController.resgistraInstanciaAlarme(context, alarme, ai);
                                }
                            }
                        }
                        Log.v("registrarAlarme", "terminou - doBackGround");
                        return null;
                    }
                };

        registrarTask.execute();
    }

    public void atualizarAlarme(Alarme alarme) {
        //Exclui todas as AlarmesInfos do Banco
        alarmeInfoDAO.deletarAlarmeInfosDoAlarme(alarme.getId());
        //Exclui todas as instanciaAlarme do banco
        instanciaAlarmeController.deletarTodasInstanciaDoAlarme(alarme.getId());
        alarmeDAO.atualizarAlarme(alarme);
        registrarAlarmeAsync(alarme);

    }

    public void desativarAlarme(Alarme alarme) {
        //Verifica o status do alarme, se tiver ativo desative
        if (alarme.isStatus()) {
            alarme.setStatus(false);
        }
        //Deletar todas as instâncias relacionadas a este alarme
        instanciaAlarmeController.deletarTodasInstanciaDoAlarme(alarme.getId());
        //Atualizo o alarme no banco
        alarmeDAO.atualizarAlarme(alarme);
    }

    public void ativarAlarme(Alarme alarme){
        //Verifica o status do alarme
        if(!alarme.isStatus()){
            alarme.setStatus(true);
        }
        //Atualiza o alarme
        alarmeDAO.atualizarAlarme(alarme);
        //Registra o alarme no banco
        registrarAlarmeAsync(alarme);

    }

    public long cadastrarAlarme(Alarme alarme) {
        //Salvo o alarme no banco
        long id = alarmeDAO.cadastrarAlarme(alarme);
        return id;
    }

    public void deletarAlarme(Alarme alarme) {
        //Deleta todas as instancias do alarme
        instanciaAlarmeController.deletarTodasInstanciaDoAlarme(alarme.getId());

        //Deleta todas as informações de AlarmeInfo do alarme
        alarmeInfoDAO.deletarAlarmeInfosDoAlarme(alarme.getId());

        //Deleta todas as informações deste alarme
        alarmeDAO.excluirAlarme(alarme.getId());
    }

    public void deletarAlarme(long idAlarme) {
        //Deleta todas as instancias do alarme
        instanciaAlarmeController.deletarTodasInstanciaDoAlarme(idAlarme);

        //Deleta todas as informações de AlarmeInfo do alarme
        alarmeInfoDAO.deletarAlarmeInfosDoAlarme(idAlarme);

        //Deleta todas as informações deste alarme
        alarmeDAO.excluirAlarme(idAlarme);
    }


    public List<Alarme> listarTodosAlarmes(){
        List<Alarme> alarmes = alarmeDAO.listarTodosAlarmes();
        //Para cada alarme adiciona o seu alarmeInfo
        for(int i = 0; i<alarmes.size(); i++){
            List<AlarmeInfo> infos = alarmeInfoDAO.listarAlarmeInfoPorAlarmeId(alarmes.get(i).getId());
            Alarme atualizado = alarmes.get(i);
            atualizado.setAlarmeInfo(infos);
            alarmes.set(i, atualizado);
        }
        return alarmes;
    }

    public long buscarIdAlarmePorMedID(long idMedicamento){
        long id = alarmeDAO.buscarIdAlarmePorMedID(idMedicamento);
        return id;
    }

    public Alarme buscarAlarmePorIdMed(long idMedicamento){
        Alarme alarme = alarmeDAO.buscarAlarmeIdMed(idMedicamento);
        List<AlarmeInfo> infos = alarmeInfoDAO.listarAlarmeInfoPorAlarmeId(alarme.getId());
        alarme.setAlarmeInfo(infos);
        return alarme;
    }

    public Alarme buscarAlarmePorId(long id){
        Alarme alarme = alarmeDAO.buscarAlarmeId(id);
        List<AlarmeInfo> infos = alarmeInfoDAO.listarAlarmeInfoPorAlarmeId(alarme.getId());
        alarme.setAlarmeInfo(infos);
        return alarme;
    }

}
