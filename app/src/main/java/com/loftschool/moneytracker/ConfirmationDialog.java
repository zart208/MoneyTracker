package com.loftschool.moneytracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.loftschool.moneytracker.ItemsFragment.RC_CONFIRM;

public class ConfirmationDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.confirm_remove);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getTargetFragment().onActivityResult(RC_CONFIRM, RESULT_OK, null);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getTargetFragment().onActivityResult(RC_CONFIRM, RESULT_CANCELED, null);
            }
        });
        return builder.create();
    }

}
