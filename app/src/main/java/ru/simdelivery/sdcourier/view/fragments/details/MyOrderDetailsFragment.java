package ru.simdelivery.sdcourier.view.fragments.details;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.simdelivery.sdcourier.LauncherActivity;
import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.model.Item;
import ru.simdelivery.sdcourier.model.Order;
import ru.simdelivery.sdcourier.model.Point;
import ru.simdelivery.sdcourier.network.ApiClient;
import ru.simdelivery.sdcourier.network.GetMyOrders;
import ru.simdelivery.sdcourier.network.UpdateStatus;
import ru.simdelivery.sdcourier.view.adapters.DialogRecyclerAdapter;
import ru.simdelivery.sdcourier.view.adapters.MyOrdersPageAdapter;
import ru.simdelivery.sdcourier.view.fragments.MyOrdersFragment;
import ru.simdelivery.sdcourier.view.fragments.OrdersFragment;
import ru.simdelivery.sdcourier.view.fragments.SettingsFragment;

public class MyOrderDetailsFragment extends Fragment {


    private SharedPreferences sharedPref;
    private RelativeLayout tag;
    private TextView idView;
    private TextView statuz;
    private Button acceptPaymentBtn;
    private Button showItemsBtn;
    private Button changeStatus;
    private Button cancelOrder;
    private ViewPager2 viewPager2;
    private Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_order_details, container, false);
        tag = v.findViewById(R.id.my_order_tag_area);
        statuz = v.findViewById(R.id.my_order_details_status_text);
        acceptPaymentBtn = v.findViewById(R.id.my_order_details_accept_payment_button);
        showItemsBtn = v.findViewById(R.id.my_order_details_items_button);
        changeStatus = v.findViewById(R.id.my_order_details_status_button);
        cancelOrder = v.findViewById(R.id.my_order_details_cancel_button);
        idView = v.findViewById(R.id.my_order_details_id);
        viewPager2 = v.findViewById(R.id.my_order_details_viewpager2);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.auth_token), "");

        GetMyOrders service = ApiClient.getRetrofitInstance(token).create(GetMyOrders.class);
        Call<List<Order>> call = service.getMyOrders();

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.code() == 200) {

                    List<Order> ordersList = response.body();
                    Integer position = 0;
                    Drawable background = getContext().getResources().getDrawable(R.drawable.htag_gray);
                    Drawable btnBackground = getContext().getResources().getDrawable(R.drawable.background_button_blue);
                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        position = bundle.getInt("position", 0);
                    }
                    Order currentOrder = ordersList.get(position);
                    String idText = "Мой заказ № " + currentOrder.getId();//+ ". ";
                    String statusText = "";
                    String statusBtnText = "";


                    Integer currentPointID = currentOrder.getCurrentPointId();
                    if (currentPointID == null){
                        currentPointID = 1;
                    }
                    Integer currentPointNum = getPointNum(currentOrder, currentPointID);
                    String status = currentOrder.getStatus();
                    switch (status) {
                        case "STATUS_COURIER_ASSIGNED":
                            background = getContext().getResources().getDrawable(R.drawable.htag_blue);
                            btnBackground = getContext().getResources().getDrawable(R.drawable.background_button_blue);
                            statusText = "Курьер назначен";
                            statusBtnText = "На первую точку";
                            break;
                        case "STATUS_COURIER_ON_THE_WAY":
                            background = getContext().getResources().getDrawable(R.drawable.htag_violet);
                            btnBackground = getContext().getResources().getDrawable(R.drawable.background_button_violet);
                            if (currentPointNum.equals(1)) {
                                statusText = "В пути к точке 1";
                                statusBtnText = "Прибыл на 1 точку";
                            } else {
                                statusText = "В пути к точке 2";
                                statusBtnText = "Прибыл на 2 точку";
                            }
                            break;
                        case "STATUS_COURIER_IS_WAITING":
                            background = getContext().getResources().getDrawable(R.drawable.htag_orange);
                            btnBackground = getContext().getResources().getDrawable(R.drawable.background_button_orange);
                            if (currentPointNum.equals(1)) {
                                statusText += "Ожидание на точке 1";
                                statusBtnText = "На 2 точку";
                            } else {
                                statusText += "Ожидание на точке 2";
                                statusBtnText = "Закончить заказ";
                            }
                            break;
                        case "STATUS_CANCELED":
                            background = getContext().getResources().getDrawable(R.drawable.htag_red);
                            btnBackground = getContext().getResources().getDrawable(R.drawable.background_button_red);
                            statusText += "Заказ отменён";
                            statusBtnText = "Заказ отменён";
                            break;
                    }

                    statuz.setText(statusText);
                    changeStatus.setText(statusBtnText);
                    changeStatus.setBackground(btnBackground);

                    tag.setBackground(background);
                    idView.setText(idText);
                    List<Item> itemsList = currentOrder.getItems();
                    MyOrdersPageAdapter adapter = new MyOrdersPageAdapter(currentOrder, getContext());
                    viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                    viewPager2.setAdapter(adapter);
                    dialog = new Dialog(getContext(), R.style.AppTheme);
                    showItemsBtn.setOnClickListener(v1 -> {
                        Button closeBtn;
                        RecyclerView rv;
                        DialogRecyclerAdapter adapter1;
                        dialog.setContentView(R.layout.dialog_my_order_items_list_view);
                        closeBtn = dialog.findViewById(R.id.dialog_close_btn);
                        rv = dialog.findViewById(R.id.dialog_recycler);
                        adapter1 = new DialogRecyclerAdapter(itemsList, getContext());
                        rv.setAdapter(adapter1);
                        rv.setLayoutManager(new LinearLayoutManager(getContext()));
                        closeBtn.setOnClickListener(v11 -> dialog.dismiss());
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(162, 5, 5, 5)));
                        Log.d("dialog button", "done");
                    });
                    acceptPaymentBtn.setOnClickListener(v2 -> {
                        if (status.equals("STATUS_COURIER_IS_WAITING") && currentPointNum == 2) {
                            Button closeBtn;
                            dialog.setContentView(R.layout.dialog_payment);
                            closeBtn = dialog.findViewById(R.id.dialog_close_btn);
                            closeBtn.setOnClickListener(v11 -> dialog.dismiss());
                            dialog.show();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(162, 5, 5, 5)));

                        } else {
                            Toast.makeText(getContext(), "Заказ ещё в процессе доставки.\nОплата недоступна.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    cancelOrder.setOnClickListener( v3 -> cancelOrder(currentOrder, token));

                    statuz.setText(statusText);

                    changeStatus.setOnClickListener(v2 -> {

                        Integer currentPointId = currentOrder.getCurrentPointId();
                        if (currentPointId == null){
                            currentPointId = 1;
                        }
                        Integer currentPointNUM = getPointNum(currentOrder, currentPointId);
                        String curStatus = currentOrder.getStatus();
                        int action = 0;

                        switch (curStatus) {

                            case "STATUS_COURIER_ASSIGNED":

                                changeStatus.setText("Отправиться на 1 точку");
                                action = 1;
                                break;
                            case "STATUS_COURIER_ON_THE_WAY":
                                if (currentPointNUM.equals(1)) {
                                    changeStatus.setText("Прибыл на 1 точку");
                                    action = 2;
                                } else {
                                    statuz.setText("В пути на точку 2");
                                    changeStatus.setText("Прибыл на 2 точку");
                                    action = 3;
                                }
                                break;
                            case "STATUS_COURIER_IS_WAITING":
                                if (currentPointNUM.equals(1)) {
                                    changeStatus.setText("На 2 точку");
                                    action = 4;
                                } else {
                                    changeStatus.setText("Закончить заказ");
                                    action = 5;
                                }
                                break;
                            case "STATUS_CANCELED":
                                changeStatus.setText("Заказ отменён");
                                changeStatus.setEnabled(false);
                                break;
                        }
                        onChangeStatus(currentOrder, action, token);

                    });

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

    private void cancelOrder(Order currentOrder, String token) {
        Integer orderId = currentOrder.getId();
        UpdateStatus service1 = ApiClient.getRetrofitInstance(token).create(UpdateStatus.class);
        Call<ResponseBody> call1 = service1.cancelOrder(orderId);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Отмена заказа прошла успешно", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "При обновлении статуса произошла ошибка", Toast.LENGTH_SHORT).show();
                }
                returnToOrdersList();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private Integer getPointNum (Order order, int pointId) {
        Integer pNumber = 0;
        for (Point p: order.getPoints()) {
            if(p.getId().equals(pointId)) {
                pNumber = p.getNumber();
                return pNumber;
            }
        }
        return pNumber;
    }

    private Integer getPointIdByNum (Order order, int num) {
        Integer id = 0;
        for (Point p: order.getPoints()) {
            if (p.getNumber().equals(num)) id = p.getId();
        }
        return id;
    }

    private void onChangeStatus(Order currentOrder, Integer action, String token) {
        Integer orderId = currentOrder.getId();
        Integer point = 0;
        String event;
        switch (action) {
            case 1:
                event = "startToPoint";
                point = getPointIdByNum(currentOrder, 1);
                break;
            case 2:
                event = "arrivedAtPoint";
                point = getPointIdByNum(currentOrder, 1);
                break;
            case 3:
                event = "arrivedAtPoint";
                point = getPointIdByNum(currentOrder, 2);
                break;
            case 4:
                event = "startToPoint";
                point = getPointIdByNum(currentOrder, 2);
                break;
            case 5:
                event = "end";
                break;
            default:
                event = "error";
                break;
        }

        if (!event.equals("error") && !event.equals("end")){
            UpdateStatus service1 = ApiClient.getRetrofitInstance(token).create(UpdateStatus.class);
            Call<ResponseBody> call1 = service1.updStatus(orderId, event, point);
            call1.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getContext(), "Обновление статуса успешно", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "При обновлении статуса произошла ошибка", Toast.LENGTH_SHORT).show();
                    }
                    updFragment();
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } else if (event.equals("end")) {
            UpdateStatus service1 = ApiClient.getRetrofitInstance(token).create(UpdateStatus.class);
            Call<ResponseBody> call1 = service1.completeOrder(orderId);
            call1.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getContext(), "Обновление статуса успешно", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "При обновлении статуса произошла ошибка", Toast.LENGTH_SHORT).show();
                    }
                    returnToOrdersList();
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        }
    }

    private void updFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }

    private void returnToOrdersList() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new MyOrdersFragment()).commit();
    }
}