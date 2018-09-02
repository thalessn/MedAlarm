package com.gmail.thales_silva_nascimento.alarmmed.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
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
        TextView formaFar = view.findViewById(R.id.acforma);

        //Adiciona o nome
        tv.setText(resultList.get(i).getNomeGen());

        //Adiciona nome da composição.
        concentracao.setText(resultList.get(i).getConcentracao());

        //Adiciona a forma
        formaFar.setText(resultList.get(i).getForma());

        //Imagem da forma
        ImageView img = view.findViewById(R.id.acimg);

        String []formaText = resultList.get(i).getForma().split(" ");
        Log.v("Forma:",formaText[0]);
        //função que seleciona qual imagem deve ser mostrada
        Glide.with(mContext).load(selecionaImagemMed(formaText)).into(img);
        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if(charSequence != null){
                    Log.v("TamanhoChar:", String.valueOf(charSequence.length()));
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
                        Log.v("QtdEncontrado", String.valueOf(resultList.size()));

                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if((filterResults != null) && (filterResults.count > 0)){
                    notifyDataSetChanged();
                }else{
                    Log.v("publishResults", "Else não atualizou");
                    resultList.clear();
                    /*if(resultList.size() > 0){
                        resultList.clear();
                        notifyDataSetChanged();
                    }*/
                }
            }
        };
        return filter;
    }

    private int selecionaImagemMed(String [] forma){

        String nome = forma[0].toUpperCase();

        switch (nome){
            case "COMPRIMIDO":
                return R.drawable.ic_comprimido2;
            case "AEROSOL":
                return R.drawable.ic_spray2;
            case "CÁPSULA":
                return  R.drawable.ic_capsula;
            case "CREME":
                return R.drawable.ic_pomada2;
            case "DRÁGEA":
                return R.drawable.ic_dragea2;
            case "ELIXIR":
                return R.drawable.ic_elixir;
            case "EMULSÃO":
                return R.drawable.ic_emulsao_oral;
            case "ENEMA":
                return R.drawable.ic_enema;
            case "GEL":
                return R.drawable.ic_pomada2;
            case "GRANULADO":
                return R.drawable.ic_padrao;
            case "LOÇÃO":
                return R.drawable.ic_pomada2;
            case "PÓ":
                return R.drawable.ic_padrao;
            case "POMADA":
                return R.drawable.ic_pomada2;
            case "SOLUÇÂO":
                return R.drawable.ic_padrao;
            case "SUPOSITÓRIO":
                return R.drawable.ic_padrao;
            case "SUSPENSÃO":
                return R.drawable.ic_padrao;
            case "XAMPU":
                return R.drawable.ic_shampoo;
            case "XAROPE":
                return R.drawable.ic_xarope;
            case "ESMALTE":
                break;
            case "COLUTÓRIO":
                return R.drawable.ic_spray2;
            default:
                return R.drawable.ic_padrao;
        }
        return R.drawable.ic_padrao;
    }
}
