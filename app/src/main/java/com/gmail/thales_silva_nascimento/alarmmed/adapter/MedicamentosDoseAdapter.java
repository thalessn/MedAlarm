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
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedicamentosDoseAdapter extends RecyclerView.Adapter<MedicamentosDoseAdapter.MAViewHolder> {

    private List<Medicamento> medicamentos;
    private Context context;

    //Objeto da interfacea ser utilizado no adapter
    private OnItemClickListener clickListener;

    //Interface responsável por passar informação a activity de qual elemento do adapter foi selecionado.
    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }


    //Construtor
    public MedicamentosDoseAdapter(Context context, List<Medicamento> medicamentos){
        this.medicamentos = medicamentos;
        this.context = context;
    }

    //Método responsável por ligar o objeto da interface OnItemClickListener do FragRemedio ao objeto do adapter
    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return medicamentos != null ? medicamentos.size() : 0;
    }


    @Override
    public void onBindViewHolder(MedicamentosDoseAdapter.MAViewHolder holder, int position) {
        //Pega o objeto da lista e preenche o holder com as informações do objeto.
        Medicamento medicamento = medicamentos.get(position);

        //Adiciona o nome do medicamento
        holder.nomeMed.setText(medicamento.getNome());

        //Arquivo que contém o caminho da imagem no armazenamento interno
        File path = context.getFileStreamPath(medicamento.getFoto());
        Log.v("CaminhoFoto", medicamento.getFoto());
        Log.v("Observacao", medicamento.getObservacao());
        //Verifica se o caminho existe. Se existir carrega a imagem, se não carregue a imagem padrão que neste caso é o remédio.
        if (path.exists()) {
            //existe
            Glide.with(context).load(path).into(holder.foto);
            Log.v("Adapter Foto", "Foto existe");
        } else {
            //não existe
            Glide.with(context).load(R.drawable.remedio1).into(holder.foto);
            Log.v("Adapter Foto", "FOTO NÃO EXISTE");

        }
    }

    @Override
    public MedicamentosDoseAdapter.MAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Infla a view e cria o viewholder
        View view = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_medicamento_dose_fragment, parent, false);

        return new MedicamentosDoseAdapter.MAViewHolder(view);
    }

    public class MAViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Clasee que representa o cardview e seus atributos
        public TextView nomeMed;
        public CircleImageView foto;

        public MAViewHolder(View view){
            super(view);
            nomeMed = (TextView) view.findViewById(R.id.nomeMedDose);
            foto = (CircleImageView) view.findViewById(R.id.imgMedDose);
            //Associa o método OnClickListener a view
            view.setOnClickListener(this);
        }

        //Sobreescreve o método e verifica de o objeto da interface utilizada pela activity.
        @Override
        public void onClick(View view) {
            if(clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
