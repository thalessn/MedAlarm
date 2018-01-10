package com.gmail.thales_silva_nascimento.alarmmed.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.model.AlarmeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thales on 06/09/17.
 */

public class AlarmeInfoDAO {

    public static String NOME_TABELA = "alarmeInfo";
    public static String COLUNA_ID = "_id";
    public static String COLUNA_HORARIO = "horario";
    public static String COLUNA_QTD_TOMAR = "qtd_tomar";
    public static String COLUNA_ID_ALARME = "_idAlarme";

    /* CREATE TABLE alarmeInfo (_id INTEGER PRIMARY KEY AUTOINCREMENT,
     * " horario TEXT NOT NULL,
      *" qtd_tomar REAL NOT NULL,
      *" _idAlarme INTEGER,
      *" FOREIGN KEY (_idAlarme) REFERENCES alarm (_id)");*/
    public static String SCRIPT_CRIACAO_TABELA = "CREATE TABLE " +NOME_TABELA+ " (" + COLUNA_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +COLUNA_HORARIO+ " TEXT NOT NULL, "
            + COLUNA_QTD_TOMAR+ " REAL NOT NULL, " +COLUNA_ID_ALARME+ " INTEGER, FOREIGN KEY ("
            + COLUNA_ID_ALARME+ ") REFERENCES " +AlarmeDAO.NOME_TABELA+ " (" +AlarmeDAO.COLUNA_ID+ "))";


    /**
     * Atalhos para evitar de ficar chamando a função cursor.getColumnIndex()
     * Manter sincronizado com o banco
     */
    private static final int ID_INDEX = 0;
    private static final int HORARIO_INDEX = 1;
    private static final int QTD_TOMAR_INDEX = 2;
    private static final int ID_ALARME_INDEX = 3;


    private static AlarmeInfoDAO instance = null;
    private SQLiteDatabase database = null;

    private AlarmeInfoDAO(Context context){
        BancoHelper banco = BancoHelper.getInstance(context);
        database = banco.getWritableDatabase();
    }

    public static AlarmeInfoDAO getInstance(Context context){
        if(instance == null)
            instance = new AlarmeInfoDAO(context);
        return instance;
    }


    public ContentValues criarContentValues(AlarmeInfo alarmeInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUNA_HORARIO, alarmeInfo.getHorario());
        contentValues.put(COLUNA_QTD_TOMAR, alarmeInfo.getQtd_tomar());
        contentValues.put(COLUNA_ID_ALARME, alarmeInfo.getIdAlarme());
        return contentValues;
    }

    public void cadastrarAlarmeInfo(AlarmeInfo alarmeInfo){
        Log.v("Cadastrou", "AlarmeInfo");
        ContentValues contentValues = criarContentValues(alarmeInfo);
        database.insert(NOME_TABELA, null, contentValues);
    }

    public void deletarAlarmeInfosDoAlarme(long idAlarme){
        String [] whereValues = {String.valueOf(idAlarme)};
        String where = COLUNA_ID_ALARME + " = ?";
        database.delete(NOME_TABELA,where,whereValues);
    }

    public List<AlarmeInfo> listarAlarmeInfoPorAlarmeId(long idAlarme){
        String query = "Select * from " +NOME_TABELA+ " where " +COLUNA_ID_ALARME+ " = " +String.valueOf(idAlarme);
        Cursor cursor = database.rawQuery(query,null);
        List<AlarmeInfo> alarmeInfos = construiAlarmeInfoPorCursor(cursor);

        return  alarmeInfos;
    }


    private List<AlarmeInfo> construiAlarmeInfoPorCursor(Cursor cursor){
        //Com os objetos
        List<AlarmeInfo> alarmeInfos = new ArrayList<>();
        //Verifica o cursor
        if(cursor == null){
            return  alarmeInfos;
        }

        try{
            if(cursor.moveToFirst()){
                do{
                    //Recupera as informações do cursor
                    String horario = cursor.getString(HORARIO_INDEX);
                    float qtd_tomar = cursor.getFloat(QTD_TOMAR_INDEX);
                    long idAlarme = cursor.getLong(ID_ALARME_INDEX);
                    //Insntancia um novo objeto
                    AlarmeInfo ai = new AlarmeInfo(horario,qtd_tomar);
                    ai.setIdAlarme(idAlarme);
                    //Adiciona na lista
                    alarmeInfos.add(ai);
                }while (cursor.moveToNext());
            }
        }finally {
            //Encerra a utilização do objeto cursor
            cursor.close();
        }

        return alarmeInfos;
    }

}
