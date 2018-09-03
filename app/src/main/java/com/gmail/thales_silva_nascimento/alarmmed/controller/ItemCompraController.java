package com.gmail.thales_silva_nascimento.alarmmed.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.dao.BancoHelper;
import com.gmail.thales_silva_nascimento.alarmmed.dao.LembreteCompraDAO;
import com.gmail.thales_silva_nascimento.alarmmed.dao.MedicamentoDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemCompra;
import com.gmail.thales_silva_nascimento.alarmmed.model.LembreteCompra;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thales on 31/03/2018.
 */

public class ItemCompraController {

    private Context context;
    private BancoHelper banco;
    private List<ItemCompra> itenscompra;

    public ItemCompraController (Context context){
        this.context = context;
        this.itenscompra = new ArrayList<>();
        this.banco =  BancoHelper.getInstance(context);
    }

    public List<ItemCompra> listarMedicamentosComprar(){
        SQLiteDatabase database = banco.getWritableDatabase();

        String sql = "select "+ MedicamentoDAO.NOME_TABELA+".*, "+LembreteCompraDAO.NOME_TABELA+".*, "+
                "SUM("+MedicamentoDAO.COLUNA_QTD+" - "+LembreteCompraDAO.COLUNA_QTD_ALERTA+") AS Estoque"+
                " from "+MedicamentoDAO.NOME_TABELA+" inner join "+LembreteCompraDAO.NOME_TABELA+" on "+
                MedicamentoDAO.NOME_TABELA+"."+MedicamentoDAO.COLUNA_ID+" = "+LembreteCompraDAO.NOME_TABELA+"."+LembreteCompraDAO.COLUNA_ID_MEDICAMENTO+
                " group by "+MedicamentoDAO.NOME_TABELA+"."+MedicamentoDAO.COLUNA_ID+
                " order by Estoque";

        Cursor cursor = database.rawQuery(sql, null);
        if(cursor == null){
            Log.v("Cursor", "Nulo ");
            return null;
        }
        if(cursor.moveToFirst()){
            try{
                do {
                    //Medicamento
                    long id = cursor.getLong(0);
                    String nome = cursor.getString(cursor.getColumnIndex("nome"));
                    int dosagem = cursor.getInt(cursor.getColumnIndex("dosagem"));
                    int dosagemComprada = cursor.getInt(cursor.getColumnIndex("dosagemComprada"));
                    String tipoDosagem = cursor.getString(cursor.getColumnIndex("tipoDosagem"));
                    String foto = cursor.getString(cursor.getColumnIndex("foto"));
                    //Transforma para boolean
                    boolean usoContinuo = (cursor.getInt(cursor.getColumnIndex("usoContinuo")) == 1);
                    String obs = cursor.getString(cursor.getColumnIndex("observacao"));
                    int quantidade = cursor.getInt(cursor.getColumnIndex("qtd"));
                    Medicamento medicamento = new Medicamento(id, nome, dosagem,tipoDosagem, usoContinuo, obs, foto, quantidade, dosagemComprada);

                    //Lembrete Compra
                    long id2 = cursor.getLong(8);
                    long idMedicamento = cursor.getLong(9);
                    int qtd_alerta = cursor.getInt(cursor.getColumnIndex("qtd_alerta"));
                    String horario_alerta = cursor.getString(cursor.getColumnIndex("horario_alerta"));
                    LembreteCompra lembreteCompra = new LembreteCompra(id2, idMedicamento, qtd_alerta,horario_alerta);

                    //Cria o objeto ItemCompra
                    ItemCompra itemCompra = new ItemCompra(medicamento, lembreteCompra);

                    //Adiciona no arraylist
                    itenscompra.add(itemCompra);

                }while (cursor.moveToNext());
            }finally {
                cursor.close();
            }
        }


        return itenscompra;
    }



}
