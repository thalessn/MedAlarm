package com.gmail.thales_silva_nascimento.alarmmed.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemCompra;
import com.gmail.thales_silva_nascimento.alarmmed.model.LembreteCompra;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class CompraRecycleAdapter extends RecyclerView.Adapter<CompraRecycleAdapter.ViewHolder> {

    private Context context;
    private List<ItemCompra> itenscompra;

    private List<Long> selectedIds = new ArrayList<>();


    public CompraRecycleAdapter(Context context, List<ItemCompra> itenscompra){
        this.context = context;
        this.itenscompra = itenscompra;
    }


    @Override
    public CompraRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_compra_fragment,parent, false);
        CompraRecycleAdapter.ViewHolder holder = new CompraRecycleAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CompraRecycleAdapter.ViewHolder holder, int position) {
        Medicamento med = itenscompra.get(position).getMedicamento();
        LembreteCompra lembreteCompra = itenscompra.get(position).getLembreteCompra();
        //Insere o nome do medicamento
        holder.nome.setText(med.getNome());
        //Insere o text quantidade em estoque
        holder.qtdEstq.setText("Qtd. Estq: "+String.valueOf(med.getQuantidade()));
        //Insere a quantidade para alerta.
        holder.qtdAlerta.setText("Qtd. Alerta: "+String.valueOf(lembreteCompra.getQtd_alerta()));
        //Medicamento id
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
            //holder.rl.setBackgroundColor(Color.parseColor("#c5e9ff"));
            holder.rl.setBackgroundColor(Color.parseColor("#9ea2a8"));
        }else{
            //else remove selected item color.
            holder.rl.setBackgroundColor(selecionaCor(itenscompra.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return this.itenscompra !=null ? this.itenscompra.size() : 0;
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nome;
        private CircleImageView img;
        private RelativeLayout rl;
        private TextView qtdEstq;
        private TextView qtdAlerta;

        public ViewHolder(View view){
            super(view);
            rl = (RelativeLayout) view.findViewById(R.id.rlCOmpraRow);
            nome = (TextView) view.findViewById(R.id.nCompraMed);
            img = (CircleImageView) view.findViewById(R.id.imCompraMed);
            qtdEstq = (TextView) view.findViewById(R.id.qtdEstCompra);
            qtdAlerta = (TextView) view.findViewById(R.id.qtdAlertaCompra);
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
        return itenscompra.get(position).getMedicamento();
    }

    public int selecionaCor(ItemCompra item){
        int qtd_alerta = item.getLembreteCompra().getQtd_alerta();
        int dobro_qtd = 2*qtd_alerta;
        int metademais1 = ((qtd_alerta/2)+1);
        int diffEstq = (item.getMedicamento().getQuantidade() - qtd_alerta);

        if(diffEstq > 0){
            if(diffEstq < metademais1){
                //cor amarelo
                return Color.YELLOW;
            }else{
                //cor verde
                return Color.GREEN;
            }
        }else{
            //Cor vermlho menor ou igual ao alerta.
            return Color.RED;
        }
    }

}
