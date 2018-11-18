package com.morova.budgettracker.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.morova.budgettracker.R;

public class AddEditCashMovementItemFragment extends Fragment {

    private Button newCategoryButton;
    private EditText amountEditText;
    private EditText commentEditText;
    private Spinner categorySpinner;
    private Button saveItemButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.add_edit_cash_movement_item, container, false);

        newCategoryButton = rootView.findViewById(R.id.NewCategoryButton);
        amountEditText = rootView.findViewById(R.id.AmountEditText);
        commentEditText = rootView.findViewById(R.id.CommentEditText);
        categorySpinner = rootView.findViewById(R.id.CategorySpinner);
        saveItemButton = rootView.findViewById(R.id.SaveItemButton);


        return rootView;
    }
}
