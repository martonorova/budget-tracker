package com.morova.budgettracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.morova.budgettracker.data.entities.CashMovementItem;
import com.morova.budgettracker.data.entities.Category;
import com.morova.budgettracker.data.viewmodels.CashMovementItemViewModel;
import com.morova.budgettracker.data.viewmodels.CategoryViewModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddCashMovementItemActivity extends AppCompatActivity {

    private List<Category> categoryList = new ArrayList<>();

    private CashMovementItemViewModel cashMovementItemViewModel;
    private CategoryViewModel categoryViewModel;

    private EditText amountEditText;
    private EditText commentEditText;
    private Spinner categorySpinner;
    private Button saveItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cash_movement_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_cash_movement_item_toolbar);
        setSupportActionBar(toolbar);

        amountEditText = findViewById(R.id.AmountEditText);
        commentEditText = findViewById(R.id.CommentEditText);
        categorySpinner = findViewById(R.id.CategorySpinner);
        saveItemButton = findViewById(R.id.SaveItemButton);



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










    }

    private boolean isValidInput() {
        return !(amountEditText.getText().toString().isEmpty());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
