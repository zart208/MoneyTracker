package com.loftschool.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loftschool.moneytracker.api.AuthResult;
import com.loftschool.moneytracker.api.LSApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 999;
    GoogleApiClient googleApiClient;
    private LSApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        api = ((App) getApplicationContext()).getApi();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        SignInButton signInButton = findViewById(R.id.login_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess() && result.getSignInAccount() != null) {
                GoogleSignInAccount account = result.getSignInAccount();
                api.auth(account.getId()).enqueue(new Callback<AuthResult>() {
                    @Override
                    public void onResponse(Call<AuthResult> call, Response<AuthResult> response) {
                        AuthResult authResult = response.body();
                        if (authResult != null ) {
                            ((App) getApplication()).setAuthToken(authResult.authToken);
                            ((App) getApplication()).setAfterAddItem(false);
                            finish();
                        } else {
                            showError(getString(R.string.error));
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResult> call, Throwable t) {
                        showError(t.getMessage());
                    }
                });
            }
        }
    }

    private void showError(String errorText) {
        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
    }
}
