package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.Utils;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ConfigSom extends AppCompatActivity {
    private Toolbar toolbar;
    private RadioGroup radioGroup;
    private RadioButton rbMusic;
    private RadioButton rbSelecionado;
    private HashMap<String,String> toques;
    private CountDownTimer timer;
    private Ringtone ringtone;
    private String uri_som;
    private String nome_som;
    private int rbanterior;
    private boolean alterou = false;
    private String nomeToquePersonalizado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_som);

        toolbar = (Toolbar) findViewById(R.id.tbConfigSom);
        toolbar.setTitle("Som");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alterou){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConfigSom.this);
                    alertDialog.setTitle("Tem Certeza?");
                    alertDialog.setMessage("Você tem certeza de sair sem salvar as alterações?");
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
                }else{
                    finish();
                }

            }
        });

        //Identifica o radiogroup
        radioGroup = (RadioGroup) findViewById(R.id.rgConfigSom);

        //Adiciona os radio buttons programatically
        adicionaRadioButton();

        //Verifica se já existe salvo alguma cofiguração.
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        uri_som = preferences.getString(getString(R.string.uri_som), "/system/alarm_alert");
        nome_som = preferences.getString(getString(R.string.nome_som), "AlarmePadrao");

        Log.v("uriSound", uri_som);
        Log.v("nameSound", nome_som);

        //Procura pelo audio salvo na sharedpreference
        for (int i = 0; i<radioGroup.getChildCount(); i++){
            View v = radioGroup.getChildAt(i);
            if(v!=null){
                if((v.getTag() != null) && (v.getTag().toString().equals(nome_som))){

                    //Verificia se é o toque personalizado se for edita o texto radio button.
                    if(v.getTag().toString().equals("Escolher")){
                        String parte1= "Personalize o toque sonoro\n";
                        String parte2= preferences.getString(getString(R.string.nome_toque_personalizado),"Adicione o seu toque favorito");
                        //Formata texto do radiobutton
                        SpannableStringBuilder spanBuilder = formataTexto(parte1, parte2);
                        //Adiciona texto no radio button
                        rbMusic.setText(spanBuilder);
                    }

                    //RadioButton selecionado - utilizado para facilitar a gravação das informações
                    rbSelecionado = (RadioButton) v;
                    radioGroup.check(v.getId());
                    rbanterior = v.getId();
                    break;
                }
            }
        }

        //Listener para verifica quando houver mudança de radio buttons.
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //Reponsável por indicar que houve uma modificação de radio button e deve exibir o diálogo
                alterou=true;

                //Verifica se o som já está tocando.
                if((ringtone != null) && (ringtone.isPlaying())){
                    ringtone.stop();
                    timer.cancel();
                }

                RadioButton rb = radioGroup.findViewById(i);
                String tag = rb != null ? rb.getTag().toString() : "";
                if(tag == "AlarmePadrao"){
                    rbSelecionado = rb;
                    if(rbanterior != rb.getId()){
                        Uri p = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                        rbanterior = rb.getId();
                        ringtone = RingtoneManager.getRingtone(ConfigSom.this, p);
                        timer = criaTimer(5000, 1000);
                        timer.start();
                    }
                }else if(tag == "Escolher"){
                    rbSelecionado = rb;
                    Intent intent_upload = new Intent();
                    intent_upload.setType("audio/*");
                    intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent_upload,1);

                }else{
                    rbSelecionado = rb;
                    if(rbanterior != rb.getId()) {
                        Uri uri = Uri.parse(toques.get(tag));
                        rbanterior = rb.getId();
                        //Toque
                        ringtone = RingtoneManager.getRingtone(ConfigSom.this, uri);
                        timer = criaTimer(5000, 1000);
                        timer.start();
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_salvar, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.iTMedSave:
                //Nome e uri do toque selecionado
                String uriSom = toques.get(rbSelecionado.getTag().toString());
                String nSom = rbSelecionado.getTag().toString();
                //Abri o arquivo de preferencia
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferences),Context.MODE_PRIVATE);
                //Inicia o editor
                SharedPreferences.Editor editor = sharedPref.edit();
                //Salva a uri do som
                editor.putString(getString(R.string.uri_som), uriSom);
                //Salva o nome do alarme
                editor.putString(getString(R.string.nome_som), nSom);
                //Verifica se o toque é personalizado se sim salvar o nome para exibir novamente
                if(nSom.equals("Escolher")) {
                    editor.putString(getString(R.string.nome_toque_personalizado), nomeToquePersonalizado);
                    Log.v("NomeTOque", "Passou");
                }
                //Salva as configurações
                editor.commit();
                //Encerra a atividade
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**Função responsável por adicionar os RadioButton em RunTime na tela */
    private void adicionaRadioButton(){
        //Primeiro botão
        rbMusic = new RadioButton(ConfigSom.this);
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(0,0,0,40);
        rbMusic.setLayoutParams(lp);
        //Cria a String Personalizada
        String parte1= "Personalize o toque sonoro\n";
        String parte2= "Adicione o seu toque favorito";
        //Formata texto do radiobutton
        SpannableStringBuilder spanBuilder = formataTexto(parte1, parte2);
        //Adiciona texto no radio button
        rbMusic.setText(spanBuilder);
        rbMusic.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        rbMusic.setTag("Escolher");
        //Adiciona o evento onclick no radio button
        rbMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //Adiciona no radiogroup
        radioGroup.addView(rbMusic);

        /**TextView Contendo o texto fazendo divisão*/
        TextView tv = new TextView(ConfigSom.this);
        LinearLayout.LayoutParams tvlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvlp.setMargins(0, 30, 0, 60);
        tv.setLayoutParams(tvlp);
        tv.setText("Toques do dispositivo");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        //Adiciona
        radioGroup.addView(tv);

        /**RadioButton Alarme Padrão */
        RadioButton rbAlarm = new RadioButton(ConfigSom.this);
        RadioGroup.LayoutParams rglpp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        rglpp.setMargins(0,20,0,20);
        rbAlarm.setLayoutParams(rglpp);
        rbAlarm.setText("Alarme Padrão");
        rbAlarm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        rbAlarm.setTag("AlarmePadrao");

        //Adiciona ao RadioGroup
        radioGroup.addView(rbAlarm);


        /** Preenche com os toques disponívéis no aparelho*/
        RingtoneManager rig = new RingtoneManager(ConfigSom.this);
        rig.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = rig.getCursor();

        //Map contendo os nomes e as repectivas uri's
        toques = new HashMap<>();

        //Percorre todos os toques padrões disponíveis no aparelho.
        while(cursor.moveToNext()){
            String nome = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String caminho = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            String id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            String uri = caminho+"/"+id;

            //Adiciona ao hashmap
            toques.put(nome,uri);

            RadioButton radio = new RadioButton(ConfigSom.this);
            RadioGroup.LayoutParams rglp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            rglp.setMargins(0,20,0,20);
            radio.setLayoutParams(rglp);
            radio.setText(nome);
            radio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            radio.setTag(nome);
            //Adiciona o radiobutton
            radioGroup.addView(radio);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == 1){
            if(data != null){
                if(resultCode == RESULT_OK){
                    try{
                        //the selected audio.
                        Uri uri = data.getData();
                        nomeToquePersonalizado = uri.getLastPathSegment();
                        SpannableStringBuilder spbuilder = formataTexto("Personalize o toque sonoro\n",nomeToquePersonalizado);
                        rbMusic.setText(spbuilder);
                        //adiciona a uri no hashmap
                        toques.put("Escolher",uri.getPath());
                        //Toque
                        ringtone = RingtoneManager.getRingtone(ConfigSom.this,uri);
                        timer = criaTimer(5000, 1000);
                        timer.start();
                        Log.v("Uri - ",uri.toString()+" - "+uri.getPath()+" - "+uri.getLastPathSegment());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }else{
            //Volta para o radiobutton anterior
            radioGroup.check(rbanterior);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private SpannableStringBuilder formataTexto(String texto1, String texto2){
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(texto1);
        spanBuilder.append(texto2);
        spanBuilder.setSpan(new RelativeSizeSpan(0.90f), (spanBuilder.length() - texto2.length()), spanBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanBuilder.setSpan(new ForegroundColorSpan(Color.GRAY),(spanBuilder.length() - texto2.length()), spanBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanBuilder;
    }

    private CountDownTimer criaTimer(long duracao, long intervalo){
        CountDownTimer t = new CountDownTimer(duracao, intervalo) {
            @Override
            public void onTick(long l) {
                ringtone.play();
            }

            @Override
            public void onFinish() {
                ringtone.stop();
            }
        };
        return t;
    }
}
