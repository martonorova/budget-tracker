package com.morova.budgettracker.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.morova.budgettracker.data.entities.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category_table")
    LiveData<List<Category>> getAllCategories();

    @Insert
    long insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);
}
