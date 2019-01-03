package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCODE extends AppCompatActivity implements ZXingScannerView.ResultHandler{


    private ZXingScannerView mScannerView;
    private int mCcameraId = -1;
    private Toolbar toolbar;
    private ZXingScannerView.ResultHandler resultHandler;
    private List<BarcodeFormat> formatosSuportados;
    private Result resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        //Adiciona a toolbar
        toolbar = (Toolbar) findViewById(R.id.tBQrCode);
        toolbar.setTitle("QR Code");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Para volta e fecha a camera. Gambiarra pois a bibioteca está com problema. Refatorar
                mScannerView.resumeCameraPreview(resultHandler);
                mScannerView.stopCamera();
                finish();
            }
        });

        //Inicia e adiciona na lista os tipos QRCode e DATA Matrix, isto serve para deixar o leitor
        formatosSuportados = new ArrayList<>();
        formatosSuportados.add(BarcodeFormat.QR_CODE);
        formatosSuportados.add(BarcodeFormat.DATA_MATRIX);

        //Referência Utilizada no método onclick do dialogo ao selecionar a opção sair do sistema
        resultHandler = this;
        ViewGroup contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCcameraId);
        //Habilita o AutoFoco da Camera
        mScannerView.setAutoFocus(true);
        //Limita a biblioteca a ler somente QrCode
        mScannerView.setFormats(formatosSuportados);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        try{
            if (result != null){
                //Passa o objeto do resultado.
                resultado = result;

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Resultado");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String url = resultado.getText().trim();
                        intent.setData(Uri.parse(url));

                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        finish();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

                builder.setMessage("Neste QRCode somente foi encontrado um link para realizar o acesso a um site. \n\nDeseja visitar o site?");
                AlertDialog dialog = builder.create();
                dialog.show();

                Log.v("QrCode", "Formato QRCode: "+result.getBarcodeFormat().toString()+" Texto: "+result.getText()+" RawBytes"+result.getBarcodeFormat().name());
                mScannerView.resumeCameraPreview(this);
                mScannerView.stopCamera();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
