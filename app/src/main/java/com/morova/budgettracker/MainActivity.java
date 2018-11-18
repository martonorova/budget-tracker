package com.morova.budgettracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.morova.budgettracker.adapter.CashMovementAdapter;
import com.morova.budgettracker.data.BudgetTrackerDatabase;
import com.morova.budgettracker.data.entities.CashMovementItem;
import com.morova.budgettracker.data.entities.Category;
import com.morova.budgettracker.data.viewmodels.CashMovementItemViewModel;
import com.morova.budgettracker.data.viewmodels.CategoryViewModel;

import java.time.LocalDateTime;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements CashMovementAdapter.OnItemClickListener {

    public static final int ADD_CASH_MOV_ITEM_REQUEST = 1;
    public static final int EDIT_CASH_MOV_ITEM_REQUEST = 2;

            ///TODO create resources where possible

    private CashMovementItemViewModel cashMovementItemViewModel;
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.MainRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final CashMovementAdapter adapter = new CashMovementAdapter();
        recyclerView.setAdapter(adapter);

        categoryViewModel = ViewModelProviders.of(this)
                .get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                adapter.setCategories(categories);
            }
        });

        cashMovementItemViewModel = ViewModelProviders.of(this)
                .get(CashMovementItemViewModel.class);
        cashMovementItemViewModel.getAllItems().observe(this, new Observer<List<CashMovementItem>>() {
            @Override
            public void onChanged(@Nullable List<CashMovementItem> cashMovementItems) {
                adapter.setCashMovementItems(cashMovementItems);
            }
        });




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddCashMovementItemActivity.class);
                startActivityForResult(intent, ADD_CASH_MOV_ITEM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CASH_MOV_ITEM_REQUEST && resultCode == RESULT_OK) {
            int amount = data.getIntExtra(AddCashMovementItemActivity.EXTRA_AMOUNT, -1);
            LocalDateTime dateTime = LocalDateTime.parse(
                    data.getStringExtra(AddCashMovementItemActivity.EXTRA_DATE));
            String comment = data.getStringExtra(AddCashMovementItemActivity.EXTRA_COMMENT);
            long categoryId = data.getLongExtra(AddCashMovementItemActivity.EXTRA_CATEGORY_ID, -1);

            CashMovementItem newItem = new CashMovementItem(
                    amount,
                    dateTime,
                    comment,
                    categoryId
            );

            if (amount == -1 || categoryId == -1) {
                Toast.makeText(
                        this,"Item cannot be saved, wrong amount or categoryId",
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                cashMovementItemViewModel.insert(newItem);
                Toast.makeText(this, "Item saved", Toast.LENGTH_LONG).show();
            }
        }  else {
            Toast.makeText(this, "Item not saved", Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    @Override
    public void onItemRemoveClick(CashMovementItem cashMovementItem) {
        //TODO implement warn + remove
    }

    @Override
    public void onItemEditClick(CashMovementItem cashMovementItem) {
        //TODO implement intent to edit activity
    }
}
