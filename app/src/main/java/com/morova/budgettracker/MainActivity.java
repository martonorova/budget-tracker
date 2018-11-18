package com.morova.budgettracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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

import com.morova.budgettracker.adapter.CashMovementAdapter;
import com.morova.budgettracker.data.BudgetTrackerDatabase;
import com.morova.budgettracker.data.entities.CashMovementItem;
import com.morova.budgettracker.data.entities.Category;
import com.morova.budgettracker.data.viewmodels.CashMovementItemViewModel;
import com.morova.budgettracker.data.viewmodels.CategoryViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity/* implements
        CashMovementAdapter.CashMovementItemClickListener*/ {

            ///TODO create resources where possible

    private CashMovementItemViewModel cashMovementItemViewModel;
    private CategoryViewModel categoryViewModel;

//    private RecyclerView recyclerView;

//    private BudgetTrackerDatabase database;

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

                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                startActivity(intent);

                //TODO implement
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

//        database = BudgetTrackerDatabase.getInstance(getApplicationContext());
    }

//    private void initRecyclerView() {
//        recyclerView = findViewById(R.id.MainRecyclerView);
//        adapter = new CashMovementAdapter(this);
//        loadItemsInBackground();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//    }

//    private void loadItemsInBackground() {
//
//        //CashMovementItems
//
//        new AsyncTask<Void, Void, List<CashMovementItem>>() {
//            @Override
//            protected List<CashMovementItem> doInBackground(Void... voids) {
//                return database.cashMovementItemDao().getAllItems();
//            }
//
//            @Override
//            protected void onPostExecute(List<CashMovementItem> cashMovementItems) {
//                adapter.updateCashMovementItems(cashMovementItems);
//            }
//        }.execute();
//
//
//        //Categories
//
//        new AsyncTask<Void, Void, List<Category>>() {
//            @Override
//            protected List<Category> doInBackground(Void... voids) {
//                return database.categoryDao().getAll();
//            }
//
//            @Override
//            protected void onPostExecute(List<Category> categories) {
//                adapter.updateCategories(categories);
//            }
//        }.execute();
//
//
//
//    }

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

//    @Override
//    public void onItemChanged(CashMovementItem cashMovementItem) {
//
//    }
}
