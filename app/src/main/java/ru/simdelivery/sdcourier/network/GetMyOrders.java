package ru.simdelivery.sdcourier.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.simdelivery.sdcourier.model.Order;

public interface GetMyOrders {

    @GET("order/my")
    Call<List<Order>> getMyOrders();

}

