package ru.simdelivery.sdcourier.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.simdelivery.sdcourier.R;

public class ApiClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.simdelivery.ru/courier/";



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
//
//                        HttpUrl newHttpUrl = httpUrl.newBuilder().addQueryParameter("gcmToken", gcmToken).build();

//                        if(!httpUrl.toString().endsWith("/login")){
//                            requestBuilder = original.newBuilder()
//                                    .url(httpUrl)
//                                    .addHeader("X-Auth-Token", token);
//                        }
//                        else {
//                            requestBuilder = original.newBuilder()
//                                    .url(httpUrl);
//                        }
//                        requestBuilder = original.newBuilder()
//                                .url(newHttpUrl);
//
//
//                        Request request = requestBuilder.build();
//                        return chain.proceed(request);
//                    })
//                    .build();
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .client(okHttpClient)
//                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            GetUserToken service = retrofit.create(GetUserToken.class);
        }
        return retrofit;
    }

    public static Retrofit updateStatus(Integer orderId, Integer pointId, boolean waiting) {
        //courier/order/{id_заказа}/startToPoint/{id_точки}
        //courier/order/{id_заказа}/arrivedAtPoint/{id_точки}
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    HttpUrl httpUrl = original.url();
                    Request.Builder requestBuilder;
                    String parameters;
                    if(waiting){
                        parameters = "courier/order/"+orderId+"/arrivedAtPoint/"+pointId;
                    }else {
                        parameters = "courier/order/"+orderId+"/startToPoint/"+pointId;
                    }
                    String newHttpUrl = httpUrl + parameters;
                    requestBuilder = original.newBuilder()
                            .url(newHttpUrl);
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();



        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}

