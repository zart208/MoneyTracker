package com.loftschool.moneytracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ItemsFragment extends Fragment {
    public static final int TYPE_EXPENSE = 0;
    public static final int TYPE_INCOME = 1;
    private static final int TYPE_UNKNOWN = -1;
    private static final String KEY_TYPE = "TYPE";
    private static final String TAG = "ItemsFragment";
    private int type = TYPE_EXPENSE;

    public static ItemsFragment createItemsFragment(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(ItemsFragment.KEY_TYPE, type);
        ItemsFragment fragment = new ItemsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemsAdapter(getContext()));
        type = getArguments().getInt(KEY_TYPE, TYPE_UNKNOWN);
        if (type == TYPE_UNKNOWN) {
            throw new IllegalStateException("Unknown fragment type");
        }
    }
}