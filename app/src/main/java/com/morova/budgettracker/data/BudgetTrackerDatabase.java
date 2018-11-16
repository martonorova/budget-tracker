package com.morova.budgettracker.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.morova.budgettracker.data.daos.CashMovementItemDao;
import com.morova.budgettracker.data.daos.CategoryDao;
import com.morova.budgettracker.data.entities.CashMovementItem;
import com.morova.budgettracker.data.entities.Category;

@Database(
        entities = {CashMovementItem.class, Category.class},
        version = 1
)
@TypeConverters(value = {Converters.class})
public abstract class BudgetTrackerDatabase extends RoomDatabase {

    private static BudgetTrackerDatabase instance;

    public abstract CategoryDao categoryDao();

    public abstract CashMovementItemDao cashMovementItemDao();

    public static synchronized BudgetTrackerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BudgetTrackerDatabase.class,
                    "budget_tracker_database")
                    .build();
        }
        return instance;
    }
}
