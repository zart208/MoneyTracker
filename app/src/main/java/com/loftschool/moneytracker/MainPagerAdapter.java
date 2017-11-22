package com.loftschool.moneytracker;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static com.loftschool.moneytracker.api.Item.TYPE_EXPENSE;
import static com.loftschool.moneytracker.api.Item.TYPE_INCOME;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private final static int PAGE_EXPENSES = 0;
    private final static int PAGE_INCOMES = 1;
    private final static int PAGE_BALANCE = 2;
    private String[] titles;


    MainPagerAdapter(FragmentManager fm, Resources res) {
        super(fm);
        titles = res.getStringArray(R.array.tabs_titles);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGE_EXPENSES:
                return ItemsFragment.createItemsFragment(TYPE_EXPENSE);
            case PAGE_INCOMES:
                return ItemsFragment.createItemsFragment(TYPE_INCOME);
            case PAGE_BALANCE:
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