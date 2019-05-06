package com.popularmovies.util;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * See https://square.github.io/retrofit/
 * */
public abstract class RetrofitServiceGenerator {

    private final static Map<Class, Object> services = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T generateService(String baseUrl, Class<T> serviceClass) {

        if (services.containsKey(serviceClass)) {
            //noinspection unchecked
            return (T) services.get(serviceClass);
        }

        T service = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(serviceClass);
        services.put(serviceClass, service);
        return service;
    }
}
