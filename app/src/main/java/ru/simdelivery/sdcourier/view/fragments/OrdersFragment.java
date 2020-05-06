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
import ru.simdelivery.sdcourier.model.Order;
import ru.simdelivery.sdcourier.network.ApiClient;
import ru.simdelivery.sdcourier.network.GetOrders;
import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.view.adapters.OrdersAdapter;

public class OrdersFragment extends Fragment {
    OrdersAdapter adapter;
    RecyclerView recyclerView;
    private SharedPreferences sharedPref;
    String d = "!!!!!MYDEBUG!!!!!!";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders_available_view, container, false);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        String token = sharedPref.getString(getString(R.string.auth_token), "");

        recyclerView = v.findViewById(R.id.available_orders_recycler);
        GetOrders servise = ApiClient.getRetrofitInstance(token).create(GetOrders.class);
        Call<List<Order>> call = servise.getFreeOrders();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {

                Log.d("authToken", token);
//                Log.d("gcmToken", String.valueOf(R.string.gcm_token));
                Log.d(d, String.valueOf(response));
                List<Order> ordersList = response.body();
                RecyclerView rv = (RecyclerView) v.findViewById(R.id.available_orders_recycler);
                OrdersAdapter adapter = new OrdersAdapter(ordersList, getActivity());
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));



            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void generateDataList(List<Order> orderList) {

        recyclerView = recyclerView.findViewById(R.id.available_orders_recycler);
        adapter = new OrdersAdapter(orderList, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
