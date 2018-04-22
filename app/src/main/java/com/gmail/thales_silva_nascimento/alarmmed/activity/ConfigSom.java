package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.provider.MediaStore;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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

public class ConfigSom extends AppCompatActivity {
    private Toolbar toolbar;
    private RadioGroup radioGroup;
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConfigSom.this);
                alertDialog.setTitle("Tem Certeza?");
                alertDialog.setMessage("Você tem certeza de sair sem salvar o remédio?");
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

        radioGroup = (RadioGroup) findViewById(R.id.rgConfigSom);
        radioGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               View v = radioGroup.getChildAt(radioGroup.getCheckedRadioButtonId());
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });

        adicionaRadioButton();
    }

    private void adicionaRadioButton(){
        //LinearLayout ll = (LinearLayout) findViewById(R.id.llConfigSom);
        radioGroup = (RadioGroup) findViewById(R.id.rgConfigSom);

        //Primeiro botão
        RadioButton radio1 = new RadioButton(ConfigSom.this);
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(0,0,0,20);
        radio1.setLayoutParams(lp);
        radio1.setText("Personalize o toque sonoro\ntesdo do subtituolo");
        radio1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        radio1.setTag("primeiro");
        //Adiciona no radiogroup
        radioGroup.addView(radio1);

        //TextView Contendo o texto fazendo divisão
        TextView tv = new TextView(ConfigSom.this);
        LinearLayout.LayoutParams tvlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvlp.setMargins(0, 40, 0, 40);
        tv.setLayoutParams(tvlp);
        tv.setText("Toques Padrão");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        //Adiciona
        radioGroup.addView(tv);

        //Preenche com os toques disponívéis no aparelho
        RingtoneManager rig = new RingtoneManager(ConfigSom.this);
        rig.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = rig.getCursor();
        Log.v("Cursor: ", String.valueOf(cursor.moveToNext()));
        while(cursor.moveToNext()){
            String nome = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);

            RadioButton radio = new RadioButton(ConfigSom.this);
            RadioGroup.LayoutParams rglp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            rglp.setMargins(0,20,0,20);
            radio.setLayoutParams(rglp);
            radio.setText(nome);
            radio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            radio.setTag(nome);

            radioGroup.addView(radio);
        }
    }
}
