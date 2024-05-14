package com.example.individualproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Word(
    /*
        In this case the Primary Key is our id, which is why we set it to be nullable,
        because it will only be created when the word is created.
    */
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val definition: String,
    val synonyms: String,
    val details: String,
    val dateCreated: LocalDateTime = LocalDateTime.now(),
    val isCompleted: Boolean = false
)
