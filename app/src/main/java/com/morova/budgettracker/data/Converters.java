package com.morova.budgettracker.data;

import android.arch.persistence.room.TypeConverter;

import com.morova.budgettracker.data.entities.Category;

import java.time.LocalDateTime;

public class Converters {

    @TypeConverter
    public LocalDateTime fromDateTimeString(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString);
    }

    @TypeConverter
    public String toDateTimeString(LocalDateTime localDateTime) {
        return localDateTime.toString();
    }

    @TypeConverter
    public static Category.Direction getByOrdinal(int ordinal) {
        Category.Direction result = null;
        for (Category.Direction dir : Category.Direction.values()) {
            if (dir.ordinal() == ordinal) {
                result = dir;
                break;
            }
        }
        return result;
    }

    @TypeConverter
    public static int toInt(Category.Direction direction) {
        return direction.ordinal();
    }
}
