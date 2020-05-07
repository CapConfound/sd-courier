package ru.simdelivery.sdcourier.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.simdelivery.sdcourier.LauncherActivity;
import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.model.savedData.SavedOrders;
import ru.simdelivery.sdcourier.network.ApiClient;
import ru.simdelivery.sdcourier.network.GetMyOrders;
import ru.simdelivery.sdcourier.network.GetOrders;
import ru.simdelivery.sdcourier.view.fragments.LoginFragment;
import ru.simdelivery.sdcourier.view.fragments.OrdersFragment;

public class DataLoader {
    FragmentManager fragmentManager;
    private SharedPreferences sharedPref;

    public void loadAllOrders (String token) {
        loadOrders(token);
        loadMyOrders(token);
    }

    public void loadOrders (String token) {
        GetOrders service = ApiClient.getRetrofitInstance(token).create(GetOrders.class);
        Call<List<Order>> call = service.getFreeOrders();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Log.d("response code", String.valueOf(response.code()));
                if (response.code() == 401){

                    LoginFragment loginFragment = new LoginFragment();
                    fragmentManager = new LauncherActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();

                } else {
                    if (response.code() == 200) {
                        Log.d("response code", String.valueOf(response.code()));
                        SavedOrders.setFreeOrder(response.body());
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("loadOrders called", "onFailure");
            }
        });
    }

    public void loadMyOrders (String token) {
        GetMyOrders service = ApiClient.getRetrofitInstance(token).create(GetMyOrders.class);
        Call<List<Order>> call = service.getMyOrders();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                SavedOrders.setMyOrder(response.body());
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("loadMyOrders called", "onFailure");
            }
        });
    }

    public void loadOrdersHistory (String token) {
        // TODO implement when order history is ready
    }




}
