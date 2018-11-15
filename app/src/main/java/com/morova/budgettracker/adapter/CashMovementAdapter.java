package com.morova.budgettracker.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class CashMovementAdapter
        extends RecyclerView.Adapter<CashMovementAdapter.CashMovementViewHolder> {

    @Override
    public CashMovementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CashMovementViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CashMovementViewHolder extends RecyclerView.ViewHolder {

        public CashMovementViewHolder(View itemView) {
            super(itemView);
        }
    }
}
