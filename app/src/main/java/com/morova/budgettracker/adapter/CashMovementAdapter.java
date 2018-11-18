package com.morova.budgettracker.adapter;


import android.support.annotation.NonNull;
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

    private List<CashMovementItem> items = new ArrayList<>();
    private Map<Long, Category> categoryMap = new HashMap<>();

//    private CashMovementItemClickListener listener;

//    public CashMovementAdapter(CashMovementItemClickListener listener) {
////        this.listener = listener;
//        items = new ArrayList<>();
//        categoryMap = new HashMap<>();
//    }

    @NonNull
    @Override
    public CashMovementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.cash_movement_item, parent, false);
        return new CashMovementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CashMovementViewHolder holder, int position) {

        CashMovementItem item = items.get(position);
        Category actualCategory = categoryMap.get(item.getCategoryId());
        holder.categoryTextView.setText(actualCategory.getName());
        holder.directionTextView.setText(actualCategory.getDirection().toString());
        holder.amountTextVIew.setText(String.valueOf(item.getAmount()));
        //TODO item.getComment()

//        holder.item = item;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

//    public void addItem(CashMovementItem cashMovementItem) {
//        items.add(cashMovementItem);
//        notifyItemInserted(items.size() - 1);
//    }

    public void setCashMovementItems(List<CashMovementItem> cashMovementItems) {
        items = cashMovementItems;
        notifyDataSetChanged();
    }

//    public void addCategory(Category category) {
//        categoryMap.put(category.getId(), category);
//    }

    public void setCategories(List<Category> categories) {
        categoryMap.clear();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category);
        }
        notifyDataSetChanged();
    }

//    public interface CashMovementItemClickListener {
//        void onItemChanged(CashMovementItem cashMovementItem);
//    }

    class CashMovementViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTextView;
        TextView directionTextView;
        TextView amountTextVIew;
        ImageButton removeButton;
        ImageButton editButton;

        public CashMovementViewHolder(View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.CategoryTextView);
            directionTextView = itemView.findViewById(R.id.DirectionTextView);
            amountTextVIew = itemView.findViewById(R.id.AmountTextView);
            removeButton = itemView.findViewById(R.id.CashMovementItemRemoveButton);
            editButton = itemView.findViewById(R.id.EditCashMovementItemButton);
        }
    }
}
