package com.udemyandroid.minitwitter.retrofit;

import com.udemyandroid.minitwitter.common.Constantes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiniTwitterClient {

    private static MiniTwitterClient instance = null;
    private static MiniTwitterService miniTwitterService;
    private Retrofit retrofit;

    public MiniTwitterClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITIWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        miniTwitterService = retrofit.create(MiniTwitterService.class);
    }

    // Patrón Singleton

    public static MiniTwitterClient getInstance(){
        if (instance == null){
            instance = new MiniTwitterClient();
        }
        return instance;
    }

    public static MiniTwitterService getMiniTwitterService(){
        return miniTwitterService;
    }
}
