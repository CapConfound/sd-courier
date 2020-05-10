package ru.simdelivery.sdcourier.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import ru.simdelivery.sdcourier.model.Order;

public interface GetOrders {

    @GET("order/free")
    Call<List<Order>> getFreeOrders();

}

