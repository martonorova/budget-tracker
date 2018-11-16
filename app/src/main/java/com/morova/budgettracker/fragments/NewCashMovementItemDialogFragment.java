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

import com.morova.budgettracker.R;
import com.morova.budgettracker.data.entities.CashMovementItem;

public class NewCashMovementItemDialogFragment extends DialogFragment {

    public static final String TAG = "NewCashMovementItemDialogFragment";



    private NewCashMovementItemDialogListener listener;

    public NewCashMovementItemDialogFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity activity = getActivity();
        if (activity instanceof NewCashMovementItemDialogListener) {
            listener = (NewCashMovementItemDialogListener) activity;

        } else {
            throw new RuntimeException(
                    "Activity must implement the NewCashMovementItemDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //TODO itt requireContext volt a peldaban
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.new_cash_movement_item)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO implement item creation
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {

        View contentView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_new_cash_movement_item, null);
    }

    public interface NewCashMovementItemDialogListener {
        void onCashMovementItemCreated(CashMovementItem newItem);
    }


}
