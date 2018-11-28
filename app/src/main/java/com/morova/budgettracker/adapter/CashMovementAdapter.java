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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashMovementAdapter
        extends RecyclerView.Adapter<CashMovementAdapter.CashMovementViewHolder> {

    private List<CashMovementItem> items = new ArrayList<>();
    private Map<Long, Category> categoryMap = new HashMap<>();
    private OnItemClickListener listener;

    public CashMovementAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

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

        LocalDateTime localDateTime = item.getDateTime();
        String dateTimeText = localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
        holder.dateTextView.setText(dateTimeText);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setCashMovementItems(List<CashMovementItem> cashMovementItems) {
        items = cashMovementItems;
        notifyDataSetChanged();
    }

    public void setCategories(List<Category> categories) {
        categoryMap.clear();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category);
        }
        notifyDataSetChanged();
    }

    class CashMovementViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView;
        TextView directionTextView;
        TextView amountTextVIew;
        TextView dateTextView;
        ImageButton removeButton;
        ImageButton editButton;

        public CashMovementViewHolder(final View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.CategoryTextView);
            directionTextView = itemView.findViewById(R.id.DirectionTextView);
            amountTextVIew = itemView.findViewById(R.id.AmountTextView);
            dateTextView = itemView.findViewById(R.id.DateTextView);
            removeButton = itemView.findViewById(R.id.RemoveCashMovementItemButton);
            editButton = itemView.findViewById(R.id.EditCashMovementItemButton);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemRemoveClick(items.get(position));
                    }
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemEditClick(items.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemRemoveClick(CashMovementItem cashMovementItem);
        void onItemEditClick(CashMovementItem cashMovementItem);
    }
}
