package com.morova.budgettracker.data;

import android.app.LoaderManager;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.morova.budgettracker.data.daos.CashMovementItemDao;
import com.morova.budgettracker.data.daos.CategoryDao;
import com.morova.budgettracker.data.entities.CashMovementItem;
import com.morova.budgettracker.data.entities.Category;

import java.time.LocalDateTime;

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
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CategoryDao categoryDao;
        private CashMovementItemDao cashMovementItemDao;

        private PopulateDbAsyncTask(BudgetTrackerDatabase database) {
            categoryDao = database.categoryDao();
            cashMovementItemDao = database.cashMovementItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //Categories
             Category food = new Category("Food", Category.Direction.EXPENSE);
             Category transportation = new Category("Transportation", Category.Direction.EXPENSE);
             Category scholarship = new Category("Scholarship", Category.Direction.INCOME);
             Category allowance = new Category("Allowance", Category.Direction.INCOME);

            categoryDao.insert(food);
            categoryDao.insert(transportation);
            categoryDao.insert(scholarship);
            categoryDao.insert(allowance);

            //CashMovementItems

            cashMovementItemDao.insert(new CashMovementItem(
                    1000,
                    LocalDateTime.now(),
                    "comment 1",
                    allowance.getId()
            ));

            cashMovementItemDao.insert(new CashMovementItem(
                    353,
                    LocalDateTime.now(),
                    "comment 2",
                    food.getId()
            ));

            return null;
        }
    }
}
