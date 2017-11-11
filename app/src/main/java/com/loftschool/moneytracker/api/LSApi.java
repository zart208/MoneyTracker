package com.loftschool.moneytracker.api;

import com.loftschool.moneytracker.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LSApi {
    @GET("items")
    Call<List<Item>> items(@Query("type") String type);

    @POST("items/add")
    Call add(@Query("id") int id, @Query("name") String name, @Query("price") int price, @Query("type") String type);
}