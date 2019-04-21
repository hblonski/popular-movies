package com.popularmovies.util;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * See https://square.github.io/retrofit/
 * */
public abstract class RetrofitServiceGenerator {

    public static <T> T generateService(String baseUrl, Class<T> serviceClass) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(serviceClass);
    }
}
