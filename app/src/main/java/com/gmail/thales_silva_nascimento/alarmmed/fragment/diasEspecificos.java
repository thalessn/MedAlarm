package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by thales on 28/01/17.
 */

public class diasEspecificos extends DialogFragment {

    private Button btnDiaEspCancel;
    private Button btnDiaEspDef;
    private ArrayList<String> dSelecionados;


    /** Maps calendar weekdays to the bit masks that represent them in this class. */
    private static final Map<String, Integer> DayToId;
    static {
        final Map<String, Integer> map = new android.support.v4.util.ArrayMap<>(7);
        map.put("seg",    R.id.cbSeg);
        map.put("ter",   R.id.cbTer);
        map.put("quar", R.id.cbQuar);
        map.put("quin",  R.id.cbQuin);
        map.put("sex",    R.id.cbSex);
        map.put("sáb",  R.id.cbSab);
        map.put("dom",    R.id.cbDom);
        DayToId = Collections.unmodifiableMap(map);
    }

    public diasEspecificos(){
        //Construtor vazio é requerido pelo DialogFragment
    }


    //Interface Utilizada para enviar as informações para a activity host (No caso a activity medicamentoCadastro)
    public interface diasEspecificosListener{
        public void onClickListenerPositivoDiasEspecificos(DialogFragment dialog, ArrayList<String> diasSelecionados);
        public void onClickListenerNegativoDiasEspecificos(DialogFragment dialog);
    }

    //Instância do objeto da interface para a entrega das informacoes a activity host
    diasEspecificosListener interfaceListener;


    //Sobreescreve o método Fragment.onAttach() para instanciar o objeto interfaceListener
    //O código está sobreescrevendo duas vezes, pois para dispositvos abaixo da API 23 o parâmetro de entrado do método
    //onAttach é uma Activity já para API > 23 utiliza-se um Context.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Verifica se a API do android instalado no aparelho é maior que a API 22 (Lollipop). Se for return para sair e entrar no onAttach seguinte.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof diasEspecificosListener) {
            interfaceListener = (diasEspecificosListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement diasEspecificosListener");
        }
    }

    //onAttach para API > 22
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof diasEspecificosListener) {
            interfaceListener = (diasEspecificosListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement diasEspecificosListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Cria a Janela de Dialogo
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        //LayoutInflater para inflar o layout customizável Layout.diasEspecíficos
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View customizada do alertDialog
        View alertView = inflater.inflate(R.layout.fragment_dias_especificos, null);

        //Define o Arraylist que conterá as strings dos CheckBoxes selecionados
        dSelecionados = new ArrayList<String>();

        Bundle dias = getArguments();
        if(dias != null){
            Log.v("DiasSelecionados", "Encontrou dias selecionados");
            String teste = dias.getString("dias");
            teste = teste.replaceAll(" ", "");
            String []split = teste.split(",");
            for(int i =0; i< split.length; i++){
                Log.v("Dia", split[i]);
                CheckBox checkBox = (CheckBox) alertView.findViewById(DayToId.get(split[i]));
                if(checkBox!=null) checkBox.toggle();
            }
        }


        //Botão Cancelar do AlertDialog
        btnDiaEspCancel = (Button) alertView.findViewById(R.id.btnDiaEspCancel);

        //Botão Definir do AlertDialog
        btnDiaEspDef = (Button) alertView.findViewById(R.id.btnDiaEspDef);

        //OnClickListener do botão Cancelar
        btnDiaEspCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceListener.onClickListenerNegativoDiasEspecificos(diasEspecificos.this);
            }
        });

        //OnClickListener do botão Definir do ALertDialog
        btnDiaEspDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pega a referência do LinearLayout que contém as checkbox dos dias da semana no Layout personalizado
                //Para isto faz-se um cast do dialog (Parâmentro da função) o tornando um AlertDialog para poder utilizar o método findViewById()
                LinearLayout ll = (LinearLayout) getDialog().findViewById(R.id.llTest);
                //Verifica se o dSeleciona já possui objetos nele se sim apaga para receber os novos
                if(dSelecionados.size() > 0){
                    dSelecionados.clear();
                }

                //getChildCount retorna o número de views contido neste viewParent, neste caso o LinearLayout do AlertDialog
                for(int i = 0; i < ll.getChildCount(); i++){
                    //Objeto para auxiliar na checagem
                    CheckBox checar = (CheckBox) ll.getChildAt(i);
                    if(checar.isChecked()){
                        //Se for selecionado adiciona no Arraylist
                        dSelecionados.add(checar.getText().toString());
                    }
                }
                //Verifica se o usuário selecionou algum dia da semana
                if(dSelecionados.size() <= 0){
                    Toast.makeText(getContext(), "Selecione um dia da semana", Toast.LENGTH_LONG).show();
                }else{
                    interfaceListener.onClickListenerPositivoDiasEspecificos(diasEspecificos.this, dSelecionados);
                }

            }
        });

        //Adiciona a view customizada ao AlertDialog
        alert.setView(alertView);
        //Retorna o Dialog Customizado Criado
        return alert.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        interfaceListener.onClickListenerNegativoDiasEspecificos(diasEspecificos.this);
    }
}
