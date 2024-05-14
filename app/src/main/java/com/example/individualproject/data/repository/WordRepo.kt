package com.example.individualproject.data.repository

import com.example.individualproject.data.db.WordDao
import com.example.individualproject.data.model.Word
import kotlinx.coroutines.flow.Flow

// This is the repository which communicates with the DAO
class WordRepo(private val dao: WordDao) {

    fun getAllNewWords(query: String): Flow<List<Word>> = dao.getAllNewWords(query)

    fun getAllCompletedWords(query: String): Flow<List<Word>> =dao.getAllCompletedWords(query)

    fun getWordById(id: Int): Word? = dao.getWordById(id)

    fun addWord(word: Word) = dao.addWord(word)

    fun updateWord(word: Word) = dao.updateWord(word)

    fun deleteWord(word: Word) = dao.deleteWord(word)

}