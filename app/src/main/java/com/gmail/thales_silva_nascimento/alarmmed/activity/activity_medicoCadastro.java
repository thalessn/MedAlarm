package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicoController;
import com.gmail.thales_silva_nascimento.alarmmed.dao.EspecialidadeDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.Especialidade;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medico;

import java.util.List;

public class activity_medicoCadastro extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    private Spinner spinner;
    private MedicoController medicoController;
    //private EditText nome;
    private TextInputEditText nome;
    private EditText endereco;
    private EditText telefone;
    private EditText observacao;
    private Medico medico;
    private int idEspec;
    private int tipoTela;
    private ImageView imageView;
    private static final int SELECIONAR_CONTATO = 1;
    private static final int PERMISSAO_SELECIONAR_CONTATO = 123;
    private TextInputLayout inputLayoutNome, inputLayoutTelefone, inputLayoutEndereco, inputLayoutObservacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_cadastro);

        //Inicializa idEspec utilizada para informar qual especialidade foi escolhida
        idEspec = 0;

        //Inicializa a controlador do médico
        medicoController = MedicoController.getInstance(getBaseContext());

        //EditText
        nome = (TextInputEditText) findViewById(R.id.medicoNome);
        endereco = (EditText) findViewById(R.id.medicoEndereco);
        telefone = (EditText) findViewById(R.id.medicoTelefone);
        observacao = (EditText) findViewById(R.id.medicoObs);

        //Adiciona o método
        nome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    if (!validateName(nome.getText().toString()))
                        Log.i("FOCUS", String.valueOf(hasFocus));
            }
        });

        //Adiciona ao EditText o listener responsável pela validação dos campos
        //MyTextWatcher recebe como parâmetro um objeto do tipo View, no caso o proprio EditText
        nome.addTextChangedListener(new MyTextWatcher(nome));
        endereco.addTextChangedListener(new MyTextWatcher(endereco));
        telefone.addTextChangedListener(new MyTextWatcher(telefone));

        //Da a funcionalidade da dica (Hint) flutuar sobre EditText e Adição de informação de erros
        inputLayoutNome = (TextInputLayout) findViewById(R.id.inputLayoutNome);
        inputLayoutEndereco = (TextInputLayout) findViewById(R.id.inputLayoutEndereco);
        inputLayoutObservacao = (TextInputLayout) findViewById(R.id.inputLayoutObservacao);

        //Teste da Animacao do Hint
        inputLayoutNome.setHintEnabled(false);
        inputLayoutEndereco.setHintEnabled(false);
        inputLayoutObservacao.setHintEnabled(false);

        //Inicializa a toolbar
        toolbar = (Toolbar) findViewById(R.id.tBMedCad);
        setSupportActionBar(toolbar);

        //Inicializa e preenche o spinner com as especialidades
        setSpinner();

        //Adiciona a funcionalidade de pegar um contato da agenda do celular
        imageView = (ImageView) findViewById(R.id.imContato);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se a API do aplicativo for maior ou igual a 23, necessita pedir permissao
                // para acessar os contatos em tempo de execução.
                if(ContextCompat.checkSelfPermission(activity_medicoCadastro.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED){

                    //Manda uma requisição de permissão
                    //Isto irá mostrar o diálogo padrão de permissão.
                    ActivityCompat.requestPermissions(activity_medicoCadastro.this, new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSAO_SELECIONAR_CONTATO);

                }else{
                    //Api <23 começa automaticamente pois já possui a permissao no manifest.
                    startContactPickActivity();
                }

            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            tipoTela = intent.getExtras().getInt("tipoTela");
            //1 - Se tipo tela for cadastro
            if (tipoTela == 1) {
                getSupportActionBar().setTitle("Cadastrar Medico");

                //Adiciona um icone de navegação na ToolBar
                toolbar.setNavigationIcon(R.drawable.ic_close_white_24px);
                //Adiciona o evento onclickListener no icone
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity_medicoCadastro.this);
                        alertDialog.setTitle("Tem Certeza?");
                        alertDialog.setMessage("Você tem certeza de sair sem salvar o Médico?");
                        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(activity_medicoCadastro.this, "Cancelou", Toast.LENGTH_LONG).show();
                            }
                        });

                        alertDialog.show();
                    }
                });

            } else { //2 - Se o tipo tela for edição de médico
                getSupportActionBar().setTitle("Editar Médico");
                medico = intent.getExtras().getParcelable("medico");
                nome.setText(medico.getNome());
                endereco.setText(medico.getEndereco());
                telefone.setText(medico.getTelefone());
                observacao.setText(medico.getObservacao());

                spinner.setSelection(medico.getIdEspec());

                //Adiciona um icone de navegação na ToolBar
                toolbar.setNavigationIcon(R.drawable.ic_close_white_24px);
                //Adiciona o evento onclickListener no icone
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }

        //Mantém o teclado escondido quando a activity inicia. Mantendo o foco no Editext
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    private void startContactPickActivity(){
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, SELECIONAR_CONTATO);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adiciona uma menu a ToolBar(ActonBar)
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_salvar, menu);
        return true;
    }

    // Metodo que gerencia o menu da Toolbar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iTMedSave:
                if (validateName(nome.getText().toString())) {
                    if (tipoTela == 1) {
                        //Salva o medico com as informações da tela
                        salvarMedico();
                    } else if (tipoTela == 2) {
                        //Salva as novas informações do medico já cadastrado
                        salvarMedicoEditado();
                    }

                    //Função para retornar resultado para activity pai
                    retornaResultadoActivity();
                    //Finaliza a activity
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Métodos para gerenciar o spinner de especialidades

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Passa a position que foi escolhida no spinner para o idEspec
        //Lembradno que no spinner comeca do zero
        idEspec = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void salvarMedico() {
        //Parametros para a criação do médico
        String nNome = nome.getText().toString();
        String nEndereco = endereco.getText().toString();
        String nTelefone = telefone.getText().toString();
        String nObs = observacao.getText().toString();
        int espec = idEspec;

        //Instancia um novo médico
        Medico nMedico = new Medico(nNome, nEndereco, nTelefone, nObs, espec);
        //Salva o novo médico no banco de dados.
        medicoController.salvarMedico(nMedico);
    }

    private void salvarMedicoEditado() {
        medico.setNome(nome.getText().toString());
        medico.setEndereco(endereco.getText().toString());
        medico.setTelefone(telefone.getText().toString());
        medico.setObservacao(observacao.getText().toString());

        medicoController.editarMedico(medico);
    }

    private void retornaResultadoActivity() {
        //Retorna para a activity pai se ela deve atualizar as suas informações
        //Utilizando o startResultActivity
        //Cria um intent para retorna o valor
        Intent i = new Intent();

        //Adiciona o resultado a ser comparado e a intent
        setResult(1, i);
    }

    private void setSpinner() {
        //Declara e atribui i método implementado pela interface OnItemSelectedListener
        spinner = (Spinner) findViewById(R.id.medicoEspSpinner);
        //O método onItemCLickListener foi implemetado utilizando interface AdapterView.OnItemSelectedListener
        // que gera os métodos onItemSelected onNothingSelected.
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        //Classe DAO da Especilidade
        EspecialidadeDAO especDAO = EspecialidadeDAO.getInstance(getBaseContext());
        List<Especialidade> categories = especDAO.recuperaTodas();

        // Creating adapter for spinner
        ArrayAdapter<Especialidade> dataAdapter = new ArrayAdapter<Especialidade>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    protected void onActivityResult(int codigo, int resultado, Intent data) {
        super.onActivityResult(codigo, resultado, data);
        String id = null;
        String contactNumber = null;
        String name = null;
        if (data == null)
            return;

        if (codigo == SELECIONAR_CONTATO) {
            if (resultado == RESULT_OK) {

                try {
                    //Uri do contato selecionado
                    Uri uri = data.getData();
                    //Cursor para recuperar a id do contato escolhido
                    //query(uri do contato, e o que desejo desse contato, null,null,null)
                    Cursor cursorId = getContentResolver().query(uri, new String[]{ContactsContract.Contacts._ID}, null, null, null);
                    if (cursorId.moveToFirst()) {
                        //Pega a id do contato escolhido
                        id = cursorId.getString(cursorId.getColumnIndex(ContactsContract.Contacts._ID));
                    }
                    //Fecha o cursor da busca pela Id.
                    cursorId.close();

                    //Cursor para buscar os dados do contato com a id do usuário selecionado
                    //query(uri para acessar o banco de dados dos contatos, os parâmetro que devem ser retornados do banco no caso Número e Nome, clásula where - neste caso o telefone que possui a Id ? e telefone tipo Mobile,
                    // parâmetros que substituiem os ? da clásula 'Where')
                    Cursor contactData = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.DISPLAY_NAME},
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ", new String[]{id}, null);

                    //If para verificar se foi retornado as informações
                    if (contactData.moveToFirst()) {
                        //Preenche as Strings com os dados requisitados
                        contactNumber = contactData.getString(contactData.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        name = contactData.getString(contactData.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        //Adiciona no textView
                        nome.setText(name);
                        telefone.setText(contactNumber);

                        Log.i("ContatoInfo - ", name + " = " + contactNumber);
                    }
                    //Fecha o cursor
                    contactData.close();
                } catch (Exception e) {
                    //Exceção do porque nãop consegui acessar
                    e.printStackTrace();
                }
            }
        }
    }

    //Classe Criada para validação dos campos Nome, Telefone, Endereço, Observacao
    //Depois pensar se não será melhor implementar uma geral que atenda qualquer formulário.
    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (view.getId() == R.id.medicoNome)
                inputLayoutNome.setErrorEnabled(false);
            inputLayoutNome.setHintEnabled(true);

            if (view.getId() == R.id.medicoEndereco)
                inputLayoutEndereco.setHintEnabled(true);

            if (view.getId() == R.id.medicoObs)
                inputLayoutObservacao.setHintEnabled(true);

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.medicoNome:
                    if (nome.getText().toString().trim().isEmpty()) {
                        inputLayoutNome.setHintEnabled(false);
                    }

                    //validateName();
                    break;
                case R.id.medicoEndereco:
                    if (endereco.getText().toString().trim().isEmpty())
                        inputLayoutEndereco.setHintEnabled(false);

                    //validateEmail();
                    break;
                case R.id.medicoObs:
                    if (observacao.getText().toString().trim().isEmpty())
                        inputLayoutObservacao.setHintEnabled(false);

                    //validatePassword();
                    break;
            }
        }
    }

    private boolean validateName(String name) {
        if (name.trim().isEmpty() || (!name.matches("^[\\p{L} .'-]+$"))) {
            inputLayoutNome.setError("Nome Inválido");
            return false;
        }
        return true;
    }

    //Callback que informa se o usuário deu a permissão para acessar os contatos
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case PERMISSAO_SELECIONAR_CONTATO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    startContactPickActivity();

                } else {

                    // permission denied! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }


    }
}
