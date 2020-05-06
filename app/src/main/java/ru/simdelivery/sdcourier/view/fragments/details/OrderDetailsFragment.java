package ru.simdelivery.sdcourier.view.fragments.details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.model.Item;
import ru.simdelivery.sdcourier.model.Order;
import ru.simdelivery.sdcourier.model.Point;
import ru.simdelivery.sdcourier.model.Product;
import ru.simdelivery.sdcourier.network.ApiClient;
import ru.simdelivery.sdcourier.network.GetOrders;

public class OrderDetailsFragment extends Fragment {

    private SharedPreferences sharedPref;
    private TextView idView;
    private Button acceptOrderBtn;
    private Button showItemsBtn;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_order_acceptance_details, container, false);
//        acceptOrderBtn.findViewById(R.id.order_details_accept_button);
//        showItemsBtn.findViewById(R.id.order_details_items_button);
        idView = v.findViewById(R.id.order_details_id);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        String token = sharedPref.getString(getString(R.string.auth_token), "");
        GetOrders service = ApiClient.getRetrofitInstance(token).create(GetOrders.class);
        Call<List<Order>> call = service.getFreeOrders();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                List<Order> ordersData = response.body();
                Integer id = 0;
                Integer position = 0;
                Bundle bundle = getArguments();
                if (bundle != null) {
                    position = bundle.getInt("position", 0);
                }




                // TODO Отображение карточек, диалог с товарами, запрос на сервер с изменением статуса.

                Order currentOrder = ordersData.get(position);
                String idText = "Заказ № " + currentOrder.getId();
                idView.setText(idText);
                List<Item> orderItemList = currentOrder.getItems(); // список предметов в заказе
                Integer cost = null; // Общая стоимость предметов заказа
                for (Item i: orderItemList) {
                    if (i != null) {
                        Integer price = i.getProduct().getPrice();
                        Integer count = i.getCount();
                        Integer mult = price * count;
                        Integer discount = i.getDiscount();
                        if (discount != 0){
                            cost += mult - (mult / 100 * discount);
                        } else {
                            cost = mult;
                        }


                    }
                }













            }
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getContext(), "При загрузке данных произошла ошибка", Toast.LENGTH_SHORT)
                        .show();

            }
        });





        return v;
    }

    private void showOrders (Integer position) {

    }

    private void acceptOrder (Integer position) {

    }


    public void openMap() {
        Uri uri = Uri.parse("yandexmaps://?whatshere[point]=30.331346,59.925857999999998&whatshere[zoom]=17");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        // Проверяем, установлено ли хотя бы одно приложение, способное выполнить это действие.

        PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            startActivity(intent);
        } else {
            // Открываем страницу приложения Яндекс.Карты в Google Play.
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=ru.yandex.yandexmaps"));
            startActivity(intent);
        }
    }


}
