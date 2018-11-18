package com.morova.budgettracker.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.morova.budgettracker.R;
import com.morova.budgettracker.data.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter
        extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> items = new ArrayList<>();

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        Category actualCategory = items.get(position);
        holder.categoryNameTextView.setText(actualCategory.getName());
        holder.categoryDirectionTextView.setText(actualCategory.getDirection().toString());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryNameTextView;
        private TextView categoryDirectionTextView;
        private ImageButton editCategoryItemButton;
        private ImageButton removeCategoryItemButton;

        CategoryViewHolder(View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.CategoryNameTextView);
            categoryDirectionTextView = itemView.findViewById(R.id.CategoryDirectionTextView);
            editCategoryItemButton = itemView.findViewById(R.id.EditCategoryItemButton);
            removeCategoryItemButton = itemView.findViewById(R.id.RemoveCategoryItemButton);

            editCategoryItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO implementation
                }
            });

            removeCategoryItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO implementation
                }
            });
        }
    }

    public void setCategoryItems(List<Category> categoryItems) {
        items = categoryItems;
        notifyDataSetChanged();
    }
}
