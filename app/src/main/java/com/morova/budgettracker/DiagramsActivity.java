package com.morova.budgettracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.morova.budgettracker.data.entities.CashMovementItem;
import com.morova.budgettracker.data.entities.Category;
import com.morova.budgettracker.data.viewmodels.CashMovementItemViewModel;
import com.morova.budgettracker.data.viewmodels.CategoryViewModel;

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


        initGraphView();
    }

    private void initGraphView() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                new DataPoint[] {
                        new DataPoint(0, 1),
                        new DataPoint(1, 5),
                        new DataPoint(2, 3),
                        new DataPoint(3, 2),
                        new DataPoint(4, 6)
                }
        );

        mainGraphView.addSeries(series);
    }

    private void initContentView() {
        mainGraphView = findViewById(R.id.MainGraphView);
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
        }
    }

    private class CategoryObserver implements Observer<List<Category>> {
        @Override
        public void onChanged(@Nullable List<Category> categories) {
            setCategoryMap(categories);
        }
    }


}
