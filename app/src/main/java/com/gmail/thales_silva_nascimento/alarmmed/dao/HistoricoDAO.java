package com.gmail.thales_silva_nascimento.alarmmed.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarme;

/**
 * Created by thales on 09/09/17.
 */

public class HistoricoDAO {

    public static String NOME_TABELA = "historico";
    public static String COLUNA_ID = "_id";
    public static String COLUNA_ID_MEDICAMENTO = "_idMedicamento";
    public static String COLUNA_DATA_PROG = "dataProg";
    public static String COLUNA_ID_HORARIO = "_idHorario";
    public static String COLUNA_DATA_ADMINISTRADO = "dataAdministrado";
    public static String COLUNA_HORA_ADMINISTRADO = "horaAdministrado";
    public static String COLUNA_STATUS = "status";

     /*
        CREATE TABLE historico (_id INTEGER PRIMARY KEY AUTOINCREMENT,
               " _idMedicamento INTEGER,"+
               " dataProg TEXT,"+
               " _idHorario INTEGER,"+
               " dataTomado TEXT,"+
               " horaTomado TEXT,"+
               " status TEXT,"+
               " FOREIGN KEY (_idMedicamento) REFERENCES medicamento (_id))+
               " FOREIGN KEY (_idHorario) REFERENCES horario (_id)
     */

    public static String SCRIPT_CRIACAO = "CREATE TABLE " +NOME_TABELA+ " (" +COLUNA_ID+
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +COLUNA_ID_MEDICAMENTO+ " INTEGER, "+
            COLUNA_DATA_PROG+ " TEXT, " +COLUNA_ID_HORARIO+ " INTEGER, " +COLUNA_DATA_ADMINISTRADO+
            " TEXT, " +COLUNA_HORA_ADMINISTRADO+ " TEXT, " +COLUNA_STATUS+ " TEXT, FOREIGN KEY "+
            "(" +COLUNA_ID_MEDICAMENTO+ ") REFERENCES " +MedicamentoDAO.NOME_TABELA+ " (" +
            MedicamentoDAO.COLUNA_ID+ "), FOREIGN KEY (" +COLUNA_ID_HORARIO+ ") REFERENCES "+
            HorarioDAO.NOME_TABELA+ " (" +HorarioDAO.COLUNA_ID+ "))";

    private SQLiteDatabase database = null;
    private static HistoricoDAO instance = null;

    private HistoricoDAO(Context context){
        BancoHelper banco = BancoHelper.getInstance(context);
        database = banco.getWritableDatabase();
    }

    public static HistoricoDAO getInstance(Context context){
        if(instance == null)
            instance = new HistoricoDAO(context);
        return instance;
    }


    private ContentValues criarContentValues(ItemAlarme itemAlarme){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUNA_ID_MEDICAMENTO, itemAlarme.getMed().getId());
        contentValues.put(COLUNA_DATA_PROG, itemAlarme.getDataProgramada());
        contentValues.put(COLUNA_ID_HORARIO,itemAlarme.getHorario().getId());
        contentValues.put(COLUNA_DATA_ADMINISTRADO, itemAlarme.getDataAdministrado());
        contentValues.put(COLUNA_HORA_ADMINISTRADO, itemAlarme.getHoraAdministrado());
        contentValues.put(COLUNA_STATUS, itemAlarme.getStatus());

        return contentValues;
    }

    public void cadastrarHistoricoMedicamento(ItemAlarme itemAlarme){
        ContentValues contentValues = criarContentValues(itemAlarme);
        database.insert(NOME_TABELA, null, contentValues);
    }

    public void deletarHistoricoMedicamento(long idMedicamento){
        String [] IdString = {String.valueOf(idMedicamento)};
        database.delete(NOME_TABELA, COLUNA_ID_MEDICAMENTO+ " = ?", IdString);
    }



}
