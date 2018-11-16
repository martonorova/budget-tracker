package com.morova.budgettracker.data;

import android.arch.persistence.room.TypeConverter;

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
}
