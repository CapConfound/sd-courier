package ru.simdelivery.sdcourier.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import ru.simdelivery.sdcourier.model.Order;
import ru.simdelivery.sdcourier.network.ApiClient;
import ru.simdelivery.sdcourier.network.GetOrders;
import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.view.adapters.OrdersAdapter;

public class OrdersFragment extends Fragment {
    RecyclerView rv;
    OrdersAdapter adapter;
    SharedPreferences sharedPref;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders_available_view, container, false);
        LauncherActivity la = (LauncherActivity) getActivity();
        assert la != null;
        context = getActivity();
        rv = v.findViewById(R.id.available_orders_recycler);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.auth_token), "");
        Dialog dialog = createDialog();
        dialog.show();
        GetOrders service = ApiClient.getRetrofitInstance(token).create(GetOrders.class);
        Call<List<Order>> call = service.getFreeOrders();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.code() == 200) {
                    Log.d("response code", String.valueOf(response.code()));
                    List<Order> ordersList = response.body();
                    bindAdapter(ordersList);
                    dialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("OrdersFragment", "onFailure");
            }
        });

        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void bindAdapter(List<Order> list) {
        adapter = new OrdersAdapter(list, getActivity());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

    }
    private Dialog createDialog () {
        Dialog dialog = new Dialog(getContext(), R.style.AppTheme);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(162, 5, 5, 5)));
        return dialog;
    }

}
