package com.morova.budgettracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.morova.budgettracker.adapter.CategoryAdapter;
import com.morova.budgettracker.data.entities.Category;
import com.morova.budgettracker.data.viewmodels.CategoryViewModel;

import java.util.List;

public class CategoryListActivity extends AppCompatActivity {

    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        FloatingActionButton addCategoryFAB = findViewById(R.id.AddCategoryFAB);

        RecyclerView recyclerView = findViewById(R.id.CategoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final CategoryAdapter categoryAdapter = new CategoryAdapter();
        recyclerView.setAdapter(categoryAdapter);

        categoryViewModel = ViewModelProviders.of(this)
                .get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                categoryAdapter.setCategoryItems(categories);
            }
        });

        addCategoryFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO start addeditCAtegoryActivity
            }
        });
    }
}
