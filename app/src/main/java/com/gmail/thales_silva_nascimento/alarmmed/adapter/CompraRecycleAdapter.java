package com.gmail.thales_silva_nascimento.alarmmed.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class CompraRecycleAdapter extends RecyclerView.Adapter<CompraRecycleAdapter.ViewHolder> {

    private Context context;
    private List<Medicamento> meds;

    private List<Long> selectedIds = new ArrayList<>();


    public CompraRecycleAdapter(Context context, List<Medicamento> meds){
        this.context = context;
        this.meds = meds;
    }


    @Override
    public CompraRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_row_medicamento,parent, false);
        CompraRecycleAdapter.ViewHolder holder = new CompraRecycleAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CompraRecycleAdapter.ViewHolder holder, int position) {
        Medicamento med = meds.get(position);
        holder.nome.setText(med.getNome());

        long id = med.getId();


        //Arquivo que contém o caminho da imagem no armazenamento interno
        File path = context.getFileStreamPath(med.getFoto());
        //Verifica se o caminho existe. Se existir carrea a imagem, se não carregue a imagem padrão que neste caso é o cachorro.
        if(path.exists()){
            //existe
            Glide.with(context).load(path).into(holder.img);

        }else{
            //não existe
            Glide.with(context).load(R.drawable.remedio1).into(holder.img);

        }

        if(selectedIds.contains(id)){
            //if item is selected then,set foreground color of FrameLayout
            holder.rl.setBackgroundColor(Color.BLUE);
        }else{
            //else remove selected item color.
            holder.rl.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    @Override
    public int getItemCount() {
        return this.meds !=null ? this.meds.size() : 0;
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nome;
        private CircleImageView img;
        private RelativeLayout rl;

        public ViewHolder(View view){
            super(view);
            rl = (RelativeLayout) view.findViewById(R.id.rlListRow);
            nome = (TextView) view.findViewById(R.id.nLvMedicamento);
            img = (CircleImageView) view.findViewById(R.id.imLvMedicamento);

        }

    }

    public void setSelectedIds(List<Long> selectedIds) {
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }

    public int getSelectedIds(){
        return selectedIds == null? 0 : selectedIds.size();
    }

    public Medicamento getItem(int position){
        return meds.get(position);
    }

}
