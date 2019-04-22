package com.example.serviceapp;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://41.32.255.228:9090/";
    private static ApiService sInstance = null;

    public static ApiService get() {

        if (sInstance == null) {

            HttpLoggingInterceptor loggingInterceptor =
                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

            // Building OkHttp client
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
            // Retrofit Builder
            Retrofit.Builder builder =
                    new Retrofit
                            .Builder()
                            .client(client)
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create());
            ;

            sInstance = builder.build().create(ApiService.class);

        }

        return sInstance;
    }
}
