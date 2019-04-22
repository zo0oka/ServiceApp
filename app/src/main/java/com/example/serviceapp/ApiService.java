package com.example.serviceapp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("OpenWay/services/1/TestService")
    Call<ApiResponse> apiCall(@Field("p1") int p1);
}
