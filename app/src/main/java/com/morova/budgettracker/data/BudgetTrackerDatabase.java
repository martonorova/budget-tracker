package com.morova.budgettracker.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(
        entities = {CashMovementItem.class, Category.class},
        version = 1
)
@TypeConverters(value = {Category.Direction.class, Converters.class})
public abstract class BudgetTrackerDatabase extends RoomDatabase {

    private static BudgetTrackerDatabase instance;

    public abstract CategoryDao categoryDao();
    public abstract CashMovementItemDao cashMovementItemDao();

    public static BudgetTrackerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BudgetTrackerDatabase.class,
                    "budget-tracker").build();
        }

        return instance;
    }

    public static void destroyInstance() {
        //TODO is that it?
        instance = null;
    }
}
