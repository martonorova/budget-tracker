package com.morova.budgettracker;

import android.app.Notification;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.morova.budgettracker.adapter.CashMovementAdapter;
import com.morova.budgettracker.data.entities.CashMovementItem;
import com.morova.budgettracker.data.entities.Category;
import com.morova.budgettracker.data.viewmodels.CashMovementItemViewModel;
import com.morova.budgettracker.data.viewmodels.CategoryViewModel;
import com.morova.budgettracker.fragments.SetLimitDialogFragment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements CashMovementAdapter.OnItemClickListener, SetLimitDialogFragment.SetLimitDialogListener{

    public static final int ADD_CASH_MOV_ITEM_REQUEST = 1;
    public static final int EDIT_CASH_MOV_ITEM_REQUEST = 2;

    public static final String KEY_LIMIT = "com.morova.budgettracker.MainActivity.KEY_LIMIT";

    private CashMovementItemViewModel cashMovementItemViewModel;
    private CategoryViewModel categoryViewModel;

    private FloatingActionButton fab;
    private TextView spentTextView;
    private TextView limitTextView;
    private Button diagramsButton;

    private CashMovementAdapter adapter = new CashMovementAdapter(MainActivity.this);

    private List<CashMovementItem> itemList = new ArrayList<>();
    private HashMap<Long, Category> categoryMap = new HashMap<>();

    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = NotificationManagerCompat.from(this);

        initContentView();
        loadLimit();

        initRecyclerView();
        initViewModels();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.MainRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void initViewModels() {
        categoryViewModel = ViewModelProviders.of(MainActivity.this)
                .get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(MainActivity.this, new CategoryObserver());

        cashMovementItemViewModel = ViewModelProviders.of(MainActivity.this)
                .get(CashMovementItemViewModel.class);
        cashMovementItemViewModel.getAllItems().observe(MainActivity.this, new CashMovementItemsObserver());
    }

    private void initContentView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spentTextView = findViewById(R.id.SpentTextView);
        limitTextView = findViewById(R.id.LimitTextView);
        diagramsButton = findViewById(R.id.DiagramsButton);
        diagramsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiagramsActivity.class);
                startActivity(intent);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddEditCashMovementItemActivity.class);
                startActivityForResult(intent, ADD_CASH_MOV_ITEM_REQUEST);
            }
        });
    }

    private void updateSummary() {

        int sumSpentAmount = 0;
        for (CashMovementItem item : itemList) {
            Category actualCategory;

            try {
                actualCategory = categoryMap.get(item.getCategoryId());

            } catch (NullPointerException ex) {
                Toast.makeText(MainActivity.this,
                        String.format(getString(R.string.category_load_error), item.getId()),
                        Toast.LENGTH_LONG).show();
                continue;
            }
            sumSpentAmount += actualCategory.getDirection().equals(Category.Direction.EXPENSE) ?
                            item.getAmount() : 0;
        }

        spentTextView.setText(String.valueOf(sumSpentAmount));
        checkLimit(sumSpentAmount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CASH_MOV_ITEM_REQUEST && resultCode == RESULT_OK) {

            CashMovementItem newItem = createItemFromIntent(data);

            if (newItem.getAmount() == -1 || newItem.getCategoryId() == -1) {
                Toast.makeText(
                        this, R.string.item_save_error,
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                cashMovementItemViewModel.insert(newItem);

                Toast.makeText(this, R.string.item_saved, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == EDIT_CASH_MOV_ITEM_REQUEST && resultCode == RESULT_OK) {

            long id = data.getLongExtra(AddEditCashMovementItemActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(
                        MainActivity.this, R.string.item_update_error, Toast.LENGTH_LONG).show();
                return;
            }

            CashMovementItem newItem = createItemFromIntent(data);
            newItem.setId(id);

            if (newItem.getAmount() == -1 || newItem.getCategoryId() == -1) {
                Toast.makeText(
                        this,R.string.item_update_error,
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                cashMovementItemViewModel.update(newItem);
                Toast.makeText(this, R.string.item_updated, Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, R.string.item_not_saved, Toast.LENGTH_LONG)
                    .show();
        }
    }

    private CashMovementItem createItemFromIntent(Intent data) {
        int amount = data.getIntExtra(AddEditCashMovementItemActivity.EXTRA_AMOUNT, -1);
        LocalDateTime dateTime = LocalDateTime.parse(
                data.getStringExtra(AddEditCashMovementItemActivity.EXTRA_DATE));
        String comment = data.getStringExtra(AddEditCashMovementItemActivity.EXTRA_COMMENT);
        long categoryId = data.getLongExtra(AddEditCashMovementItemActivity.EXTRA_CATEGORY_ID, -1);

        return new CashMovementItem(
                amount,
                dateTime,
                comment,
                categoryId
        );
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

        switch (id) {
            case R.id.action_manage_categories:
                Intent intent = new Intent(MainActivity.this, CategoryListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_set_limit:
                new SetLimitDialogFragment()
                        .show(getSupportFragmentManager(), SetLimitDialogFragment.TAG);
                return true;
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

    private class CashMovementItemsObserver implements Observer<List<CashMovementItem>> {
        @Override
        public void onChanged(@Nullable List<CashMovementItem> cashMovementItems) {

            adapter.setCashMovementItems(cashMovementItems);
            setItemList(cashMovementItems);
        }
    }

    private class CategoryObserver implements Observer<List<Category>> {
        @Override
        public void onChanged(@Nullable List<Category> categories) {

            adapter.setCategories(categories);
            setCategoryMap(categories);
        }
    }

    private void setItemList(List<CashMovementItem> cashMovementItems) {
        itemList.clear();
        itemList.addAll(cashMovementItems);

        updateSummary();
    }

    private void setCategoryMap(List<Category> categories) {
        categoryMap.clear();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category);
        }
        updateSummary();
    }

    @Override
    public void onLimitSet(int limit) {
        limitTextView.setText(String.valueOf(limit));
        saveLimit(limit);
    }

    private void loadLimit() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        int limit = sharedPreferences.getInt(KEY_LIMIT, 100000);

        if (limit == 100000) {
            Toast.makeText(this, getString(R.string.warn_limit_not_loaded), Toast.LENGTH_LONG);
        }

        limitTextView.setText(String.valueOf(limit));
    }

    private void saveLimit(int limit) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_LIMIT, limit);

        editor.apply();
    }

    private void checkLimit(int sumSpentAmount) {
        int limit = Integer.parseInt(limitTextView.getText().toString().trim());

        if (sumSpentAmount >= limit) {
            Notification notification = new Notification.Builder(this, App.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_warning_black)
                    .setContentTitle(getString(R.string.limit_warning))
                    .setContentText(String.format(getString(R.string.limit_cross_msg_format), limit))
                    .build();

            notificationManager.notify(1, notification);
            return;
        }

        if (sumSpentAmount >= (limit * 0.8)) {
            Notification notification = new Notification.Builder(this, App.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_warning_black)
                    .setContentTitle(getString(R.string.limit_warning))
                    .setContentText(String.format(getString(R.string.limit_close_msg_format), limit))
                    .build();

            notificationManager.notify(1, notification);
        }
    }
}
