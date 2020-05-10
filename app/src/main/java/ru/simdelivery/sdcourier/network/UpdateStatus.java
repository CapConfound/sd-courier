package ru.simdelivery.sdcourier.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UpdateStatus {
//    order/{id_заказа)/startToPoint/{id_точки}
//    order/{id_заказа)/arrivedAtPoint/{id_точки}
    @POST("order/{order_id}/{event}/{point_id}")
    Call<ResponseBody> updStatus(@Path ("order_id") Integer orderId, @Path ("event") String event, @Path ("point_id") Integer point);
}



