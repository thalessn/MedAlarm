package com.gmail.thales_silva_nascimento.alarmmed.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.dao.AlarmeDAO;
import com.gmail.thales_silva_nascimento.alarmmed.dao.BancoHelper;
import com.gmail.thales_silva_nascimento.alarmmed.dao.MedicamentoDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.Alarme;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;
import com.gmail.thales_silva_nascimento.alarmmed.model.MedicamentoAgendado;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Thales on 28/01/2018.
 */

public class MedicamentoAgendadoController {

    private Context context;
    private SQLiteDatabase database;
    private List<MedicamentoAgendado> medicamentoAgendados;

    public MedicamentoAgendadoController(Context context){
        this.context = context;
        medicamentoAgendados = new ArrayList<>();
    }


    public List<MedicamentoAgendado> buscarMedicamentoAgendados(String hInicial, String hFinal){
        //Cria uma conexão com o banco de dados
        BancoHelper banco = BancoHelper.getInstance(context);
        database = banco.getWritableDatabase();

        String sql = "select medicamento.*, alarme.*, alarmeInfo.horario " +
                     "from medicamento inner join alarme on medicamento._id = alarme._id " +
                     "inner join alarmeInfo on alarme._id = alarmeInfo._idAlarme " +
                     "where (alarme.status = 1) and (alarmeInfo.horario between (\""+ hInicial +"\") and (\"" +hFinal+ "\")) order by alarmeInfo.horario";

        //Consulta no banco de dados
        Cursor cursor = database.rawQuery(sql,null);

        if(cursor == null){
            Log.v("Cursor", "Nulo ");
            return null;
        }
        if(cursor.moveToFirst()){
            try{
                do{
                    //Medicamento
                    long id = cursor.getLong(0);
                    String nome = cursor.getString(cursor.getColumnIndex("nome"));
                    int dosagem = cursor.getInt(cursor.getColumnIndex("dosagem"));
                    int dosagemComprada = cursor.getInt(cursor.getColumnIndex("dosagemComprada"));
                    String tipoDosagem = cursor.getString(cursor.getColumnIndex("tipoDosagem"));
                    String foto = cursor.getString(cursor.getColumnIndex("foto"));
                    //Transforma para boolean
                    boolean usoContinuo = (cursor.getInt(cursor.getColumnIndex("usoContinuo")) == 1);
                    String obs = cursor.getString(cursor.getColumnIndex("observacao"));
                    int quantidade = cursor.getInt(cursor.getColumnIndex("qtd"));
                    Medicamento medicamento = new Medicamento(id, nome, dosagem,tipoDosagem, usoContinuo, obs, foto, quantidade,dosagemComprada);

                    //Alarme
                    long id2 = cursor.getLong(9);
                    Calendar dataInicio = Utils.DataStringToCalendar( cursor.getString(10) );
                    Calendar dataFim = Utils.DataStringToCalendar( cursor.getString(11));
                    int periodo = cursor.getInt(12);
                    int tipoRepeticao = cursor.getInt(13);
                    int intervaloRepeticao = cursor.getInt(14);
                    boolean status = (cursor.getInt(15) == 1 );
                    long idMedicamento = cursor.getLong(16);
                    String freqHorario = cursor.getString(17);
                    String freqDias = cursor.getString(18);
                    Alarme alarme = new Alarme(id2, dataInicio, dataFim, periodo, tipoRepeticao, intervaloRepeticao, status, idMedicamento, freqHorario, freqDias);

                    //String horario
                    String horario = cursor.getString(19);
                    //Adiciona espaço em branco no texto
                    horario = horario.substring(0,2) +" "+ horario.substring(2,3) +" "+ horario.substring(3,5);

                    //Medicamento Agendado
                    MedicamentoAgendado medicamentoAgendado = new MedicamentoAgendado(medicamento,alarme, horario);

                    //Adiciona o medicmaneto agendado ao List
                    medicamentoAgendados.add(medicamentoAgendado);

                }while(cursor.moveToNext());
            }finally {
                cursor.close();
            }
        }

        return medicamentoAgendados;
    }

}
