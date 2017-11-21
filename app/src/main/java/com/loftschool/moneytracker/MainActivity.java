package com.loftschool.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ViewPager pager;
    TabLayout tabs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.pages);
        tabs = findViewById(R.id.tabs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        if (!((App) getApplication()).isLoggedIn()) {
            startActivity(new Intent(this, AuthActivity.class));
        } else {
            if (!((App) getApplication()).isAfterAddItem()) {
                pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), getResources()));
                tabs.setupWithViewPager(pager);
            }
        }
    }
}