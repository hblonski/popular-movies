package com.popularmovies.util;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Generates a service for a Retrofit API Client class.
 * @see <a href="https://square.github.io/retrofit/">https://square.github.io/retrofit/</a>
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
