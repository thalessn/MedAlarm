package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Thales on 28/01/2018.
 */

public class medicamentosAgendadosAdapter extends RecyclerView.Adapter<medicamentosAgendadosAdapter.MAViewHolder> {


    private Context context;

    public  medicamentosAgendadosAdapter (Context context){
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    @Override
    public void onBindViewHolder(MAViewHolder holder, int position) {
        //Pega o objeto da lista e preenche o holder com as informações do objeto.

        /*Medicamento med = itensAlarme.get(i).getMed();

        holder.nomeMed.setText(med.getNome());
        String hProg = context.getString(R.string.horarioProg)+ " " + itensAlarme.get(i).getHorario().getHorario();
        holder.horarioProg.setText(hProg);


        //Arquivo que contém o caminho da imagem no armazenamento interno
        File path = context.getFileStreamPath(med.getFoto());
        //Verifica se o caminho existe. Se existir carrega a imagem, se não carregue a imagem padrão que neste caso é o remédio.
        if(path.exists()){
            //existe
            Glide.with(context).load(path).into(holder.foto);
            Log.v("Img Existe","Conseguiu ler");
        }else{
            //não existe
            Glide.with(context).load(R.drawable.remedio1).into(holder.foto);
            Log.v("Img NãoExiste","Não conseguiu ler");
        }
        */
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
