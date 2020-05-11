package ru.simdelivery.sdcourier.view.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import ru.simdelivery.sdcourier.LauncherActivity;
import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.model.Address;
import ru.simdelivery.sdcourier.model.Order;
import ru.simdelivery.sdcourier.model.Point;
import ru.simdelivery.sdcourier.view.fragments.OrdersFragment;
import ru.simdelivery.sdcourier.view.fragments.details.MyOrderDetailsFragment;
import ru.simdelivery.sdcourier.view.fragments.details.OrderDetailsFragment;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<Order> orderListResult;
    private Context context;
    MyOrderDetailsFragment myOrdersDetailsFragment;




    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout item_order;
        private TextView orderID;
        private TextView time;
        private TextView p1City;
        private TextView p1Address;
        private TextView p2City;
        private TextView p2Address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_order = itemView.findViewById(R.id.order_item_id);
            orderID = itemView.findViewById(R.id.order_number_view);
            time = itemView.findViewById(R.id.order_time_view);
            p1City = itemView.findViewById(R.id.order_point1_city_view);
            p1Address = itemView.findViewById(R.id.order_point1_address_view);
            p2City = itemView.findViewById(R.id.order_point2_city_view);
            p2Address = itemView.findViewById(R.id.order_point2_address_view);

        }


    }

    public OrdersAdapter(List<Order> orderList, Context context) {
        this.orderListResult = orderList;
        this.context = context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderView = inflater.inflate(R.layout.item_available_order, parent, false);
        ViewHolder viewHolder = new ViewHolder(orderView);


//      я не знаю почему, но у меня было 2 onClickListener`а
//        viewHolder.item_order.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 Toast.makeText(context, "test click" + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
//                 AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                 OrderDetailsFragment fragment = new OrderDetailsFragment();
//                 activity.getSupportFragmentManager()
//                             .beginTransaction()
//                             .replace(R.id.fragment_container, fragment)
//                             .addToBackStack(null)
//                             .commit();
//
//             }
//        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder v, int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Order items = orderListResult.get(position);
        String orderId = "№ ";
        orderId = orderId + items.getId().toString();

        String city1 = "";
        String city2 = "";
        String address1 = "";
        String address2 = "";
        Date arrivalAtFrom = null;
        Date arrivalAtTo = null;
        int hour1 = 0;
        int minutes1 = 0;
        int hour2 = 0;
        int minutes2 = 0;
        String timeText = null;



        List<Point> points = orderListResult.get(position).getPoints();
        //Проверяю тип точки. 1 - откуда принять, 2 - куда доставить.
        for(int i = 0; i < points.size(); i++){
            Point currentPoint;
            currentPoint = points.get(i);
            if(currentPoint.getNumber().equals(1)){
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


                Log.d("milliseconds total", String.valueOf(arrivalAtFrom.getTime()));
                Log.d("Time should be ", "10:00");
                Log.d("Time", getTimeString(arrivalAtFrom));
                Log.d("date is" , String.valueOf(arrivalAtFrom.getDate())+"."+String.valueOf(arrivalAtFrom.getMonth()));
                Log.d("formattedDate", getDayMonthString(arrivalAtFrom));

                Address currentPointAddress = currentPoint.getAddress();
                city1 += currentPointAddress.getLocality().getName();
                address1 += currentPointAddress.getRoute() + ", " + currentPointAddress.getHouse();

            }
            if(currentPoint.getNumber().equals(2)) {
                currentPoint = points.get(i);
                Address currentPointAddress = currentPoint.getAddress();
                city2 += currentPointAddress.getLocality().getName();
                address2 += currentPointAddress.getRoute() + ", " + currentPointAddress.getHouse();
            }
        }

        Log.i("id", orderId);
        //Log.i("time", getTimeText(arrivalAtFrom, arrivalAtTo));
        Log.i("city1", city1);
        Log.i("address1", address1);
        Log.i("city2", city2);
        Log.i("address2", address2);
        String time = timeText;

        String sss = String.valueOf(hour1)+":"+String.valueOf(minutes1);
        v.orderID.setText(orderId);
//        v.time.setText(getTimeText(arrivalAtFrom, arrivalAtTo));
        v.time.setText(time);
        v.p1City.setText(city1);
        v.p1Address.setText(address1);
        v.p2City.setText(city2);
        v.p2Address.setText(address2);


        v.item_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Integer id = orderListResult.get(position).getId();
                Log.d("id", String.valueOf(id));
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                OrderDetailsFragment fragment = new OrderDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                Log.d("position is", String.valueOf(position));
                bundle.putInt("position", position);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();


            }
        });

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

    @Override
    public int getItemCount() {
        return orderListResult.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
