package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicoController;
import com.gmail.thales_silva_nascimento.alarmmed.dao.EspecialidadeDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medico;

import org.w3c.dom.Text;


public class activity_medicoDetalhe extends AppCompatActivity {
    private TextView nome;
    private TextView telefone;
    private TextView endereco;
    private TextView observacao;
    private TextView especialidade;
    private Medico medico;
    private MedicoController medicoController;
    private static final int CODIGO_RESULT_ACTIVITY = 1;
    private TextView texto;
    private LinearLayout llTelMed;
    private LinearLayout llEndMed;
    private LinearLayout llObsMed;
    private View view1, view2;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("medico", medico);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_detalhe);

        //Inicializa a controladora do médico
        medicoController = MedicoController.getInstance(getBaseContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbMedicoDetalhe);
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow_back);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Histórico");

        //Onclick no botão de retonar da toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        nome = (TextView) findViewById(R.id.tbNomeMedicoD);
        telefone = (TextView) findViewById(R.id.telMedicoDetalhe);
        endereco = (TextView) findViewById(R.id.endMedicoDetalhe);
        observacao = (TextView) findViewById(R.id.obsMedicoDetalhe);
        especialidade = (TextView) findViewById(R.id.tbEspecMedicoD);
        texto = (TextView) findViewById(R.id.tvTextoMedicoDetalhe);
        view1 = findViewById(R.id.viewMedDet1);
        view2 = findViewById(R.id.viewMedDet2);

        llObsMed = (LinearLayout) findViewById(R.id.LLobsMedDetalhe);


        llTelMed = (LinearLayout) findViewById(R.id.LLTelMedDetalhe);
        llTelMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+ telefone.getText().toString()));
                startActivity(callIntent);
            }
        });

        llEndMed = (LinearLayout) findViewById(R.id.LLEndMedDetalhe);
        llEndMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Uri para a pesquisa de um endereco
                Uri IntentUri = Uri.parse("geo:0,0?q= " + endereco.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, IntentUri);
                startActivity(mapIntent);

            }
        });

        if(savedInstanceState == null){
            //Recupera as infomações passada da outra activity
            Intent i = getIntent();
            if(i != null){
                //Cria um médico que recebe um medico da activity anterior
                medico =  i.getExtras().getParcelable("medico");
            }
        }else{
            medico = savedInstanceState.getParcelable("medico");
            Log.i("savedInstanceState else", "entrou");
        }


        //Preenche os TextView com as infomarções do TextView
        preencheTextView(medico);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adiciona um menu a ToolBar(ActonBar)
        getMenuInflater().inflate(R.menu.toolbar_excluir_editar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Metodo que gerencia os itens do menu da Toolbar.
        switch (item.getItemId()) {
            case R.id.miExcluir:
                deletarMedico();
                return true;
            case R.id.miEditar:
                editar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void deletarMedico(){
        //Cria o Dialogo para perguntar ao usuário se ele tem certeza da exclusão
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity_medicoDetalhe.this);
        alertDialog.setTitle("Você tem certeza?");
        alertDialog.setMessage("Você deseja excluir este médico?");
        alertDialog.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Chama a controladora para excluit o médico
                medicoController.excluirMedico(medico);
                //Cria uma intent para retorna o resultado se deve atualizar a lista de médicos
                Intent i = new Intent();
                //Retorna o resultado a activity pai
                setResult(1, i);
                //Fecha a activity atual
                finish();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();

    }


    private void editar(){
        //Chama a tela de edicao
        Intent intent = new Intent(activity_medicoDetalhe.this, activity_medicoCadastro.class);
        intent.putExtra("medico", medico);
        intent.putExtra("tipoTela", 2);
        startActivityForResult(intent, CODIGO_RESULT_ACTIVITY);
    }


    @Override
    protected void onActivityResult(int codigo, int resultado, Intent data) {
        super.onActivityResult(codigo, resultado, data);
        if(codigo == CODIGO_RESULT_ACTIVITY){
            if (data != null){
                if(resultado == 1){
                    //Atualiza os detalhes do médico
                    atualizaMedico();
                }
            }
        }
    }

    private void preencheTextView(Medico medico){
        //Preenche os TextViews com as infomarções do médico
        nome.setText(medico.getNome());
        if(!(medico.getTelefone().isEmpty() && medico.getEndereco().isEmpty()&& medico.getObservacao().isEmpty())){
            texto.setVisibility(View.GONE);
        }
        if(medico.getTelefone().isEmpty()){
            llTelMed.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }
        if(medico.getEndereco().isEmpty()){
            llEndMed.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }
        if(medico.getObservacao().isEmpty()){
            llObsMed.setVisibility(View.GONE);
        }
        telefone.setText(medico.getTelefone());
        endereco.setText(medico.getEndereco());
        observacao.setText(medico.getObservacao());

        //Controladora para buscar o nome da especialidade pelo id
        EspecialidadeDAO especDAO = EspecialidadeDAO.getInstance(getBaseContext());

        //String que recebe o nome do banco.
        String espec = especDAO.encontrarNomeEspecId(medico.getIdEspec()+1);

        //Adiciona a string especialidade no textview
        especialidade.setText(espec);
    }

    private void atualizaMedico(){
        medico = medicoController.buscarMedicoPorId(medico.getId());
        preencheTextView(medico);



    }
}
