package com.morova.budgettracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.morova.budgettracker.adapter.CashMovementAdapter;
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

        final CashMovementAdapter adapter = new CashMovementAdapter(this);
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

                Intent intent = new Intent(MainActivity.this, AddEditCashMovementItemActivity.class);
                startActivityForResult(intent, ADD_CASH_MOV_ITEM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CASH_MOV_ITEM_REQUEST && resultCode == RESULT_OK) {

            int amount = data.getIntExtra(AddEditCashMovementItemActivity.EXTRA_AMOUNT, -1);
            LocalDateTime dateTime = LocalDateTime.parse(
                    data.getStringExtra(AddEditCashMovementItemActivity.EXTRA_DATE));
            String comment = data.getStringExtra(AddEditCashMovementItemActivity.EXTRA_COMMENT);
            long categoryId = data.getLongExtra(AddEditCashMovementItemActivity.EXTRA_CATEGORY_ID, -1);

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
        } else if (requestCode == EDIT_CASH_MOV_ITEM_REQUEST && resultCode == RESULT_OK) {

            long id = data.getLongExtra(AddEditCashMovementItemActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(
                        MainActivity.this, "Item cannot be updated.", Toast.LENGTH_LONG).show();
                return;
            }

            int amount = data.getIntExtra(AddEditCashMovementItemActivity.EXTRA_AMOUNT, -1);
            LocalDateTime dateTime = LocalDateTime.parse(
                    data.getStringExtra(AddEditCashMovementItemActivity.EXTRA_DATE));
            String comment = data.getStringExtra(AddEditCashMovementItemActivity.EXTRA_COMMENT);
            long categoryId = data.getLongExtra(AddEditCashMovementItemActivity.EXTRA_CATEGORY_ID, -1);

            CashMovementItem newItem = new CashMovementItem(
                    amount,
                    dateTime,
                    comment,
                    categoryId
            );

            newItem.setId(id);

            if (amount == -1 || categoryId == -1) {
                Toast.makeText(
                        this,"Item cannot be updated, wrong amount or categoryId",
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                cashMovementItemViewModel.update(newItem);
                Toast.makeText(this, "Item updated", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Item not saved", Toast.LENGTH_LONG)
                    .show();
        }

    }

    //TODO use this (as in categories)
    private CashMovementItem createItemFromIntent(Intent data) {
        int amount = data.getIntExtra(AddEditCashMovementItemActivity.EXTRA_AMOUNT, -1);
        LocalDateTime dateTime = LocalDateTime.parse(
                data.getStringExtra(AddEditCashMovementItemActivity.EXTRA_DATE));
        String comment = data.getStringExtra(AddEditCashMovementItemActivity.EXTRA_COMMENT);
        long categoryId = data.getLongExtra(AddEditCashMovementItemActivity.EXTRA_CATEGORY_ID, -1);

        CashMovementItem newItem = new CashMovementItem(
                amount,
                dateTime,
                comment,
                categoryId
        );

        return newItem;
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

        if (id == R.id.action_manage_categories) {
            Intent intent = new Intent(MainActivity.this, CategoryListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemRemoveClick(final CashMovementItem cashMovementItem) {
         AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
         alertBox.setMessage(R.string.warn_delete_item);
         alertBox.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 cashMovementItemViewModel.delete(cashMovementItem);
             }
         });
         alertBox.setNeutralButton(R.string.cancel, null);
         alertBox.show();


        //cashMovementItemViewModel.delete(cashMovementItem);
    }

    @Override
    public void onItemEditClick(CashMovementItem cashMovementItem) {


        Intent intent = new Intent(MainActivity.this, AddEditCashMovementItemActivity.class);
        intent.putExtra(AddEditCashMovementItemActivity.EXTRA_ID, cashMovementItem.getId());
        intent.putExtra(AddEditCashMovementItemActivity.EXTRA_AMOUNT, cashMovementItem.getAmount());
        intent.putExtra(AddEditCashMovementItemActivity.EXTRA_DATE, cashMovementItem.getDateTime().toString());
        intent.putExtra(AddEditCashMovementItemActivity.EXTRA_COMMENT, cashMovementItem.getComment());
        intent.putExtra(AddEditCashMovementItemActivity.EXTRA_CATEGORY_ID, cashMovementItem.getCategoryId());

        startActivityForResult(intent, EDIT_CASH_MOV_ITEM_REQUEST);
    }
}
