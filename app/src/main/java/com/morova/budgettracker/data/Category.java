package com.morova.budgettracker.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

@Entity(tableName = "category")
public class Category {

    public enum Direction {
        INCOME, EXPENSE;

        @TypeConverter
        public static Direction getByOrdinal(int ordinal) {
            Direction result = null;
            for (Direction dir : Direction.values()) {
                if (dir.ordinal() == ordinal) {
                    result = dir;
                    break;
                }
            }
            return result;
        }

        @TypeConverter
        public static int toInt(Direction direction) {
            return direction.ordinal();
        }
    }

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "direction")
    private Direction direction;

    public Category(Long id, String name, Direction direction) {
        this.id = id;
        this.name = name;
        this.direction = direction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
