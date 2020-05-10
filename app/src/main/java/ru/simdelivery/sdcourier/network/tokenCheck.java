package ru.simdelivery.sdcourier.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;

public interface tokenCheck {
    @POST("checkAuthToken")
    Call<ResponseBody> checkToken();
}
