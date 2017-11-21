package com.loftschool.moneytracker;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import static com.loftschool.moneytracker.Item.TYPE_EXPENSE;
import static com.loftschool.moneytracker.Item.TYPE_INCOME;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private final static int PAGE_EXPENSES = 0;
    private final static int PAGE_INCOMES = 1;
    private final static int PAGE_BALANCE = 2;
    private static final String TAG = "MainPagerAdapter";
    private String[] titles;


    MainPagerAdapter(FragmentManager fm, Resources res) {
        super(fm);
        Log.d(TAG, "MainPagerAdapter: ");
        titles = res.getStringArray(R.array.tabs_titles);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: ");
        switch (position) {
            case PAGE_EXPENSES:
                Log.d(TAG, "getItem: page expens");
                return ItemsFragment.createItemsFragment(TYPE_EXPENSE);
            case PAGE_INCOMES:
                Log.d(TAG, "getItem: page income");
                return ItemsFragment.createItemsFragment(TYPE_INCOME);
            case PAGE_BALANCE:
                Log.d(TAG, "getItem: page balance");
                return BalanceFragment.createFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}