package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.gmail.thales_silva_nascimento.alarmmed.R;

public class Creditos extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        toolbar = (Toolbar) findViewById(R.id.tbCreditos);
        toolbar.setTitle("Créditos");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webview = (WebView) findViewById(R.id.wvCredito);
        //webview.loadUrl("https://www.uol.com.br/");
        webview.loadUrl("file:///android_asset/credito.html");
    }
}
