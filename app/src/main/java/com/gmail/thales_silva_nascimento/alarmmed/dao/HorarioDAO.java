package com.gmail.thales_silva_nascimento.alarmmed.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.model.Horario;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thales on 06/09/17.
 */

public class HorarioDAO {

    public static String NOME_TABELA = "horario";
    public static String COLUNA_ID = "_id";
    public static String COLUNA_HORARIO = "horario";

    public static String SCRIPT_CRIACAO_TABELA = "CREATE TABLE " +NOME_TABELA+ " (" +COLUNA_ID+
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +COLUNA_HORARIO+ " TEXT NOT NULL)";

    //O SQLite usa o tipo de dados 'TEXT' para guarda uma data. Ele não possui o tipo date.
    //Quarda o horário como uma String

    /*db.execSQL("CREATE TABLE horario (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " horario TEXT NOT NULL)");*/

    private static final int ID_INDEX = 0;
    private static final int HORARIO_INDEX = 1;

    private static HorarioDAO instance = null;
    private SQLiteDatabase database = null;

    private HorarioDAO(Context context){
        BancoHelper banco = BancoHelper.getInstance(context);
        database = banco.getWritableDatabase();
    }

    public static HorarioDAO getInstance(Context context){
        if(instance == null)
            instance = new HorarioDAO(context);
        return instance;
    }

    public ContentValues criarContentValues(String horario){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUNA_HORARIO, horario);
        return contentValues;
    }

    public long cadastrarHorario(String horario){
        ContentValues contentValues = criarContentValues(horario);
        long id = database.insert(NOME_TABELA, null, contentValues);

        return id;
    }

    public void excluirHorario(long id){
        String [] whereValues = {String.valueOf(id)};
        database.delete(NOME_TABELA, COLUNA_ID +" = ?", whereValues);
    }

    public Horario buscarHorario(long id){
        String IDstring = String.valueOf(id);
        String query = "SELECT * FROM " +NOME_TABELA+ " WHERE " +COLUNA_ID+ " = " +IDstring;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor == null){
            return null;
        }
        if(cursor.moveToFirst()){
            try{
                String h = cursor.getString(HORARIO_INDEX);
                //Adiciona espaço em branco no texto
                h = h.substring(0,2) +" "+ h.substring(2,3) +" "+ h.substring(3,5);

                Log.v("horarioFormatado", h);
                Horario horario = new Horario(id, h);
                return horario;

            }finally {
                cursor.close();
            }
        }
        return null;
    }


    public Map<String,Long> listarTodosHorarios(){
        Map<String, Long> horarios = new HashMap<String, Long>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + NOME_TABELA, null);

        if(cursor == null){
            return null;
        }
        if(cursor.moveToFirst()){
            try{
                do{
                    long id = cursor.getLong(ID_INDEX);
                    String horario = cursor.getString(HORARIO_INDEX);
                    //Adiciona espaço em branco no texto
                    horario = horario.substring(0,2) +" "+ horario.substring(2,3) +" "+ horario.substring(3,5);
                    horarios.put(horario, id);
                }while(cursor.moveToNext());
            }finally {
                cursor.close();
            }
        }
        return horarios;
    }

    public long buscarIdHorario(String horario){
        String sql = "SELECT _id from horario where horario = ?";
        String []sqlValues = {horario};
        long id = -1;

        Cursor cursor = database.rawQuery(sql, sqlValues);
        if(cursor == null){
            Log.v("HorarioCursorNull", "Cursor null");
            return cadastrarHorario(horario);
        }else{
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                try{
                    Log.v("HorarioCursor>0", "Cursor >0");
                    int index = cursor.getColumnIndex(COLUNA_ID);
                    id = cursor.getLong(index);
                    Log.v("HorarioCursor>0", "Id= "+String.valueOf(id));
                    return id;
                }finally {
                    cursor.close();
                    Log.v("HorarioIDBANCO", String.valueOf(id));

                }
            }
            else {
                Log.v("HorarioCADASTROU", "Cadastrou horario");
                return cadastrarHorario(horario);
            }
        }
    }

}
