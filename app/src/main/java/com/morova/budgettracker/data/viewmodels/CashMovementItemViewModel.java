package com.morova.budgettracker.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.morova.budgettracker.data.entities.CashMovementItem;
import com.morova.budgettracker.data.repositories.CashMovementItemRepository;

import java.util.List;

public class CashMovementItemViewModel extends AndroidViewModel {

    private CashMovementItemRepository repository;
    private LiveData<List<CashMovementItem>> allItems;

    public CashMovementItemViewModel(@NonNull Application application) {
        super(application);
        repository = new CashMovementItemRepository(application);
        allItems = repository.getAllItems();
    }

    public CashMovementItem getItemById(Long id) {
        return repository.getItemById(id);
    }

    public void insert(CashMovementItem cashMovementItem) {
        repository.insert(cashMovementItem);
    }

    public void update(CashMovementItem cashMovementItem) {
        repository.update(cashMovementItem);
    }

    public void delete(CashMovementItem cashMovementItem) {
        repository.delete(cashMovementItem);
    }

    public LiveData<List<CashMovementItem>> getAllItems() {
        return allItems;
    }
}
