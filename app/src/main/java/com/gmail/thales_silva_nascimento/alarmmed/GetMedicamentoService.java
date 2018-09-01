package com.gmail.thales_silva_nascimento.alarmmed;

import com.gmail.thales_silva_nascimento.alarmmed.model.RetroMedicamento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface GetMedicamentoService {
    @GET("/medicamentos/{nome}")
    Call<List<RetroMedicamento>> getTodosMedicamentos(@Path("nome") String nome);

}
