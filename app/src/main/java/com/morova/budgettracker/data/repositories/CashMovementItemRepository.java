package com.morova.budgettracker.data.repositories;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.morova.budgettracker.data.BudgetTrackerDatabase;
import com.morova.budgettracker.data.daos.CashMovementItemDao;
import com.morova.budgettracker.data.entities.CashMovementItem;

import java.util.List;

public class CashMovementItemRepository {
    private CashMovementItemDao cashMovementItemDao;
    private LiveData<List<CashMovementItem>> allItems;

    public CashMovementItemRepository(Application application) {
        BudgetTrackerDatabase database = BudgetTrackerDatabase.getInstance(application);
        cashMovementItemDao = database.cashMovementItemDao();
        allItems = cashMovementItemDao.getAllItems();
    }

    public void insert(CashMovementItem cashMovementItem) {
        new InsertCashMovementItemAsyncTask(cashMovementItemDao)
                .execute(cashMovementItem);
    }

    public void update(CashMovementItem cashMovementItem) {
        new UpdateCashMovementItemAsyncTask(cashMovementItemDao)
                .execute(cashMovementItem);
    }

    public void delete(CashMovementItem cashMovementItem) {
        new DeleteCashMovementItemAsyncTask(cashMovementItemDao)
                .execute(cashMovementItem);
    }

    public LiveData<List<CashMovementItem>> getAllItems() {
        return allItems;
    }

    private static class InsertCashMovementItemAsyncTask
            extends AsyncTask<CashMovementItem, Void, Void> {

        private CashMovementItemDao cashMovementItemDao;

        private InsertCashMovementItemAsyncTask(CashMovementItemDao cashMovementItemDao) {
            this.cashMovementItemDao = cashMovementItemDao;
        }

        @Override
        protected Void doInBackground(CashMovementItem... cashMovementItems) {
            cashMovementItemDao.insert(cashMovementItems[0]);
            return null;
        }
    }

    private static class UpdateCashMovementItemAsyncTask
            extends AsyncTask<CashMovementItem, Void, Void> {

        private CashMovementItemDao cashMovementItemDao;

        private UpdateCashMovementItemAsyncTask(CashMovementItemDao cashMovementItemDao) {
            this.cashMovementItemDao = cashMovementItemDao;
        }

        @Override
        protected Void doInBackground(CashMovementItem... cashMovementItems) {
            cashMovementItemDao.update(cashMovementItems[0]);
            return null;
        }
    }

    private static class DeleteCashMovementItemAsyncTask
            extends AsyncTask<CashMovementItem, Void, Void> {

        private CashMovementItemDao cashMovementItemDao;

        private DeleteCashMovementItemAsyncTask(CashMovementItemDao cashMovementItemDao) {
            this.cashMovementItemDao = cashMovementItemDao;
        }

        @Override
        protected Void doInBackground(CashMovementItem... cashMovementItems) {
            cashMovementItemDao.delete(cashMovementItems[0]);
            return null;
        }
    }

}
