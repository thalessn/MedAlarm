package com.gmail.thales_silva_nascimento.alarmmed.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.gmail.thales_silva_nascimento.alarmmed.GetMedicamentoService;
import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.RetroMedicamentoClientInstance;
import com.gmail.thales_silva_nascimento.alarmmed.model.RetroMedicamento;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MedicamentoAutoCompleteAdapter extends BaseAdapter implements Filterable {
    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private List<RetroMedicamento> resultList;

    public MedicamentoAutoCompleteAdapter(Context context){
        this.mContext = context;
        resultList = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int i) {
        return resultList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dropdown_medicamento, viewGroup, false);
        }
        TextView tv = view.findViewById(R.id.acnome);
        TextView concentracao = view.findViewById(R.id.acconcentracao);
        //Adiciona o nome
        tv.setText(resultList.get(i).getNomeGen());
        //Adiciona nome da composição.
        concentracao.setText(resultList.get(i).getConcentracao());

        String []formaText = resultList.get(i).getForma().split(" ");


        //Glide.with(context).load(R.drawable.remedio1).into(holder.img);
        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if(charSequence != null){
                    //Intancia um objeto da interface GetMedicamentoService Utilizando a fábrica de objetos Retrofit
                    GetMedicamentoService service = RetroMedicamentoClientInstance.getRetrofitInstance().create(GetMedicamentoService.class);
                    //Executa a requisição utilizando o objeto da interface GetMedicamentoService
                    Call<List<RetroMedicamento>> call = service.getTodosMedicamentos(charSequence.toString());
                    call.enqueue(new Callback<List<RetroMedicamento>>() {
                        @Override
                        public void onResponse(Call<List<RetroMedicamento>> call, Response<List<RetroMedicamento>> response) {
                            resultList.clear();
                            for(RetroMedicamento med : response.body()){
                                resultList.add(med);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<RetroMedicamento>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if((filterResults != null) && (filterResults.count > 0)){
                    notifyDataSetChanged();
                }else{
                    Log.v("publishResults", "Else não atualizou");
                    if(resultList.size() > 0){
                        resultList.clear();
                        notifyDataSetChanged();
                    }
                }
            }
        };
        return filter;
    }

    private int selecionaImagemMed(String [] forma){

        String nome = forma[0].toUpperCase();

        switch (nome){
            case "COMPRIMIDO":
                break;
            case "AEROSOL":
                break;
            case "CÁPSULA":
                break;
            case "CREME":
                break;
            case "DRÁGEA":
                break;
            case "ELIXIR":
                break;
            case "EMULSÃO":
                break;
            case "ENEMA":
                return R.drawable.ic_enema;
            case "GEL":
                break;
            case "GRANULADO":
                break;
            case "LOÇÃO":
                break;
            case "PÓ":
                break;
            case "POMADA":
                break;
            case "SOLUÇÂO":
                //Tem vário tipos verificar cada um
                break;
            case "SUPOSITÓRIO":
                break;
            case "SUSPENSÃO":
                break;
            case "XAMPU":
                break;
            case "XAROPE":
                break;
            case "ESMALTE":
                break;
            default:
                break;
        }

    }
}
