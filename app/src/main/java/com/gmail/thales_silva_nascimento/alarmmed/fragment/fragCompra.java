package com.gmail.thales_silva_nascimento.alarmmed.fragment;


import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.RecyclerItemClickListener;
import com.gmail.thales_silva_nascimento.alarmmed.activity.MainActivity;
import com.gmail.thales_silva_nascimento.alarmmed.adapter.CompraRecycleAdapter;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.util.ArrayList;
import java.util.List;


public class fragCompra extends Fragment implements ActionMode.Callback {

    private TextView texto;
    private RecyclerView recyclerView;
    private ActionMode actionMode;
    private boolean isMultiSelect = false;
    private List<Long> selectedIds = new ArrayList<>();
    private CompraRecycleAdapter adapter;
    private MedicamentoController medcontrol;


    public fragCompra() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_compra, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Altera o titulo da toolbar
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Comprar");

        texto = (TextView) view.findViewById(R.id.tvCompraTexto);

        medcontrol = new MedicamentoController(getContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.rvCompra);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        List<Medicamento> meds = medcontrol.listarTodosMedicamentos();
        if(meds.size()>0){
            texto.setVisibility(View.GONE);
        }

        adapter = new CompraRecycleAdapter(getContext(), medcontrol.listarTodosMedicamentos());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(isMultiSelect){
                    //if multiple selection is enabled then select item on single click else perform normal click on item.
                    multiSelect(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

                if (!isMultiSelect){
                    Log.v("NOT SElEcTED", "onItemLongClick");
                    selectedIds = new ArrayList<>();
                    isMultiSelect = true;

                    if (actionMode == null){
                        Log.v("actionMode=null", "onItemLongClick");
                        actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(fragCompra.this);
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
                Log.v("actionModeMultiSe", "!=null");
                if (selectedIds.contains(data.getId())){
                    Log.v("remedioId", String.valueOf(data.getId()));
                    selectedIds.remove(data.getId());
                }
                else{
                    Log.v("actionMode", "não nulo");
                    selectedIds.add(data.getId());
                }

                /**
                 * Adiciona o texto na Action bar
                 */
                if (selectedIds.size() > 0)
                    actionMode.setTitle(String.valueOf(selectedIds.size())+" selecionados"); //show selected item count on action mode.
                else{
                    actionMode.setTitle(""); //remove item count from action mode.
                    actionMode.finish(); //hide action mode.
                }
                adapter.setSelectedIds(selectedIds);
            }
        }
    }



    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Log.v("OnCreateActionMode", "Passou");
        mode.getMenuInflater().inflate(R.menu.toolbar_comprar_compartilhar, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.findItem(R.id.iTCompartilhar).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        switch (item.getItemId()){
            case R.id.iTCompartilhar:
                Toast.makeText(getContext(), "Botão",Toast.LENGTH_LONG).show();
//                Intent share = new Intent();
//                share.setType("text/plain");
//
//                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
//
//                share.putExtra(Intent.EXTRA_TEXT, "OI testando");
//                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                startActivity(Intent.createChooser(share, "Compartilhar remédios a comprar"));

                Intent share = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText("Oi seu pai tem boi")
                        .getIntent();
                //Verifica se existe algum aplicativo que receba esta intent. Para caso não possuir não gerar um exceção de ActivitynptFound
                if (share.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(share);
                }
                break;
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
}
