package com.loftschool.moneytracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    private boolean isEnabledAddButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Новая запись");
        final EditText captionEdit = findViewById(R.id.caption);
        final EditText coastEdit = findViewById(R.id.coast);
        final ImageButton addButton = findViewById(R.id.add_button);
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
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEnabledAddButton) {
                    Toast.makeText(AddActivity.this, "Necessary fields not filled", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
