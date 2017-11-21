package com.loftschool.moneytracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loftschool.moneytracker.api.BalanceResult;
import com.loftschool.moneytracker.api.LSApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceFragment extends Fragment {
    private static final String TAG = "BalanceFragment";
    private TextView balance;
    private TextView expense;
    private TextView income;
    private DiagramView diagram;
    private LSApi api;

    public static BalanceFragment createFragment() {
        Log.d(TAG, "createFragment: ");
        return new BalanceFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        api = ((App) getActivity().getApplication()).getApi();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        balance = view.findViewById(R.id.total_balance);
        expense = view.findViewById(R.id.balance_expenses);
        income = view.findViewById(R.id.balance_income);
        diagram = view.findViewById(R.id.diagram);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: ");
        if (isVisibleToUser && isResumed()) {
            Log.d(TAG, "setUserVisibleHint: true");
            updateData();
        }
    }

    private void updateData() {
        api.balance().enqueue(new Callback<BalanceResult>() {
            @Override
            public void onResponse(Call<BalanceResult> call, Response<BalanceResult> response) {
                BalanceResult result = response.body();
                if (result != null && result.isSuccess()) {
                    balance.setText(getString(R.string.price_regex_string,
                            String.valueOf(result.totalIncome - result.totalExpenses), getString(R.string.currency)));
                    expense.setText(getString(R.string.price_regex_string, String.valueOf(result.totalExpenses),
                            getString(R.string.currency)));
                    income.setText(getString(R.string.price_regex_string, String.valueOf(result.totalIncome),
                            getString(R.string.currency)));
                    diagram.update(result.totalExpenses, result.totalIncome);
                }
            }

            @Override
            public void onFailure(Call<BalanceResult> call, Throwable t) {
                t.getMessage();
            }
        });
    }
}
