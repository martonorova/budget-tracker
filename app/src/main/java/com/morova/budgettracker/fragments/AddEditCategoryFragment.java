package com.morova.budgettracker.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.morova.budgettracker.R;

public class AddEditCategoryFragment extends Fragment {
    private RecyclerView categoryRecyclerView;
    private EditText newCategoryNameEditText;
    private ToggleButton categoryDirectionToggleButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.add_edit_category, container, false);

        categoryRecyclerView = rootView.findViewById(R.id.CategoryRecyclerView);
        newCategoryNameEditText = rootView.findViewById(R.id.NewCategoryNameEditText);
        categoryDirectionToggleButton = rootView.findViewById(R.id.CategoryDirectionToggleButton);

        return rootView;
    }
}
