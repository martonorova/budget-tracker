package com.morova.budgettracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.morova.budgettracker.adapter.CategoryAdapter;
import com.morova.budgettracker.data.entities.Category;
import com.morova.budgettracker.data.viewmodels.CategoryViewModel;

import java.util.List;

public class CategoryListActivity extends AppCompatActivity
        implements CategoryAdapter.OnItemClickListener {

    public static final int ADD_CATEGORY_REQUEST = 1;
    public static final int EDIT_CATEGORY_REQUEST = 2;

    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        FloatingActionButton addCategoryFAB = findViewById(R.id.AddCategoryFAB);

        RecyclerView recyclerView = findViewById(R.id.CategoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final CategoryAdapter categoryAdapter = new CategoryAdapter(this);
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
                Intent intent = new Intent(CategoryListActivity.this, AddEditCategoryActivity.class);
                startActivityForResult(intent, ADD_CATEGORY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CATEGORY_REQUEST && resultCode == RESULT_OK) {

            Category newCategory = createCategoryFromIntent(data);

            categoryViewModel.insert(newCategory);
            Toast.makeText(CategoryListActivity.this, getString(R.string.category_saved), Toast.LENGTH_LONG)
                    .show();


        } else if (requestCode == EDIT_CATEGORY_REQUEST && resultCode == RESULT_OK) {

            long id = data.getLongExtra(AddEditCategoryActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, R.string.category_cannot_be_updated, Toast.LENGTH_LONG)
                        .show();
                return;
            }

//            String name = data.getStringExtra(AddEditCategoryActivity.EXTRA_NAME);
//            Category.Direction direction =
//                    data.getStringExtra(AddEditCategoryActivity.EXTRA_DIRECTION)
//                            .equals(Category.Direction.INCOME.toString()) ?
//                            Category.Direction.INCOME : Category.Direction.EXPENSE;
//
//            Category category = new Category(name, direction);

            Category category = createCategoryFromIntent(data);

            category.setId(id);

            categoryViewModel.update(category);

            Toast.makeText(this, getString(R.string.category_updated), Toast.LENGTH_LONG)
                    .show();

        } else {
            Toast.makeText(CategoryListActivity.this, getString(R.string.category_not_saved), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private Category createCategoryFromIntent(Intent data) {
        String name = data.getStringExtra(AddEditCategoryActivity.EXTRA_NAME);
        Category.Direction direction =
                data.getStringExtra(AddEditCategoryActivity.EXTRA_DIRECTION)
                        .equals(Category.Direction.INCOME.toString()) ?
                        Category.Direction.INCOME : Category.Direction.EXPENSE;

        return new Category(name, direction);
    }

    @Override
    public void onItemRemoveClick(final Category category) {

        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setMessage(R.string.warn_delete_category);
        alertBox.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoryViewModel.delete(category);
            }
        });
        alertBox.setNeutralButton(R.string.cancel, null);
        alertBox.show();
    }

    @Override
    public void onItemEditClick(Category category) {
        Intent intent = new Intent(CategoryListActivity.this, AddEditCategoryActivity.class);

        intent.putExtra(AddEditCategoryActivity.EXTRA_ID, category.getId());
        intent.putExtra(AddEditCategoryActivity.EXTRA_NAME, category.getName());
        intent.putExtra(AddEditCategoryActivity.EXTRA_DIRECTION, category.getDirection().toString());

        startActivityForResult(intent, EDIT_CATEGORY_REQUEST);
    }
}
