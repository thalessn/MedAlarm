package com.gmail.thales_silva_nascimento.alarmmed.controller;


import android.content.Context;

import com.gmail.thales_silva_nascimento.alarmmed.model.HeaderHistoricoRow;
import com.gmail.thales_silva_nascimento.alarmmed.ListItemHistorico;
import com.gmail.thales_silva_nascimento.alarmmed.dao.HistoricoDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarmeHistorico;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class HistoricoController {

    private HistoricoDAO historicoDAO;

    public HistoricoController(Context context){
        this.historicoDAO = HistoricoDAO.getInstance(context);
    }


    public void cadastrarHistoricoMedicamento(ItemAlarmeHistorico itemAlarmeHistorico){
        historicoDAO.cadastrarHistoricoMedicamento(itemAlarmeHistorico);
    }

    public void deletarHistoricoMedicamento(long idMedicamento){
        historicoDAO.deletarHistoricoMedicamento(idMedicamento);
    }

    public List<ListItemHistorico> listarTodosItensHistorico(){
        //Cria um hash map para quardar o dia e os medicamentos relacionados.
        LinkedHashMap<String, List<ItemAlarmeHistorico>> map = new LinkedHashMap<>();


        //Busca no banco todos os historicos gravados ordenados por dataProg
        //DESC e medicamento nome.
        List<ItemAlarmeHistorico> itens = historicoDAO.listarTodosItemsHistoricos();
        //Popula o hashMap com as informações do banco
        if(itens!= null){
            for(ItemAlarmeHistorico iah : itens){
                //A data programada é a chave do hashmap para separar os remédio utilizando a data
                //como parâmetro.
                String dataProgKey = iah.getDataProgramada();

                if(map.containsKey(dataProgKey)){
                    // The key is already in the HashMap; add the pojo object
                    // against the existing key.
                    map.get(dataProgKey).add(iah);
                }else {
                    // The key is not there in the HashMap; create a new key-value pair
                    List<ItemAlarmeHistorico> list = new ArrayList<>();
                    list.add(iah);
                    map.put(dataProgKey, list);
                }
            }

            //Lista que contem as datas e os respectivos medicamentos a serem exibidos no
            //recyclerview misturados na lista.
            List<ListItemHistorico> listItemHistoricos = new ArrayList<>();
            //Percorre com a data encontrada e Adiciona os remédios na lista de itens
            for (String dataProgMap : map.keySet()) {
                //Objeto que representa a linha acima dos dados
                HeaderHistoricoRow row = new HeaderHistoricoRow(dataProgMap);
                listItemHistoricos.add(row);

                for (ItemAlarmeHistorico itemHistorico : map.get(dataProgMap)) {
                    //Adiciona na lista os medicamentos relacionado a data
                    listItemHistoricos.add(itemHistorico);
                }
            }

            return listItemHistoricos;

        }else{
            return null;
        }

    }

    public List<ListItemHistorico> listarHistoricoPeriodo(String datainicial, String datafinal, long idMedicamento){
        //Cria um hash map para quardar o dia e os medicamentos relacionados.
        LinkedHashMap<String, List<ItemAlarmeHistorico>> map = new LinkedHashMap<>();
        //Busca no banco todos os historicos gravados ordenados por dataProg
        //Ordem Decrescente
        String order = "DESC";
        List<ItemAlarmeHistorico> itens = historicoDAO.listarHistoricoPeriodo(datainicial, datafinal,idMedicamento, order);
        //Popula o hashMap com as informações do banco
        if(itens!= null){
            for(ItemAlarmeHistorico iah : itens){
                //A data programada é a chave do hashmap para separar os remédio utilizando a data
                //como parâmetro.
                String dataProgKey = iah.getDataProgramada();

                if(map.containsKey(dataProgKey)){
                    // The key is already in the HashMap; add the pojo object
                    // against the existing key.
                    map.get(dataProgKey).add(iah);
                }else {
                    // The key is not there in the HashMap; create a new key-value pair
                    List<ItemAlarmeHistorico> list = new ArrayList<>();
                    list.add(iah);
                    map.put(dataProgKey, list);
                }
            }

            //Lista que contem as datas e os respectivos medicamentos a serem exibidos no
            //recyclerview misturados na lista.
            List<ListItemHistorico> listItemHistoricos = new ArrayList<>();
            //Percorre com a data encontrada e Adiciona os remédios na lista de itens
            for (String dataProgMap : map.keySet()) {
                //Objeto que representa a linha acima dos dados
                HeaderHistoricoRow row = new HeaderHistoricoRow(dataProgMap);
                listItemHistoricos.add(row);

                for (ItemAlarmeHistorico itemHistorico : map.get(dataProgMap)) {
                    //Adiciona na lista os medicamentos relacionado a data
                    listItemHistoricos.add(itemHistorico);
                }
            }

            return listItemHistoricos;

        }else{
            return null;
        }
    }

    public List<ListItemHistorico> listarHistoricoPeriodo(String datainicial, String datafinal){
        //Cria um hash map para quardar o dia e os medicamentos relacionados.
        LinkedHashMap<String, List<ItemAlarmeHistorico>> map = new LinkedHashMap<>();
        //Busca no banco todos os historicos gravados ordenados por dataProg
        String order = "ASC";
        List<ItemAlarmeHistorico> itens = historicoDAO.listarHistoricoPeriodo(datainicial, datafinal,0, order);
        //Popula o hashMap com as informações do banco
        if(itens!= null){
            for(ItemAlarmeHistorico iah : itens){
                //A data programada é a chave do hashmap para separar os remédio utilizando a data
                //como parâmetro.
                String dataProgKey = iah.getDataProgramada();

                if(map.containsKey(dataProgKey)){
                    // The key is already in the HashMap; add the pojo object
                    // against the existing key.
                    map.get(dataProgKey).add(iah);
                }else {
                    // The key is not there in the HashMap; create a new key-value pair
                    List<ItemAlarmeHistorico> list = new ArrayList<>();
                    list.add(iah);
                    map.put(dataProgKey, list);
                }
            }

            //Lista que contem as datas e os respectivos medicamentos a serem exibidos no
            //recyclerview misturados na lista.
            List<ListItemHistorico> listItemHistoricos = new ArrayList<>();
            //Percorre com a data encontrada e Adiciona os remédios na lista de itens
            for (String dataProgMap : map.keySet()) {
                //Objeto que representa a linha acima dos dados
                HeaderHistoricoRow row = new HeaderHistoricoRow(dataProgMap);
                listItemHistoricos.add(row);

                for (ItemAlarmeHistorico itemHistorico : map.get(dataProgMap)) {
                    //Adiciona na lista os medicamentos relacionado a data
                    listItemHistoricos.add(itemHistorico);
                }
            }

            return listItemHistoricos;

        }else{
            return null;
        }

    }
}
