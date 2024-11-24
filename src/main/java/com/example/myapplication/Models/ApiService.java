package com.example.myapplication.Models;
import com.example.myapplication.Models.DniResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {

    // Método para consultar el RUC
    @GET("api/v1/ruc/{ruc}")
    Call<RucResponse> getRucInfo(@Path("ruc") String ruc, @Query("token") String token);

    // Método para consultar el DNI
    @GET("api/v1/dni/{dni}")
    Call<DniResponse> getDniInfo(@Path("dni") String dni, @Query("token") String token);
}

