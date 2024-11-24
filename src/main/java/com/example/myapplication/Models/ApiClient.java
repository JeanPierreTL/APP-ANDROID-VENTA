package com.example.myapplication.Models;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://dniruc.apisperu.com/")  // URL base de la API
                    .addConverterFactory(GsonConverterFactory.create())  // Convertir respuestas JSON
                    .build();
        }
        return retrofit;
    }
}
