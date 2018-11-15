package com.morova.budgettracker.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(
        entities = {CashMovementItem.class, Category.class},
        version = 1
)
@TypeConverters(value = {Category.Direction.class})
public abstract class BudgetTrackerDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
    public abstract CashMovementItemDao cashMovementItemDao();
}
