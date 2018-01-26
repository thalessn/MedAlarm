package com.gmail.thales_silva_nascimento.alarmmed.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.TelaAlarme;
import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.controller.AlarmeController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.HistoricoController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.InstanciaAlarmeController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.LembreteCompraController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.Alarme;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarme;
import com.gmail.thales_silva_nascimento.alarmmed.model.LembreteCompra;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ItemAlarmeAdapter extends RecyclerView.Adapter<ItemAlarmeAdapter.MAViewHolder> {

    private List<ItemAlarme> itensAlarme;
    private Context context;
    private HistoricoController historicoController;
    private InstanciaAlarmeController instanciaAlarmeController;
    private AlarmeController alarmeController;
    private LembreteCompraController lembreteCompraController;
    private MedicamentoController medicamentoController;


    public ItemAlarmeAdapter(List<ItemAlarme> itensAlarme, Context context){
        this.itensAlarme = itensAlarme;
        this.context = context;
        this.historicoController = new HistoricoController(context);
        this.instanciaAlarmeController = InstanciaAlarmeController.getInstanciaAlarmeController(context);
        this.alarmeController = new AlarmeController(context);
        this.lembreteCompraController = new LembreteCompraController(context);
        this.medicamentoController = new MedicamentoController(context);
    }

    @Override
    public int getItemCount() {
        return this.itensAlarme !=null ? this.itensAlarme.size() : 0;
    }

    @Override
    public void onBindViewHolder(MAViewHolder holder, final int i) {
        //Pega o medicamento na posição i
        Medicamento med = itensAlarme.get(i).getMed();

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

        holder.tomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ItemAlarme itemAlarmeSalvar = itensAlarme.get(i);
                itensAlarme.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, itensAlarme.size());

                gerenciaStatusItemAlarme(itemAlarmeSalvar, ItemAlarme.STATUS_TOMADO);

                //Verifica se só tem esse medicamento se sim finaliza a atividade
                verificaUltimoItem();

            }
        });

        holder.adiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Botão Adiar, posicao = " +String.valueOf(i), Toast.LENGTH_LONG).show();
                ItemAlarme itemAlarme = itensAlarme.get(i);
                gerenciaStatusItemAlarme(itemAlarme, ItemAlarme.STATUS_ADIOU);
                itensAlarme.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, itensAlarme.size());

                //Verifica se só tem esse medicamento se sim finaliza a atividade
                verificaUltimoItem();
            }
        });

        holder.pular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Botão pular, posicao = " +String.valueOf(i), Toast.LENGTH_LONG).show();
                ItemAlarme itemAlarmeSalvar = itensAlarme.get(i);
                itensAlarme.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, itensAlarme.size());

                gerenciaStatusItemAlarme(itemAlarmeSalvar, ItemAlarme.STATUS_PULOU);

                //Verifica se só tem esse medicamento se sim finaliza a atividade
                verificaUltimoItem();
            }
        });

    }

    @Override
    public MAViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //Infla a view e cria o viewholder
        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_medicamento_tela_alarme, viewGroup, false);

        return new MAViewHolder(view);
    }

    public static class MAViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeMed;
        public TextView horarioProg;
        public CircleImageView foto;
        public Button pular;
        public Button adiar;
        public Button tomar;

        public MAViewHolder(View view){
            super(view);

            nomeMed = (TextView) view.findViewById(R.id.nomeMed);
            horarioProg = (TextView) view.findViewById(R.id.horarioProg);
            foto = (CircleImageView) view.findViewById(R.id.img);
            tomar = (Button) view.findViewById(R.id.btnAlTomar);
            pular = (Button) view.findViewById(R.id.btnAlPular);
            adiar = (Button) view.findViewById(R.id.btnAlAdiar);
        }

    }

    public void tomarTodosMedicamentos(){

        Calendar dataEhora = Calendar.getInstance();
        Log.v("Qtd_Alarme", String.valueOf(itensAlarme.size()));
        for(ItemAlarme itemAlarmeSalvar : itensAlarme){

            //Salva na tabela histórico
            itemAlarmeSalvar.setStatus(ItemAlarme.STATUS_TOMADO);
            itemAlarmeSalvar.setDataAdministrado(Utils.CalendarToStringData(dataEhora));
            itemAlarmeSalvar.setHoraAdministrado(Utils.CalendarToStringHora(dataEhora));
            historicoController.cadastrarHistoricoMedicamento(itemAlarmeSalvar);

            //Busca o alarme com a devida id
            Alarme alarme = alarmeController.buscarAlarmePorId(itemAlarmeSalvar.getIdAlarme());

            //Verifica se necessita registrar uma nova instância desta instância
            instanciaAlarmeController.resgistraProxInstanciaAlarme(context, alarme, itemAlarmeSalvar.getHorario());

            //Exclui a instancia anterior do banco de dados
            instanciaAlarmeController.deletarInstanciaPorDataAlarmeHorario(itemAlarmeSalvar.getDataProgramada(),
                    itemAlarmeSalvar.getIdAlarme(), itemAlarmeSalvar.getHorario().getId());

            //Verifica se é necessário registrar um lebrete de compra para o medicamento
            gerenciarEstoque(itemAlarmeSalvar);

        }
        itensAlarme.clear();
        notifyDataSetChanged();


    }

    public void adiarTodosMedicamentos(){
        long idHorario = -1;
        for(int i = 0; i<itensAlarme.size(); i++){
            if(idHorario != itensAlarme.get(i).getHorario().getId()){
                idHorario = itensAlarme.get(i).getHorario().getId();
                instanciaAlarmeController.adiarInstanciaAlarmePorIdHorario(idHorario);

            }
            itensAlarme.remove(i);
            notifyItemRemoved(i);
            notifyItemRangeChanged(i, itensAlarme.size());
        }
    }

    public void adicionarItemAlarme(ItemAlarme itemAlarme){
        itensAlarme.add(itemAlarme);
        notifyDataSetChanged();
    }

    private void verificaUltimoItem(){
        //Verifica se só tem esse medicamento se sim finaliza a atividade
        if(itensAlarme.size() <= 0){
            Intent intent = new Intent(TelaAlarme.TELA_ENCERRAR);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    private void gerenciaStatusItemAlarme(ItemAlarme itemAlarme, String status){

        Calendar dataEhora = Calendar.getInstance();
        Alarme alarme = null;

        switch (status){
            case ItemAlarme.STATUS_TOMADO:
                //Salva na tabela histórico
                itemAlarme.setStatus(ItemAlarme.STATUS_TOMADO);
                itemAlarme.setDataAdministrado(Utils.CalendarToStringData(dataEhora));
                itemAlarme.setHoraAdministrado(Utils.CalendarToStringHora(dataEhora));
                historicoController.cadastrarHistoricoMedicamento(itemAlarme);

                //Busca o alarme com a devida id
                alarme = alarmeController.buscarAlarmePorId(itemAlarme.getIdAlarme());

                //Verifica se necessita registrar um nova instância desta instância
                instanciaAlarmeController.resgistraProxInstanciaAlarme(context, alarme, itemAlarme.getHorario());

                //Exclui a instancia do banco de dados
                instanciaAlarmeController.deletarInstanciaPorDataAlarmeHorario(itemAlarme.getDataProgramada(),
                        itemAlarme.getIdAlarme(), itemAlarme.getHorario().getId());

                //Verifica se é necessário registrar um lebrete de compra para o medicamento
                gerenciarEstoque(itemAlarme);

                break;
            case ItemAlarme.STATUS_PULOU:
                //Salva na tabela historico
                itemAlarme.setStatus(ItemAlarme.STATUS_PULOU);
                itemAlarme.setDataAdministrado(Utils.CalendarToStringData(dataEhora));
                itemAlarme.setHoraAdministrado(Utils.CalendarToStringHora(dataEhora));
                historicoController.cadastrarHistoricoMedicamento(itemAlarme);

                //Busca o alarme com a devida id
                alarme = alarmeController.buscarAlarmePorId(itemAlarme.getIdAlarme());

                //Verifica se necessita registrar um nova instância desta instância
                instanciaAlarmeController.resgistraProxInstanciaAlarme(context, alarme, itemAlarme.getHorario());

                //Exclui a instancia do banco
                instanciaAlarmeController.deletarInstanciaPorDataAlarmeHorario(itemAlarme.getDataProgramada(),
                        itemAlarme.getIdAlarme(), itemAlarme.getHorario().getId());
                break;
            case ItemAlarme.STATUS_ADIOU:
                instanciaAlarmeController.adiarInstanciaAlarmePorIdHorario(itemAlarme.getHorario().getId());
        }

    }

    private void gerenciarEstoque(ItemAlarme itemAlarme){
        //Procura se existe um lembrete de compra no banco de dados
        LembreteCompra lembreteCompra = lembreteCompraController.buscarLembretePorIDMed(itemAlarme.getMed().getId());
        if(lembreteCompra != null){
            //Atualiza a quantidade de remédios no banco
            int qtdMedAnterior = itemAlarme.getMed().getQuantidade();
            int qtdMedAtual = qtdMedAnterior -1;
            //Validação para não permitir número negativo no estoque
            if(qtdMedAtual < 0) qtdMedAtual = 0;
            medicamentoController.updateQtdMedicamento(itemAlarme.getMed().getId(), qtdMedAtual);

            if(qtdMedAtual <= lembreteCompra.getQtd_alerta()){
                lembreteCompraController.registrarLembreteCompra(lembreteCompra);
            }
        }
    }
}
