package ru.simdelivery.sdcourier.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.simdelivery.sdcourier.model.Auth;
import ru.simdelivery.sdcourier.model.AuthResponse;

public interface GetUserToken {
    @POST("login")
    Call<AuthResponse> getAuthResponse(@Body Auth data);


}

