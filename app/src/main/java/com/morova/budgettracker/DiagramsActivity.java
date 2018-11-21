package com.morova.budgettracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.morova.budgettracker.data.entities.CashMovementItem;
import com.morova.budgettracker.data.entities.Category;
import com.morova.budgettracker.data.viewmodels.CashMovementItemViewModel;
import com.morova.budgettracker.data.viewmodels.CategoryViewModel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiagramsActivity extends AppCompatActivity {

    private GraphView mainGraphView;

    private CashMovementItemViewModel cashMovementItemViewModel;
    private CategoryViewModel categoryViewModel;

    private List<CashMovementItem> itemList = new ArrayList<>();
    private HashMap<Long, Category> categoryMap = new HashMap<>();

    //TODO filter functions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagrams);

        initContentView();


        categoryViewModel = ViewModelProviders.of(this)
                .get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, new DiagramsActivity.CategoryObserver());

        cashMovementItemViewModel = ViewModelProviders.of(this)
                .get(CashMovementItemViewModel.class);
        cashMovementItemViewModel.getAllItems().observe(this, new DiagramsActivity.CashMovementItemsObserver());
    }

    private void updateGraphView() {

        DataPoint[] dataPoints = getDataPointsFromList(itemList, Category.Direction.EXPENSE);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                dataPoints
        );
        mainGraphView.addSeries(series);

        mainGraphView.getGridLabelRenderer().setLabelFormatter(
                new DateAsXAxisLabelFormatter(this)
        );
    }

    private DataPoint[] getDataPointsFromList(List<CashMovementItem> cashMovementItems, Category.Direction direction) {
        List<DataPoint> series = new ArrayList<>();
        int sumDirection = 0;

        for (CashMovementItem item : cashMovementItems) {

            if (categoryMap.get(item.getCategoryId()).getDirection()
                    .equals(direction)) {

                sumDirection += item.getAmount();

                series.add(new DataPoint(
                        convertToDate(item.getDateTime()),
                        sumDirection));
            }
        }

        return series.toArray(new DataPoint[series.size()]);
    }

    private java.util.Date convertToDate(LocalDateTime dateTime) {
        return java.util.Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void initContentView() {
        mainGraphView = findViewById(R.id.MainGraphView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_diagrams);
        setSupportActionBar(toolbar);
    }

    private void setItemList(List<CashMovementItem> cashMovementItems) {
        itemList.clear();
        itemList.addAll(cashMovementItems);
    }

    private void setCategoryMap(List<Category> categories) {
        categoryMap.clear();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category);
        }
    }

    private class CashMovementItemsObserver implements Observer<List<CashMovementItem>> {
        @Override
        public void onChanged(@Nullable List<CashMovementItem> cashMovementItems) {
            setItemList(cashMovementItems);
            updateGraphView();
        }
    }

    private class CategoryObserver implements Observer<List<Category>> {
        @Override
        public void onChanged(@Nullable List<Category> categories) {
            setCategoryMap(categories);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diagrams, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case R.id.action_expenses_diagram:
                //TODO show expense diagram
                return true;
            case R.id.action_income_diagram:
                //TODO show income diagram
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
