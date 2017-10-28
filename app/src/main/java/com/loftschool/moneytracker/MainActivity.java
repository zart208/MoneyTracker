package com.loftschool.moneytracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart() override");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop() override");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy() override");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause() override");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume() override");
    }
}
