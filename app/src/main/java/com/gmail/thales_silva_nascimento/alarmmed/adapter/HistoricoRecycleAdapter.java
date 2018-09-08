package com.gmail.thales_silva_nascimento.alarmmed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gmail.thales_silva_nascimento.alarmmed.model.HeaderHistoricoRow;
import com.gmail.thales_silva_nascimento.alarmmed.ListItemHistorico;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarmeHistorico;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class HistoricoRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<ListItemHistorico> dadosHistorico;
    private Context context;

    //Objeto da interface ser utilizado no adapter
    private OnItemClickListener clickListener;

    //Interface responsável por passar informação a activity de qual elemento do adapter foi selecionado.
    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

    public HistoricoRecycleAdapter(Context context, List<ListItemHistorico> dadosHistorico){
        this.dadosHistorico = dadosHistorico;
        this.context = context;
    }

    //Método responsável por ligar o objeto da interface OnItemClickListener do FragRemedio ao objeto do adapter
    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return dadosHistorico.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return dadosHistorico != null ? dadosHistorico.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case ListItemHistorico.TYPE_GENERAL:
                View v1 = inflater.inflate(R.layout.item_row_historico, parent,
                        false);
                viewHolder = new ItemViewHolder(v1);
                break;

            case ListItemHistorico.TYPE_HEADER:
                View v2 = inflater.inflate(R.layout.item_header_row_historico, parent, false);
                viewHolder = new HeaderViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {

            case ListItemHistorico.TYPE_GENERAL:
                ItemAlarmeHistorico generalItem  = (ItemAlarmeHistorico) dadosHistorico.get(position);
                ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

                // Populate general item data here
                itemViewHolder.nomeMed.setText(generalItem.getMed().getNome());
                itemViewHolder.horaProg.setText(generalItem.getHorario().getHorario());


                //Arquivo que contém o caminho da imagem no armazenamento interno
                File path = context.getFileStreamPath(generalItem.getMed().getFoto());
                //Verifica se o caminho existe. Se existir carrega a imagem, se não carregue a imagem padrão que neste caso é o remédio.
                if(path.exists()){
                    //existe
                    Glide.with(context).load(path).into(itemViewHolder.img);
                }else{
                    //não existe
                    Glide.with(context).load(R.drawable.remedio1).into(itemViewHolder.img);
                }

                break;

            case ListItemHistorico.TYPE_HEADER:
                Log.v("Position", String.valueOf(position));
                HeaderHistoricoRow dateItem = (HeaderHistoricoRow) dadosHistorico.get(position);
                HeaderViewHolder dataProgViewHolder = (HeaderViewHolder) viewHolder;

                //Verifica se o conteúdo é maior que 5 caracteres se não, a data é um traço devido ao
                //OnBootReceiver
                if(dateItem.getStringDataProg().length() > 5){
                    //Calendário para verificar se a data do historico é igual a de hoje
                    Calendar hoje = Calendar.getInstance();
                    String dataH = Utils.CalendarToStringData(hoje);
                    //Retira um dia para verificar se é igual a ontem
                    hoje.add(Calendar.DATE,-1);
                    String dataOntem = Utils.CalendarToStringData(hoje);
                    //Verifica s é igual a hoje
                    if(dataH.equals(dateItem.toString())){
                        SimpleDateFormat dia = new SimpleDateFormat("d");
                        SimpleDateFormat mes = new SimpleDateFormat("MMM");
                        String texto = "Hoje, "+ dia.format(hoje.getTime()) +" de "+ mes.format(hoje.getTime());
                        // Populate date item data here
                        dataProgViewHolder.tvDataProg.setText(texto);
                        break;
                    }
                    if(dataOntem.equals(dateItem.toString())){
                        SimpleDateFormat dia = new SimpleDateFormat("d");
                        SimpleDateFormat mes = new SimpleDateFormat("MMM");
                        String texto = "Ontem, "+ dia.format(hoje.getTime()) +" de "+ mes.format(hoje.getTime());
                        // Populate date item data here
                        dataProgViewHolder.tvDataProg.setText(texto);
                        break;
                    }

                    String dataFormatada = Utils.CalendarToStringFormatada(dateItem.getDataProg());
                    // Populate date item data here
                    dataProgViewHolder.tvDataProg.setText(dataFormatada);
                }else{
                    dataProgViewHolder.tvDataProg.setText(dateItem.getStringDataProg());
                }
                break;
        }

    }

    // ViewHolder for date row item
    class HeaderViewHolder extends RecyclerView.ViewHolder  {

        private TextView tvDataProg;
        public HeaderViewHolder(View v) {
            super(v);
            //TextView que contém a data
            tvDataProg = (TextView) v.findViewById(R.id.tvDataProgHist);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView img;
        private TextView nomeMed;
        private TextView horaProg;

        public ItemViewHolder(View v) {
            super(v);
            img = (ImageView) v.findViewById(R.id.imgMedHistorico);
            nomeMed = (TextView) v.findViewById(R.id.tvNomeMedHist);
            horaProg = (TextView) v.findViewById(R.id.tvHoraprogHist);
            v.setOnClickListener(this);
        }

        //Sobreescreve o método e verifica de o objeto da interface utilizada pela activity.
        @Override
        public void onClick(View view) {
            if(clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    public void UpdateAdapterList(List<ListItemHistorico> medicamentos){
        dadosHistorico.clear();
        dadosHistorico = medicamentos;
        notifyDataSetChanged();
    }

}
