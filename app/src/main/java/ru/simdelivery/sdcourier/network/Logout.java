package ru.simdelivery.sdcourier.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import okhttp3.Response;
import retrofit2.http.POST;

public interface Logout {
    @POST("logout")
    Call<ResponseBody> sendLogout();
}
