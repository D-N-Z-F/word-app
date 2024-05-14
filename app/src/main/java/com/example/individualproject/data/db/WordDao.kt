package com.example.individualproject.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.individualproject.data.model.Word
import kotlinx.coroutines.flow.Flow

/*
    This is where we inject the queries and/or requests to the database.
*/
@Dao
interface WordDao {

    // LIKE is similar to .contains() and ORDER BY is to sort.
    @Query("SELECT * FROM Word WHERE isCompleted = 0 AND title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun getAllNewWords(query: String): Flow<List<Word>>

    @Query("SELECT * FROM Word WHERE isCompleted = 1 AND title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun getAllCompletedWords(query: String): Flow<List<Word>>

    @Query("SELECT * FROM Word WHERE id = :id")
    fun getWordById(id: Int): Word?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWord(word: Word)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWord(word: Word)

    @Delete
    fun deleteWord(word: Word)

}