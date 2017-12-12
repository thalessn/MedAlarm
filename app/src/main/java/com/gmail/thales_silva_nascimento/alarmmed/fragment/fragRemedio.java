package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.thales_silva_nascimento.alarmmed.MedicamentoAdapter;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.activity.MedicamentoCadastro;
import com.gmail.thales_silva_nascimento.alarmmed.activity.MedicamentoDetalhe;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.util.List;


public class fragRemedio extends Fragment implements MedicamentoAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private List<Medicamento> medicamentos;
    private FloatingActionButton fab;
    private MedicamentoController medicamentoController;
    private MedicamentoAdapter adapter;
    private static final int CODIGO_RESULT_ACTIVITY = 1;

    public fragRemedio() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        medicamentoController = new MedicamentoController(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_remedio, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Altera o Titulo da atividade quando este fragment aparece
        getActivity().setTitle("Remédios");

        fab = (FloatingActionButton) view.findViewById(R.id.fabMedicamento);
        medicamentos = medicamentoController.listarTodosMedicamentos();
        Log.v("Tamanho lista", String.valueOf(medicamentos.size()));

        recyclerView = (RecyclerView) view.findViewById(R.id.rvMedicamento);
        LinearLayoutManager ll = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(ll);
        recyclerView.setHasFixedSize(true);

        //Adapter RecycleView
        adapter = new MedicamentoAdapter(medicamentos, view.getContext());
        //Set a interface OnItemClickListener ao adapter, para obter o método OnClick de uma view do adapter
        adapter.setClickListener(this);
        //Adiciona o adapter ao recyclerView
        recyclerView.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MedicamentoCadastro.class);
                i.putExtra("tipoTela", MedicamentoCadastro.TELA_CADASTRAR_MED);
                startActivityForResult(i, CODIGO_RESULT_ACTIVITY);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int codigo, int resultado, Intent intent) {
        //super.onActivityResult(codigo, resultado, intent);
        if(codigo == CODIGO_RESULT_ACTIVITY){
            if(intent != null){
                if(resultado == 1){ //Atualiza
                   Log.v("Resultado", "Salvou devo atualizar");
                    atualizaRecyclerViewMedicamento();
                }
            }
        }
    }


    private void atualizaRecyclerViewMedicamento(){
        //Apaga o conteúdo da lista
        medicamentos.clear();
        //Preeche novamente a listo já com o novo médico
        medicamentos = medicamentoController.listarTodosMedicamentos();
        //Atualiza a ListView atualizando a lista do medicoAdapter associado
        adapter.updateAdapter(medicamentos);
    }

    /**
     * Método responsável por responder ao click de um Item no RecyclerView utilizando a interface do MedicamentoAdapter
     * @param view
     * @param position
     */

    @Override
    public void onClick(View view, int position) {
        Medicamento med = medicamentos.get(position);
        Intent i = new Intent(getContext(), MedicamentoDetalhe.class);
        i.putExtra("medicamento", med);
        startActivityForResult(i, CODIGO_RESULT_ACTIVITY);
    }
}
