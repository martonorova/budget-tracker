package com.morova.budgettracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.morova.budgettracker.data.entities.CashMovementItem;
import com.morova.budgettracker.data.entities.Category;
import com.morova.budgettracker.data.viewmodels.CashMovementItemViewModel;
import com.morova.budgettracker.data.viewmodels.CategoryViewModel;
import com.morova.budgettracker.fragments.NewCategoryDialogFragment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddCashMovementItemActivity extends AppCompatActivity
        implements NewCategoryDialogFragment.NewCategoryDialogListener{

    private List<Category> categoryList = new ArrayList<>();

    private CashMovementItemViewModel cashMovementItemViewModel;
    private CategoryViewModel categoryViewModel;

    private Button newCategoryButton;
    private EditText amountEditText;
    private EditText commentEditText;
    private Spinner categorySpinner;
    private Button saveItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cash_movement_item);

        initContentView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_cash_movement_item_toolbar);
        setSupportActionBar(toolbar);


        cashMovementItemViewModel = ViewModelProviders.of(this)
                .get(CashMovementItemViewModel.class);
//        cashMovementItemViewModel.getAllItems().observe(this, new Observer<List<CashMovementItem>>() {
//            @Override
//            public void onChanged(@Nullable List<CashMovementItem> cashMovementItems) {
//                adapter.setCashMovementItems(cashMovementItems);
//            }
//        });

        final ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(this,
                R.layout.support_simple_spinner_dropdown_item, categoryList);
        categorySpinner.setAdapter(categoryArrayAdapter);

        categoryViewModel = ViewModelProviders.of(this)
                .get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                categoryList.clear();
                categoryList.addAll(categories);
                categoryArrayAdapter.notifyDataSetChanged();
            }
        });

        saveItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInput()) {
                    CashMovementItem newItem = new CashMovementItem(
                            Integer.parseInt(amountEditText.getText().toString()),
                            LocalDateTime.now(),
                            commentEditText.getText().toString(),
                            ((Category) categorySpinner.getSelectedItem()).getId()
                    );

                    cashMovementItemViewModel.insert(newItem);
                    finish();

                } else {
                    Snackbar.make(v, "Please fill the amount",Snackbar.LENGTH_LONG).show();
                }
            }

        });

        newCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NewCategoryDialogFragment()
                        .show(getSupportFragmentManager(), NewCategoryDialogFragment.TAG);
            }
        });
    }

    private void initContentView() {
        newCategoryButton = findViewById(R.id.NewCategoryButton);
        amountEditText = findViewById(R.id.AmountEditText);
        commentEditText = findViewById(R.id.CommentEditText);
        categorySpinner = findViewById(R.id.CategorySpinner);
        saveItemButton = findViewById(R.id.SaveItemButton);
    }

    private boolean isValidInput() {
        return !(amountEditText.getText().toString().isEmpty());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_cash_movement_item, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_category) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCategoryCreated(Category category) {
        Toast.makeText(this, "Create category", Toast.LENGTH_LONG).show();
        categoryViewModel.insert(category);
        //Select the newly added category
        categorySpinner.setSelection(categoryList.size());
    }
}
