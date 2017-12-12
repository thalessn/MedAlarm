package com.gmail.thales_silva_nascimento.alarmmed.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thales on 06/09/17.
 */

public class MedicamentoDAO {

    public static String NOME_TABELA = "medicamento";
    public static String COLUNA_ID = "_id";
    public static String COLUNA_NOME = "nome";
    public static String COLUNA_DOSAGEM = "dosagem";
    public static String COLUNA_TIPO_DOSAGEM = "tipoDosagem";
    public static String COLUNA_USO_CONTINUO = "usoContinuo";
    public static String COLUNA_OBSERVACAO = "observacao";
    public static String COLUNA_FOTO = "foto";

    /*"CREATE TABLE medicamento (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " nome VARCHAR(100) NOT NULL," +
            " dosagem INTEGER NOT NULL," +
            " tipoDosagem TEXT,"+
            " usoContinuo INTEGER NOT NULL," +
            " observacao VARCHAR(300))");*/

    public static String SCRIPT_CRIACAO_TABELA_MEDICAMENTO = "CREATE TABLE " +NOME_TABELA+
            " (" +COLUNA_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +COLUNA_NOME+ " VARCHAR(100) NOT NULL, " +
            COLUNA_DOSAGEM+ " INTEGER NOT NULL, " +COLUNA_TIPO_DOSAGEM+ " TEXT, " +COLUNA_USO_CONTINUO+ " INTEGER NOT NULL, "
            +COLUNA_OBSERVACAO+ " VARCHAR(300), " +COLUNA_FOTO+ " VARCHAR(500))";

    /**
     *  Atalhos para evitar de ficar chamando a função cursor.getColumnIndex()
     *  Manter sincronizado com o banco
     */
    private static final int ID_INDEX = 0;
    private static final int NOME_INDEX = 1;
    private static final int DOSAGEM_INDEX = 2;
    private static final int TIPO_DOSAGEM = 3;
    private static final int USO_CONTINUO_INDEX = 4;
    private static final int OBSERVACAO_INDEX = 5;
    private static final int FOTO_INDEX = 6;

    private SQLiteDatabase database = null;
    private static MedicamentoDAO instance = null;

    private MedicamentoDAO (Context context){
        BancoHelper banco = BancoHelper.getInstance(context);
        database = banco.getWritableDatabase();
    }

    public static MedicamentoDAO getInstance(Context context){
        if(instance == null)
            instance = new MedicamentoDAO(context);
        return instance;
    }

    public ContentValues criarContentValue(Medicamento medicamento){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUNA_NOME, medicamento.getNome());
        contentValues.put(COLUNA_DOSAGEM, medicamento.getDosagem());
        contentValues.put(COLUNA_TIPO_DOSAGEM, medicamento.getTipoDosagem());
        contentValues.put(COLUNA_USO_CONTINUO, medicamento.isUso_continuo()? 1:0);
        contentValues.put(COLUNA_OBSERVACAO, medicamento.getObservacao());
        contentValues.put(COLUNA_FOTO, medicamento.getFoto());

        return contentValues;
    }

    public long cadastrarMedicamento(Medicamento medicamento){
        ContentValues values = criarContentValue(medicamento);
        long id = database.insert(NOME_TABELA, null, values);

        return id;

    }

    public void atualizarMedicamento(Medicamento medicamento){
        ContentValues contentValues = criarContentValue(medicamento);
        //String contendo os valores que substituem os '?' da cláusula where
        String [] whereArgumentos = { String.valueOf( medicamento.getId() ) };
        database.update(NOME_TABELA,contentValues, COLUNA_ID+ " = ?", whereArgumentos);
    }

    public void excluirMedicamento(long id){
        String [] IdString = {String.valueOf(id)};
        database.delete(NOME_TABELA, COLUNA_ID + " = ?", IdString);
    }

    public List<Medicamento> listarTodosMedicamentos(){
        List<Medicamento> meds = new ArrayList<>();
        String query = "SELECT * FROM " +NOME_TABELA+ " ORDER BY " +COLUNA_NOME+ " COLLATE NOCASE";
        Cursor cursor = database.rawQuery(query, null);

        if(cursor == null){
            return null;
        }

        try{
            if(cursor.moveToFirst()){
                do{
                    long id = cursor.getLong(ID_INDEX);
                    String nome = cursor.getString(NOME_INDEX);
                    int dosagem = cursor.getInt(DOSAGEM_INDEX);
                    String tipoDosagem = cursor.getString(TIPO_DOSAGEM);
                    String foto = cursor.getString(FOTO_INDEX);
                    //Transforma para boolean
                    boolean usoContinuo = (cursor.getInt(USO_CONTINUO_INDEX) == 1);
                    String obs = cursor.getString(OBSERVACAO_INDEX);

                    Medicamento medicamento = new Medicamento(id, nome, dosagem,tipoDosagem, usoContinuo, obs, foto);
                    meds.add(medicamento);
                }while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }
        return meds;
    }

    public List<String> listarNomesMedicamentos(){
        ArrayList<String> meds = new ArrayList<>();
        String query = "SELECT " +COLUNA_NOME+ " FROM " +NOME_TABELA;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor == null){
            return null;
        }

        try{
            if(cursor.moveToFirst()){
                do{
                    int indexNome = cursor.getColumnIndex(COLUNA_NOME);
                    meds.add(cursor.getString(indexNome));

                }while ( cursor.moveToNext() );
            }
        }finally {
            cursor.close();
        }
        return meds;
    }

    public Medicamento buscarMedicamentoID(long id){
        String IDString = String.valueOf(id);
        String query = "SELECT * FROM " +NOME_TABELA+ " WHERE " +COLUNA_ID+ " = " +IDString;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor == null){
            return null;
        }

        try{
            if(cursor.moveToFirst()){
                String nome = cursor.getString(NOME_INDEX);
                int dosagem = cursor.getInt(DOSAGEM_INDEX);
                String tipoDosagem = cursor.getString(TIPO_DOSAGEM);
                String obs = cursor.getString(OBSERVACAO_INDEX);
                boolean usoCOntinuo = ( cursor.getInt(USO_CONTINUO_INDEX) == 1 ? true: false );
                String foto = cursor.getString(FOTO_INDEX);

                Medicamento medicamento = new Medicamento(id, nome, dosagem, tipoDosagem, usoCOntinuo, obs, foto);

                return medicamento;
            }
        }finally {
            cursor.close();
        }

        return null;
    }

    /**
     * Busca os medicamento com a data e horário especificados
     * @param idHorario
     * @param data
     * @return
     */

    public List<Medicamento> medicamentosPorHorarioEData(long idHorario, String data){

        List<Medicamento> medicamentos = new ArrayList<>();
        String query = "Select * FROM " +NOME_TABELA+ " inner join " +AlarmeDAO.NOME_TABELA+ " on "+
                NOME_TABELA +"."+ COLUNA_ID +" = "+ AlarmeDAO.NOME_TABELA +"."+ AlarmeDAO.COLUNA_ID+
                " inner join " +InstanciaAlarmeDAO.NOME_TABELA+ " on " +AlarmeDAO.NOME_TABELA +"."+
                AlarmeDAO.COLUNA_ID+ " = " +InstanciaAlarmeDAO.NOME_TABELA+ "." + InstanciaAlarmeDAO.COLUNA_ID_ALARME+
                " where " +InstanciaAlarmeDAO.NOME_TABELA+ "." +InstanciaAlarmeDAO.COLUNA_ID_HORARIO +" = ? and "+
                InstanciaAlarmeDAO.NOME_TABELA +"."+ InstanciaAlarmeDAO.COLUNA_DATA+ " = ? group by "
                +NOME_TABELA+ "." +COLUNA_NOME;

        String [] whereValues = {String.valueOf(idHorario), data};
        Cursor cursor = database.rawQuery(query, whereValues);

        if(cursor == null){
            return null;
        }
        try{
            if(cursor.moveToFirst()){
                do{
                    long id = cursor.getLong(ID_INDEX);
                    String nome = cursor.getString(NOME_INDEX);
                    int dosagem = cursor.getInt(DOSAGEM_INDEX);
                    String tipoDosagem = cursor.getString(TIPO_DOSAGEM);
                    String foto = cursor.getString(FOTO_INDEX);
                    //Transforma para boolean
                    boolean usoContinuo = (cursor.getInt(USO_CONTINUO_INDEX) == 1);
                    String obs = cursor.getString(OBSERVACAO_INDEX);

                    Medicamento medicamento = new Medicamento(id, nome, dosagem, tipoDosagem, usoContinuo, obs, foto);
                    medicamentos.add(medicamento);
                }while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }

        return medicamentos;
    }

}
