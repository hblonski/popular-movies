package com.popularmovies.network;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class RetrofitClient {

    public static Retrofit getInstance(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
