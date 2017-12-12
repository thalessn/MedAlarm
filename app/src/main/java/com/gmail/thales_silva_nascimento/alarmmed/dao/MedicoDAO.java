package com.gmail.thales_silva_nascimento.alarmmed.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.thales_silva_nascimento.alarmmed.model.Medico;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thales on 10/02/2017.
 */

public class MedicoDAO {
    public static final String NOME_TABELA = "medico";
    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_NOME = "nome";
    public static final String COLUNA_ENDERECO = "endereco";
    public static final String COLUNA_TELEFONE = "telefone";
    public static final String COLUNA_OBS = "observacao";
    public static final String COLUNA_ESPECIALIDADE = "especialidade";


    public static final String SCRIPT_CRIACAO_TABELA_MEDICO = "CREATE TABLE " +NOME_TABELA+ " (" + COLUNA_ID
            +" INTEGER PRIMARY KEY AUTOINCREMENT, " +COLUNA_NOME+ " VARCHAR(100) NOT NULL, " +COLUNA_ENDERECO+ " VARCHAR(300), "
            +COLUNA_TELEFONE+ " VARCHAR(20) NOT NULL, " +COLUNA_OBS+ " VARCHAR(300), " +COLUNA_ESPECIALIDADE
            + " INTEGER, FOREIGN KEY(" +COLUNA_ESPECIALIDADE+ ") REFERENCES " +EspecialidadeDAO.NOME_TABELA+ "(" +EspecialidadeDAO.COLUNA_ID+ "))";

    //Variavel que receberá da função BancoHelper a ligação com o banco de dados
    private SQLiteDatabase database = null;

    private static MedicoDAO instance;

    private MedicoDAO(Context context){
        BancoHelper banco = BancoHelper.getInstance(context);
        database = banco.getWritableDatabase();
    }
    public static MedicoDAO getInstance(Context context){
        if(instance == null)
            instance = new MedicoDAO(context);
        return instance;
    }

    public void salvar(Medico medico){
        //ContentValues - contém os valores extraidos do médico passado como parâmentro de entrada para o banco.
        //A função gerarContentValuesMedico retorna um objeto ContentValues já preenchido.
        ContentValues values = gerarContentValuesMedico(medico);
        database.insert(NOME_TABELA, null, values);
    }

    private ContentValues gerarContentValuesMedico(Medico medico){
        ContentValues values = new ContentValues();
        values.put(COLUNA_NOME, medico.getNome());
        values.put(COLUNA_ENDERECO, medico.getEndereco());
        values.put(COLUNA_TELEFONE, medico.getTelefone());
        values.put(COLUNA_OBS, medico.getObservacao());
        values.put(COLUNA_ESPECIALIDADE, medico.getIdEspec());

        return values;
    }

    public List<Medico> listarTodos(){
        //String contendo a query a ser executada
        //COLLATE NOCASE - independente se ser maiúscula ou minúscula
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA + " ORDER BY " +COLUNA_NOME+ " COLLATE NOCASE";
        //Cursor - onejto que contém todas as linhas recuperadas do banco na tabela médico
        Cursor cursor = database.rawQuery(queryReturnAll, null);
        //Lista para retornar todos os médicos
        List<Medico> medicos = constroiMedicoPorCursor(cursor);

        return medicos;
    }

    private List<Medico> constroiMedicoPorCursor(Cursor cursor){
        //Lista que retornará todos os médicos cadastrados
        List<Medico> medicos = new ArrayList<>();
        //Se não encontrar nehum resultado retorna a instância criada vazia
        if (cursor == null){
            return medicos;
        }
        //O bloco try para caso haver alguma exception aparecer no log
        try{
            //Verifica se o cursor possui dados. Por padrão o primeitro elemento do curso não é o começo dos dados e sim uma
            // referência ao ponteiro do cursor.
            if (cursor.moveToFirst()){
                do{
                    //Armazena os indices de cada coluna para utilizar na recuperação dos dados
                    int indexID = cursor.getColumnIndex(COLUNA_ID);
                    int indexNome = cursor.getColumnIndex(COLUNA_NOME);
                    int indexEndereco = cursor.getColumnIndex(COLUNA_ENDERECO);
                    int indexTelefone = cursor.getColumnIndex(COLUNA_TELEFONE);
                    int indexObs = cursor.getColumnIndex(COLUNA_OBS);
                    int indexEspecialidade = cursor.getColumnIndex(COLUNA_ESPECIALIDADE);

                    //Variáveis para a criação do objeto medico recuperado do banco
                    long id = cursor.getLong(indexID);
                    String nome = cursor.getString(indexNome);
                    String end = cursor.getString(indexEndereco);
                    String tel = cursor.getString(indexTelefone);
                    String obs = cursor.getString(indexObs);
                    int idEspec = cursor.getInt(indexEspecialidade);

                    //Cria o novo médico para ser adicionado a lista de medicos
                    Medico novo = new Medico(id, nome, end, tel, obs, idEspec);
                    //Adicona o médico na lista
                    medicos.add(novo);
                }while (cursor.moveToNext());
            }
        }finally {
            //Encerra a utilização do objeto cursor
            cursor.close();
        }
        return medicos;
    }

    public void editar(Medico medico){
        //ContentValues contém as novas infomações do médico
        ContentValues values = gerarContentValuesMedico(medico);
        //Vetor de string utilizado para substituir os "?" da clausa where do SQL
        String[] valorParaSubstituir = {String.valueOf(medico.getId())};
        //O objeto dataBase executa a query de atualização do banco de acordo com a "id"
        database.update(NOME_TABELA, values, COLUNA_ID + " = ?", valorParaSubstituir);

    }

    public void excluir(Medico medico){
        //Vetor criado para armazanar as informações que substituirão os "?" da clausa where do SQL
        String[] valoresParaSubstituir = {String.valueOf(medico.getId())};
        //O objeto dataBase executa a query excluir a especialidade com a devida "id"
        database.delete(NOME_TABELA, COLUNA_ID + " = ?", valoresParaSubstituir);
    }

    public Medico buscarMedicoPorId(long id){
        //Sql aser executada ao banco
        String sql = "SELECT * FROM " +NOME_TABELA+ " WHERE " +COLUNA_ID+ " = " +id;

        //Cursor que receberá o resultado da consulta
        Cursor cursor = database.rawQuery(sql, null);
        //Verifica se o cursor recebeu informação
        if(cursor != null){
            if(cursor.moveToNext()){
                //Variáveis para a criação do objeto medico recuperado do banco
                long idd = cursor.getLong(cursor.getColumnIndex(COLUNA_ID));
                String nome = cursor.getString(cursor.getColumnIndex(COLUNA_NOME));
                String end = cursor.getString(cursor.getColumnIndex(COLUNA_ENDERECO));
                String tel = cursor.getString(cursor.getColumnIndex(COLUNA_TELEFONE));
                String obs = cursor.getString(cursor.getColumnIndex(COLUNA_ESPECIALIDADE));
                int idEspec = cursor.getInt(cursor.getColumnIndex(COLUNA_ESPECIALIDADE));

                Medico medico = new Medico(idd, nome, end, tel, obs, idEspec);

                return medico;
            }
        }
        return null;
    }
}
