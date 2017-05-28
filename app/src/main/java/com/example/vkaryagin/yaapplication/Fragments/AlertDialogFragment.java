package com.example.vkaryagin.yaapplication.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.vkaryagin.yaapplication.R;

/**
 * Created by tripo on 5/28/2017.
 */
public class AlertDialogFragment extends DialogFragment {

    public static AlertDialogFragment newInstance(@NonNull String title, String description) {
        AlertDialogFragment dialog = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String description = getArguments().getString("description");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(description)
                .setPositiveButton(R.string.alert_dialog_positive, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        }
                ).create();
    }

}
