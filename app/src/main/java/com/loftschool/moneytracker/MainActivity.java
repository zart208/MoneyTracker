package com.loftschool.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loftschool.moneytracker.api.LogoutResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        super.onResume();
        Log.d(TAG, "onResume: start");
        if (!((App) getApplication()).isLoggedIn()) {
            Log.d(TAG, "onResume: auth");
            startActivity(new Intent(this, AuthActivity.class));
        } else {
            if (!((App) getApplication()).isAfterAddItem()) {
                Log.d(TAG, "onResume: after add");
                pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), getResources()));
                tabs.setupWithViewPager(pager);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        logout();
        return true;
    }

    private void logout() {
        ((App) getApplication()).getApi().logout().enqueue(new Callback<LogoutResult>() {
            @Override
            public void onResponse(Call<LogoutResult> call, Response<LogoutResult> response) {

            }

            @Override
            public void onFailure(Call<LogoutResult> call, Throwable t) {
                t.getMessage();
            }
        });
        ((App) getApplication()).setAuthToken(null);
        Log.d(TAG, "logout: after logout");
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}