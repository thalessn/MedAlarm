package com.gmail.thales_silva_nascimento.alarmmed.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.model.AlarmeInfo;

/**
 * Created by Thales on 07/02/2017.
 */
//Clase utilizada para criação e atualização do banco.
//No método onCreate são definidas todas as tabelas que serão utilizadas no banco de dados.
public class BancoHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NOME = "AlarmMed";
    private static final int DATABASE_VERSION = 1;

    private static BancoHelper instance;

    private BancoHelper(Context context) {
        super(context, DATABASE_NOME, null, DATABASE_VERSION);
    }

    public static BancoHelper getInstance(Context context){
        if(instance == null)
            instance = new BancoHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("ONCREATE BANCO", "Passou");
        db.execSQL(EspecialidadeDAO.SCRIPT_CRIACAO_TABELA_ESPECIALIDADE);
        db.execSQL(EspecialidadeDAO.SCRIPT_INSERT_INICIAL_ESPECIALIDADES);
        db.execSQL(MedicoDAO.SCRIPT_CRIACAO_TABELA_MEDICO);

        db.execSQL(AlarmeDAO.SCRIPT_CRIACAO_TABELA);
        db.execSQL(MedicamentoDAO.SCRIPT_CRIACAO_TABELA_MEDICAMENTO);
        db.execSQL(AlarmeInfoDAO.SCRIPT_CRIACAO_TABELA);
        db.execSQL(HorarioDAO.SCRIPT_CRIACAO_TABELA);
        db.execSQL(InstanciaAlarmeDAO.SCRIPT_CRIACAO_TABELA);
        db.execSQL(HistoricoDAO.SCRIPT_CRIACAO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(EspecialidadeDAO.SCRIPT_DELECAO_TABELA);
    }
}
