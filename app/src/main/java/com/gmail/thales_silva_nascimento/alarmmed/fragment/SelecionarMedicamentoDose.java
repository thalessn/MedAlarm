package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.activity.AdicionarDose;
import com.gmail.thales_silva_nascimento.alarmmed.adapter.MedicamentosDoseAdapter;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.util.List;


public class SelecionarMedicamentoDose extends DialogFragment implements MedicamentosDoseAdapter.OnItemClickListener {
    private TextView textView;
    private RecyclerView recyclerView;
    private MedicamentoController medicamentoController;
    private List<Medicamento> medicamentos;
    private static final int CODIGO_RESULT_ACTIVITY = 1;

    public SelecionarMedicamentoDose(){

    }

    //Interface Utilizada para enviar as informações para a activity host (No caso a activity medicamentoCadastro)
    public interface SelecionaMedicamentoDoseListerner{
        public void onCancelFragSelecionaMedicamentoDose(DialogFragment dialog);
    }

    //Instância do objeto da interface para a entrega das informacoes a activity host
    SelecionaMedicamentoDoseListerner interfaceListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Verifica se a API do android instalado no aparelho é maior que a API 22 (Lollipop). Se for return para sair e entrar no onAttach seguinte.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof SelecionarMedicamentoDose.SelecionaMedicamentoDoseListerner) {
            interfaceListener = (SelecionarMedicamentoDose.SelecionaMedicamentoDoseListerner) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement diasIntervalosListerner");
        }
    }

    //onAttach para API > 22
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelecionarMedicamentoDose.SelecionaMedicamentoDoseListerner) {
            interfaceListener = (SelecionarMedicamentoDose.SelecionaMedicamentoDoseListerner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement diasIntervalosListerner");
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
        View alertView = inflater.inflate(R.layout.fragment_selecionar_medicamento_dose, null);

        //Views
        textView = (TextView) alertView.findViewById(R.id.textSelecionar);
        recyclerView = (RecyclerView) alertView.findViewById(R.id.rvSelecionarMed);

        //Controladora dos medicamentos
        medicamentoController = new MedicamentoController(getContext());
        medicamentos = medicamentoController.listarTodosMedicamentos();

        //Adapter do recycle view
        MedicamentosDoseAdapter adapter = new MedicamentosDoseAdapter(getContext(), medicamentos);

        //Adiciona o método OnItemClickListener no adapter
        adapter.setClickListener(this);

        //Seta o adapter ao recycle view
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        alert.setView(alertView);

        return alert.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.v("SelecionarMedDose", "Cancelou");
        interfaceListener.onCancelFragSelecionaMedicamentoDose(SelecionarMedicamentoDose.this);
    }

    //Método da interface do MedicamentoDoseAdapter. Quando o usuário click em um item do adapter.
    //Chama a activity de adicionar dose
    @Override
    public void onClick(View view, int position) {
        Medicamento med = medicamentos.get(position);
        Intent i = new Intent(getContext(), AdicionarDose.class);
        i.putExtra("medicamento", med);
        startActivityForResult(i, CODIGO_RESULT_ACTIVITY);
    }

    @Override
    public void onActivityResult(int codigo, int resultado, Intent intent) {
        super.onActivityResult(codigo, resultado, intent);
        if(codigo == CODIGO_RESULT_ACTIVITY){
            if(intent != null){
                if(resultado == 1){
                    Log.v("Terminou", " e salvou");
                    interfaceListener.onCancelFragSelecionaMedicamentoDose(SelecionarMedicamentoDose.this);
                }else{
                    Log.v("Terminou", " e não salvou");
                    interfaceListener.onCancelFragSelecionaMedicamentoDose(SelecionarMedicamentoDose.this);
                }
            }
        }
        //Encerra o fragment caso o usuário desista de selecionar um medicamento
        interfaceListener.onCancelFragSelecionaMedicamentoDose(SelecionarMedicamentoDose.this);
    }
}
