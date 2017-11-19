package com.loftschool.moneytracker.api;

import com.loftschool.moneytracker.AuthResult;
import com.loftschool.moneytracker.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LSApi {
    @GET("items")
    Call<List<Item>> items(@Query("type") String type);

    @GET("auth")
    Call<AuthResult> auth(@Query("social_user_id") String socialUserId);

    @POST("items/add")
    Call<Integer> add(@Query("name") String name, @Query("price") int price, @Query("type") String type);
}