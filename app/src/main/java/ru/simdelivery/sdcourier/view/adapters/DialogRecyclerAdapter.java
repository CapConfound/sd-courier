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
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.model.Address;
import ru.simdelivery.sdcourier.model.Item;
import ru.simdelivery.sdcourier.model.Point;
import ru.simdelivery.sdcourier.view.fragments.details.OrderDetailsFragment;

public class DialogRecyclerAdapter extends RecyclerView.Adapter<DialogRecyclerAdapter.ViewHolder> {

    private List<Item> itemList;
    private Context context;




    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView count;
        private TextView name;
        private TextView cost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.recycler_item_count_view);
            name = itemView.findViewById(R.id.recycler_item_name_view);
            cost = itemView.findViewById(R.id.recycler_item_cost_view);
        }
    }
    public DialogRecyclerAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderView = inflater.inflate(R.layout.item_dialog_product, parent, false);
        ViewHolder viewHolder = new ViewHolder(orderView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder v, int position) {
        Item item = itemList.get(position);

        Integer cost = null; // Общая стоимость предметов заказа
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
        v.count.setText(String.valueOf(item.getCount()));
        v.name.setText(item.getProduct().getName());
        v.cost.setText(String.valueOf(cost));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
