package com.gmail.thales_silva_nascimento.alarmmed.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.model.Especialidade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thales on 07/02/2017.
 */

public class EspecialidadeDAO {
    public static final String NOME_TABELA = "especialidade";
    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_NOME = "nome";

    //SQL para a criação da Tabela
    public static final String SCRIPT_CRIACAO_TABELA_ESPECIALIDADE = "CREATE TABLE " + NOME_TABELA
            + "(" + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUNA_NOME + " VARCHAR(50) NOT NULL)";
    //public static final  String SCRIPT_INSERT_INICIAL_ESPECIALIDADES = "INSERT INTO " + NOME_TABELA + " (" + COLUNA_NOME
     //       + ") VALUES ('Acunpuntura'), ('Alergia e Imunologia'), ('Anestesiologia'), ('Angiologia'), ('Cardiologia'), ('Cirurgia Geral'), ('Cirurgia Pediátrica'), ('Cirurgia Plástica'), ('Clínica médica'), ('Coloproctologia')";

    //Funcionaa em versões anteriores a sqlite 3.7.11
    public static  final String SCRIPT_INSERT_INICIAL_ESPECIALIDADES = "INSERT INTO " +NOME_TABELA+ " (" +COLUNA_NOME+ ") " +
            "SELECT 'Acupuntura' UNION ALL SELECT 'Alergia e Imunologia' UNION ALL SELECT 'Anestesiologia' UNION ALL SELECT 'Angiologia' UNION ALL SELECT 'Cardiologia' " +
            "UNION ALL SELECT 'Cirurgia Geral' UNION ALL SELECT 'Cirurgia Pediátrica' UNION ALL SELECT 'Cirurgia Plástica' UNION ALL SELECT 'Clínica médica' UNION ALL SELECT 'Coloproctologia' " ;

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + NOME_TABELA;

    //Variavel que receberá da função BancoHelper a ligação com o banco de dados
    private SQLiteDatabase database = null;

    private static EspecialidadeDAO instance;

    public static EspecialidadeDAO getInstance(Context context){
        if(instance == null){
            instance = new EspecialidadeDAO(context);
        }
        return instance;
    }

    private EspecialidadeDAO (Context context){
        BancoHelper banco = BancoHelper.getInstance(context);
        database = banco.getWritableDatabase();
    }

    public void salvar(Especialidade espec){
        //Para adicionar um valor ao banco você precisa primeiramente armazená-lo
        // em uma variável do tipo ContentValues.
        ContentValues values = new ContentValues();
        //O objeto dessa classe recebe como parâmetro o nome da coluna e o valor a ser armazenado.
        values.put(COLUNA_NOME, espec.getNome());

        //Após adicionar as informações a variável values
        //A variável que possui a conxexão com o banco agora pode inserir.
        //O objeto recebe como parâmetro o nome da tabela onde eu irei inserir e os valores correspondentes
        database.insert(NOME_TABELA, null, values);
    }

    public List<Especialidade> recuperaTodas(){
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA;
        Cursor cursor = database.rawQuery(queryReturnAll, null);
        List<Especialidade> especialidades = constroiEspecialidadePorCursor(cursor);
        return especialidades;
    }

    private List<Especialidade> constroiEspecialidadePorCursor(Cursor cursor){
        List<Especialidade> especialidades = new ArrayList<>();
        if(cursor == null)
            return especialidades;

        try{
            if (cursor.moveToFirst()){
                do{
                    int indexID = cursor.getColumnIndex(COLUNA_ID);
                    int indexNome = cursor.getColumnIndex(COLUNA_NOME);

                    int id = cursor.getInt(indexID);
                    String nome = cursor.getString(indexNome);

                    Especialidade nova = new Especialidade(id, nome);

                    especialidades.add(nova);

                }while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }
        return especialidades;
    }


    public void deletar(Especialidade espec){
        //Vetor criado para armazanar as informações que substituirão os "?" da clausa where do SQL
        String[] valoresParaSubstituir = {String.valueOf(espec.getId())};
        //O objeto dataBase executa a query excluir a especialidade com a devida "id"
        database.delete(NOME_TABELA, COLUNA_ID + " = ?", valoresParaSubstituir);
    }

    public void editar(Especialidade espec){
        //Gera um objeto contentValues já contendo as novas informações
        ContentValues values = gerarContentValuesEspecialidade(espec);
        //Vetor de string utilizado para substituir os "?" da clausa where do SQL
        String[] valorParaSubstituir = {String.valueOf(espec.getId())};
        //O objeto dataBase executa a query de atualização do banco de acordo com a "id"
        database.update(NOME_TABELA, values, COLUNA_ID + " = ?", valorParaSubstituir);
    }

    private ContentValues gerarContentValuesEspecialidade(Especialidade espec){
        ContentValues values = new ContentValues();
        values.put(COLUNA_ID, espec.getId());
        values.put(COLUNA_NOME, espec.getNome());

        return values;
    }

    public String encontrarNomeEspecId(int id){
        String nome = "";
        String sql = "SELECT " + COLUNA_NOME+ " FROM " +NOME_TABELA+ " WHERE " +COLUNA_ID+ " = " + id;
        Cursor cursor = database.rawQuery(sql, null);

        if(cursor == null){
            Log.i("ENCONTRARNOME", "SIM");
            return nome;
        }

        try{
            if(cursor.moveToNext()){
                nome = cursor.getString(cursor.getColumnIndex(COLUNA_NOME));
            }
        }finally{
            cursor.close();
        }

        return nome;
    }
}
