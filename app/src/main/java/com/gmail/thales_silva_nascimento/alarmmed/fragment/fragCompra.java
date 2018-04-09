package com.gmail.thales_silva_nascimento.alarmmed.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.RecyclerItemClickListener;
import com.gmail.thales_silva_nascimento.alarmmed.activity.MainActivity;
import com.gmail.thales_silva_nascimento.alarmmed.adapter.CompraRecycleAdapter;
import com.gmail.thales_silva_nascimento.alarmmed.controller.ItemCompraController;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemCompra;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fragCompra extends Fragment implements ActionMode.Callback {

    private ActionMode actionMode;
    private boolean isMultiSelect = false;
    //i created List of int type to store id of data, you can create custom class type data according to your need.
    private List<Long> selectedIds = new ArrayList<>();
    private CompraRecycleAdapter adapter;
    private TextView texto;
    private HashMap<Long, Medicamento> medsSelecionados;



    public fragCompra() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_compra, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        texto = (TextView) view.findViewById(R.id.tvCompraTexto);

        medsSelecionados = new HashMap<Long, Medicamento>();

        //Altera o título da toolbar
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Comprar");


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvCompra);
        adapter = new CompraRecycleAdapter(getContext(), getList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect){
                    //if multiple selection is enabled then select item on single click else perform normal click on item.
                    multiSelect(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect){
                    selectedIds = new ArrayList<>();
                    isMultiSelect = true;

                    if (actionMode == null){
                        actionMode = ((AppCompatActivity) getActivity()).startActionMode(fragCompra.this);
                    }
                }

                multiSelect(position);
            }
        }));

    }

    private void multiSelect(int position) {
        Medicamento data = adapter.getItem(position);
        if (data != null){
            if (actionMode != null) {

                if (selectedIds.contains(data.getId())){
                    selectedIds.remove(data.getId());
                    medsSelecionados.remove(data.getId());
                }
                else{
                    selectedIds.add(data.getId());
                    medsSelecionados.put(data.getId(), data);
                }


                if (selectedIds.size() > 0){
                    if(selectedIds.size() == 1){
                        actionMode.setTitle("1 selecionado");
                    }else{
                        actionMode.setTitle(String.valueOf(selectedIds.size())+" selecionados"); //show selected item count on action mode.
                    }

                }
                else{
                    actionMode.setTitle(""); //remove item count from action mode.
                    actionMode.finish(); //hide action mode.
                }
                adapter.setSelectedIds(selectedIds);
            }
        }
    }

    private List<ItemCompra> getList(){
        ItemCompraController ic = new ItemCompraController(getContext());
        List<ItemCompra> list = ic.listarMedicamentosComprar();
        if(list.size() > 0){
            texto.setVisibility(View.GONE);
        }

        return list;
    }



    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.toolbar_comprar_compartilhar, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {

        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.iTCompartilhar:

                Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(criaListaCompra())
                        .getIntent();
                if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(shareIntent);
                }
                actionMode.finish();

                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        isMultiSelect = false;
        selectedIds = new ArrayList<>();
        adapter.setSelectedIds(new ArrayList<Long>());
    }

    public String criaListaCompra(){
        if(selectedIds.size()>0){
            StringBuilder stringBuilder = new StringBuilder();
            for (Long i : medsSelecionados.keySet()){
                Medicamento a = medsSelecionados.get(i);
                String nome = a.getNome();
                String dosagem = a.getDosagem() != -1 ? String.valueOf(a.getDosagem()) : "";
                if(dosagem.isEmpty()){
                    stringBuilder.append("° "+nome);
                    stringBuilder.append("\n");
                }else{
                    stringBuilder.append("° "+nome+" - "+dosagem+" "+a.getTipoDosagem());
                    stringBuilder.append("\n");
                }
            }
            return stringBuilder.toString();
        }
        return null;
    }
}
