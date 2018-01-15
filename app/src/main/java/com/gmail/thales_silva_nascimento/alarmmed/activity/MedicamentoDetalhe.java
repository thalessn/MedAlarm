package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.controller.AlarmeController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.HistoricoController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.LembreteCompraController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.reabastecerRemedio;
import com.gmail.thales_silva_nascimento.alarmmed.model.Alarme;
import com.gmail.thales_silva_nascimento.alarmmed.model.AlarmeInfo;
import com.gmail.thales_silva_nascimento.alarmmed.model.LembreteCompra;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedicamentoDetalhe extends AppCompatActivity implements reabastecerRemedio.reabastecerRemedioListener {

    private Medicamento medicamento;
    private CircleImageView img;
    private TextView nomeMed;
    private HistoricoController historicoController;
    private AlarmeController alarmeController;
    private MedicamentoController medicamentoController;
    private TextView freqText;
    private LinearLayout llHorariosMed;
    private Alarme alarme;
    private static final int CODIGO_RESULT_ACTIVITY = 1;
    private LembreteCompraController lembreteCompraController;
    private TextView tvDetalheLembrete;
    private TextView tvQtdLembrete;
    private LembreteCompra lembreteCompra;
    private Button btnReabastecer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamento_detalhe);

        img = (CircleImageView) findViewById(R.id.imgMedDetalhe);
        nomeMed = (TextView) findViewById(R.id.nomeMedDetalhe);
        freqText = (TextView) findViewById(R.id.freqTextMedDetalhe);
        llHorariosMed = (LinearLayout) findViewById(R.id.llHorariosMed);

        //Instancia as controladoras
        historicoController = new HistoricoController(MedicamentoDetalhe.this);
        alarmeController = new AlarmeController(MedicamentoDetalhe.this);
        medicamentoController = new MedicamentoController(MedicamentoDetalhe.this);
        lembreteCompraController = new LembreteCompraController(MedicamentoDetalhe.this);
        btnReabastecer = (Button) findViewById(R.id.btnReabastecer);

        btnReabastecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Contrói um novo dialog fragNumeroDias
                DialogFragment dialog = new reabastecerRemedio();
                //Envia informação para o dialog
                Bundle arg = new Bundle();
                int qtdRemedio = medicamento.getQuantidade();
                arg.putLong("idMedicamento", medicamento.getId());
                dialog.setArguments(arg);
                //Mostra o dialogo
                dialog.show(getSupportFragmentManager(), "reabastecerRemedio");
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.tbMedicamentoDetalhe);
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Onclick no botão de retonar da toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cria um intent para retorna o valor
                Intent i = new Intent();
                //Adiciona o resultado a ser comparado e a intent
                setResult(1, i);
                finish();
            }
        });


        if(savedInstanceState == null){
            //Recupera as infomações passada da outra activity
            Intent i = getIntent();
            if(i != null){
                //Cria um médico que recebe um medico da activity anterior
                long idMedicamento =  i.getExtras().getLong("idMedicamento");
                medicamento = medicamentoController.buscarMedicamentoPorId(idMedicamento);
            }
        }else{
            medicamento = savedInstanceState.getParcelable("medicamento");
            Log.v("savedInstanceState else", "entrou");
        }

        //Carrega a image do medicamento
        loadImagem(medicamento);

        //Inclui o nome do medicamento
        nomeMed.setText(medicamento.getNome());

        //Carrega os horários no Linearlayout
        alarme = alarmeController.buscarAlarmePorIdMed(medicamento.getId());
        if(alarme != null){
            preencherHorarios(alarme.getAlarmeInfo());
        }

        //Atribui o texto da frequencia acima do linear layout
        String freqTexto = "Frequência: " +alarme.getFreqDias()+ ", "+ alarme.getFreqHorario().toLowerCase();
        freqText.setText(freqTexto);

        //Atribui  o texto sobre o lebrete de compra
        tvDetalheLembrete = (TextView) findViewById(R.id.tvDetalheLembrete);
        tvQtdLembrete = (TextView) findViewById(R.id.tvDetalheQtdLembrete);

        if((medicamento.getQuantidade() != -1)){
            //Texto Comprimidos restantes
            String qtdEstq = String.valueOf(medicamento.getQuantidade());
            String  texto = null;
            if(medicamento.getQuantidade() < 2) texto = qtdEstq + " comprimido restante";
            else texto = qtdEstq + " comprimidos restantes";
            tvDetalheLembrete.setText(texto);
        }

        lembreteCompra = lembreteCompraController.buscarLembretePorIDMed(medicamento.getId());
        if(lembreteCompra != null){
            String qtdAlerta = String.valueOf(lembreteCompra.getQtd_alerta());
            String texto2 = "Lembrete de compra: Quando restarem " +qtdAlerta+ " remédios";
            tvQtdLembrete.setText(texto2);
        }
    }

    private void loadImagem(Medicamento med){
        //Arquivo que contém o caminho da imagem no armazenamento interno
        File path = MedicamentoDetalhe.this.getFileStreamPath(med.getFoto());
        //Verifica se o caminho existe. Se existir carrega a imagem, se não, carregue a imagem padrão.
        if(path.exists()){
            //existe
            Glide.with(MedicamentoDetalhe.this).load(path).into(img);
            Log.v("Img Existe","Conseguiu ler");
        }else{
            //não existe
            Glide.with(MedicamentoDetalhe.this).load(R.drawable.remedio1).into(img);
            Log.v("Img NãoExiste","Não conseguiu ler");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("medicamento", medicamento);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adiciona um menu a ToolBar(ActonBar)
        getMenuInflater().inflate(R.menu.toolbar_excluir_editar, menu);
        return true;
    }


    /**
     * Responsável pelos eventos dos botões editar e exxcluir da toolbar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Metodo que gerencia os itens do menu da Toolbar.
        switch (item.getItemId()) {
            case R.id.miExcluir:
                //Exibe o diálogo de confirmação de exclusão
                dialogoDeletarMedicamento();
                return true;
            case R.id.miEditar:
                //Edita o medicamento
                editarMedicamento();
                Toast.makeText(MedicamentoDetalhe.this, "Editar", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void deletarMedicamento(){

        final ProgressDialog dialog = new ProgressDialog(MedicamentoDetalhe.this);

        //Cria uma nova Thread para executar as funções no banco
        AsyncTask<Void,Void,Void> deletarTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Excluindo Medicamento");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                //Exclui todas as informações deste medicamento do histórico
                historicoController.deletarHistoricoMedicamento(medicamento.getId());
                //Busca a id do Alarme associado a esse medicamento e deleta o alarme do banco
                long idAlarme = alarmeController.buscarIdAlarmePorMedID(medicamento.getId());
                alarmeController.deletarAlarme(idAlarme);
                //Exclui o lembrete de compra associado a esse remédio
                lembreteCompraController.excluirLembreteCompraPorIdMed(medicamento.getId());
                //Exclui o medicamento do banco
                medicamentoController.excluirMedicamento(medicamento, MedicamentoDetalhe.this);

                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(dialog != null){
                    dialog.dismiss();
                }

                //Cria uma intent para retornar o resultado se deve atualizar a lista de medicamentos
                Intent i = new Intent();
                //Retorna o resultado a activity pai
                setResult(1, i);
                //Fecha a activity atual
                finish();
            }
        };

        deletarTask.execute();

    }

    private void dialogoDeletarMedicamento(){

        //Cria o Dialogo para perguntar ao usuário se ele tem certeza da exclusão
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MedicamentoDetalhe.this);
        alertDialog.setTitle("Você tem certeza?");
        alertDialog.setMessage("Você deseja excluir este remédio?");
        alertDialog.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Executa a Thread de exclusão
                deletarMedicamento();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }



    private void preencherHorarios(List<AlarmeInfo> alarmeInfoList){
        //Remove todas as view anteriores
        llHorariosMed.removeAllViews();

        for (int i = 0; i < alarmeInfoList.size(); i++) {
            TextView t = new TextView(MedicamentoDetalhe.this);
            //Adiciona a Tag ao textview
            t.setTag("TextView" + String.valueOf(i));
            //Tamanho do Texto pela varialvel "sp" (TypeValue.Complex_unit_SP)
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            //Atribui o texto
            t.setText(alarmeInfoList.get(i).getHorario());
            //Adiciona a cor preta
            t.setTextColor(ContextCompat.getColor(MedicamentoDetalhe.this, R.color.preto));
            //Adiciona o TextView no LinearLayout
            llHorariosMed.addView(t);
        }

    }

    private void editarMedicamento(){
        Intent i = new Intent(MedicamentoDetalhe.this, MedicamentoCadastro.class);
        i.putExtra("medicamento", medicamento);
        i.putExtra("alarme", alarme);
        i.putExtra("tipoTela", MedicamentoCadastro.TELA_EDITAR_MED);
        i.putExtra("lembreteCompra", lembreteCompra);
        startActivityForResult(i, CODIGO_RESULT_ACTIVITY);
    }

    private void atualizaInfoRemedio(){

        Medicamento medAtualizado = medicamentoController.buscarMedicamentoPorId(medicamento.getId());
        medicamento = medAtualizado;

        //Carrega a image do medicamento
        loadImagem(medicamento);
        //Preenche o nome do Medicamento
        nomeMed.setText(medicamento.getNome());
        //Carrega os horários no Linearlayout
        alarme = alarmeController.buscarAlarmePorIdMed(medicamento.getId());
        if(alarme != null){
            preencherHorarios(alarme.getAlarmeInfo());
        }

        //Atribui o texto da frequencia acima do linear layout
        String freqTexto = "Frequência: " +alarme.getFreqDias()+ ", "+ alarme.getFreqHorario().toLowerCase();
        freqText.setText(freqTexto);

        //Atribui o texto do Lembrete
        if((medicamento.getQuantidade() != -1)){
            //Texto Comprimidos restantes
            String qtdEstq = String.valueOf(medicamento.getQuantidade());
            String  texto = null;
            if(medicamento.getQuantidade() < 2) texto = qtdEstq + " comprimido restante";
            else texto = qtdEstq + " comprimidos restantes";
            tvDetalheLembrete.setText(texto);
        }

        lembreteCompra = lembreteCompraController.buscarLembretePorIDMed(medicamento.getId());
        if(lembreteCompra != null){
            String qtdAlerta = String.valueOf(lembreteCompra.getQtd_alerta());
            String texto2 = "Lembrete de compra: Quando restarem " +qtdAlerta+ " remédios";
            tvQtdLembrete.setText(texto2);
        }else{
            tvQtdLembrete.setText("");
        }
    }

    @Override
    protected void onActivityResult(int codigo, int resultado, Intent intent) {
        super.onActivityResult(codigo, resultado, intent);
        if(codigo == CODIGO_RESULT_ACTIVITY){
            if(intent != null){
                if(resultado == 1){
                    atualizaInfoRemedio();
                }
            }
        }

    }

    @Override
    public void onClickListenerPositivoReabastecerRemedio(DialogFragment dialog, int qtd) {
        String  texto = null;
        medicamento.setQuantidade(qtd);
        if(qtd >= 0){
            if(medicamento.getQuantidade() < 2) texto = qtd + " comprimido restante";
            else texto = qtd + " comprimidos restantes";
            tvDetalheLembrete.setText(texto);
        }

        dialog.dismiss();

    }

    @Override
    public void onClickListenerNegativoReabastecerRemedio(DialogFragment dialog) {
        dialog.dismiss();
    }
}
