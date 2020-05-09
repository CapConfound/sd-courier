package ru.simdelivery.sdcourier.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.simdelivery.sdcourier.model.Order;

public interface UpdateStatus {
//    order/{id_заказа)/startToPoint/{id_точки}
//    order/{id_заказа)/arrivedAtPoint/{id_точки}
    @POST("order/{orderId)/{event}/{pointId}")
    Call<List<Order>> updStatus(@Path ("orderId") Integer orderId, @Path ("event") Integer event, @Path ("pointId") Integer point);
}



