package com.gmail.thales_silva_nascimento.alarmmed.activity;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.LinkAddress;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.CameraPreview;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.Weekdays;
import com.gmail.thales_silva_nascimento.alarmmed.controller.AlarmeController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.diasEspecificos;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.diasIntervalos;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.fragNumeroDias;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.fragQtdLembrete;
import com.gmail.thales_silva_nascimento.alarmmed.model.Alarme;
import com.gmail.thales_silva_nascimento.alarmmed.model.AlarmeInfo;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


//Implementei OnItemSelectedListener por causa do spinner
public class MedicamentoCadastro extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, diasEspecificos.diasEspecificosListener,
                    diasIntervalos.diasIntervalosListerner, fragNumeroDias.numeroDiasListerner,
                    fragQtdLembrete.lembreteListerner{

    //Cards
    private CardView cardNome;
    private CardView cardDosagem;
    private CardView cardHorario;
    private CardView cardAgend;
    private CardView cardLembrete;
    private ImageView imgSetaCardNome;
    private ImageView imgSetaCardDosagem;
    private ImageView imgSetaCardHorario;
    private ImageView imgSetaCardAgend;
    private boolean expandirCardNome = false;
    private boolean expandirCardDosagem = false;
    private boolean expandirCardHorario = false;
    private boolean expandirCardAgend = false;

    //Card Lembrete
    private Switch switchLembrete;
    private int qtdLembreteMed;
    private int qtdMed;
    private TextView textoLembrete;

    //Card Horarios
    private Spinner spinnerHorario;
    private List<String> repeticao;
    private ArrayAdapter<String> dataAdapter;
    private ArrayList<TextView> textViewsHorarios;
    private boolean spinnerHourController = true;
    private boolean spinnerIntervalController = true;

    //Card Agendamento
    private RadioGroup radioGroup;
    private RadioButton rbDiaEspecificos;
    private RadioButton rbDiaIntervalos;
    private int rbAnterior;
    private boolean rbCheckDiasEsp;
    private boolean rbCheckDiasInter;

    //Radiobuttons
    private int numeroDias;
    private RadioGroup radioGroupDias;
    private RadioButton rbContinuo;
    private RadioButton rbNumeroDias;
    private int rbAnterior2;
    private boolean rbCheckNumeroDias;
    private TextView dataInicialTv;
    private Spinner dosSpinner;

    //Toolbar
    private Toolbar toolbar;

    //Nome, dosagem
    private EditText nomeMed;
    private EditText dosagemMed;
    private int intevaloSel;
    private ArrayList<String> diasSelecionados;
    private Menu menu;

    //Camera
    private AppBarLayout appBarLayout;
    private FloatingActionButton btnFoto;
    private FloatingActionButton btnCancelCam;
    private FloatingActionButton btnCapturar;
    private CameraPreview cameraPreview;
    private Camera camera;
    FrameLayout framePreview;
    private static final int PERMISSAO_CAMERA = 123;
    private View view;
    private CircleImageView imgMed;
    private Bitmap imageBitmap;
    String photoFile;
    private boolean temFoto = false;

    //Tipo tela
    public static int TELA_CADASTRAR_MED = 1;
    public static int TELA_EDITAR_MED = 2;
    private int tipoTela;
    private Alarme alarmeEdit;
    private boolean telaEditHorario = false;
    private Medicamento medEdit;


   
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("rbCheckDiasEsp", rbCheckDiasEsp);
        outState.putBoolean("rbCheckDiasInter", rbCheckDiasInter);
        outState.putBoolean("rbCheckNumeroDias", rbCheckNumeroDias);
        outState.putInt("rbAnterior", rbAnterior);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.activity_medicamento_cadastro, null);
        setContentView(view);

        //Instacia a controladora do medicamento
        MedicamentoController mc = new MedicamentoController(MedicamentoCadastro.this);

        //ArrayList para guarda os dias da semana escolhidos
        diasSelecionados = new ArrayList<>();

        //Dosagem EditText
        dosagemMed = (EditText) findViewById(R.id.edDosagem);

        //Nome do medicamento e evento para habilitar somente quando possui um nome
        nomeMed = (EditText) findViewById(R.id.edNomeMed);
        nomeMed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    if(menu !=null){
                        menu.findItem(R.id.iTMedSave).setEnabled(true);
                    }

                }else{
                    if(menu !=null){
                        menu.findItem(R.id.iTMedSave).setEnabled(false);
                    }
                }
            }
        });

        //TextView que contém a data do Início do tratamento
        dataInicialTv = (TextView) findViewById(R.id.agDataInicio);
        //Para formatar a data
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String data = sdf.format(Calendar.getInstance().getTime());
        //Inicia a dataInicial do tratamento
        dataInicialTv = (TextView) findViewById(R.id.agDataInicio);
        dataInicialTv.setText(data);
        dataInicialTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(MedicamentoCadastro.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String data = Utils.formataDataBrasil(dayOfMonth, month, year);
                        dataInicialTv.setText(data);

                    }
                }, year, month, day);
                datePicker.show();
            }
        });



        //--------------------------------------
        // RadioButtons
        //--------------------------------------
        if (savedInstanceState == null) {
            Log.i("STRING BOOLEAN CHECK", "ENTROU NULL");
            rbCheckDiasEsp = true;
            rbCheckDiasInter = true;
            rbCheckNumeroDias =true;
        } else {
            Log.i("STRING BOOLEAN CHECK", "ENTROU - NÃO NULL");
            rbCheckDiasEsp = savedInstanceState.getBoolean("rbCheckDiasEsp");
            rbCheckDiasInter = savedInstanceState.getBoolean("rbCheckDiasInter");
            rbCheckNumeroDias = savedInstanceState.getBoolean("rbCheckNumeroDias");
            //Guarda a Id do radioButton antes de rotacionar
            rbAnterior = savedInstanceState.getInt("rbAnterior");
            RadioButton teste = (RadioButton) findViewById(rbAnterior);
            Log.i("savedInstanceState", "RadioButton Anterior " + teste.getText().toString());
        }

        //Inicializa os cards
        setupCardView();

        //Configura o spinner do CardView Horário
        setupSpinnerHorario();

        //Setup dos RadioButtons
        setupRadioGroup();

        //Adiciona a toolbar
        adicionaToobar();

        //Configura as condições da câmera
        setupCamera();

        //Gerencia o tipo de tela ao iniciar
        setupTipoTela();

    }

    private void setupTipoTela(){
        //Define se a tela é de cadastrar ou editar
        Intent intent = getIntent();
        if(intent != null){
            tipoTela = intent.getIntExtra("tipoTela", 1);
            if(tipoTela == MedicamentoCadastro.TELA_EDITAR_MED){
                medEdit = intent.getParcelableExtra("medicamento");
                alarmeEdit = intent.getParcelableExtra("alarme");
                if((alarmeEdit != null) && (medEdit !=null)){
                    nomeMed.setText(medEdit.getNome());

                    String dosagem = medEdit.getDosagem() != -1 ? String.valueOf(medEdit.getDosagem()) : "";
                    dosagemMed.setText(dosagem);

                    spinnerHourController = false;
                    repeticao.clear();
                    String []repeticaoHorario = {"Uma vez ao dia", "Duas vezes ao dia", "3 vezes ao dia", "4 vezes ao dia",
                            "5 vezes ao dia", "6 vezes ao dia", "7 vezes ao dia", "8 vezes ao dia", "9 vezes ao dia",
                            "10 vezes ao dia", "11 vezes ao dia", "12 vezes ao dia","Intervalos","A cada 12 horas",
                            "A cada 8 horas", "A cada 6 horas", "A cada 4 horas", "A cada 3 horas",
                            "A cada 2 horas", "Por hora"};
                    for(int i=0; i<repeticaoHorario.length; i++){
                        repeticao.add(repeticaoHorario[i]);
                    }
                    dataAdapter.notifyDataSetChanged();


                    //Recupera a posição no spinner através do texto
                    int pos = repeticao.indexOf(alarmeEdit.getFreqHorario());
                    Log.v("PosicaoSpinner", String.valueOf(pos));

                    //Permite carregar do alarmeEdit as infomação do horario no alarme ao ativar a posição
                    telaEditHorario = true;
                    spinnerHorario.setSelection(pos);

                    insereTimeTextView(alarmeEdit.getAlarmeInfo());

                    //Insere a data do tratamento
                    dataInicialTv.setText(Utils.formataDataBrasil(Utils.CalendarToStringData(alarmeEdit.getDataInicio())));

                    /**
                     * Colocar em função
                     */
                    int tipoRepeticao = alarmeEdit.getTipoRepeticao();
                    switch (tipoRepeticao){
                        case Alarme.REP_TODO_DIA:
                            break;

                        case Alarme.REP_DIASDASEMANA:
                            rbCheckDiasEsp = false;
                            rbAnterior = R.id.rbDiaEspec;
                            radioGroup.check(R.id.rbDiaEspec);
                            DialogFragment dialog = new diasEspecificos();
                            //Passa como argumento os dias da semana a serem marcados
                            Bundle args = new Bundle();
                            args.putString("dias", alarmeEdit.getFreqDias());
                            dialog.setArguments(args);
                            dialog.show(getSupportFragmentManager(), "diasEspecificos");
                            //Formata o texto para ficar colorido
                            SpannableStringBuilder result = formataTextoDiasEspecificos(alarmeEdit.getFreqDias());
                            rbDiaEspecificos.setText(result);
                            break;

                        case Alarme.REP_DIASINTERVALOS:
                            rbCheckDiasInter = false;
                            rbAnterior = R.id.rbDiaIntervalo;
                            radioGroup.check(R.id.rbDiaIntervalo);
                            DialogFragment d = new diasIntervalos();
                            //Passa como argumento o intervalo de dias
                            String texto = alarmeEdit.getFreqDias();
                            String []split = texto.split(" ");
                            int intervalo = 0;
                            try{
                                intervalo = Integer.parseInt(split[2]);
                            }catch (Exception e){
                                e.toString();
                            }

                            Bundle argss = new Bundle();
                            argss.putInt("intervalo", intervalo);
                            d.setArguments(argss);
                            d.show(getSupportFragmentManager(), "diasIntervalos");
                            SpannableStringBuilder res = formataTextoDiasIntervalos(intervalo);
                            rbDiaIntervalos.setText(res);
                            break;

                        default:
                            break;
                    }
                }
            }
        }
    }

    private void setupSpinnerHorario(){
        //------------------------------------------------------------------------
        // Spinner Horário
        //------------------------------------------------------------------------

        //Arraylist que contém os TextView dos horário criados
        textViewsHorarios = new ArrayList<>();
        //Spinner reponsável pelo horário
        spinnerHorario = (Spinner) findViewById(R.id.posFreqSpinner);
        //Arraylist que contém a lista inicial do spinner
        repeticao = new ArrayList<>();
        repeticao.add("Uma vez ao dia");
        repeticao.add("Duas vezes ao dia");
        repeticao.add("3 vezes ao dia");
        repeticao.add(" ... ");
        repeticao.add("Intervalos");
        repeticao.add("A cada 12 horas");
        repeticao.add("A cada 8 horas");
        repeticao.add("A cada 6 horas");
        repeticao.add(" ... ");

        dataAdapter = new ArrayAdapter<>(MedicamentoCadastro.this, android.R.layout.simple_spinner_item, repeticao);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorario.setAdapter(dataAdapter);
        spinnerHorario.setOnItemSelectedListener(this);

        //------------------------------------------------------------------------
        // Spinner Dosagem
        //------------------------------------------------------------------------
        dosSpinner = (Spinner) findViewById(R.id.dosSpinner);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("mg");
        categories.add("g");
        categories.add("ml");
        categories.add("gotas");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        dosSpinner.setAdapter(dataAdapter);
    }

    private void setupCamera(){
        /**
         *
         * Parte Responsável pela camera
         *
         */
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        final View v = getLayoutInflater().inflate(R.layout.medicamento_foto, null);
        final View n = getLayoutInflater().inflate(R.layout.medicamento_camera_view, null);
        //Deixa a View da camera invisível
        n.setVisibility(View.GONE);
        //Adiciona as duas view no AppBarLayout
        appBarLayout.addView(v);
        appBarLayout.addView(n);

        framePreview = (FrameLayout) findViewById(R.id.frameLayoutCamera);
        imgMed = (CircleImageView) findViewById(R.id.imgMed);

        //FloatingActionButton para tirar a foto
        btnCapturar = (FloatingActionButton) findViewById(R.id.btnCapturarCam);
        btnCapturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });

        //FloatingActionButton que cancela a parte de tirar foto do remédio
        btnCancelCam = (FloatingActionButton) findViewById(R.id.btnCancelCam);
        btnCancelCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Libera a camera e remove da view para evitar o freeze de imagem
                liberaCamera();
            }
        });

        //FloatingActionButton Central que aciona a camera
        btnFoto = (FloatingActionButton) findViewById(R.id.fbFoto);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retiro a view do remédio inicial e coloca a view da camera
                appBarLayout.getChildAt(0).setVisibility(View.GONE);
                appBarLayout.getChildAt(1).setVisibility(View.VISIBLE);

                //Primeiro é necessario desativar o comportamento de se esconder automaticamente
                //Quando o scroll comeca. Depois disso você pode esconder o botão.
                //https://stackoverflow.com/questions/31269958/floatingactionbutton-doesnt-hide
                CoordinatorLayout.LayoutParams cl = (CoordinatorLayout.LayoutParams) btnFoto.getLayoutParams();
                FloatingActionButton.Behavior behavior = (FloatingActionButton.Behavior) cl.getBehavior();
                if (behavior != null) {
                    behavior.setAutoHideEnabled(false);
                }
                btnFoto.hide();

                //Se a API do aplicativo for maior ou igual a 23, necessita pedir permissao
                // para acessar a camera em tempo de execução.

                if(ContextCompat.checkSelfPermission(MedicamentoCadastro.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){

                    //Manda uma requisição de permissão
                    //Isto irá mostrar o diálogo padrão de permissão.
                    ActivityCompat.requestPermissions(MedicamentoCadastro.this, new String[]{Manifest.permission.CAMERA},
                            PERMISSAO_CAMERA);

                }else{
                    //Api <23 começa automaticamente pois já possui a permissao no manifest.
                    iniciaCamera();
                }
            }
        });
    }

    private void setupCardView(){
        //Cards
        cardNome = (CardView) findViewById(R.id.card_view_nome);
        cardDosagem = (CardView) findViewById(R.id.card_view_dosagem);
        cardHorario = (CardView) findViewById(R.id.card_view_horario);
        cardAgend = (CardView) findViewById(R.id.card_view_agendamento);
        cardLembrete = (CardView) findViewById(R.id.card_view_lembrete);

        imgSetaCardNome = (ImageView) findViewById(R.id.cardSetaNome);
        imgSetaCardDosagem = (ImageView) findViewById(R.id.cardSetaDosagem);
        imgSetaCardHorario = (ImageView) findViewById(R.id.cardSetaHorario);
        imgSetaCardAgend = (ImageView) findViewById(R.id.cardSetaAgend);

        //Card Lembrete
        //Qtd de remedido para lembrete
        qtdLembreteMed = 10;
        //Switch cardview Lembrete
        switchLembrete = (Switch) findViewById(R.id.switch1);
        //Textview com o texto
        textoLembrete = (TextView) findViewById(R.id.tvQtdRemedio);
        String texto = "Quando restarem "+ qtdLembreteMed +" remédios";
        textoLembrete.setText(texto);

        textoLembrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                Fragment prev = getSupportFragmentManager().findFragmentByTag("qtdLembrete");
                if (prev != null) {
                    //Faz-se um cast para obter o DialogFragment onde encontra-se o AlertDialog
                    DialogFragment a = ((DialogFragment) prev);
                    //Pega o AlertDialog cotido no DialogFragment
                    AlertDialog b = (AlertDialog) a.getDialog();
                    //Mostra o AlertDialog
                    b.show();
                } else {
                    //Contrói um novo dialog fragNumeroDias
                    DialogFragment dialog = new fragQtdLembrete();
                    //Envia informação para o dialog
                    Bundle arg = new Bundle();
                    arg.putInt("qtdLembreteMed", qtdLembreteMed);
                    dialog.setArguments(arg);
                    //Mostra o dialogo
                    dialog.show(getSupportFragmentManager(), "qtdLembrete");
                }
            }
        });


        //Desabilita o LinearLayout com o conteúdo do Lembrete
        final LinearLayout ll = (LinearLayout) findViewById(R.id.llLembreteHorario);
        ll.setVisibility(View.GONE);

        switchLembrete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ll.setVisibility(View.VISIBLE);
                }else{
                    ll.setVisibility(View.GONE);
                }
            }
        });

        cardNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escondeCardView(v);
            }
        });

        cardDosagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escondeCardView(v);
            }
        });

        cardHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escondeCardView(v);
            }
        });

        cardAgend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escondeCardView(v);
            }
        });

    }


    private void escondeCardView(View view){
        int id = view.getId();

        switch (id){
            case R.id.card_view_nome:
                if(expandirCardNome){
                    expandirCardNome = false;
                    imgSetaCardNome.animate().rotation(0).start();

                    nomeMed.setVisibility(View.VISIBLE);
                }else {
                    expandirCardNome = true;
                    imgSetaCardNome.animate().rotation(-180).start();
                    nomeMed.setVisibility(View.GONE);
                }
                break;
            case R.id.card_view_dosagem:
                if(expandirCardDosagem){
                    //expanda
                    expandirCardDosagem = false;
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlCardDosagem);
                    rl.setVisibility(View.VISIBLE);
                    imgSetaCardDosagem.animate().rotation(0).start();

                }else{
                    expandirCardDosagem =  true;
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlCardDosagem);
                    rl.setVisibility(View.GONE);
                    imgSetaCardDosagem.animate().rotation(-180).start();
                }
                break;
            case R.id.card_view_horario:
                if(expandirCardHorario){
                    expandirCardHorario = false;
                    //Pego a referência do elemento onde irei inserir novas views
                    LinearLayout LL = (LinearLayout) findViewById(R.id.cardHorarioLL2);
                    LL.setVisibility(View.VISIBLE);
                    spinnerHorario.setVisibility(View.VISIBLE);
                    imgSetaCardHorario.animate().rotation(0).start();
                }else{
                    expandirCardHorario = true;
                    //Pego a referência do elemento onde irei inserir novas views
                    LinearLayout LL = (LinearLayout) findViewById(R.id.cardHorarioLL2);
                    LL.setVisibility(View.GONE);
                    spinnerHorario.setVisibility(View.GONE);

                    imgSetaCardHorario.animate().rotation(-180).start();
                }
                break;
            case R.id.card_view_agendamento:
                if(expandirCardAgend){
                    expandirCardAgend = false;
                    LinearLayout ll = (LinearLayout) findViewById(R.id.llCardAgend);
                    ll.setVisibility(View.VISIBLE);
                    imgSetaCardAgend.animate().rotation(0).start();
                }else{
                    expandirCardAgend = true;
                    LinearLayout ll = (LinearLayout) findViewById(R.id.llCardAgend);
                    ll.setVisibility(View.GONE);
                    imgSetaCardAgend.animate().rotation(-180).start();
                }
                break;
            default:
                break;
        }

    }


    /**
     * Verifica se necessita esconder um dialogo ao iniciar,caso a tela seja to tipo TELA_EDIT_MED
     */

    @Override
    protected void onResume() {
        super.onResume();
        if(tipoTela == TELA_EDITAR_MED) {
            int tipoRepeticao = alarmeEdit.getTipoRepeticao();
            switch (tipoRepeticao) {
                case Alarme.REP_DIASDASEMANA:
                    hideDialog("diasEspecificos");
                    break;
                case Alarme.REP_DIASINTERVALOS:
                    hideDialog("diasIntervalos");
                    break;
                default:
                    break;
            }
        }

    }

    //CallBack para capturar a imagem
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {

            final AsyncTask<Void, Void, Bitmap> decodeImageTask = new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... params) {
                    imageBitmap = Utils.decodeSampledBitmap(data, 500, 500);
                    if(imageBitmap == null){
                        Toast.makeText(MedicamentoCadastro.this, "Não foi possível obter a foto", Toast.LENGTH_LONG).show();
                        return null;
                    }
                    //Rotaciona a image em 90 graus
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);

                    return imageBitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    imgMed.setImageBitmap(imageBitmap);
                    //Variavel utilizada para saber se tem que adicionar caminho da foto ao medicamento
                    temFoto = true;
                    liberaCamera();
                }
            };
            decodeImageTask.execute();
        }

    };


    private void liberaCamera(){
        //Retiro a view da camera e reponho a view do remedio normal
        appBarLayout.getChildAt(0).setVisibility(View.VISIBLE);
        appBarLayout.getChildAt(1).setVisibility(View.GONE);

        //Depois de habilitar o botão novamente, é necesário habilitar também a função de
        //se esconder automaticamente "após o scroll view subir"
        btnFoto.show();
        CoordinatorLayout.LayoutParams cl = (CoordinatorLayout.LayoutParams) btnFoto.getLayoutParams();
        FloatingActionButton.Behavior behavior = (FloatingActionButton.Behavior) cl.getBehavior();
        if(behavior != null){
            behavior.setAutoHideEnabled(true);
        }

        if(camera != null){
            //Evita freeze de imagem ao remover
            framePreview.removeViewAt(0);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
    private void iniciaCamera(){
        //Colocar em função
        camera = checkDeviceCamera();
        cameraPreview = new CameraPreview(MedicamentoCadastro.this, camera, view);
        framePreview.addView(cameraPreview,0);
    }
    private Camera checkDeviceCamera(){
        Camera c = null;
        try {
            c = Camera.open(0); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.v("Exception Camera", e.toString());
        }
        if(c == null)Log.v("Camera", "Camera Null");

        return c; // returns null if camera is unavailable
    }


    //Callback que informa se o usuário deu a permissão para acessar a camera
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSAO_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    iniciaCamera();

                } else {

                    liberaCamera();
                }
                return;
            }
        }


    }

    private int retornaTipoRepeticao(){
        int tipo = radioGroup.getCheckedRadioButtonId();
        switch (tipo){
            case R.id.rbTodoDia:
                return Alarme.REP_TODO_DIA;
            case R.id.rbDiaIntervalo:
                return Alarme.REP_DIASINTERVALOS;
            case R.id.rbDiaEspec:
                return Alarme.REP_DIASDASEMANA;
            default:
                return Alarme.REP_TODO_DIA;
        }
    }

    private void adicionaToobar() {
        toolbar = (Toolbar) findViewById(R.id.tBCadMedicamento);
        toolbar.setTitle("Adicionar Medicamento");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MedicamentoCadastro.this);
                alertDialog.setTitle("Tem Certeza?");
                alertDialog.setMessage("Você tem certeza de sair sem salvar o medicamento?");
                alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
        });
    }


    /**
     * Adiciona uma menu a ToolBar(ActonBar)
     * Inflate the menu; this adds items to the action bar if it is present.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_salvar, menu);
        //Desabilita o menu salvar ao criar
        menu.findItem(R.id.iTMedSave).setEnabled(false);
        if(menu != null){
            this.menu = menu;
            if(tipoTela == MedicamentoCadastro.TELA_EDITAR_MED){
                menu.findItem(R.id.iTMedSave).setEnabled(true);
            }
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     *  Metodo que gerencia os itens do menu da Toolbar.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Metodo que gerencia os itens do menu da Toolbar.
        switch (item.getItemId()) {
            case R.id.iTMedSave:
                salvarMedicamento();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        liberaCamera();
        Log.v("OnPause", "Entrou no onPause");
        super.onPause();
    }

    private int setHourFrequencyTextView(int qtd) {
        if (qtd != 0) {
            if (qtd == 1) {
                return 0;
            }
            //Hora Inicial
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 8);
            c.set(Calendar.MINUTE, 0);
            //Hora final
            Calendar d = Calendar.getInstance();
            d.set(Calendar.HOUR_OF_DAY, 23);
            d.set(Calendar.MINUTE, 0);
            //Calcula a quantidade de minutos entre a hora final e a hora inicial em milisegundos
            long td = d.getTimeInMillis() - c.getTimeInMillis();
            //Intervalo de tempo que será acrescentado na hora inicial para dividir o intervalo de tempo (td) de forma igual
            int result = (int) ((((td / (qtd - 1)) / 1000) / 60));
            //Retorna o intervalo
            return result;
        }
        return 0;
    }

    /**
     * Função responsável por inserir os textview salvo no banco no LinearLayout
     * @param alarmeInfos
     */
    private void insereTimeTextView(List<AlarmeInfo> alarmeInfos){
        Log.v("insereTimeView", "entrou");
        //Pego a referência do elemento onde irei inserir novas views
        LinearLayout LL = (LinearLayout) findViewById(R.id.cardHorarioLL2);
        //Crio uma instancia do objeto que recebe o método onclick listener para as Views criadas dinamicamente
        //Desse jeito posso adicionar um "Onclicklistener" somente nas devidas views criadas dinamicamente
        //Neste caso para cada TextView criada abaixo eu adiciono este método
        View.OnClickListener Otm = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int id = v.getId(); //Define final para poder passar como argumento
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);
                final View vClick = v; //Define uma view do tipo final para passar como argumento
                //Cria o TimePicker e já adiciona o evento de click
                TimePickerDialog time = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hora = hourOfDay < 10 ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                        String minuto = minute < 10 ? ("0" + String.valueOf(minute)) : String.valueOf(minute);
                        TextView horario = (TextView) vClick;
                        horario.setText(hora + " : " + minuto);
                    }
                }, hour, min, true);
                time.show();
            }
        };

        int i = 0;
        //Remove todos os componetes já adicionados na view
        LL.removeAllViews();
        //Remove todos os elementos do ArrayList
        textViewsHorarios.clear();

        for(i=0; i<alarmeInfos.size(); i++){
            Log.v("insereTimeView", "Posicao " + String.valueOf(i));
            AlarmeInfo ai = alarmeInfos.get(i);
            TextView t = new TextView(MedicamentoCadastro.this);
            //Adiciona a Tag ao textview
            t.setTag("TextView" + String.valueOf(i));
            //Tamanho do Texto pela varialvel "sp" (TypeValue.Complex_unit_SP)
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            //Adiciona o método OnClickListener
            t.setOnClickListener(Otm);
            //Objeto necessário para adicionar margin ao texto. (Usou-se Linear Layout pois é a view parent da TextView)
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 0);
            lp.gravity = Gravity.CENTER;
            //Adiciona a margin ao textView
            t.setLayoutParams(lp);
            Log.v("insereTimeView", "Antes de inserir");
            //Adiciona o texto
            t.setText(ai.getHorario());
            Log.v("insereTextviewhorario", ai.getHorario());
            //Adiciona o TextView no LinearLayout
            LL.addView(t);
            //Adiciona no ArrayList
            textViewsHorarios.add(t);
            Log.v("altura", String.valueOf(LL.getChildAt(0).getHeight()));
        }



    }


    private void criaTimeTextView(int qtd) {
        //Pego a referência do elemento onde irei inserir novas views
        LinearLayout LL = (LinearLayout) findViewById(R.id.cardHorarioLL2);
        //Crio uma instancia do objeto que recebe o método onclick listener para as Views criadas dinamicamente
        //Desse jeito posso adicionar um "Onclicklistener" somente nas devidas views criadas dinamicamente
        //Neste caso para cada TextView criada abaixo eu adiciono este método
        View.OnClickListener Otm = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int id = v.getId(); //Define final para poder passar como argumento
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);
                final View vClick = v; //Define uma view do tipo final para passar como argumento
                //Cria o TimePicker e já adiciona o evento de click
                TimePickerDialog time = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hora = hourOfDay < 10 ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                        String minuto = minute < 10 ? ("0" + String.valueOf(minute)) : String.valueOf(minute);
                        TextView horario = (TextView) vClick;
                        horario.setText(hora + " : " + minuto);
                    }
                }, hour, min, true);
                time.show();
            }
        };

        int i;
        //Remove todos os componetes já adicionados na view
        LL.removeAllViews();

        //Remove todos os elementos do ArrayList
        textViewsHorarios.clear();

        //Calcula com o parametro qtd o intervalo entre as horas
        int minutosAdd = setHourFrequencyTextView(qtd);
        //Variável que armazena a hora inicial no caso (08:00) para ser utilizada para o incremento do tempo no "for" abaixo
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 8);
        c.set(Calendar.MINUTE, 0);
        String hora;
        String minuto;
        for (i = 0; i < qtd; i++) {
            TextView t = new TextView(MedicamentoCadastro.this);
            //Adiciona a Tag ao textview
            t.setTag("TextView" + String.valueOf(i));
            //Tamanho do Texto pela varialvel "sp" (TypeValue.Complex_unit_SP)
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            //Adiciona o método OnClickListener
            t.setOnClickListener(Otm);
            //Objeto necessário para adicionar margin ao texto. (Usou-se Linear Layout pois é a view parent da TextView)
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 0);
            lp.gravity = Gravity.CENTER;
            //Adiciona a margin ao textView
            t.setLayoutParams(lp);
            //hora e minuto - Variaveis do tipo String que recebem o valor da hora e do  minuto respectivamento do horario de tratamento (c.Calendar)
            hora = (c.get(Calendar.HOUR_OF_DAY)) < 10 ? ("0" + String.valueOf(c.get(Calendar.HOUR_OF_DAY))) : String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            minuto = (c.get(Calendar.MINUTE)) < 10 ? ("0" + String.valueOf(c.get(Calendar.MINUTE))) : String.valueOf(c.get(Calendar.MINUTE));
            //Adiciona o texto
            t.setText(hora + " : " + minuto);
            //Adiciona o TextView no LinearLayout
            LL.addView(t);
            //Adiciona no ArrayList
            textViewsHorarios.add(t);
            //Incrementa o horário do tratamento
            c.add(Calendar.MINUTE, minutosAdd);
        }



    }


    /**
     * Função que monitora quando o usuário clicar no spinner na opção "..." e adicionar mais escolhas.
     * Recebe como parâmetro de entrada a posição da escolha feita no spinner
     *
     * @param id
     */
    private void spinnerControllerOptions(int id) {
        if (id == 3) {
            repeticao.remove(3);
            String[] freq = {"4 vezes ao dia", "5 vezes ao dia", "6 vezes ao dia", "7 vezes ao dia", "8 vezes ao dia", "9 vezes ao dia", "10 vezes ao dia", "11 vezes ao dia", "12 vezes ao dia"};
            int i = 0;
            int b;
            for (b = 0; b < freq.length; b++) {
                dataAdapter.insert(freq[b], (id + i));
                i += 1;
            }
            dataAdapter.notifyDataSetChanged();
            spinnerHorario.setSelection(0);
            spinnerHorario.performClick();
        }
        if (id == 8 || id == 16) {
            id = (id == 8 ? 8 : 16);
            repeticao.remove(id);
            String[] inter = {"A cada 4 horas", "A cada 3 horas", "A cada 2 horas", "Por hora"};
            int ii = 0;
            for (int bb = 0; bb < inter.length; bb++) {
                dataAdapter.insert(inter[bb], (id + ii));
                ii += 1;
            }
            dataAdapter.notifyDataSetChanged();
            spinnerHorario.setSelection(0);
            spinnerHorario.performClick();
        }
    }

    /**
     * Função para recalcular o intervalo quando o usuário define um nova hora inicial.
     * 'Opcao' representa o número de horas de intervalos, 'hn]New' e 'mNew' representam a nova hora
     *
     * @param opcao
     * @param hNew
     * @param mNew
     */
    private void recalculaIntervaloHoras(int opcao, int hNew, int mNew) {
        //Crio o calendário que receberá a nova hora inicial definida pelo usuário
        Calendar novo = Calendar.getInstance();
        novo.set(Calendar.HOUR_OF_DAY, hNew);
        novo.set(Calendar.MINUTE, mNew);
        novo.set(Calendar.SECOND, 0);
        //Acrescento zero se for necessário a hora e ao minuto
        String hour = hNew < 10 ? ("0" + String.valueOf(hNew)) : String.valueOf(hNew);
        String minute = mNew < 10 ? ("0" + String.valueOf(mNew)) : String.valueOf(mNew);
        //Percorro todos os TextViews alterando para as novas horas.
        for (int i = 0; i < (textViewsHorarios.size()); i++) {
            TextView v = textViewsHorarios.get(i);
            v.setText(hour + " : " + minute);
            novo.add(Calendar.HOUR_OF_DAY, opcao);
            hour = novo.get(Calendar.HOUR_OF_DAY) < 10 ? ("0" + String.valueOf(novo.get(Calendar.HOUR_OF_DAY))) : String.valueOf(novo.get(Calendar.HOUR_OF_DAY));
            minute = novo.get(Calendar.MINUTE) < 10 ? ("0" + String.valueOf(novo.get(Calendar.MINUTE))) : String.valueOf(novo.get(Calendar.MINUTE));
        }

    }

    /**
     * Função para recalcular o intervalo quando o usuário define uma nova hora final.
     * @param opcao
     * @param horaFinalNew
     * @param minutoFinalNew
     */
    private void recalculaIntervaloHorasFinal(int opcao, int horaFinalNew, int minutoFinalNew) {
        //Calendário com a hora inicial
        Calendar hInicio = Calendar.getInstance();
        String[] horarioInicial = textViewsHorarios.get(0).getText().toString().split(" : ");
        hInicio.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horarioInicial[0]));
        hInicio.set(Calendar.MINUTE, Integer.parseInt(horarioInicial[1]));

        //Calendário com a Hora final
        Calendar hfinal = Calendar.getInstance();
        hfinal.set(Calendar.HOUR_OF_DAY, horaFinalNew);
        hfinal.set(Calendar.MINUTE, minutoFinalNew);

        //Verifica se a hora final está entre 00:00 e  08:00
        //Se verdadeiro adiciona um dia ao calendário para se considerado um novo dia e não haver um intervalo negativo.
        if (hfinal.before(hInicio)) {
            hfinal.add(Calendar.DATE, 1);
        }

        //Duração do tempo em milisegundo da diferença entre a Hora final e a Inicial
        long duracao = hfinal.getTimeInMillis() - hInicio.getTimeInMillis();
        //O intervalo em horas da duração do intervalo entre as horas final e inicial.
        int intervalo = (int) (((duracao / 1000) / 60) / 60);
        //Quantidade de vezes que o intervalo será divido em relação a "opcao" que o usuário escolheu.
        //Se caso o intervalo não de para dividir pela opcao, então é setado pelo menos um intervalo.
        int qtdIntervalo = intervalo / opcao == 0 ? 1 : (intervalo / opcao);

        //Pega a referência do Layout onde o TextView será inserido
        LinearLayout LL = (LinearLayout) findViewById(R.id.cardHorarioLL2);
        //"For" para retirar os TextView do layout e retirar do Arraylist, menos o primeiro.
        for (int i = textViewsHorarios.size() - 1; i > 0; i--) {
            LL.removeView(textViewsHorarios.get(i));
            textViewsHorarios.remove(i);
        }

        //String utilizadas para formatar as horas
        String hora, minuto;
        //Cria os TextView do novo intervalo.
        for (int b = 0; b < qtdIntervalo; b++) {
            TextView add = new TextView(MedicamentoCadastro.this);
            add.setId(21 + b);
            //Determina o tamanho do texto
            add.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            //Objeto necessário para adicionar margin ao texto. (Usou-se Linear Layout pois é a view parent da TextView)
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            lp.setMargins(0, 10, 0, 0);
            //Adiciona a margin ao textView
            add.setLayoutParams(lp);
            //Adiciona o Intervalo ao horário
            hInicio.add(Calendar.HOUR_OF_DAY, (opcao));
            //hora e minuto - Variaveis do tipo String que recebem o valor da hora e do  minuto respectivamento do horario de tratamento (c.Calendar)
            hora = (hInicio.get(Calendar.HOUR_OF_DAY)) < 10 ? ("0" + String.valueOf(hInicio.get(Calendar.HOUR_OF_DAY))) : String.valueOf(hInicio.get(Calendar.HOUR_OF_DAY));
            minuto = (hInicio.get(Calendar.MINUTE)) < 10 ? ("0" + String.valueOf(hInicio.get(Calendar.MINUTE))) : String.valueOf(hInicio.get(Calendar.MINUTE));
            //Adiciona o texto
            add.setText(hora + " : " + minuto);
            LL.addView(add);
            textViewsHorarios.add(add);
        }
        //Adiciona o onclick ao novo "último" TextView do Layout.
        textViewsHorarios.get(textViewsHorarios.size() - 1).setOnClickListener(OnClinkRecalculaIntervalFinal(opcao));
    }

    /**
     * Função que retorna um objeto do tipo OnClickListerner.
     * É Utilizada para adicionar um evento ao último textView quando o usuário seleciona "A cada h horas"
     * que recalcula o intervalo de acordo com a hora final selecionada
     *
     * @param op
     * @return OnClickListener
     */
    private View.OnClickListener OnClinkRecalculaIntervalFinal(int op) {
        //Variavel criada para passar a opcao escolhida pelo usuário
        final int opcao = op;
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pega o horário final (último TextView) do intervalo para passar de referência ao TimerPicker para o usuário escolher
                String[] horario = textViewsHorarios.get(textViewsHorarios.size() - 1).getText().toString().split(" : ");
                int horaAtual = Integer.parseInt(horario[0]);
                int minutoAtual = Integer.parseInt(horario[1]);
                TimePickerDialog time = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        recalculaIntervaloHorasFinal(opcao, hourOfDay, minute);
                    }
                }, horaAtual, minutoAtual, true);
                time.show();
            }
        };
        return click;
    }

    /**
     * Recebe como parâmetro intervaloHora. Corresponde ao intervalo de hora que o usuário escolheu no spinner
     * por exemplo "A cada 12 horas", então 'intervaloHora' possui o valor 12
     *
     * @param intervaloHora
     * @return OnClickListener
     */
    private View.OnClickListener OnClickIntervalTextView(int intervaloHora) {
        //Variavel que passa a opcao escolhda pelo usuário
        final int opcao = intervaloHora;
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Crio um calendário para pegar o horário que está atualmente na view
                Calendar c = Calendar.getInstance();
                TextView horaTela = (TextView) v;
                String texto = horaTela.getText().toString();
                String[] resultado = texto.split(" : ");
                final View horaInterval = v;
                //Adiciona a hora ao calendário
                c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(resultado[0]));
                c.set(Calendar.MINUTE, Integer.parseInt(resultado[1]));
                //Crio o timepicker para o usuário selecionar a nova hora inicial do tratamento
                TimePickerDialog time = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Pego a referência do TextView para setar a novo hora Inicial
                        TextView horaInicioNova = (TextView) horaInterval;
                        String h = hourOfDay < 10 ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                        String m = minute < 10 ? ("0" + String.valueOf(minute)) : String.valueOf(minute);
                        //Escreve a nova hora inicial no TextView
                        horaInicioNova.setText(h + " : " + m);
                        //Função que recalcula os intevalos em relação a  nova hora inicial
                        recalculaIntervaloHoras(opcao, hourOfDay, minute);
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                time.show();
            }
        };
        return click;
    }

    /**
     * Função para criar os intervalo "A cada h horas".
     * @param opcao
     */
    private void criaIntervalosTextView(int opcao){
        Calendar c = Calendar.getInstance();
        c.set((Calendar.HOUR_OF_DAY), 8);
        c.set(Calendar.MINUTE, 0);
        String hora, minuto;

        int i = 0;
        int tam = 24/opcao;

        //Pego a referência do elemento onde irei inserir novas views
        LinearLayout LL = (LinearLayout) findViewById(R.id.cardHorarioLL2);
        //Remove todos os componetes já adicionados na view
        LL.removeAllViews();
        //Limpa o ArrayList contendo os TextViews. Isso é utilizado pois quando o usuário fica trocando de opção de intervalos, o ArrayList não ficar desatualizado.
        textViewsHorarios.clear();


//        //Cria o linearlayout do horário inicial
        LinearLayout llhoraInicio = new LinearLayout(MedicamentoCadastro.this);
        LinearLayout.LayoutParams lphora = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llhoraInicio.setLayoutParams(lphora);
        llhoraInicio.setOrientation(LinearLayout.VERTICAL);

        //Adiciona um padding de 8dp ao LinearLayout
        int pad = (int) Utils.convertDpToPixel(8);
        llhoraInicio.setPadding(pad,pad,pad,pad);


        //Cria o textview contendo o horário
        TextView hh  = new TextView(MedicamentoCadastro.this);
        hh.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        l.gravity = Gravity.CENTER;
        l.setMargins(0, 10, 0, 0);
        //Adiciona a margin ao textView
        hh.setLayoutParams(l);
        //hora e minuto - Variaveis do tipo String que recebem o valor da hora e do  minuto respectivamento do horario de tratamento (c.Calendar)
        hora = (c.get(Calendar.HOUR_OF_DAY)) < 10 ? ("0" + String.valueOf(c.get(Calendar.HOUR_OF_DAY))) : String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        minuto = (c.get(Calendar.MINUTE)) < 10 ? ("0" + String.valueOf(c.get(Calendar.MINUTE))) : String.valueOf(c.get(Calendar.MINUTE));
        //Adiciona o texto
        hh.setText(hora + " : " + minuto);
        //Adiciona o TextView ao Arraylist tvHorarios
        textViewsHorarios.add(hh);
        //Incrementa o horário
        c.add(Calendar.HOUR_OF_DAY, opcao);

        //Cria o textview com 'Horário de início'
        TextView texto = new TextView(MedicamentoCadastro.this);
        texto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        texto.setLayoutParams(l);
        texto.setText("Hora de início");
        //Adiciona cor ao texto;
        texto.setTextColor(ContextCompat.getColor(MedicamentoCadastro.this, R.color.colorAccent));
        //Add os text view ao linearlayout filho
        llhoraInicio.addView(hh);
        llhoraInicio.addView(texto);
        //Adiciona no linearlayout pai
        LL.addView(llhoraInicio);

        for(i=1; i < tam; i++){
            //Cria o TextView que receberá a hora
            TextView v  = new TextView(MedicamentoCadastro.this);
            //Adiciona a id ao objeto
            v.setId(20 + i);
            //Adiciona um padding de 8dp ao textview
            int padding = (int) Utils.convertDpToPixel(8);
            v.setPadding(padding,padding,padding,padding);
            //Determina o tamanho do texto
            v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            //Seta o onclick listener ao objeto. Neste caso A funcao OnClickIntervalTextView determina o método.
            //v.setOnClickListener();
            //Objeto necessário para adicionar margin ao texto. (Usou-se Linear Layout pois é a view parent da TextView)
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            lp.setMargins(0, 10, 0, 0);
            //Adiciona a margin ao textView
            v.setLayoutParams(lp);
            //hora e minuto - Variaveis do tipo String que recebem o valor da hora e do  minuto respectivamento do horario de tratamento (c.Calendar)
            hora = (c.get(Calendar.HOUR_OF_DAY)) < 10 ? ("0" + String.valueOf(c.get(Calendar.HOUR_OF_DAY))) : String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            minuto = (c.get(Calendar.MINUTE)) < 10 ? ("0" + String.valueOf(c.get(Calendar.MINUTE))) : String.valueOf(c.get(Calendar.MINUTE));
            //Adiciona o texto
            v.setText(hora + " : " + minuto);
            //Adiciona o TextView ao Arraylist tvHorarios
            textViewsHorarios.add(v);
            //Adiciona o TextView no LinearLayout
            LL.addView(v);
            c.add(Calendar.HOUR_OF_DAY, opcao);
        }
        //Adiciona o metodo OnclickListener no Horario inicial
        //TextView horarioInicial = (TextView) findViewById(20);
        //horarioInicial.setOnClickListener(OnClickIntervalTextView(opcao));
        textViewsHorarios.get(0).setOnClickListener(OnClickIntervalTextView(opcao));
        //Adiciona OnClickEvent no último TextView do ArrayList
        textViewsHorarios.get(textViewsHorarios.size()-1).setOnClickListener(OnClinkRecalculaIntervalFinal(opcao));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.v("textViewSize", String.valueOf(telaEditHorario));

        if((tipoTela == MedicamentoCadastro.TELA_EDITAR_MED) && (telaEditHorario)){
            telaEditHorario = false;
            insereTimeTextView(alarmeEdit.getAlarmeInfo());
        }else{
            switch (position) {
                case 0:
                    criaTimeTextView(1);
                    break;
                case 1:
                    criaTimeTextView(2);
                    break;
                case 2:
                    criaTimeTextView(3);
                    break;
                case 3:
                    if (spinnerHourController) {
                        spinnerHourController = false;
                        spinnerControllerOptions(position);
                        break;
                    } else if ((spinnerHourController == false)) {
                        criaTimeTextView(4);
                        break;
                    }
                case 4:
                    if (spinnerHourController) {
                        Log.v("Spinner Horario ","Intervalo");
                        spinnerHorario.setSelection(5);
                        break;
                    } else if ((spinnerHourController == false)) {
                        criaTimeTextView(5);
                        break;
                    }
                case 5:
                    if ((spinnerHourController == false)) {
                        criaTimeTextView(6);
                        break;
                    } else if (spinnerHourController) {
                        criaIntervalosTextView(12);
                        Log.v("Spinner Horario ","A cada 12 horas");
                        break;
                    }
                case 6:
                    if ((spinnerHourController == false)) {
                        criaTimeTextView(7);
                        break;
                    } else if (spinnerHourController) {
                        criaIntervalosTextView(8);
                        Log.v("Spinner Horario ","A cada 8 horas");
                        break;
                    }
                case 7:
                    if ((spinnerHourController == false)) {
                        criaTimeTextView(8);
                        break;
                    } else if (spinnerHourController) {
                        criaIntervalosTextView(6);
                        Log.v("Spinner Horario ","A cada 6 horas");
                        break;
                    }
                case 8:
                    if ((spinnerHourController == false)) {
                        criaTimeTextView(9);
                        break;
                    } else if (spinnerIntervalController) {
                        spinnerIntervalController = false;
                        spinnerControllerOptions(position);
                        break;
                    } else if (spinnerHourController && (spinnerIntervalController == false)) {
                        criaIntervalosTextView(4);
                        Log.v("Spinner Horario ","A cada 4 horas");
                        break;
                    }
                case 9:
                    if ((spinnerHourController == false)) {
                        criaTimeTextView(10);
                        break;
                    } else if (spinnerHourController && (spinnerIntervalController == false)) {
                        criaIntervalosTextView(3);
                        Log.v("Spinner Horario ","A cada 3 horas");
                        break;
                    }
                case 10:
                    if ((spinnerHourController == false)) {
                        criaTimeTextView(11);
                        break;
                    } else if (spinnerHourController && (spinnerIntervalController == false)) {
                        criaIntervalosTextView(2);
                        Log.v("Spinner Horario ","A cada 2 horas");
                        break;
                    }
                case 11:
                    if ((spinnerHourController == false)) {
                        criaTimeTextView(12);
                        break;
                    } else if (spinnerHourController && (spinnerIntervalController == false)) {
                        criaIntervalosTextView(1);
                        Log.v("Spinner Horario ","Por hora");
                        break;
                    }
                case 12:
                    if (spinnerHourController == false) {
                        //O usuário selecionou a opção intervalos - Então seleciona o primeiro intervalo
                        spinnerHorario.setSelection(13);
                        break;
                    }
                case 13:
                    criaIntervalosTextView(12);
                    Log.v("Spinner Horario ","A cada 12 horas");
                    break;
                case 14:
                    criaIntervalosTextView(8);
                    Log.v("Spinner Horario ","A cada 8 horas");
                    break;
                case 15:
                    criaIntervalosTextView(6);
                    Log.v("Spinner Horario ","A cada 6 horas");
                    break;
                case 16:
                    if (spinnerIntervalController) {
                        spinnerIntervalController = false;
                        spinnerControllerOptions(position);
                        break;
                    } else {
                        criaIntervalosTextView(4);
                        Log.v("Spinner Horario ","A cada 4 horas");
                        break;
                    }
                case 17:
                    criaIntervalosTextView(3);
                    Log.v("Spinner Horario ","A cada 3 horas");
                    break;
                case 18:
                    criaIntervalosTextView(2);
                    Log.v("Spinner Horario ","A cada 2 horas");
                    break;
                case 19:
                    criaIntervalosTextView(1);
                    Log.v("Spinner Horario ","Por horas");
                    break;
                default:
                    break;
            }
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    /************************************************************************************************************
     *
     *
     *
     *      Controlam Radio buttons de Dias específicos, dias da semana e todo dia
     *
     *
     *
     ***********************************************************************************************************/

    //Função que exibe novamente um dialog que foi escondido pelo função dialog.hide()
    private void showDialog(String dialog) {
        //Procura pelo AlertDialog com a tag 'dialog'
        Fragment prev = getSupportFragmentManager().findFragmentByTag(dialog);
        //Verifica se encontrou o AlertDialog a tag passada como parâmetro
        if (prev != null) {
            //Faz-se um cast para obter o DialogFragment onde encontra-se o AlertDialog
            DialogFragment a = ((DialogFragment) prev);
            //Pega o AlertDialog contido no DialogFragment
            AlertDialog b = (AlertDialog) a.getDialog();
            //Mostra o AlertDialog
            b.show();
            Log.i("IF PREV ONCLICK", "É DIFERENTE DE NULL");
        }
        return;
    }

    private void hideDialog(String dialog){
        Fragment prev = getSupportFragmentManager().findFragmentByTag(dialog);
        if (prev != null) {
            //Faz-se um cast para obter o DialogFragment onde encontra-se o AlertDialog
            DialogFragment a = ((DialogFragment) prev);
            //Pega o AlertDialog contido no DialogFragment
            AlertDialog b = (AlertDialog) a.getDialog();
            //Mostra o AlertDialog
            b.hide();
            Log.v("Dialog", "Escondeu dialog");
        }
    }

    //Função para esconder um dialog
    private void esconderAlertDialog(DialogFragment dialog) {

        AlertDialog esconder = (AlertDialog) dialog.getDialog();
        esconder.hide();
    }

    private void removerDialog(String dialog) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag = getSupportFragmentManager().findFragmentByTag(dialog);
        if (frag != null) {
            String tag = frag.getTag();
            DialogFragment remover = ((DialogFragment) frag);
            remover.dismiss();
            Log.i("ENTROU IF REMOVE", "REMOVEU FRAGMENT: " + tag);
        }
        ft.addToBackStack(null);
    }

    private void setupRadioGroup() {

        //RadioButtons
        //DiasEspecíficos
        rbDiaEspecificos = (RadioButton) findViewById(R.id.rbDiaEspec);
        //DiasIntervalo
        rbDiaIntervalos = (RadioButton) findViewById(R.id.rbDiaIntervalo);

        //OnCLickListener do radio Button Dias Intervalos
        rbDiaIntervalos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbAnterior == v.getId()){
                    showDialog("diasIntervalos");
                }
            }
        });

        //OnCLickListener do radioButton Dias Específicos
        rbDiaEspecificos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("diasEspecificos");
            }
        });

        //RadioGroup - Gerencia os RadioButtons de Repetição de dias do remédio
        radioGroup = (RadioGroup) findViewById(R.id.agRGDias);

        //Seleciona o radioButton 'Todo dia' como opcao padrão
        radioGroup.check(R.id.rbTodoDia);

        //Guarda a id do objeto selecionado
        rbAnterior = radioGroup.getCheckedRadioButtonId();

        //Adiciona o listener 'OnCheckedChange ao radioGroup
        //Este listener monitora quando o usuário seleciona um novo radioButton do grupo
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //Verifica se o RadioButton já foi selecionado. Isto é para evitar de ser chamado mais de uma vez
            private boolean isChecked(RadioGroup group, int viewId) {
                if (viewId != -1) {
                    View v = group.findViewById(viewId);
                    if (v instanceof RadioButton) {
                        Log.i("Entrou no IF", ((RadioButton) v).getText().toString());
                        Log.i("RadioButto " + ((RadioButton) v).getText().toString() + " " + String.valueOf(((RadioButton) v).isChecked()), ((RadioButton) v).getText().toString());
                        return ((RadioButton) v).isChecked();
                    }
                }
                Log.i("Nao Entrou no IF", String.valueOf(viewId));
                return true;
            }

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Verifica se o radioButton já está selecionado
                if (!isChecked(group, checkedId)) {
                    Log.i("ENTROU", "IsChecked");
                    return;
                }
                switch (checkedId) {
                    case R.id.rbTodoDia:
                        //Apaga as informações dos TextView para caso o usuário tiver selecionado anteriormente
                        // Dias específicos ou Dias Intervalos. A variável rbAnterior possui qual o radio button foi selecionado anteriormente.
                        if ((rbAnterior == R.id.rbDiaIntervalo) || (rbAnterior == R.id.rbDiaEspec)) {
                            if (rbAnterior == R.id.rbDiaIntervalo) {
                                rbDiaIntervalos.setText("Dias de intervalo");
                                removerDialog("diasIntervalos");
                            } else {
                                rbDiaEspecificos.setText("Dias específicos da semana");
                                removerDialog("diasEspecificos");
                            }
                        }
                        Log.i("CheckedId", "Todo dia");
                        //Torna o anterior o radioButton Todo dia
                        rbAnterior = checkedId;
                        //Variáveis utilizada para o controle dos radioButton com fragment. Isto é necessário pois quando o usuário seleciona
                        //outro radioButton e seleciona cancelar será aberto novamente desnecesáriamente. Essa variáveis não permitem isso.
                        rbCheckDiasInter = true;
                        rbCheckDiasEsp = true;
                        break;
                    case R.id.rbDiaEspec:
                        //Esta verificação é necessária para controlar quando o usuário já selecionou dias especificos, agora seleciona dias intervalo
                        //caso o usuário aperte no botão cancelar do dialog DiasEspecífico volta a marcar o RadioButton Diasntervalo porém sem instancia ou mostrar o dialogo novamente
                        if (rbCheckDiasEsp) {
                            //Verifica se existe algum dialog já aberto, se existir, mostre ele novamente, caso contrário construa um novo.
                            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            Fragment prev = getSupportFragmentManager().findFragmentByTag("diasEspecificos");
                            if (prev != null) {
                                //Faz-se um cast para obter o DialogFragment onde encontra-se o AlertDialog
                                DialogFragment a = ((DialogFragment) prev);
                                //Pega o AlertDialog cotido no DialogFragment
                                AlertDialog b = (AlertDialog) a.getDialog();
                                //Mostra o AlertDialog
                                b.show();
                                Log.i("Entrou Especifico", "É DIFERENTE DE NULL");
                                break;
                            } else {
                                //Contrói um novo dialog Dias Intervalo
                                Log.i("Entrou Especifico", "NO Else do PREV IGUAL A NULL");
                                DialogFragment dialog = new diasEspecificos();
                                dialog.show(getSupportFragmentManager(), "diasEspecificos");
                                break;
                            }
                        } else {
                            //Esse else controla quando o dialog foi escodido hide(), para que ele não abra sozinho quando o usuário rotacionar a tela
                            Fragment frag = getSupportFragmentManager().findFragmentByTag("diasEspecificos");
                            if (frag != null) {
                                DialogFragment a = ((DialogFragment) frag);
                                AlertDialog b = (AlertDialog) a.getDialog();
                                b.hide();
                                break;
                            }
                            break;
                        }
                    case R.id.rbDiaIntervalo:
                        //Esta verificação é necessária para controlar quando o usuário já selecionou dias intervalo, agora seleciona dias Específicos
                        //caso o usuário aperte no botão cancelar do dialog DiasEspecífico volta a marcar o RadioButton Diasntervalo porém sem instancia ou mostrar o dialogo novamente

                        if (rbCheckDiasInter) {
                            //Verifica se existe algum dialog já aberto, se existir, mostre ele novamente, caso contrário construa um novo.
                            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            Fragment prev = getSupportFragmentManager().findFragmentByTag("diasIntervalos");
                            if (prev != null) {
                                //Faz-se um cast para obter o DialogFragment onde encontra-se o AlertDialog
                                DialogFragment a = ((DialogFragment) prev);
                                //Pega o AlertDialog cotido no DialogFragment
                                AlertDialog b = (AlertDialog) a.getDialog();
                                //Mostra o AlertDialog
                                b.show();
                                Log.i("Entrou Intervalo", "É DIFERENTE DE NULL");
                                break;
                            } else {
                                //Contrói um novo dialog Dias Intervalo
                                Log.i("Entrou Intervalo", "NO Else do PREV");
                                DialogFragment dialog = new diasIntervalos();
                                dialog.show(getSupportFragmentManager(), "diasIntervalos");
                                break;
                            }
                        } else {
                            //Esse else controla quando o dialog foi escodido hide(), para que ele não abra sozinho quando o usuário rotacionar a tela
                            Fragment frag = getSupportFragmentManager().findFragmentByTag("diasIntervalos");
                            if (frag != null) {
                                DialogFragment a = ((DialogFragment) frag);
                                AlertDialog b = (AlertDialog) a.getDialog();
                                b.hide();
                                break;
                            }
                            break;
                        }
                    default:
                        break;
                }
            }
        });


        /**
         * Número de dias
         */

        radioGroupDias = (RadioGroup) findViewById(R.id.agRGDuracao);
        rbContinuo = (RadioButton) findViewById(R.id.rbContinuo);
        rbNumeroDias = (RadioButton) findViewById(R.id.rbNumeroDias);
        radioGroupDias.check(R.id.rbContinuo);

        rbAnterior2 = radioGroupDias.getCheckedRadioButtonId();

        rbNumeroDias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("fragNumeroDias");
            }
        });

        radioGroupDias.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //Verifica se o RadioButton já foi selecionado. Isto é para evitar de ser chamado mais de uma vez
            private boolean isChecked(RadioGroup group, int viewId) {
                if (viewId != -1) {
                    View v = group.findViewById(viewId);
                    if (v instanceof RadioButton) {
                        Log.i("Entrou no IF", ((RadioButton) v).getText().toString());
                        Log.i("RadioButto " + ((RadioButton) v).getText().toString() + " " + String.valueOf(((RadioButton) v).isChecked()), ((RadioButton) v).getText().toString());
                        return ((RadioButton) v).isChecked();
                    }
                }
                Log.i("Nao Entrou no IF", String.valueOf(viewId));
                return true;
            }
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                //Verifica se o radioButton já está selecionado
                if (!isChecked(group, checkedId)) {
                    Log.i("ENTROU", "IsChecked");
                    return;
                }

                switch (checkedId){
                    case R.id.rbContinuo:
                        if(rbAnterior2 == R.id.rbNumeroDias){
                            rbNumeroDias.setText("número de dias");
                            removerDialog("fragNumeroDias");
                        }
                        //Torna o anterior o radioButton contínuo
                        rbAnterior2 = checkedId;
                        //Variáveis utilizada para o controle dos radioButton com fragment. Isto é necessário pois quando o usuário seleciona
                        //outro radioButton e seleciona cancelar será aberto novamente desnecesáriamente. Essa variáveis não permitem isso.
                        rbCheckNumeroDias = true;
                        break;
                    case R.id.rbNumeroDias:
                        if(rbCheckNumeroDias){
                            //Verifica se existe algum dialog já aberto, se existir, mostre ele novamente, caso contrário construa um novo.
                            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            Fragment prev = getSupportFragmentManager().findFragmentByTag("fragNumeroDias");
                            if (prev != null) {
                                //Faz-se um cast para obter o DialogFragment onde encontra-se o AlertDialog
                                DialogFragment a = ((DialogFragment) prev);
                                //Pega o AlertDialog cotido no DialogFragment
                                AlertDialog b = (AlertDialog) a.getDialog();
                                //Mostra o AlertDialog
                                b.show();
                                Log.i("Entrou NumeroDias", "É DIFERENTE DE NULL");
                                break;
                            } else {
                                //Contrói um novo dialog fragNumeroDias
                                Log.i("Entrou NumeroDias", "NO Else do PREV");
                                DialogFragment dialog = new fragNumeroDias();
                                dialog.show(getSupportFragmentManager(), "fragNumeroDias");
                                break;
                            }
                        }else{
                            //Esse else controla quando o dialog foi escodido hide(), para que ele não abra sozinho quando o usuário rotacionar a tela
                            Fragment frag = getSupportFragmentManager().findFragmentByTag("fragNumeroDias");
                            if (frag != null) {
                                DialogFragment a = ((DialogFragment) frag);
                                AlertDialog b = (AlertDialog) a.getDialog();
                                b.hide();
                                break;
                            }
                            break;
                        }

                    default:
                        break;
                }
            }
        });

    }

    private SpannableStringBuilder formataTextoDiasEspecificos(String dias){

        //String texto, contém o texto inicial básico do checkbox
        String texto = "Dias específicos da semana: ";

        //Adiciona Span (Marcadores) a string
        SpannableStringBuilder result = new SpannableStringBuilder();

        //Adiciona a 'result' o valor da variável 'texto'
        result.append(texto);

        //Adiciona a 'result' o valor da variável 'dias'
        result.append(dias);

        //Cria um objeto descendente de Span, no caso Foreground, que muda a cor da fonte.
        //A cor da fonte seta é a cor Accent definia em values/color.
        //Tem que usar o ContextCompat pois getResource está depreciada nas versões atuais
        ForegroundColorSpan color = new ForegroundColorSpan(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));

        //Informa qual o parâmetro (cor), o índice inicial, o índice final do intervalo e a flag.
        result.setSpan(color, (result.length() - dias.length()), result.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return result;
    }
    private SpannableStringBuilder formataTextoDiasIntervalos(int intervalo){
        //Variáveis para compor o texto do RadioButton intervalo com o intervalo escolhido pelo usuário
        String texto1 = "Dias de intervalo: ";
        String texto2 = "cada " + intervalo + " dias";

        //Adiciona Span (Marcadores) ao texto permitindo adicionar Style a string
        SpannableStringBuilder result = new SpannableStringBuilder(texto1);
        result.append(texto2);

        //Marcador do tipo ForegroundColorSpan, troca a cor do texto
        //Neste caso está sendo utilizado a cor accent do values/color
        ForegroundColorSpan color = new ForegroundColorSpan(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));

        //Diz a string em que intervalo será adicionado a cor definida anteriormente
        result.setSpan(color, (result.length() - texto2.length()), result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return result;
    }


    /**
     * O Fragment diasEspecificos recebe uma referência desta Activity através do método
     * onAttach callback, que é utilizada para chamar os métodos onClickListenerPositivoDiasEspecificos
     * e onClickListenerNegativoDiasEspecificos definidos na interface diasEspecificos.diasEspecificosListener.
     * 'diasSelecionados' contém os dias selecionados pelo usuário
     * 'dialog' representa o dialogo que abriu para o usuário
     * @param dialog
     * @param diasSelecionados
     */
    @Override
    public void onClickListenerPositivoDiasEspecificos(DialogFragment dialog, ArrayList<String> diasSelecionados) {
        //salva os dias
        this.diasSelecionados = diasSelecionados;
        //Variáveis utilizada para o controle dos radioButton com fragment. Isto é necessário pois quando o usuário seleciona
        //outro radioButton e seleciona cancelar será aberto novamente desnecesáriamente. Essa variáveis não permitem isso.
        rbCheckDiasEsp = false;
        rbCheckDiasInter = true;

        //Apaga as opções escolhida pelo usuário caso ele tenha selecionado anterioriormente DiasEspecíficos
        if (rbAnterior == R.id.rbDiaIntervalo) {
            rbDiaIntervalos.setText("Dias de intervalo");
            //remover Fragmento criado da pilha
            removerDialog("diasIntervalos");

        }

        if(diasSelecionados.size()>=7){
            RadioButton radio = (RadioButton) findViewById(R.id.rbTodoDia);
            radio.toggle();
            dialog.dismiss();
            return;
        }

        //Passa para a string 'dias' os dias selecionados pelo usuário
        String dias = diasSelecionados.toString();

        //Retira os colchetes da string
        dias = dias.replace("[", "");
        dias = dias.replace("]", "");

        //Deixa os dias da semana com outro cor
        SpannableStringBuilder result = formataTextoDiasEspecificos(dias);

        //Adiciona a string personalizada ao RadioButton
        rbDiaEspecificos.setText(result);

        //Define o novo "anterior" dos radiobutton caso o usuário cancele sua opção
        rbAnterior = radioGroup.getCheckedRadioButtonId();

        esconderAlertDialog(dialog);
        Log.i("Escondeu Dialog", " Específico - OK");

    }

    @Override
    public void onClickListenerNegativoDiasEspecificos(DialogFragment dialog) {
        //RadioButton utilizado para retornar a opção anterior ao selecionar o botão cancelar
        RadioButton radio = (RadioButton) findViewById(rbAnterior);
        radio.toggle();

        //Retira o Dialog da pilha do FragmentManager
        if(rbAnterior != R.id.rbDiaEspec){
            dialog.dismiss();
            Log.i("Dismiss Dialog", " Específico");
        }else{
            esconderAlertDialog(dialog);
        }


    }

    /**
     * O Fragment diasIntervalos recebe uma referência desta Activity através do método
     * onAttach callback, que é utilizado para chamar os métodos onClickListenerPositivoDiasIntervalos
     * e onClickListenerNegativoDiasIntervalos definidos na interface diasIntervalos.diasIntervalosListener.
     * Interface utilizada para trazer os Dias de intevalos escolhido pelo usuário
     * @param dialog
     * @param intervalo
     */
    @Override
    public void onClickListenerPositivoDiasIntervalos(DialogFragment dialog, int intervalo) {
        //salva o intervalo
        intevaloSel = intervalo;

        //Variáveis utilizada para o controle dos radioButton com fragment. Isto é necessário pois quando o usuário seleciona
        //outro radioButton e seleciona cancelar será aberto novamente desnecesáriamente. Essa variáveis não permitem isso.
        rbCheckDiasEsp = true;
        rbCheckDiasInter = false;

        //Apaga as opções escolhida pelo usuário caso ele tenha selecionado anterioriormente DiasEspecíficos
        if (rbAnterior == R.id.rbDiaEspec) {
            rbDiaEspecificos.setText("Dias específicos da semana");
            removerDialog("diasEspecificos");
        }


        SpannableStringBuilder result = formataTextoDiasIntervalos(intervalo);

        //Adiciona a String ao RadioButton
        rbDiaIntervalos.setText(result);

        //Define o novo "anterior" dos radiobutton caso o usuário cancele sua opção
        rbAnterior = radioGroup.getCheckedRadioButtonId();


        esconderAlertDialog(dialog);
        Log.i("Escondeu Dialog", " Intervalo - OK");
    }

    @Override
    public void onClickListenerNegativoDiasIntervalos(DialogFragment dialog) {
        //RadioButton utilizado para retornar a opção anterior ao selecionar o botão cancelar
        RadioButton radio = (RadioButton) findViewById(rbAnterior);
        radio.toggle();


        //Retira o Dialog da pilha do FragmentManager
        if(rbAnterior != R.id.rbDiaIntervalo){
            dialog.dismiss();
            Log.i("Dismiss Dialog", " Específico");
        }else{
            esconderAlertDialog(dialog);
        }

    }

    /**
     * O Fragment fragNumeroDias recebe uma referência desta Activity através do método
     * onAttach callback, que é utilizado para chamar os métodos onClickListenerPositivoNumeroDias
     * e onClickListenerNegativoNumeroDias definidos na interface fragNumeroDias.IntervalosListener.
     * Interface utilizada para trazer os Dias de intevalos escolhido pelo usuário
     * @param dialog
     * @param numeroDias
     */
    @Override
    public void onClickListenerPositivoNumeroDias(DialogFragment dialog, int numeroDias) {

        this.numeroDias = numeroDias;

        //Esconde o dialogo
        esconderAlertDialog(dialog);

        rbCheckNumeroDias = false;
        rbAnterior2 = radioGroupDias.getCheckedRadioButtonId();

        //Variáveis para compor o texto do RadioButton Numero de dias com a quantidade escolhida pelo usuário
        String texto1 = "número de dias: ";
        String texto2 = String.valueOf(numeroDias);

        //Adiciona Span (Marcadores) ao texto permitindo adicionar Style a string
        SpannableStringBuilder result = new SpannableStringBuilder(texto1);
        result.append(texto2);

        //Marcador do tipo ForegroundColorSpan, troca a cor do texto
        //Neste caso está sendo utilizado a cor accent do values/color
        ForegroundColorSpan color = new ForegroundColorSpan(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));

        //Diz a string em que intervalo será adicionado a cor definida anteriormente
        result.setSpan(color, (result.length() - texto2.length()), result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //Adiciona o texto
        rbNumeroDias.setText(result);
    }

    @Override
    public void onClickListenerNegativoNumeroDias(DialogFragment dialog) {
        //RadioButton utilizado para retornar a opção anterior ao selecionar o botão cancelar
        RadioButton radio = (RadioButton) findViewById(rbAnterior2);
        radio.toggle();

        //Retira o Dialog da pilha do FragmentManager
        dialog.dismiss();
        Log.i("Dismiss Dialog", " Intervalo");
    }

    /**
     * O Fragment fragQtdLembrete recebe uma referência desta Activity através do método
     * onAttach callback, que é utilizado para chamar os métodos onClickListenerLembretePositivo
     * e onClickListenerLembreteNegativo definidos na interface fragQtdLembrete.lembreteListerner.
     * Interface utilizada para selecionar a quantidade de remédio a ser considera para gerar um lembrete
     */
    @Override
    public void onClickListenerLembretePositivo(DialogFragment dialog, int qtdRemedio) {

        qtdLembreteMed = qtdRemedio;

        //Esconde o dialogo
        esconderAlertDialog(dialog);

        String texto = "Quando restarem "+ qtdLembreteMed +" remédios";
        textoLembrete.setText(texto);
    }

    @Override
    public void onClickListenerLembreteNegativo(DialogFragment dialog) {
        //Retira o Dialog da pilha do FragmentManager
        dialog.dismiss();
    }


    private class SalvarMedicamentoAsyncTask extends AsyncTask<Void,Void,Void>{

        ProgressDialog dialog;
        Context context;
        Medicamento medicamento;
        Alarme alarme;
        MedicamentoController mc;
        AlarmeController ac;

        public SalvarMedicamentoAsyncTask(Context context, Medicamento med, Alarme alarme){
            this.context = context;
            this.dialog = new ProgressDialog(context);
            this.medicamento = med;
            this.alarme = alarme;
            this.mc = new MedicamentoController(context);
            this.ac = new AlarmeController(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Salvando Medicamento...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(tipoTela == MedicamentoCadastro.TELA_CADASTRAR_MED){
                //Cadastra o medicamento
                Log.v("CadastrarMedicamento", "Iniciou");
                long id = mc.cadastrarMedicamento(medicamento);
                //Insere id do medicanento
                alarme.setIdMedicamento(id);
                Log.v("CadastrarAlarme", "Iniciou");
                //Ativa  e registra o alarme
                ac.registrarAlarmeAsync(alarme);
                return null;
            }else{
                //Tela de editar
                mc.atualizarMedicamento(medicamento);
                ac.atualizarAlarme(alarme);
                return null;
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(dialog!= null){
                dialog.dismiss();
                Log.v("SalvarMedicamento", "Terminu de executar");
            }
        }
    }


    private void salvarFoto(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String date = dateFormat.format(new Date());

        //Depois Adicionar o nome do medicamento
        //String photoFile = "Picture_" + date + ".jpg";

        /**
         * Guardar o nome do arquivo ao salvar a foto para inserir no medicamento/
         */
        photoFile = "Picture_" + date + ".jpg";

        //Deleta a foto anterior do medicamento;
        if(tipoTela == MedicamentoCadastro.TELA_EDITAR_MED){
            File path = MedicamentoCadastro.this.getFileStreamPath(medEdit.getFoto());
            if(path != null){
                path.delete();
                Log.v("Excluiu_a_Foto", "Excluiu a foto anterior");
            }
        }

        FileOutputStream fos = null;
        try {
            fos = MedicamentoCadastro.this.openFileOutput(photoFile, Context.MODE_PRIVATE);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            Log.v("CaminhoCompleto", fos.toString());
        } catch (Exception error) {
            Log.v("ErroAoSalvarImagem", "File" + photoFile + "not saved: "
                    + error.getMessage());
            Toast.makeText(MedicamentoCadastro.this, "Image could not be saved.", Toast.LENGTH_LONG).show();
        }finally {
            try {
                fos.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String retornaTextoRepeticao(){
        int tipo = radioGroup.getCheckedRadioButtonId();
        switch (tipo){
            case R.id.rbTodoDia:
                return "todos os dias";
            case R.id.rbDiaIntervalo:
                String texto = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                String [] dias = texto.split(":");
                return dias[1];
            case R.id.rbDiaEspec:
                String t = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                String [] d = t.split(":");
                return d[1];
            default:
                return "todos os dias";
        }


    }

    private void salvarMedicamento(){

        if(tipoTela != MedicamentoCadastro.TELA_EDITAR_MED){
            //Medicamento
            String nome = nomeMed.getText().toString();
            //Se não inserir dosagem a dosage será nula = '-1'
            int dosagem = dosagemMed.getText().toString().isEmpty() ? -1 : Integer.parseInt(dosagemMed.getText().toString());
            String tipoDosagem = (String) dosSpinner.getSelectedItem();
            boolean usoContinuo = radioGroupDias.getCheckedRadioButtonId() == R.id.rbContinuo ? true : false;
            String observacao = "Não possui";

            String foto = "null";
            if(temFoto){
                salvarFoto();
                foto = photoFile; //Caminho da foto
            }

            final Medicamento medicamento = new Medicamento(nome, dosagem, tipoDosagem, usoContinuo, observacao, foto);

            //Alarme
            Calendar dataInicio = Utils.DataDiaMesAnoToCalendar(dataInicialTv.getText().toString());
            Calendar dataFim = Utils.DataDiaMesAnoToCalendar(dataInicialTv.getText().toString());
            int periodo = medicamento.isUso_continuo() ? 1 : numeroDias;
            int tipoRepeticao = retornaTipoRepeticao();
            int intervalo = 1;
            if(tipoRepeticao == Alarme.REP_DIASINTERVALOS){
                intervalo = intevaloSel;
            }else if (tipoRepeticao == Alarme.REP_DIASDASEMANA){
                Weekdays days = Weekdays.fromStringDays(diasSelecionados);
                intervalo = days.getBits();
            }

            if(usoContinuo){
                dataFim.add(Calendar.YEAR, 2000);
            }else{
                dataFim.add(Calendar.DAY_OF_YEAR, periodo-1);
            }

            boolean status = true;
            //Trocar pela controllerMedicamento

            //Verificar se está pegando a informação certa
            String freqHorario = (String) spinnerHorario.getSelectedItem();
            String freqDias = retornaTextoRepeticao();


            //Vetor com os alarmes infos
            ArrayList<AlarmeInfo> alarmeInfos = new ArrayList<>();
            for(TextView tv : textViewsHorarios){
                String horario = tv.getText().toString();
                AlarmeInfo novo = new AlarmeInfo(horario, 1);
                alarmeInfos.add(novo);
            }

            //Cria um novo Alarme
            final Alarme alarme = new Alarme(dataInicio, dataFim,periodo,tipoRepeticao,intervalo,status,Alarme.ID_INVALIDA,freqHorario, freqDias);
            alarme.setAlarmeInfo(alarmeInfos);

            Log.v("datainicio", "Data = "+String.valueOf(dataInicio.get(Calendar.DAY_OF_MONTH) +
                    "/"+String.valueOf(dataInicio.get(Calendar.MONTH))+
                    "/"+String.valueOf(dataInicio.get(Calendar.YEAR))));
            Log.v("alarme", alarme.toString());
            Log.v("medicamento", medicamento.toString());
            Log.v("alameinfos", alarme.getAlarmeInfo().toString());


            /**
             * Salva o alarme no banco de dados e registra o alarme no sistema
             */

            SalvarMedicamentoAsyncTask task = new SalvarMedicamentoAsyncTask(MedicamentoCadastro.this, medicamento, alarme);
            task.execute();
        }else{
            medEdit.setNome(nomeMed.getText().toString());
            //Se não inserir dosagem a dosage será nula = '-1'
            int dosagem = dosagemMed.getText().toString().isEmpty() ? -1 : Integer.parseInt(dosagemMed.getText().toString());
            medEdit.setDosagem(dosagem);
            //Tipo Dosagem
            String tipoDosagem = (String) dosSpinner.getSelectedItem();
            medEdit.setTipoDosagem(tipoDosagem);

            boolean usoContinuo = radioGroupDias.getCheckedRadioButtonId() == R.id.rbContinuo ? true : false;
            medEdit.setUso_continuo(usoContinuo);

            String foto = "null";
            if(temFoto){
                salvarFoto();
                foto = photoFile; //Caminho da foto
            }
            medEdit.setFoto(foto);

            //Alarme
            Calendar dataInicio = Utils.DataDiaMesAnoToCalendar(dataInicialTv.getText().toString());
            Calendar dataFim = Utils.DataDiaMesAnoToCalendar(dataInicialTv.getText().toString());
            int periodo = medEdit.isUso_continuo() ? 1 : numeroDias;
            int tipoRepeticao = retornaTipoRepeticao();
            int intervalo = 1;
            if(tipoRepeticao == Alarme.REP_DIASINTERVALOS){
                intervalo = intevaloSel;
            }else if (tipoRepeticao == Alarme.REP_DIASDASEMANA){
                Weekdays days = Weekdays.fromStringDays(diasSelecionados);
                intervalo = days.getBits();
            }

            if(usoContinuo){
                dataFim.add(Calendar.YEAR, 2000);
            }else{
                dataFim.add(Calendar.DAY_OF_YEAR, periodo-1);
            }

            boolean status = true;
            String freqHorario = (String) spinnerHorario.getSelectedItem();
            String freqDias = retornaTextoRepeticao();

            //Vetor com os alarmes infos
            ArrayList<AlarmeInfo> alarmeInfos = new ArrayList<>();
            for(TextView tv : textViewsHorarios){
                String horario = tv.getText().toString();
                AlarmeInfo novo = new AlarmeInfo(horario, 1);
                alarmeInfos.add(novo);
            }

            alarmeEdit.setAlarmeInfo(alarmeInfos);
            alarmeEdit.setDataInicio(dataInicio);
            alarmeEdit.setDataFim(dataFim);
            alarmeEdit.setPeriodo(periodo);
            alarmeEdit.setTipoRepeticao(tipoRepeticao);
            alarmeEdit.setIntervalo(intervalo);
            alarmeEdit.setStatus(status);
            alarmeEdit.setFreqHorario(freqHorario);
            alarmeEdit.setFreqDias(freqDias);

            SalvarMedicamentoAsyncTask task = new SalvarMedicamentoAsyncTask(MedicamentoCadastro.this, medEdit, alarmeEdit);
            task.execute();

        }

        //Retorna para a activity pai se ela deve atualizar as suas informações
        //Utilizando o startResultActivity
        //Cria um intent para retorna o valor
        Intent i = new Intent();
        //Adiciona o resultado a ser comparado e a intent
        setResult(1, i);
        finish();
    }









}


