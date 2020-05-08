package ru.simdelivery.sdcourier.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.model.Item;
import ru.simdelivery.sdcourier.model.Order;
import ru.simdelivery.sdcourier.model.Point;

public class MyOrdersPageAdapter extends RecyclerView.Adapter<MyOrdersPageAdapter.ViewHolder> {

    private Order order;
    private List<Point> pointsList;
    Context context;
    SharedPreferences sharedPref;
    private TextView status;
    private TextView name;
    private TextView city;
    private TextView street;
    private TextView building;
    private TextView apartmentNum;
    private TextView time;
    private TextView paymentType;
    private TextView paymentAmount;
    private Button callBtn;
    private Button commentBtn;
    private Button mapBtn;
    private PackageManager packageManager;

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.my_orders_page_status_view);
            name = itemView.findViewById(R.id.my_orders_page_name_view);
            city = itemView.findViewById(R.id.my_orders_page_city_view);
            street = itemView.findViewById(R.id.my_orders_page_street_view);
            building = itemView.findViewById(R.id.my_orders_page_building_view);
            apartmentNum = itemView.findViewById(R.id.my_orders_page_apartment_view);
            time = itemView.findViewById(R.id.my_orders_page_time_view);
            paymentType = itemView.findViewById(R.id.my_orders_page_payment_type_view);
            paymentAmount = itemView.findViewById(R.id.my_orders_page_cost_view);
            callBtn = itemView.findViewById(R.id.my_orders_page_call_btn);
            commentBtn = itemView.findViewById(R.id.my_orders_page_commentary_btn);
            mapBtn = itemView.findViewById(R.id.my_orders_page_map_btn);
        }
    }

    public MyOrdersPageAdapter (List<Point> pointsList, Order order , Context context) {
        this.pointsList = pointsList;
        this.order = order;
        this.context = context;
    }


    @NonNull
    @Override
    public MyOrdersPageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_order_page, parent, false);

        return new MyOrdersPageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersPageAdapter.ViewHolder holder, int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Collections.sort(pointsList);
        Point currentPoint = pointsList.get(position);
        String statusText = "Направление";
        Double lat = currentPoint.getAddress().getLatitude();
        Double lon = currentPoint.getAddress().getLongitude();
        String pName = currentPoint.getPerson().getName();
        String tel = currentPoint.getPerson().getPhone();
        String cityText = currentPoint.getAddress().getLocality().getName();
        String streetText = currentPoint.getAddress().getRoute();
        String buildingText = currentPoint.getAddress().getHouse();
        String apartmentText = String.valueOf(currentPoint.getAddress().getApartment());
        String timeText = null;
        Date arrivalAtFrom = null;
        Date arrivalAtTo = null;
        int hour1 = 0;
        int minutes1 = 0;
        int hour2 = 0;
        int minutes2 = 0;

        try {
            arrivalAtFrom = sdf.parse(currentPoint.getArrivalAtFrom());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(String.valueOf(currentPoint.getArrivalAtTo()).equals("null")) {
            timeText = getDayMonthString(arrivalAtFrom) + " как можно скорее";


        } else {
            try {
                arrivalAtTo = sdf.parse(currentPoint.getArrivalAtTo());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            hour1 = getHours(arrivalAtFrom);

            hour2 = getHours(arrivalAtTo);

            minutes1 = getMinutes(arrivalAtFrom);
            minutes2 = getMinutes(arrivalAtTo);

            if(hour1 == hour2) {
                if(minutes1 == minutes2) {
                    timeText = getDayMonthString(arrivalAtFrom) + " ровно в " + getTimeString(arrivalAtFrom);
                } else {
                    timeText = getDayMonthString(arrivalAtFrom) + " c " + getTimeString(arrivalAtFrom) + " до " + getTimeString(arrivalAtTo);
                }
            } else {
                timeText = getDayMonthString(arrivalAtFrom) + " c " + getTimeString(arrivalAtFrom) + " до " + getTimeString(arrivalAtTo);
            }
        }

        Log.d("OrdersPageAdapter", "mess starts mow");
        Log.d("milliseconds total", String.valueOf(arrivalAtFrom.getTime()));
        Log.d("Time should be ", "10:00");
        Log.d("Time", getTimeString(arrivalAtFrom));
        Log.d("date is" , String.valueOf(arrivalAtFrom.getDate())+"."+String.valueOf(arrivalAtFrom.getMonth()));
        Log.d("formattedDate", getDayMonthString(arrivalAtFrom));



        switch (currentPoint.getNumber()) {
            case 1:
                statusText = "Откуда";
                break;
            case 2:
                statusText = "Куда";
                break;
        }




        String paymentText = "К оплате";
        String paymentCountText = "";

        String costText = "";
        int deliveryAndCost = getCost(order) + order.getCost();

        switch (currentPoint.getPaymentObject()) {
            case "PAYMENT_OBJECT_NOTHING":
                paymentText = "Оплата не взымается";
                break;
            case "PAYMENT_OBJECT_PRODUCTS":
                paymentText = "Оплата за товары";
                paymentCountText = costText + "р.";
                break;
            case "PAYMENT_OBJECT_DELIVERY":
                paymentText = "Оплата за доставку";
                costText = order.getCost() + "р.";
                break;
            case "PAYMENT_OBJECT_PRODUCTS_AND_DELIVERY":
                paymentText = "Оплата за всё";
                costText = deliveryAndCost + "р.";
                break;
        }


        status.setText(statusText);
        name.setText(pName);
        callBtn.setOnClickListener(v -> {
            openDialer(tel);

        });
        city.setText(cityText);
        street.setText(streetText);
        building.setText(buildingText);
        apartmentNum.setText(apartmentText);
        time.setText(timeText);
        mapBtn.setOnClickListener(v -> openMap(lon, lat));
        paymentType.setText(paymentText);
        paymentAmount.setText(costText);
        street.setText(streetText);
        building.setText(buildingText);
        apartmentNum.setText(apartmentText);

        callBtn.setOnClickListener(v -> {
            openDialer(tel);

        });

        commentBtn.setOnClickListener(v -> {
            // todo open custom dialog here



        });


    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void openMap(Double lon, Double lat) {
        String coordinates = lon+","+lat;
//        Uri uri = Uri.parse("yandexmaps://?whatshere[point]=30.331346,59.925857999999998&whatshere[zoom]=17");
        Uri uri = Uri.parse("yandexmaps://?whatshere[point]="+ coordinates +"&whatshere[zoom]=17");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        // Проверяем, установлено ли хотя бы одно приложение, способное выполнить это действие.
        packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            context.startActivity(intent);
        } else {
            // Открываем страницу приложения Яндекс.Карты в Google Play.
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=ru.yandex.yandexmaps"));
            context.startActivity(intent);
        }
    }

    private void openDialer (String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri uri = Uri.parse("tel:" + tel);
        intent.setData(uri);
        context.startActivity(intent);
    }

    private int getHours(Date date) {
        int utcOffset = TimeZone.getDefault().getOffset(date.getTime()) / 3600000;
        return (int) ((date.getTime() % 86400000)) / 3600000 + utcOffset;
    }

    private int getMinutes(Date date) {
        return (int) ((date.getTime() % 86400000)) / 3600000 / 60000;
    }

    private String getTimeString(Date date) {
        SimpleDateFormat hoursMinutesFormat = new SimpleDateFormat("HH:mm");
        return hoursMinutesFormat.format(date);
    }

    private String getDayMonthString(Date date) {
        SimpleDateFormat dayMonthFormat = new SimpleDateFormat("dd.MM");
        return dayMonthFormat.format(date);
    }

    private Integer getCost (Order order) {
        List<Item> itemList = order.getItems();


        Integer cost = null; // Общая стоимость предметов заказа
        for (Item item : itemList) {
            if (item != null) {
                Integer price = item.getProduct().getPrice();
                Integer count = item.getCount();
                Integer mult = price * count;
                Integer discount = item.getDiscount();
                if (discount != 0){
                    cost += mult - (mult / 100 * discount);
                } else {
                    cost = mult;
                }
            }
        }
        return cost;

    }
}
