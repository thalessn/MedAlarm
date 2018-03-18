package com.gmail.thales_silva_nascimento.alarmmed.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.thales_silva_nascimento.alarmmed.model.Horario;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarmeHistorico;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.util.ArrayList;
import java.util.List;


public class HistoricoDAO {

    public static String NOME_TABELA = "historico";
    public static String COLUNA_ID = "_id";
    public static String COLUNA_ID_MEDICAMENTO = "_idMedicamento";
    public static String COLUNA_DATA_PROG = "dataProg";
    public static String COLUNA_ID_HORARIO = "_idHorario";
    public static String COLUNA_DATA_ADMINISTRADO = "dataAdministrado";
    public static String COLUNA_HORA_ADMINISTRADO = "horaAdministrado";
    public static String COLUNA_STATUS = "status";
    public static String COLUNA_ID_ALARME = "_idAlarme";

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

    public static String SCRIPT_CRIACAO = "CREATE TABLE " + NOME_TABELA + " (" + COLUNA_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUNA_ID_MEDICAMENTO + " INTEGER, " +
            COLUNA_DATA_PROG + " TEXT, " + COLUNA_ID_HORARIO + " INTEGER, " + COLUNA_DATA_ADMINISTRADO +
            " TEXT, " + COLUNA_HORA_ADMINISTRADO + " TEXT, " + COLUNA_STATUS + " TEXT, FOREIGN KEY " +
            "(" + COLUNA_ID_MEDICAMENTO + ") REFERENCES " + MedicamentoDAO.NOME_TABELA + " (" +
            MedicamentoDAO.COLUNA_ID + "), FOREIGN KEY (" + COLUNA_ID_HORARIO + ") REFERENCES " +
            HorarioDAO.NOME_TABELA + " (" + HorarioDAO.COLUNA_ID + "))";

    private SQLiteDatabase database = null;
    private static HistoricoDAO instance = null;

    private HistoricoDAO(Context context) {
        BancoHelper banco = BancoHelper.getInstance(context);
        database = banco.getWritableDatabase();
    }

    public static HistoricoDAO getInstance(Context context) {
        if (instance == null)
            instance = new HistoricoDAO(context);
        return instance;
    }


    private ContentValues criarContentValues(ItemAlarmeHistorico itemAlarmeHistorico) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUNA_ID_MEDICAMENTO, itemAlarmeHistorico.getMed().getId());
        contentValues.put(COLUNA_DATA_PROG, itemAlarmeHistorico.getDataProgramada());
        contentValues.put(COLUNA_ID_HORARIO, itemAlarmeHistorico.getHorario().getId());
        contentValues.put(COLUNA_DATA_ADMINISTRADO, itemAlarmeHistorico.getDataAdministrado());
        contentValues.put(COLUNA_HORA_ADMINISTRADO, itemAlarmeHistorico.getHoraAdministrado());
        contentValues.put(COLUNA_STATUS, itemAlarmeHistorico.getStatus());

        return contentValues;
    }

    public void cadastrarHistoricoMedicamento(ItemAlarmeHistorico itemAlarmeHistorico) {
        ContentValues contentValues = criarContentValues(itemAlarmeHistorico);
        database.insert(NOME_TABELA, null, contentValues);
    }

    public void deletarHistoricoMedicamento(long idMedicamento) {
        String[] IdString = {String.valueOf(idMedicamento)};
        database.delete(NOME_TABELA, COLUNA_ID_MEDICAMENTO + " = ?", IdString);
    }

    public List<ItemAlarmeHistorico> listarTodosItemsHistoricos()
    {
        String query = "Select "+MedicamentoDAO.NOME_TABELA+".*, "+NOME_TABELA+".*, "+HorarioDAO.NOME_TABELA+"."
                +HorarioDAO.COLUNA_HORARIO+" from "+NOME_TABELA+" inner join "+MedicamentoDAO.NOME_TABELA
                + " on "+NOME_TABELA+"."+COLUNA_ID_MEDICAMENTO+" = "+MedicamentoDAO.NOME_TABELA+"."
                +MedicamentoDAO.COLUNA_ID+" inner join "+HorarioDAO.NOME_TABELA+" on "+NOME_TABELA+"."
                +COLUNA_ID_HORARIO+" = "+HorarioDAO.NOME_TABELA+"."+ HorarioDAO.COLUNA_ID+" order by date("
                +COLUNA_DATA_PROG+") DESC, "
                +HorarioDAO.NOME_TABELA+"."+HorarioDAO.COLUNA_HORARIO;

        List<ItemAlarmeHistorico> items = new ArrayList<>();
        Cursor cursor = database.rawQuery(query,null);

        if(cursor == null){
            return null;
        }

        try{
            if(cursor.moveToFirst()){
                do{
                    //Informações para criar um mendicamento
                    long id =                     cursor.getLong(0);
                    String nome =                 cursor.getString(1);
                    int dosagem =                 cursor.getInt(2);
                    String tipoDosagem =          cursor.getString(3);
                    String foto =                 cursor.getString(6);
                    //Transforma para boolean
                    boolean usoContinuo =         (cursor.getInt(4) == 1);
                    String obs =                  cursor.getString(5);
                    int quantidade =              cursor.getInt(7);

                    //Cria o medicamento do ItemAlarmeHistórico
                    Medicamento medicamento = new Medicamento(id, nome, dosagem, tipoDosagem, usoContinuo, obs, foto, quantidade);

                    //Informações necessárias para criar o itemAlarmeHistorico
                    long idHistorico =        cursor.getLong(8);
                    String dataProg =         cursor.getString(10);
                    long idHorario =          cursor.getLong(11);
                    String dataAdministrado = cursor.getString(12);
                    String horaAdministrado = cursor.getString(13);
                    String status =           cursor.getString(14);
                    String horario =          cursor.getString(15);

                    //Cria o objeto horário do itemAlarmeHistórico
                    Horario horario1 = new Horario(idHorario,horario);

                    //Cria e salva o item na lista
                    ItemAlarmeHistorico itemSalvo = new ItemAlarmeHistorico(idHistorico,medicamento,dataProg,horario1,dataAdministrado,horaAdministrado);
                    itemSalvo.setStatus(status);

                    items.add(itemSalvo);
                }while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }

        return items;
    }

    /**
     * Retorna a lista com o histórico para o periodo selecionado.
     * Se for zero a idMedicamento então irá trazer todos os medicamento para o período.
     * @param dataInicial
     * @param dataFinal
     * @param idMedicamento
     * @return
     */
    public List<ItemAlarmeHistorico> listarHistoricoPeriodo(String dataInicial, String dataFinal, long idMedicamento, String order){
        Long idMed = idMedicamento;
        String query = null;
        if((idMed == 0) || (idMed == -1)){

            query = "Select "+MedicamentoDAO.NOME_TABELA+".*, "+NOME_TABELA+".*, "+HorarioDAO.NOME_TABELA+"."
                    +HorarioDAO.COLUNA_HORARIO+
                    " from "+NOME_TABELA+" inner join "+MedicamentoDAO.NOME_TABELA
                    + " on "+NOME_TABELA+"."+COLUNA_ID_MEDICAMENTO+" = "+MedicamentoDAO.NOME_TABELA+"."
                    +MedicamentoDAO.COLUNA_ID+" inner join "+HorarioDAO.NOME_TABELA+" on "+NOME_TABELA+"."
                    +COLUNA_ID_HORARIO+" = "+HorarioDAO.NOME_TABELA+"."+ HorarioDAO.COLUNA_ID+
                    " where "+COLUNA_DATA_PROG+" between '"+dataInicial+"' and '"+dataFinal+"'"+
                    " order by date(" +COLUNA_DATA_PROG+") "+order+","+HorarioDAO.NOME_TABELA+"."+HorarioDAO.COLUNA_HORARIO;
        }else {
            query = "Select "+MedicamentoDAO.NOME_TABELA+".*, "+NOME_TABELA+".*, "+HorarioDAO.NOME_TABELA+"."
                    +HorarioDAO.COLUNA_HORARIO+
                    " from "+NOME_TABELA+" inner join "+MedicamentoDAO.NOME_TABELA
                    + " on "+NOME_TABELA+"."+COLUNA_ID_MEDICAMENTO+" = "+MedicamentoDAO.NOME_TABELA+"."
                    +MedicamentoDAO.COLUNA_ID+" inner join "+HorarioDAO.NOME_TABELA+" on "+NOME_TABELA+"."
                    +COLUNA_ID_HORARIO+" = "+HorarioDAO.NOME_TABELA+"."+ HorarioDAO.COLUNA_ID+
                    " where "+MedicamentoDAO.NOME_TABELA+"."+MedicamentoDAO.COLUNA_ID+" = "+idMed.toString()+" and "+COLUNA_DATA_PROG+" between '"+dataInicial+"' and '"+dataFinal+"'"+
                    " order by date(" +COLUNA_DATA_PROG+") "+order+"," +HorarioDAO.NOME_TABELA+"."+HorarioDAO.COLUNA_HORARIO;
        }

        List<ItemAlarmeHistorico> items = new ArrayList<>();
        Cursor cursor = database.rawQuery(query,null);

        if(cursor == null){
            return null;
        }

        try{
            if(cursor.moveToFirst()){
                do{
                    //Informações para criar um mendicamento
                    long id =                     cursor.getLong(0);
                    String nome =                 cursor.getString(1);
                    int dosagem =                 cursor.getInt(2);
                    String tipoDosagem =          cursor.getString(3);
                    String foto =                 cursor.getString(6);
                    //Transforma para boolean
                    boolean usoContinuo =         (cursor.getInt(4) == 1);
                    String obs =                  cursor.getString(5);
                    int quantidade =              cursor.getInt(7);

                    //Cria o medicamento do ItemAlarmeHistórico
                    Medicamento medicamento = new Medicamento(id, nome, dosagem, tipoDosagem, usoContinuo, obs, foto, quantidade);

                    //Informações necessárias para criar o itemAlarmeHistorico
                    long idHistorico =        cursor.getLong(8);
                    String dataProg =         cursor.getString(10);
                    long idHorario =          cursor.getLong(11);
                    String dataAdministrado = cursor.getString(12);
                    String horaAdministrado = cursor.getString(13);
                    String status =           cursor.getString(14);
                    String horario =          cursor.getString(15);

                    //Cria o objeto horário do itemAlarmeHistórico
                    Horario horario1 = new Horario(idHorario,horario);

                    //Cria e salva o item na lista
                    ItemAlarmeHistorico itemSalvo = new ItemAlarmeHistorico(idHistorico,medicamento,dataProg,horario1,dataAdministrado,horaAdministrado);
                    itemSalvo.setStatus(status);
                    items.add(itemSalvo);
                }while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }

        return items;
    }
}





