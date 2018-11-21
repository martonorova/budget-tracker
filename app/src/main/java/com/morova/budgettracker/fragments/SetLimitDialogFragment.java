package com.morova.budgettracker.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.morova.budgettracker.R;

public class SetLimitDialogFragment extends DialogFragment {

    public static final String TAG = "SetLimitDialogFragment";

    private SetLimitDialogListener listener;

    private EditText setLimitEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof SetLimitDialogListener) {
            listener = (SetLimitDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the SetLimitDialogListener interface");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.set_limit)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isValidInput()) {
                            listener.onLimitSet(getLimitSet());
                        } else {
                            setLimitEditText.setError(getString(R.string.warn_fill_amount));
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private boolean isValidInput() {
        return !(setLimitEditText.getText().toString().trim().isEmpty());
    }

    private int getLimitSet() {
        return Integer.parseInt(setLimitEditText.getText().toString().trim());
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_set_limit, null);

        setLimitEditText = contentView.findViewById(R.id.SetLimitEditText);

        return contentView;
    }

    public interface SetLimitDialogListener {
        void onLimitSet(int limit);
    }
}
