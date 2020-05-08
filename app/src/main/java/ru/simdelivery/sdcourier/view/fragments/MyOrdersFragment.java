package ru.simdelivery.sdcourier.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.simdelivery.sdcourier.LauncherActivity;
import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.model.Order;
import ru.simdelivery.sdcourier.model.savedData.SavedOrders;
import ru.simdelivery.sdcourier.network.ApiClient;
import ru.simdelivery.sdcourier.network.GetMyOrders;
import ru.simdelivery.sdcourier.network.GetOrders;
import ru.simdelivery.sdcourier.view.adapters.MyOrdersAdapter;
import ru.simdelivery.sdcourier.view.adapters.OrdersAdapter;

public class MyOrdersFragment extends Fragment {
    MyOrdersAdapter adapter;

    RecyclerView rv;
    private SharedPreferences sharedPref;
    String d = "!!!!!MYDEBUG!!!!!!";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_orders_view, container, false);
        LauncherActivity la = (LauncherActivity) getActivity();
        assert la != null;
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.auth_token), "");

        GetMyOrders service = ApiClient.getRetrofitInstance(token).create(GetMyOrders.class);
        Call<List<Order>> call = service.getMyOrders();
        la.showProgressBar();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                la.hideProgressBar();
                List<Order> ordersList = response.body();
                rv = v.findViewById(R.id.my_orders_recycler);
                adapter = new MyOrdersAdapter(ordersList, getActivity());
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("loadMyOrders called", "onFailure");
            }
        });



        return v;
    }
}
