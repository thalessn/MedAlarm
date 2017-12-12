package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.thales_silva_nascimento.alarmmed.R;

import java.util.Calendar;

/**
 * Created by Thales on 01/02/2017.
 */

public class diasIntervalos extends DialogFragment {

    private Button btnMenosInter;
    private Button btnMaisInter;
    private Button btnDiaInterCancel;
    private Button btnDiaInterDef;
    private TextView tDiasInter;
    private TextView tDiasCountInter;
    private int MaxIntervalo;
    private int MinIntervalo;
    private int AtualIntervalo;

    public diasIntervalos(){

    }

    //O termo new instance torna este método uma fábrica de fragmentos diasEspecíficos
    //Seguindo o padrão de projeto factory
    public static diasIntervalos newInstance() {
        diasIntervalos frag = new diasIntervalos();
        return frag;
    }

    //Interface Utilizada para enviar as informações para a activity host (No caso a activity medicamentoCadastro)
    public interface diasIntervalosListerner{
        public void onClickListenerPositivoDiasIntervalos(DialogFragment dialog, int intervalo);
        public void onClickListenerNegativoDiasIntervalos(DialogFragment dialog);
    }

    //Instância do objeto da interface para a entrega das informacoes a activity host
    diasIntervalosListerner interfaceListener;

    //Sobreescreve o método Fragment.onAttach() para instanciar o objeto interfaceListener
    //O código está sobreescrevendo duas vezes, pois para dispositvos abaixo da API 23 o parâmetro de entrado do método
    //onAttach é uma Activity já para API > 23 utiliza-se um Context.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Verifica se a API do android instalado no aparelho é maior que a API 22 (Lollipop). Se for return para sair e entrar no onAttach seguinte.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof diasIntervalos.diasIntervalosListerner) {
            interfaceListener = (diasIntervalos.diasIntervalosListerner) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement diasIntervalosListerner");
        }
    }

    //onAttach para API > 22
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof diasIntervalos.diasIntervalosListerner) {
            interfaceListener = (diasIntervalos.diasIntervalosListerner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement diasIntervalosListerner");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("AtualIntervalo", AtualIntervalo);
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Define o Intervalo Max, Min e Atual, para ser utilizado pela função gerencia intervalo
        MaxIntervalo = 100;
        MinIntervalo = 2;
        if(savedInstanceState == null) {
            AtualIntervalo = 2;
            Log.i("SavedInstanceState", "Entrou - NULL");
            Log.i("AtualIntervalo", String.valueOf(AtualIntervalo));
        }else{
            AtualIntervalo = savedInstanceState.getInt("AtualIntervalo");
            Log.i("SavedInstanceState", "ENTROU - NÃO NULL");
            Log.i("AtualIntervalo", String.valueOf(AtualIntervalo));
        }

        Bundle args = getArguments();
        if(args != null){
            AtualIntervalo = args.getInt("intervalo");
        }

        //Cria a Janela de Diálogo
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        //LayoutInflater para inflar o layout customizável Layout.diasIntervalos
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View para o layout customizado
        View alertView = inflater.inflate(R.layout.fragment_dias_intervalos, null);

        //TextView abaixo do "numberPicker"
        tDiasInter = (TextView) alertView.findViewById(R.id.tDiasInter);
        //TextView do contador
        tDiasCountInter = (TextView) alertView.findViewById(R.id.tDiasCountInter);
        //Botão 'menos' da tela
        btnMenosInter = (Button) alertView.findViewById(R.id.btnMenosInter);
        //Botão 'mais' da tela
        btnMaisInter = (Button) alertView.findViewById(R.id.btnMaisInter);

        //Função gerenciaIntervalo controla os textos de quantidade de dias e quais dias de repetição
        gerenciaIntervalo();

        //Onclick do botão 'menos'
        btnMenosInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Diminui o valor do intervalo atual. Esta variável é utilizada na funcao GerenciaIntervalo
                AtualIntervalo = AtualIntervalo -1;
                gerenciaIntervalo();
            }
        });
        //Onclick do botão 'mais'
        btnMaisInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aumenta o valor do intervalo atual. Esta variável é utilizada na funcao GerenciaIntervalo
                AtualIntervalo = AtualIntervalo +1;
                gerenciaIntervalo();
            }
        });

        //Botão Cancelar do Dialog
        btnDiaInterCancel = (Button) alertView.findViewById(R.id.btnDiaInterCancel);
        //Botão Definir do Dialog
        btnDiaInterDef = (Button) alertView.findViewById(R.id.btnDiaInterDef);

        //OnClickListener do botão Cancelar
        btnDiaInterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceListener.onClickListenerNegativoDiasIntervalos(diasIntervalos.this);
            }
        });

        //OnClickListener do botão Definir
        btnDiaInterDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceListener.onClickListenerPositivoDiasIntervalos(diasIntervalos.this, AtualIntervalo);
            }
        });

        //Adiciona o layout personalizado ao AlertDialog
        alert.setView(alertView);

        //Retorna o Dialogo criado
        return alert.create();
    }



    //Retorna a String Correspondente ao dia.
    private String diaSemana(int dia){
        switch (dia){
            case 1:
                return "dom";
            case 2:
                return "seg";
            case 3:
                return "ter";
            case 4:
                return "qua";
            case 5:
                return "qui";
            case 6:
                return "sex";
            case 7:
                return "sáb";
        }
        return null;
    }

    //Adiciona a quantidade de dias na tela e os dia de repetição de acordo com o dia da semana atual.
    private void setaTextoIntervaloTela(){
        //Pega o calendário atual
        Calendar cal = Calendar.getInstance();
        //Guarda o dia da data atual
        String dia = diaSemana(cal.get(Calendar.DAY_OF_WEEK));
        //Adiciona 'AtualIntervalo' de dias no calendário
        cal.add(Calendar.DATE, AtualIntervalo);
        //Guarda o dia da semana após o acrescimo de dias
        String dia1 = diaSemana(cal.get(Calendar.DAY_OF_WEEK));
        //Adiciona 'AtualIntervalo' de dias no calendário
        cal.add(Calendar.DATE, (AtualIntervalo));
        //Guarda o dia da semana após o acrescimo de dias
        String dia2 = diaSemana(cal.get(Calendar.DAY_OF_WEEK));
        //Adiciona 'AtualIntervalo' de dias no calendário
        cal.add(Calendar.DATE, (AtualIntervalo));
        //Guarda o dia da semana após o acrescimo de dias
        String dia3 = diaSemana(cal.get(Calendar.DAY_OF_WEEK));

        //Constrói a String com os dias da semana correspondente as datas
        String texto = dia + ", " + dia1 + ", " + dia2 + ", " + dia3+ ", ...";
        //Adiciona a string ao TextView
        tDiasInter.setText(texto);
        //Contrói a string do 'spinner'
        String count = "cada " + AtualIntervalo + " dias";
        //Adicona a string ao textview
        tDiasCountInter.setText(count);
    }
    //Função para gerenciar os TextView para quando o usuário adicionar ou remover dias de intervalo
    private void gerenciaIntervalo(){
        if(AtualIntervalo <= MinIntervalo){
            AtualIntervalo = MinIntervalo;
            setaTextoIntervaloTela();
        }
        if(AtualIntervalo >= MaxIntervalo){
            AtualIntervalo = MaxIntervalo;
            setaTextoIntervaloTela();        }

        if((AtualIntervalo >= MinIntervalo) && (AtualIntervalo <= MaxIntervalo)){
            setaTextoIntervaloTela();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        interfaceListener.onClickListenerNegativoDiasIntervalos(diasIntervalos.this);
    }
}
