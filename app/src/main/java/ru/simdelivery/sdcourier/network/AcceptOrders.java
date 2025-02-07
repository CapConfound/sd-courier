package ru.simdelivery.sdcourier.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AcceptOrders {

    @POST("order/{order_id}/accept")
    Call<ResponseBody> acceptOrder(@Path("order_id") Integer id);

}

