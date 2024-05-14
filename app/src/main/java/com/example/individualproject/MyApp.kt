package com.example.individualproject

import android.app.Application
import androidx.room.Room
import com.example.individualproject.data.db.WordDatabase
import com.example.individualproject.data.repository.WordRepo

class MyApp: Application() {
    lateinit var repo: WordRepo

    override fun onCreate() {
        /*
            On Application Creation(), run the original function, then build the database,
            and the parameter to the repository.
        */
        super.onCreate()
        val database = Room.databaseBuilder(
            this, WordDatabase::class.java, WordDatabase.NAME
        ).fallbackToDestructiveMigration().build()
        repo = WordRepo(database.wordDao())
    }
}