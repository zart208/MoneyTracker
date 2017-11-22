package com.loftschool.moneytracker;

import com.loftschool.moneytracker.api.Item;

public interface ItemsAdapterListener {
    void onItemClick(Item item, int position);

    void onItemLongClick(Item item, int position);
}
