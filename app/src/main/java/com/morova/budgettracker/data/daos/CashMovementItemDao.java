package com.morova.budgettracker.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.morova.budgettracker.data.entities.CashMovementItem;

import java.util.List;

@Dao
public interface CashMovementItemDao {

    @Query("SELECT * FROM cash_movement_item")
    List<CashMovementItem> getAll();

    @Insert
    long inser(CashMovementItem cashMovementItem);

    @Update
    void update(CashMovementItem cashMovementItem);

    @Delete
    void delete(CashMovementItem cashMovementItem);
}
