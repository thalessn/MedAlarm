package com.gmail.thales_silva_nascimento.alarmmed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.model.MedicamentoAgendado;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Thales on 28/01/2018.
 */

public class medicamentosAgendadosAdapter extends RecyclerView.Adapter<medicamentosAgendadosAdapter.MAViewHolder> {


    private Context context;
    private List<MedicamentoAgendado> medsAgendado;

    public  medicamentosAgendadosAdapter (Context context, List<MedicamentoAgendado> medsAagendado){
        this.context = context;
        this.medsAgendado = medsAagendado;
    }

    @Override
    public int getItemCount() {
        return medsAgendado != null ? medsAgendado.size() : 0;
    }


    @Override
    public void onBindViewHolder(MAViewHolder holder, int position) {
        //Pega o objeto da lista e preenche o holder com as informações do objeto.
        MedicamentoAgendado medicamentoAgendado = medsAgendado.get(position);

        //Adiciona o nome do medicamento
        holder.nomeMed.setText(medicamentoAgendado.getMedicamento().getNome());
        //Adiciona o horario
        holder.horarioProg.setText(medicamentoAgendado.getHorario());

        //Arquivo que contém o caminho da imagem no armazenamento interno
        File path = context.getFileStreamPath(medicamentoAgendado.getMedicamento().getFoto());
        Log.v("CaminhoFoto", medicamentoAgendado.getMedicamento().getFoto());
        Log.v("Observacao", medicamentoAgendado.getMedicamento().getObservacao());
        //Verifica se o caminho existe. Se existir carrega a imagem, se não carregue a imagem padrão que neste caso é o remédio.
        if(path.exists()){
            //existe
            Glide.with(context).load(path).into(holder.foto);
            Log.v("Adapter Foto", "Foto existe");
        }else{
            //não existe
            Glide.with(context).load(R.drawable.remedio1).into(holder.foto);
            Log.v("Adapter Foto", "FOTO NÃO EXISTE");

        }

    }

    @Override
    public MAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Infla a view e cria o viewholder
        View view = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_card_info_med_alarme, parent, false);

        return new MAViewHolder(view);
    }


    public static class MAViewHolder extends RecyclerView.ViewHolder {
        //Clasee que representa o cardview e seus atributos
        public TextView nomeMed;
        public TextView horarioProg;
        public CircleImageView foto;


        public MAViewHolder(View view){
            super(view);

            nomeMed = (TextView) view.findViewById(R.id.nomeMedDetalhe);
            horarioProg = (TextView) view.findViewById(R.id.tvHorarioTomar);
            foto = (CircleImageView) view.findViewById(R.id.imgDetalheAlarme);
        }

    }
}
