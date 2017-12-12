package com.gmail.thales_silva_nascimento.alarmmed.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.model.InstanciaAlarme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by thales on 06/09/17.
 */

public class InstanciaAlarmeDAO {

    public static String NOME_TABELA = "instanciaAlarme";
    public static String COLUNA_ID = "_id";
    public static String COLUNA_DATA = "data";
    public static String COLUNA_QTD_TOMAR = "qtd_tomar";
    public static String COLUNA_ID_ALARME = "_idAlarme";
    public static String COLUNA_ID_HORARIO = "_idHorario";


    /* db.execSQL("CREATE TABLE instanciaAlarme (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
             " dia INTEGER NOT NULL," +
             " mes INTEGER NOT NULL," +
             " ano INTEGER NOT NULL," +
             " qtd_tomar REAL NOT NULL," +
             " _idAlarme INTEGER," +
             " _idHorario INTEGER," +
             " FOREIGN KEY (_idAlarme) REFERENCES alarme (_id)," +
             " FOREIGN KEY (_idHorario) REFERENCES horario (_id))");*/


    public static String SCRIPT_CRIACAO_TABELA = "CREATE TABLE " +NOME_TABELA+ " (" +COLUNA_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COLUNA_DATA+ " TEXT NOT NULL, " +COLUNA_QTD_TOMAR+ " REAL NOT NULL, " +COLUNA_ID_ALARME+ " INTEGER, " +COLUNA_ID_HORARIO+ " INTEGER, FOREIGN KEY (" +COLUNA_ID_ALARME+ ")" +
            " REFERENCES " +AlarmeDAO.NOME_TABELA+ " (" +AlarmeDAO.COLUNA_ID+ "), FOREIGN KEY (" +COLUNA_ID_HORARIO+ ") REFERENCES "
            +HorarioDAO.NOME_TABELA+ " (" +HorarioDAO.COLUNA_ID+ "))";

    /**
     * Atalhos para evitar de ficar chamando a função cursor.getColumnIndex()
     * Manter sincronizado com o banco
     */

    private static final int ID_INDEX = 0;
    private static final int DATA_INDEX = 1;
    private static final int QTD_TOMAR_INDEX = 2;
    private static final int ID_ALARME_INDEX = 3;
    private static final int ID_HORARIO_INDEX = 4;


    private static InstanciaAlarmeDAO instance = null;
    private SQLiteDatabase database = null;
    private InstanciaAlarmeDAO (Context context){
        BancoHelper banco = BancoHelper.getInstance(context);
        database = banco.getWritableDatabase();
    }

    public static InstanciaAlarmeDAO getInstance(Context context){
        if(instance == null)
            instance = new InstanciaAlarmeDAO(context);
        return instance;
    }

    public ContentValues criarContentValues(InstanciaAlarme instanciaAlarme){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUNA_DATA, instanciaAlarme.getDataString());
        contentValues.put(COLUNA_ID_ALARME, instanciaAlarme.getId_alarme());
        contentValues.put(COLUNA_ID_HORARIO, instanciaAlarme.getId_horario());
        contentValues.put(COLUNA_QTD_TOMAR, instanciaAlarme.getQTD_Tomar());
        return contentValues;
    }

    public long cadastrarInstanciaAlarme(InstanciaAlarme instanciaAlarme){
        ContentValues contentValues = criarContentValues(instanciaAlarme);
        long id = database.insert(NOME_TABELA, null, contentValues);

        return id;
    }

    public void atualizarInstanciaAlarme(InstanciaAlarme instanciaAlarme){
        ContentValues contentValues = criarContentValues(instanciaAlarme);
        String [] whereValues = { String.valueOf( instanciaAlarme.getId() ) };
        database.update(NOME_TABELA, contentValues, COLUNA_ID+ " = ?", whereValues);
    }

    public void deletarInstanciaAlarme(long id){
        String [] whereValues = { String.valueOf(id) };
        database.delete(NOME_TABELA, COLUNA_ID +" = ?", whereValues);
    }

    public List<InstanciaAlarme> listarTodasInstancias(){
        List<InstanciaAlarme> ai = new ArrayList<>();
        String query = "SELECT * FROM " +NOME_TABELA;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor == null){
            return null;
        }

        if(cursor.moveToFirst()){
            try{
                do{
                    long id = cursor.getLong(ID_INDEX);
                    Calendar data = Utils.DataStringToCalendar( cursor.getString(DATA_INDEX) );
                    float qtd_tomar = cursor.getFloat(QTD_TOMAR_INDEX);
                    long id_alarme = cursor.getLong(ID_ALARME_INDEX);
                    long id_horario = cursor.getLong(ID_HORARIO_INDEX);

                    InstanciaAlarme instanciaAlarme = new InstanciaAlarme(id, data, qtd_tomar, id_horario, id_alarme);
                    ai.add(instanciaAlarme);
                }while (cursor.moveToNext());
            }finally {
                cursor.close();
            }
        }
        return ai;
    }

    public InstanciaAlarme buscarInstanciaID(long id){
        String [] whereValues = {String.valueOf(id)};
        String query = "SELECT * FROM " +NOME_TABELA+ " WHERE " +COLUNA_ID+ " = ?";
        Cursor cursor = database.rawQuery(query, whereValues);

        if(cursor == null){
            return null;
        }

        if(cursor.moveToFirst()){
            try {
                Calendar data = Utils.DataStringToCalendar( cursor.getString(DATA_INDEX) );
                float qtd_tomar = cursor.getFloat(QTD_TOMAR_INDEX);
                long id_alarme = cursor.getLong(ID_ALARME_INDEX);
                long id_horario = cursor.getLong(ID_HORARIO_INDEX);

                InstanciaAlarme instanciaAlarme = new InstanciaAlarme(id, data, qtd_tomar, id_horario, id_alarme);

                return instanciaAlarme;
            }finally {
                cursor.close();
            }
        }
        return null;
    }

    public void deletarTodasInstancias(){
        //Deleta todas as linha da tabela
        database.delete(NOME_TABELA, null, null);
    }

    public void deletarTodasInstanciaDoAlarme(long id){
        String [] whereValues = {String.valueOf(id)};
        String where = COLUNA_ID_ALARME + "= ?";
        database.delete(NOME_TABELA,where,whereValues);
    }

    public void deletarInstanciaPorDataAlarmeHorario(String data, long idAlarme, long idHorario){

        String where = COLUNA_DATA +" = ? and "+ COLUNA_ID_ALARME +" = ? and "+ COLUNA_ID_HORARIO +" = ?";
        String []whereValues = {data, String.valueOf(idAlarme), String.valueOf(idHorario)};
        database.delete(NOME_TABELA, where, whereValues);
    }
}
