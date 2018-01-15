package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;


public class reabastecerRemedio extends DialogFragment {

    private Button btnCancelar;
    private Button btnOk;
    private TextInputEditText editQtdAdd;
    private TextView tvQtdRestantes;
    private long idMedicamento;
    private MedicamentoController mc;
    private Medicamento med;

    public reabastecerRemedio(){

    }

    public interface reabastecerRemedioListener{
        public void onClickListenerPositivoReabastecerRemedio(DialogFragment dialog, int qtd);
        public void onClickListenerNegativoReabastecerRemedio(DialogFragment dialog);
    }

    reabastecerRemedioListener interfaceListener;

    //Sobreescreve o método Fragment.onAttach() para instanciar o objeto interfaceListener
    //O código está sobreescrevendo duas vezes, pois para dispositvos abaixo da API 23 o parâmetro de entrado do método
    //onAttach é uma Activity já para API > 23 utiliza-se um Context.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Verifica se a API do android instalado no aparelho é maior que a API 22 (Lollipop). Se for return para sair e entrar no onAttach seguinte.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof reabastecerRemedio.reabastecerRemedioListener) {
            interfaceListener = (reabastecerRemedio.reabastecerRemedioListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement diasEspecificosListener");
        }
    }

    //onAttach para API > 22
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof reabastecerRemedio.reabastecerRemedioListener) {
            interfaceListener = (reabastecerRemedio.reabastecerRemedioListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement diasEspecificosListener");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Consegue receber argumento da atividade por este método.
        idMedicamento = getArguments().getLong("idMedicamento");
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Cria a Janela de Dialogo
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        //LayoutInflater para inflar o layout customizável Layout.diasEspecíficos
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View customizada do alertDialog
        View alertView = inflater.inflate(R.layout.fragment_reabastecer_remedio, null);

        btnOk = (Button) alertView.findViewById(R.id.btnOkReabastecer);
        btnCancelar = (Button) alertView.findViewById(R.id.btnCancelarReabastecer);
        editQtdAdd = (TextInputEditText) alertView.findViewById(R.id.editAddQtd);
        tvQtdRestantes = (TextView) alertView.findViewById(R.id.tvQtdRestante);

        //Medicamento controller
        mc = new MedicamentoController(getContext());
        med = mc.buscarMedicamentoPorId(idMedicamento);

        //Verifica se existe remédio
        if(med == null){
            return null;
        }

        //Atribui texto ao textview
        String texto = null;
        if(med.getQuantidade() == -1) texto = "Resta 0 remédio";
        else if(med.getQuantidade() < 2) texto = "Resta " +med.getQuantidade()+ " remédio";
        else texto = "Restam " +med.getQuantidade()+ " remédios";
        tvQtdRestantes.setText(texto);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adicionar = editQtdAdd.getText().toString();
                if(adicionar.isEmpty()){
                    editQtdAdd.setFocusable(true);
                    editQtdAdd.requestFocus();
                    return;
                }
                int q = med.getQuantidade() < 0 ? 0 : med.getQuantidade();
                int Add = Integer.parseInt(adicionar) + q;
                //Atualiza o banco de dados
                mc.updateQtdMedicamento(idMedicamento, Add);
                interfaceListener.onClickListenerPositivoReabastecerRemedio(reabastecerRemedio.this, Add);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               interfaceListener.onClickListenerNegativoReabastecerRemedio(reabastecerRemedio.this);
            }
        });

        alert.setView(alertView);
        return alert.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        interfaceListener.onClickListenerNegativoReabastecerRemedio(reabastecerRemedio.this);
        super.onCancel(dialog);
    }

}
