package com.morova.budgettracker.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.morova.budgettracker.R;
import com.morova.budgettracker.data.entities.CashMovementItem;
import com.morova.budgettracker.data.entities.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashMovementAdapter
        extends RecyclerView.Adapter<CashMovementAdapter.CashMovementViewHolder> {

    private final List<CashMovementItem> items;
    private final Map<Long, Category> categoryMap;

    private CashMovementItemClickListener listener;

    public CashMovementAdapter(CashMovementItemClickListener listener) {
        this.listener = listener;
        items = new ArrayList<>();
        categoryMap = new HashMap<>();
    }

    @Override
    public CashMovementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_cash_movement_list, parent, false);
        return new CashMovementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CashMovementViewHolder holder, int position) {

        CashMovementItem item = items.get(position);
        Category actualCategory = categoryMap.get(item.getCategoryId());
        holder.categoryTextView.setText(actualCategory.getName());
        holder.directionTextView.setText(actualCategory.getDirection().toString());
        holder.amountTextVIew.setText(item.getAmount());
        //TODO item.getComment()

        holder.item = item;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(CashMovementItem cashMovementItem) {
        items.add(cashMovementItem);
        notifyItemInserted(items.size() - 1);
    }

    public void updateCashMovementItems(List<CashMovementItem> cashMovementItems) {
        items.clear();
        items.addAll(cashMovementItems);
        notifyDataSetChanged();
    }

    public void addCategory(Category category) {
        categoryMap.put(category.getId(), category);
    }

    public void updateCategories(List<Category> categories) {
        for (Category category : categories) {
            categoryMap.put(category.getId(), category);
        }
    }

    public interface CashMovementItemClickListener {
        void onItemChanged(CashMovementItem cashMovementItem);
    }

    class CashMovementViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTextView;
        TextView directionTextView;
        TextView amountTextVIew;
        ImageButton removeButton;

        CashMovementItem item;

        public CashMovementViewHolder(View itemView) {
            super(itemView);

            categoryTextView = itemView.findViewById(R.id.CategoryTextView);
            directionTextView = itemView.findViewById(R.id.DirectionTextView);
            amountTextVIew = itemView.findViewById(R.id.AmountTextView);
            removeButton = itemView.findViewById(R.id.CashMovementItemRemoveButton);
        }
    }
}
