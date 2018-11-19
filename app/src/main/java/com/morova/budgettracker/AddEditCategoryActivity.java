package com.morova.budgettracker;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.morova.budgettracker.data.entities.Category;

public class AddEditCategoryActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.morova.budgettracker.AddEditCategoryActivity.EXTRA_ID";
    public static final String EXTRA_NAME = "com.morova.budgettracker.AddEditCategoryActivity.EXTRA_NAME";
    public static final String EXTRA_DIRECTION = "com.morova.budgettracker.AddEditCategoryActivity.EXTRA_DIRECTION";


    private EditText categoryNameEditText;
    private ToggleButton expenseOrIncomeToggleButton;
    private Button saveCategoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        initContentView();

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {

            setTitle(getString(R.string.edit_category));
            categoryNameEditText.setText(intent.getStringExtra(EXTRA_NAME));

            String direction = intent.getStringExtra(EXTRA_DIRECTION);

            if (direction.equals(Category.Direction.INCOME.toString())) {
                expenseOrIncomeToggleButton.setChecked(false);
            } else if (direction.equals(Category.Direction.EXPENSE.toString())) {
                expenseOrIncomeToggleButton.setChecked(true);
            } else {
                expenseOrIncomeToggleButton.setChecked(false);
            }

        } else {
            setTitle(getString(R.string.add_category));
        }

        saveCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInput()) {

                    saveCategory();
                } else {
                    Snackbar.make(v, R.string.warn_fill_name_of_category,Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveCategory() {

        String categoryName = categoryNameEditText.getText().toString();
        Category.Direction direction =
                expenseOrIncomeToggleButton.isChecked() ?
                        Category.Direction.EXPENSE : Category.Direction.INCOME;

        Intent data = new Intent();

        data.putExtra(EXTRA_NAME, categoryName);
        data.putExtra(EXTRA_DIRECTION, direction.toString());

        long id = getIntent().getLongExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    private boolean isValidInput() {
        return !(categoryNameEditText.getText().toString().trim().isEmpty());
    }

    private void initContentView() {
        categoryNameEditText = findViewById(R.id.CategoryNameEditText);
        expenseOrIncomeToggleButton = findViewById(R.id.ExpenseOrIncomeToggleButton);
        saveCategoryButton = findViewById(R.id.SaveCategoryButton);
    }
}
