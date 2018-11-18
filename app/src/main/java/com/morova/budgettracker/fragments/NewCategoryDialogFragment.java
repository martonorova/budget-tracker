package com.morova.budgettracker.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.morova.budgettracker.R;
import com.morova.budgettracker.data.entities.Category;

public class NewCategoryDialogFragment extends DialogFragment {

    public static final String TAG = "NewCategoryDialogFragment";

    private EditText categoryNameEditText;
    private ToggleButton expenseOrIncomeToggleButton;

    private NewCategoryDialogListener listener;

    public interface NewCategoryDialogListener {
        void onCategoryCreated(Category category);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewCategoryDialogListener) {
            listener = (NewCategoryDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewCategoryDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_category)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO implement category creation

                        if (isValidInput()) {
                            Category newCategory = new Category(
                                    categoryNameEditText.getText().toString(),
                                    expenseOrIncomeToggleButton.isChecked() ?
                                            Category.Direction.EXPENSE : Category.Direction.INCOME
                            );

                            listener.onCategoryCreated(newCategory);
                        } else {
                            categoryNameEditText.setError(getString(R.string.warn_specify_name));
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private boolean isValidInput() {
        return !(categoryNameEditText.getText().toString().isEmpty());
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_new_category, null);

        categoryNameEditText = contentView.findViewById(R.id.CategoryNameEditText);
        expenseOrIncomeToggleButton = contentView.findViewById(R.id.ExpenseOrIncomeToggleButton);

        return contentView;
    }
}
