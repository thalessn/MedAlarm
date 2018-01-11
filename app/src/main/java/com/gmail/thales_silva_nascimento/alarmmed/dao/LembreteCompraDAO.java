package com.gmail.thales_silva_nascimento.alarmmed.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.thales_silva_nascimento.alarmmed.model.LembreteCompra;

/**
 * Created by thales on 10/01/18.
 */

public class LembreteCompraDAO {

    public static String NOME_TABELA = "lembreteCompra";
    public static String COLUNA_ID = "_id";
    public static String COLUNA_ID_MEDICAMENTO = "_idMedicamento";
    public static String COLUNA_QTD_ALERTA = "qtd_alerta";
    public static String COLUNA_HORARIO_ALERTA = "horario_alerta";

    /*Create TABLE lembreteCompra (_id INTEGER PRIMARY KEY AUTOINCREMENT,
      _idMedicamento INTEGER,
      qtd_alerta INTEGER,
      horario_alerta TEXT,
      FOREIGN KEY (_idMedicamento) REFERENCES medicamento (_id) )
    */


    public static String SCRIPT_CRIACAO_TABELA = "CREATE TABLE " +NOME_TABELA+ " (" +COLUNA_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUNA_ID_MEDICAMENTO+ " INTEGER, " +COLUNA_QTD_ALERTA+ " INTEGER, " +COLUNA_HORARIO_ALERTA+ " TEXT, FOREIGN KEY ("+COLUNA_ID_MEDICAMENTO+") "
            +"REFERENCES " +MedicamentoDAO.NOME_TABELA+ " (" +MedicamentoDAO.COLUNA_ID+ "))";

    private SQLiteDatabase database = null;
    private static LembreteCompraDAO instance = null;

    private LembreteCompraDAO(Context context){
        BancoHelper banco = BancoHelper.getInstance(context);
        database = banco.getWritableDatabase();
    }

    public static LembreteCompraDAO getInstance(Context context){
        if(instance == null)
            instance = new LembreteCompraDAO(context);
        return instance;
    }

    public ContentValues criarContentValues(LembreteCompra lembreteCompra){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUNA_ID_MEDICAMENTO,lembreteCompra.getIdMedicamento());
        contentValues.put(COLUNA_QTD_ALERTA,lembreteCompra.getQtd_alerta());
        contentValues.put(COLUNA_HORARIO_ALERTA,lembreteCompra.getHorarioAlerta());

        return contentValues;
    }

    public void cadastrarLembreteCompra(LembreteCompra lembreteCompra){
        ContentValues contentValues = criarContentValues(lembreteCompra);
        database.insert(NOME_TABELA,null,contentValues);
    }

    public void deletarLembreteCompraPorIdMed(long idMedicamento){
        String [] whereValues = {String.valueOf(idMedicamento)};
        String where = COLUNA_ID_MEDICAMENTO+ " = ?";
        database.delete(NOME_TABELA,where,whereValues);
    }

}
