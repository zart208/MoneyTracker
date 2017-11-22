package com.loftschool.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loftschool.moneytracker.api.Item;

public class AddActivity extends AppCompatActivity {
    public static final String EXTRA_TYPE = "type";
    public static final String RESULT_ITEM = "item";
    public static final int RC_ADD_ITEM = 99;
    private boolean isEnabledAddButton = false;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle(getString(R.string.add_activity_title));
        final EditText captionEdit = findViewById(R.id.caption);
        final EditText coastEdit = findViewById(R.id.coast);
        final ImageButton addButton = findViewById(R.id.add_button);
        final TextView currencyText = findViewById(R.id.currency_text);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        captionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isEnabledAddButton = (!TextUtils.isEmpty(charSequence) && coastEdit.getText().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        coastEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isEnabledAddButton = (!TextUtils.isEmpty(charSequence) && captionEdit.getText().length() != 0);
                currencyText.setHintTextColor(getResources().getColor(R.color.inputted_text_color));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coastEdit.getText().length() == 0) {
                    currencyText.setHintTextColor(getResources().getColor(R.color.hint_text_color));
                }
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEnabledAddButton) {
                    Toast.makeText(AddActivity.this, "Necessary fields not filled", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent result = new Intent();
                result.putExtra(RESULT_ITEM, new Item(captionEdit.getText().toString(), Integer.valueOf(coastEdit.getText().toString()), type));
                setResult(RESULT_OK, result);
                ((App) getApplication()).setAfterAddItem(true);
                finish();
            }
        });

        type = getIntent().getStringExtra(EXTRA_TYPE);
    }
}
