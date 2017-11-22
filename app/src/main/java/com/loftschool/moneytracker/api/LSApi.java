package com.loftschool.moneytracker.api;

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
    Call<AddItemResult> add(@Query("name") String name, @Query("price") int price, @Query("type") String type);

    @POST("items/remove")
    Call<RemoveResult> remove(@Query("id") int id);

    @GET("balance")
    Call<BalanceResult> balance();

    @GET("logout")
    Call<LogoutResult> logout();
}