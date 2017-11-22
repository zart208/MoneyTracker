package com.loftschool.moneytracker;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loftschool.moneytracker.api.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> items = new ArrayList<>();
    private Context context;
    private ItemsAdapterListener listener = null;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    ItemsAdapter(Context activityContext) {
        context = activityContext;
    }

    void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setListener(ItemsAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(items.get(position), position, selectedItems.get(position, false), context, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    int getSelectedItemCount() {
        return selectedItems.size();
    }

    List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    Item remove(int pos) {
        final Item item = items.remove(pos);
        notifyItemRemoved(pos);
        return item;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;
        private ItemsAdapterListener listener = null;

        ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
        }

        void bind(final Item item, final int position, boolean isSelected, Context activityContext, final ItemsAdapterListener listener) {
            this.listener = listener;
            name.setText(item.name);
            String regExString = activityContext.getString(R.string.price_regex_string,
                    String.valueOf(item.price), activityContext.getString(R.string.currency));
            Spannable string = new SpannableString(regExString);
            string.setSpan(new ForegroundColorSpan(Color.GRAY), string.length() - 1, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            price.setText(string);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item, position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(item, position);
                    return true;
                }
            });
            itemView.setActivated(isSelected);
        }
    }
}