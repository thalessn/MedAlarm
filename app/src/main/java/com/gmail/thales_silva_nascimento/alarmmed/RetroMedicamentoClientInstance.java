package com.gmail.thales_silva_nascimento.alarmmed;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetroMedicamentoClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.1.22:3000/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
