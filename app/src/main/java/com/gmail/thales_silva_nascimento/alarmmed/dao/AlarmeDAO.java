package com.gmail.thales_silva_nascimento.alarmmed.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.model.Alarme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmeDAO {

    public static String NOME_TABELA = "alarme";
    public static String COLUNA_ID = "_id";
    public static String COLUNA_DATA_INICIO = "dataInicio";
    public static String COLUNA_DATA_FIM = "dataFim";
    public static String COLUNA_PERIODO = "periodo";
    public static String COLUNA_TIPO_REPETICAO = "tipoRepeticao";
    public static String COLUNA_INTERVALO_REPETICAO = "intervaloRepeticao";
    public static String COLUNA_STATUS = "status";
    public static String COLUNA_ID_MEDICAMENTO = "_idMedicamento";
    public static String COLUNA_FREQ_HORARIO = "freqHorario";
    public static String COLUNA_FREQ_DIAS = "freqDias";


    //No SQLite não possui o tipo DATE. Estou utilizando o tipo TEXT pois ele reconhece a string 'YYYY-MM-DD'
    //como uma data, podendo utilizar posteriormente as funções de data e hora do SQLite.

    //A dataFim pode ser nula? Pois no caso do uso Contínuo não existirá. Pensar sobre isso.
    /*"CREATE TABLE alarme (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " dataInicio TEXT NOT NULL," +
                " dataFim TEXT," +
                " periodo INTEGER NOT NULL," +
                " tipoRepeticao INTEGER NOT NULL," +
                " intervaloRepeticao INTEGER NOT NULL," +
                " status INTEGER NOT NULL," +
                " _idMedicamento INTEGER," +
                " freqHorario TEXT,"+
                " freqDias TEXT,"+
                " FOREIGN KEY (_idMedicamento) REFERENCES medicamento (_id))");*/

    public static String SCRIPT_CRIACAO_TABELA = "CREATE TABLE " +NOME_TABELA+ " (" +COLUNA_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUNA_DATA_INICIO+ " TEXT NOT NULL, " +COLUNA_DATA_FIM+ " TEXT, " +COLUNA_PERIODO+ " INTEGER NOT NULL, " +COLUNA_TIPO_REPETICAO+
            " INTEGER NOT NULL, " +COLUNA_INTERVALO_REPETICAO+ " INTEGER NOT NULL, " +COLUNA_STATUS+ " INTEGER NOT NULL, " +COLUNA_ID_MEDICAMENTO+
            " INTEGER, " +COLUNA_FREQ_HORARIO+ " TEXT, " +COLUNA_FREQ_DIAS+ " TEXT, FOREIGN KEY (" +COLUNA_ID_MEDICAMENTO+ ") REFERENCES " +MedicamentoDAO.NOME_TABELA+ " (" +MedicamentoDAO.COLUNA_ID+ "))";


    /**
     * Atalhos para evitar de ficar chamando a função cursor.getColumnIndex()
     * Manter sincronizado com o banco
     */
    private static final int ID_INDEX = 0;
    private static final int DATA_INICIO_INDEX = 1;
    private static final int DATA_FIM_INDEX = 2;
    private static final int PERIODO_INDEX  = 3;
    private static final int TIPO_REPETICAO_INDEX = 4;
    private static final int INTEVALO_REPETICAO_INDEX = 5;
    private static final int STATUS_INDEX = 6;
    private static final int ID_MEDICAMENTO_INDEX = 7;
    private static final int FREQ_HORARIO_INDEX = 8;
    private static final int FREQ_DIAS_INDEX = 9;

    private static AlarmeDAO instance = null;
    private SQLiteDatabase database = null;

    private AlarmeDAO (Context context){
        BancoHelper banco = BancoHelper.getInstance(context);
        database = banco.getWritableDatabase();
    }

    public static AlarmeDAO getInstance (Context context){
        if(instance == null)
            instance = new AlarmeDAO(context);
        return instance;
    }

    public ContentValues criarContentValues(Alarme alarme){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUNA_DATA_INICIO, Utils.CalendarToStringData(alarme.getDataInicio()));
        contentValues.put(COLUNA_DATA_FIM, Utils.CalendarToStringData(alarme.getDataFim()));
        contentValues.put(COLUNA_PERIODO, alarme.getPeriodo());
        contentValues.put(COLUNA_TIPO_REPETICAO, alarme.getTipoRepeticao());
        contentValues.put(COLUNA_INTERVALO_REPETICAO, alarme.getIntervaloRepeticao());
        contentValues.put(COLUNA_STATUS, alarme.isStatus()? 1:0);
        contentValues.put(COLUNA_ID_MEDICAMENTO, alarme.getIdMedicamento());
        contentValues.put(COLUNA_FREQ_HORARIO, alarme.getFreqHorario());
        contentValues.put(COLUNA_FREQ_DIAS, alarme.getFreqDias());

        return contentValues;
    }

    public long cadastrarAlarme(Alarme alarme){
        //Cria o objeto ContentValues com os valores do alarme
        ContentValues contentValues = criarContentValues(alarme);
        //Insere no banco de dados
        long idAlarme = database.insert(NOME_TABELA, null, contentValues);
        return idAlarme;
    }

    public void excluirAlarme(long id){
        String [] IdString = {String.valueOf(id)};
        database.delete(NOME_TABELA, COLUNA_ID+ " = ?", IdString);
    }

    public void atualizarAlarme(Alarme alarme){
        ContentValues contentValues = criarContentValues(alarme);
        String [] IdString = {String.valueOf(alarme.getId())};
        database.update(NOME_TABELA, contentValues, COLUNA_ID+ " = ?", IdString);
    }

    public Alarme buscarAlarmeId(long id){
        String query = "SELECT * FROM " +NOME_TABELA+ " WHERE " +COLUNA_ID+ " = " + String.valueOf(id);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor == null){
            return null;
        }

        if(cursor.moveToFirst()){
            Calendar dataInicio = Utils.DataStringToCalendar( cursor.getString(DATA_INICIO_INDEX) );
            Calendar dataFim = Utils.DataStringToCalendar( cursor.getString(DATA_FIM_INDEX) );
            int periodo = cursor.getInt(PERIODO_INDEX);
            int tipoRepeticao = cursor.getInt(TIPO_REPETICAO_INDEX);
            int intervaloRepeticao = cursor.getInt(INTEVALO_REPETICAO_INDEX);
            boolean status = (cursor.getInt(STATUS_INDEX) == 1 );
            long idMedicamento = cursor.getLong(ID_MEDICAMENTO_INDEX);
            String freqHorario = cursor.getString(FREQ_HORARIO_INDEX);
            String freqDias = cursor.getString(FREQ_DIAS_INDEX);

            Alarme alarme = new Alarme(id, dataInicio, dataFim, periodo, tipoRepeticao, intervaloRepeticao, status, idMedicamento, freqHorario, freqDias);
            return alarme;
        }
        return null;
    }

    public List<Alarme> listarTodosAlarmes(){
        List<Alarme> alarmes = new ArrayList<>();

        //Colocar a condição 'ORDER BY'
        String query = "SELECT * FROM " +NOME_TABELA;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor == null){
            return null;
        }

        try{
            if(cursor.moveToFirst()){
                do{
                    long id = cursor.getLong(ID_INDEX);
                    Calendar dataInicio = Utils.DataStringToCalendar( cursor.getString(DATA_INICIO_INDEX) );
                    Calendar dataFim = Utils.DataStringToCalendar( cursor.getString(DATA_INICIO_INDEX) );
                    int periodo = cursor.getInt(PERIODO_INDEX);
                    int tipoRepeticao = cursor.getInt(TIPO_REPETICAO_INDEX);
                    int intervaloRepeticao = cursor.getInt(INTEVALO_REPETICAO_INDEX);
                    boolean status = cursor.getInt(STATUS_INDEX) == 1;
                    long idMedicamento = cursor.getLong(ID_MEDICAMENTO_INDEX);
                    String freqHorario = cursor.getString(FREQ_HORARIO_INDEX);
                    String freqDias = cursor.getString(FREQ_DIAS_INDEX);

                    Alarme novo = new Alarme(id, dataInicio, dataFim, periodo, tipoRepeticao, intervaloRepeticao, status, idMedicamento, freqHorario, freqDias);
                    alarmes.add(novo);
                }while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }

        return alarmes;
    }

    public long buscarIdAlarmePorMedID(long idMedicamento){
        //Inicializa a id como nula
        long id = -1;
        String query = "Select " +COLUNA_ID+ " FROM " +NOME_TABELA+ " where " +COLUNA_ID_MEDICAMENTO+
                " = ?";
        String []whereValues = {String.valueOf(idMedicamento)};
        Cursor cursor = database.rawQuery(query,whereValues);

        if(cursor == null){
            return id;
        }

        try{
            if (cursor.moveToFirst()){
                id = cursor.getLong(ID_INDEX);
            }

        }finally {
            cursor.close();
        }

        return id;
    }

    public Alarme buscarAlarmeIdMed(long idMedicamento){
        String query = "SELECT * FROM " +NOME_TABELA+ " WHERE " +COLUNA_ID_MEDICAMENTO+ " = " + String.valueOf(idMedicamento);
        Cursor cursor = database.rawQuery(query, null);

        if(cursor == null){
            return null;
        }

        if(cursor.moveToFirst()){
            Calendar dataInicio = Utils.DataStringToCalendar( cursor.getString(DATA_INICIO_INDEX) );
            Calendar dataFim = Utils.DataStringToCalendar( cursor.getString(DATA_FIM_INDEX) );
            long id = cursor.getLong(ID_INDEX);
            int periodo = cursor.getInt(PERIODO_INDEX);
            int tipoRepeticao = cursor.getInt(TIPO_REPETICAO_INDEX);
            int intervaloRepeticao = cursor.getInt(INTEVALO_REPETICAO_INDEX);
            boolean status = (cursor.getInt(STATUS_INDEX) == 1 );
            String freqHorario = cursor.getString(FREQ_HORARIO_INDEX);
            String freqDias = cursor.getString(FREQ_DIAS_INDEX);

            Alarme alarme = new Alarme(id, dataInicio, dataFim, periodo, tipoRepeticao, intervaloRepeticao, status, idMedicamento, freqHorario, freqDias);
            return alarme;
        }
        return null;
    }

}
