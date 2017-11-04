package com.loftschool.moneytracker;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> items = new ArrayList<>();
    private Context context;

    ItemsAdapter(Context activityContext) {
        context = activityContext;
        items.add(new Item("Молоко", 35));
        items.add(new Item("Зубная щетка", 55));
        items.add(new Item("Сковородка с антипригарным покрытием", 1500));
        items.add(new Item("Яйцв", 46));
        items.add(new Item("Шампунь", 199));
        items.add(new Item("Туалетная бумага", 12));
        items.add(new Item("Бананы", 129));
        items.add(new Item("Яблоки", 79));
        items.add(new Item("Сосиски", 199));
        items.add(new Item("Джинсы", 2499));
        items.add(new Item("Куртка", 5999));
        items.add(new Item("Кеды", 2799));
        items.add(new Item("Проезд", 1690));
        items.add(new Item("Аренда", 25000));
        items.add(new Item("Коммунальные платежи", 3289));
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(items.get(position), context);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;

        ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
        }

        void bind(Item item, Context activityContext) {
            name.setText(item.getName());
            String regExString = activityContext.getString(R.string.price_regex_string,
                    String.valueOf(item.getPrice()), activityContext.getString(R.string.currency));
            Spannable string = new SpannableString(regExString);
            string.setSpan(new ForegroundColorSpan(Color.GRAY), string.length() - 1, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            price.setText(string);
        }
    }
}