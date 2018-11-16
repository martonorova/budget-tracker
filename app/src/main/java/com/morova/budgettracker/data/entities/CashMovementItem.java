package com.morova.budgettracker.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.morova.budgettracker.data.entities.Category;

import java.time.LocalDateTime;

@Entity(tableName = "cash_movement_item_table",
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.SET_NULL))
public class CashMovementItem {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "amount")
    private int amount;

    @ColumnInfo(name = "date_time")
    private LocalDateTime dateTime;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "category_id")
    private Long categoryId;

    public CashMovementItem(int amount, LocalDateTime dateTime, String comment, Long categoryId) {
        this.amount = amount;
        this.dateTime = dateTime;
        this.comment = comment;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
