package ru.simdelivery.sdcourier.network;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
/*
                Этот класс не используется
                Но он рабочий))

    private static final String BASE_URL = "";
    public static Retrofit retrofit;

    public static void init() {
        Interceptor interceptor;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    HttpUrl httpUrl = original.url();
                    Request.Builder requestBuilder;


                    if(!httpUrl.toString().endsWith("/login")){
                        requestBuilder = original.newBuilder()
                                .url(httpUrl)
                                .addHeader("X-Auth-Token", "s");
                    }
                    else {
                        requestBuilder = original.newBuilder()
                                .url(httpUrl);
                    }


                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetUserToken service = retrofit.create(GetUserToken.class);

    }
*/
}
