package com.morova.budgettracker.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.morova.budgettracker.data.entities.CashMovementItem;

import java.util.List;

@Dao
public interface CashMovementItemDao {

    //TODO create query to filter by date created

    @Query("SELECT * FROM cash_movement_item_table")
    LiveData<List<CashMovementItem>> getAllItems();

    @Query("SELECT * FROM cash_movement_item_table WHERE id = :id")
    CashMovementItem getItemById(Long id);

    @Insert
    long insert(CashMovementItem cashMovementItem);

    @Update
    void update(CashMovementItem cashMovementItem);

    @Delete
    void delete(CashMovementItem cashMovementItem);
}
