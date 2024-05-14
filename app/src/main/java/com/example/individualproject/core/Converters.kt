package com.example.individualproject.core

import androidx.room.TypeConverter
import java.time.LocalDateTime

/*
    This class informs the Database on how to deal with unknown Types when storing and
    to convert back when retrieving.
*/
class Converters {

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? = date?.toString()

}