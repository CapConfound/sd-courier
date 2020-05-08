package ru.simdelivery.sdcourier.view.fragments.details;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.simdelivery.sdcourier.LauncherActivity;
import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.model.Item;
import ru.simdelivery.sdcourier.model.Order;
import ru.simdelivery.sdcourier.model.Point;
import ru.simdelivery.sdcourier.model.savedData.SavedOrders;
import ru.simdelivery.sdcourier.network.ApiClient;
import ru.simdelivery.sdcourier.network.GetOrders;
import ru.simdelivery.sdcourier.view.adapters.DialogRecyclerAdapter;
import ru.simdelivery.sdcourier.view.adapters.OrdersPageAdapter;
import ru.simdelivery.sdcourier.view.fragments.LoginFragment;

public class OrderDetailsFragment extends Fragment {


    private SharedPreferences sharedPref;
    private TextView idView;
    private Button acceptOrderBtn;
    private Button showItemsBtn;
    private ViewPager2 viewPager2;
    private Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_order_acceptance_details, container, false);
        acceptOrderBtn = v.findViewById(R.id.order_details_accept_button);
        showItemsBtn = v.findViewById(R.id.order_details_items_button);
        idView = v.findViewById(R.id.order_details_id);
        viewPager2 = v.findViewById(R.id.order_details_viewpager2);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.auth_token), "");
        LauncherActivity la = (LauncherActivity) getActivity();
        assert la != null;
        GetOrders service = ApiClient.getRetrofitInstance(token).create(GetOrders.class);
        Call<List<Order>> call = service.getFreeOrders();

        la.showProgressBar();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Log.d("response code", String.valueOf(response.code()));
                if(response.code() == 200) {
                    la.hideProgressBar();
                    List<Order> ordersList = response.body();
                    Integer position = 0;
                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        position = bundle.getInt("position", 0);
                    }
                    Order currentOrder = ordersList.get(position);
                    String idText = "Заказ № " + currentOrder.getId();
                    idView.setText(idText);
                    List<Point> pointsList = currentOrder.getPoints();
                    List<Item> itemsList = currentOrder.getItems();
                    OrdersPageAdapter adapter = new OrdersPageAdapter(pointsList, getContext());
                    viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                    viewPager2.setAdapter(adapter);
                    dialog = new Dialog(getContext(), R.style.AppTheme);
                    showItemsBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button closeBtn;
                            RecyclerView rv;
                            DialogRecyclerAdapter adapter;
                            dialog.setContentView(R.layout.dialog_order_items_list_view);
                            closeBtn = dialog.findViewById(R.id.dialog_close_btn);
                            rv = dialog.findViewById(R.id.dialog_recycler);
                            adapter = new DialogRecyclerAdapter(itemsList, getContext());
                            rv.setAdapter(adapter);
                            rv.setLayoutManager(new LinearLayoutManager(getContext()));
                            closeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            Log.d("dialog button", "done");
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
            }
        });
        return v;
    }

    private void showOrders (Integer position) {

    }

    private void acceptOrder (Integer position) {

    }





}
