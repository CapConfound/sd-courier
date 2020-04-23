package ru.simdelivery.sdcourier.api.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import ru.simdelivery.sdcourier.model.Order;

public interface GetOrders {

    @GET("courier/order")
    Call<Order> getAllOrders();

}

