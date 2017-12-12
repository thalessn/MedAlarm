package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gmail.thales_silva_nascimento.alarmmed.dao.EspecialidadeDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medico;

import java.util.List;

/**
 * Created by Thales on 10/02/2017.
 */

public class MedicoAdapter extends BaseAdapter {

    private List<Medico> medicos;
    private Context context;

    public MedicoAdapter(List<Medico> medicos, Context context){
        this.medicos = medicos;
        this.context = context;
    }

    //Retorna o Tamanho do List medicos
    @Override
    public int getCount() {
        return medicos.size();
    }


    //Retorna o objeto na posição passada por parâmentro
    @Override
    public Object getItem(int position) {

        return medicos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Medico medico = medicos.get(position);
        View view;

        //O convertView fica retornando a view criada anteriormente. Para agilizar o app fazemos essa verificação.
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_row_medico, null);
        }else{
            view = convertView;
        }

        //Associa os TextViews
        TextView nomeMedico = (TextView) view.findViewById(R.id.nomeLvMedico);
        TextView especialidade = (TextView) view.findViewById(R.id.especLvMedico);

        //Atribui o nome
        nomeMedico.setText(medico.getNome());

        //Busca o nome da especialidade pelo id
        EspecialidadeDAO especDAO = EspecialidadeDAO.getInstance(context);

        String espec = especDAO.encontrarNomeEspecId(medico.getIdEspec()+1);
        //Atribui a especialidade
        especialidade.setText(espec);

        return view;
    }

    public void updateAdapter(List<Medico> medicos){
        this.medicos = medicos;
        this.notifyDataSetChanged();
    }
}
