package com.example.individualproject.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.individualproject.MyApp
import com.example.individualproject.data.model.Word
import com.example.individualproject.data.repository.WordRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ContainerViewModel(
    private val repo: WordRepo
):ViewModel() {
    val sortType: MutableLiveData<String> = MutableLiveData("title")
    val sortOrder: MutableLiveData<String> = MutableLiveData("ASC")
    private val _query: MutableLiveData<String> = MutableLiveData("")
    val query: LiveData<String> = _query
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun sortWords(words: List<Word>): List<Word> {
        return if(sortOrder.value == "ASC") {
            if(sortType.value == "title") words.sortedBy { it.title }
            else words.sortedBy { it.dateCreated }
        } else {
            if(sortType.value == "title") words.sortedByDescending { it.title }
            else words.sortedByDescending { it.dateCreated }
        }
    }

    fun setQueryValue(query: String) = _query.postValue(query)

    fun getAllNewWords(): Flow<List<Word>> = repo.getAllNewWords(query.value!!)

    fun getAllCompletedWords(): Flow<List<Word>> = repo.getAllCompletedWords(query.value!!)

    companion object {
        val Factory = viewModelFactory {
            initializer { ContainerViewModel((this[APPLICATION_KEY] as MyApp).repo) }
        }
    }
}