package com.example.individualproject.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.individualproject.core.Converters
import com.example.individualproject.data.model.Word

/*
    So we have the entity (similar to a table/column) which is the Word data class,
    and we instantiate the Converters class here.
*/
@Database(entities = [Word::class], version = 2)
@TypeConverters(Converters::class)
abstract class WordDatabase: RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object { const val NAME = "my_word_database" }

}