package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by thales on 08/11/17.
 */

public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.ViewHolder> {

    private List<Medicamento> medicamentos;
    private Context context;

    //Objeto da interface a ser utilizado no adapter
    private OnItemClickListener clickListener;
    //Interface responsável por passar informação a activity de qual elemento do adapter foi selecionado.
    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

    //Construtor
    public MedicamentoAdapter(List<Medicamento> medicamentos, Context context){
        this.context = context;
        this.medicamentos = medicamentos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nome;
        private CircleImageView img;

        public ViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.nLvMedicamento);
            img = (CircleImageView) view.findViewById(R.id.imLvMedicamento);

            //Implementao método OnClick na view que contém o nome e a imagem do objeto
            view.setOnClickListener(this);
        }
        //Passa a view e a posição do adapter para ser utilizado no fragRemedio que impleta a interface desta classe
        @Override
        public void onClick(View view) {
            if(clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    //Método responsável por ligar o objeto da interface OnItemClickListener do FragRemedio ao objeto do adapter
    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public MedicamentoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_row_medicamento,parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MedicamentoAdapter.ViewHolder holder, int position) {
        Medicamento med = medicamentos.get(position);
        holder.nome.setText(med.getNome());

        //Arquivo que contém o caminho da imagem no armazenamento interno
        File path = context.getFileStreamPath(med.getFoto());
        //Verifica se o caminho existe. Se existir carrea a imagem, se não carregue a imagem padrão que neste caso é o cachorro.
        if(path.exists()){
            //existe
            Glide.with(context).load(path).into(holder.img);
            Log.v("Img Existe","COnseguiu ler");
        }else{
            //não existe
            Glide.with(context).load(R.drawable.remedio1).into(holder.img);
            Log.v("Img NãoExiste","Não conseguiu ler");
        }

    }

    @Override
    public int getItemCount() {
        return this.medicamentos !=null ? this.medicamentos.size() : 0;
    }

    public void updateAdapter(List<Medicamento> medicamentos){
        this.medicamentos.clear();
        this.medicamentos = medicamentos;
        notifyDataSetChanged();
    }
}
