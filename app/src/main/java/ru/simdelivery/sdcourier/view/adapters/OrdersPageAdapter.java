package ru.simdelivery.sdcourier.view.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import ru.simdelivery.sdcourier.model.Point;

public class OrdersPageAdapter extends RecyclerView.Adapter<OrdersPageAdapter.ViewHolder> {

    private List<Point> pointsList;
    Context context;
    SharedPreferences sharedPref;
    private TextView status;
    private TextView city;
    private TextView street;
    private TextView building;
    private TextView apartmentNum;
    private TextView time;
    private Button commentBtn;
    private Button mapBtn;
    private Dialog dialog;
    private PackageManager packageManager;

    public OrdersPageAdapter (List<Point> pointsList, Context context) {
        this.pointsList = pointsList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.order_page_status_view);
            city = itemView.findViewById(R.id.order_page_city_view);
            street = itemView.findViewById(R.id.order_page_street_view);
            building = itemView.findViewById(R.id.order_page_house_view);
            apartmentNum = itemView.findViewById(R.id.order_page_apartment_view);
            time = itemView.findViewById(R.id.order_page_time_data_view);
            commentBtn = itemView.findViewById(R.id.order_page_commentary_button);
            mapBtn = itemView.findViewById(R.id.order_page_map_button);
        }
    }

    @NonNull
    @Override
    public OrdersPageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_page, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersPageAdapter.ViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Collections.sort(pointsList);
        Point currentPoint = pointsList.get(position);
        String statusText = null;
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
        Double lat = currentPoint.getAddress().getLatitude();
        Double lon = currentPoint.getAddress().getLongitude();
        String coordinates = lon+","+lat;
//        Uri uri = Uri.parse("yandexmaps://?whatshere[point]=30.331346,59.925857999999998&whatshere[zoom]=17");
        Uri uri = Uri.parse("yandexmaps://?whatshere[point]="+ coordinates +"&whatshere[zoom]=17");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        Integer entrance = currentPoint.getAddress().getEntrance();
        Integer floor = currentPoint.getAddress().getFloor();





        status.setText(statusText);
        time.setText(timeText);
        city.setText(cityText);
        street.setText(streetText);
        building.setText(buildingText);
        apartmentNum.setText(apartmentText);

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = "";

                if (entrance != null) {
                    commentText += "Подъезд: " + entrance + "\n";
                }
                if (floor != null) {
                    commentText += "Этаж: " + floor + "\n";
                }
                dialog = new Dialog(context, R.style.AppTheme);
                dialog.setContentView(R.layout.dialog_comments);
                Button closeBtn = dialog.findViewById(R.id.dialog_close_btn);
                TextView content = dialog.findViewById(R.id.comment_view);
                String responseCom = currentPoint.getCommentary();

                if (responseCom == null) {
                    if (commentText.equals("")) {
                        commentText = "Здесь ничего нет";
                    }
                } else {
                    commentText += responseCom;
                }
                content.setText(commentText);
                closeBtn.setOnClickListener(v1 -> {
                    dialog.dismiss();
                });
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(162, 5, 5, 5)));



            }
        });
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openMap(intent);
            }
        });

    }
    public void openMap(Intent intent) {

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
        return pointsList.size();
    }


}


