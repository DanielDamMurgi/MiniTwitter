package com.udemyandroid.minitwitter.retrofit;

import com.udemyandroid.minitwitter.common.Constantes;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthTwitterClient {

    private static AuthTwitterClient instance = null;
    private AuthTwitterService miniTwitterService;
    private Retrofit retrofit;

    public AuthTwitterClient() {
        // Incluir en la cabecera de la petición el TOKEN que autoriza al usuario
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITIWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(cliente)
                .build();

        miniTwitterService = retrofit.create(AuthTwitterService.class);
    }

    // Patrón Singleton
    public static AuthTwitterClient getInstance() {
        if(instance == null) {
            instance = new AuthTwitterClient();
        }
        return instance;
    }

    public AuthTwitterService getAuthTwitterService() {
        return miniTwitterService;
    }
}
