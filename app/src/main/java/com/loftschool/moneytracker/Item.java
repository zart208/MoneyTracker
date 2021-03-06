package com.loftschool.moneytracker;

import java.io.Serializable;

public class Item implements Serializable {
    static final String TYPE_EXPENSE = "expense";
    static final String TYPE_INCOME = "income";
    static final String TYPE_UNKNOWN = "unknown";
    public String name;
    String type;
    int price;

    Item(String name, int price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }
}