package com.gmail.thales_silva_nascimento.alarmmed;


import com.gmail.thales_silva_nascimento.alarmmed.model.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitMaps {
    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("api/place/nearbysearch/json?key=AIzaSyBY3_86PKW7YqUs_jjBtzagCcdZlJqNWHg")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

}
