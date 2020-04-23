package ru.simdelivery.sdcourier.network;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://simdelivery.ru/";


    public static Retrofit getRetrofitInstance(String token) {


        if (retrofit == null) {
            Interceptor interceptor;
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();
                        Request.Builder requestBuilder;

//                        HttpUrl newHttpUrl = httpUrl.newBuilder().addQueryParameter("apikey", "8e102e39").build();

//                        if(!httpUrl.toString().endsWith("/login")){
//                            requestBuilder = original.newBuilder()
//                                    .url(httpUrl)
//                                    .addHeader("X-Auth-Token", token);
//                        }
//                        else {
//                            requestBuilder = original.newBuilder()
//                                    .url(httpUrl);
//                        }
                        requestBuilder = original.newBuilder()
                                .url(httpUrl)
                                .addHeader("X-Auth-Token", token);


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
        return retrofit;
    }

    public static Retrofit getAuthData() {


        if (retrofit == null) {
//            Interceptor interceptor;
//            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .addInterceptor(chain -> {
//                        Request original = chain.request();
//                        HttpUrl httpUrl = original.url();
//                        Request.Builder requestBuilder;
//
////                        HttpUrl newHttpUrl = httpUrl.newBuilder().addQueryParameter("apikey", "8e102e39").build();
//
////                        if(!httpUrl.toString().endsWith("/login")){
////                            requestBuilder = original.newBuilder()
////                                    .url(httpUrl)
////                                    .addHeader("X-Auth-Token", token);
////                        }
////                        else {
////                            requestBuilder = original.newBuilder()
////                                    .url(httpUrl);
////                        }
//                        requestBuilder = original.newBuilder()
//                                .url(httpUrl);
//
//
//                        Request request = requestBuilder.build();
//                        return chain.proceed(request);
//                    })
//                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            GetUserToken service = retrofit.create(GetUserToken.class);
        }
        return retrofit;
    }

}

