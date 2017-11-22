package com.loftschool.moneytracker.api;

import java.io.Serializable;

public class Item implements Serializable {
    public static final String TYPE_EXPENSE = "expense";
    public static final String TYPE_INCOME = "income";
    public static final String TYPE_UNKNOWN = "unknown";
    public String name;
    public String type;
    public int price;
    public int id;

    public Item(String name, int price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    @Override
    public String toString() {
        return "id: " + id + ", name: " + name + ", price: " + price + ", type: " + type;
    }
}