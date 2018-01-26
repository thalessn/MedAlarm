package com.gmail.thales_silva_nascimento.alarmmed.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.adapter.MedicoAdapter;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.activity.activity_medicoCadastro;
import com.gmail.thales_silva_nascimento.alarmmed.activity.activity_medicoDetalhe;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medico;

import java.util.ArrayList;
import java.util.List;


public class fragMedico extends Fragment {

    private ListView listView;
    private List<Medico> listaMedicos;
    private FloatingActionButton fab;
    private MedicoController medicoController;
    private MedicoAdapter medicoAdapter;
    private static final int CODIGO_RESULT_ACTIVITY = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inicializa a lista de médico ao criar o fragmento
        listaMedicos = new ArrayList<>();
    }

    public fragMedico() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frag_medico, container, false);
    }


    //Método chamado após a criação da view. A view criada é passada como parâmetro
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Médicos");

        //floating Button
        fab = (FloatingActionButton) view.findViewById(R.id.testefab);
        //Adiciona o OnclickListener no botão
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cria intent para chamar a activity de cadastro
                Intent i = new Intent(getContext(), activity_medicoCadastro.class);
                //Diz para a activity que será a tela do tipo cadastro
                i.putExtra("tipoTela", 1);
                //Startforresult - permiti retornar um valor quando a activity medicoCadastro for finalizada
                //Recebe como parâmetro a intent a ser chamada e o código a ser utilizado.
                startActivityForResult(i, CODIGO_RESULT_ACTIVITY);
            }
        });


        listView = (ListView) view.findViewById(R.id.lvMedico);

        //Inicializa a controladora do médicos
        medicoController = MedicoController.getInstance(view.getContext());

        //Listar Todos os médicos
         listaMedicos = medicoController.listarTodosMedicos();
        //Inicializa o Adapter do medico para listView
        medicoAdapter = new MedicoAdapter(listaMedicos, view.getContext());

        //Seta um adapter do tipo MedicoAdapter, que contém a view customizada para cada linha da listview
        listView.setAdapter(medicoAdapter);

        //Onclick no itens da lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                //Cria uma nova activity do tipo medicoDetalhe
                Intent intent = new Intent(view.getContext(), activity_medicoDetalhe.class);
                //Passa o médico seleciona para a nova activity - Isto é possivel graças ao Parcelable
                intent.putExtra("medico", listaMedicos.get(position));
                startActivityForResult(intent, CODIGO_RESULT_ACTIVITY);
            }
        });

    }

    @Override
    public void onActivityResult(int codigo, int resultado, Intent intent) {
        //super.onActivityResult(codigo, resultado, intent);
        if(codigo == CODIGO_RESULT_ACTIVITY){
            if(intent != null){
                if(resultado == 1){
                    atualizaListViewMedico();
                }
            }
        }
    }
    //Método utilizado para atualizar o listView de médicos
    private void atualizaListViewMedico(){
        //Apaga o conteúdo da lista
        listaMedicos.clear();
        //Preeche novamente a listo já com o novo médico
        listaMedicos = medicoController.listarTodosMedicos();
        //Atualiza a ListView atualizando a lista do medicoAdapter associado
        medicoAdapter.updateAdapter(listaMedicos);
    }
}
