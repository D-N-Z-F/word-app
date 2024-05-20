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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ViewWordViewModel(
    private val repo: WordRepo
): ViewModel() {
    private val _word: MutableLiveData<Word> = MutableLiveData()
    val word: LiveData<Word> = _word
    private val _finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    val finish: SharedFlow<Unit> = _finish
    val showSnackbar: MutableLiveData<String> = MutableLiveData()

    private fun triggerFinish() = viewModelScope.launch { _finish.emit(Unit) }

    fun getWordById(id: Int) {
        /*
           The viewModelScope.launch(Dispatchers.IO) {} is needed to perform the asynchronous
           action because the main thread might be blocked if used.
       */
        viewModelScope.launch(Dispatchers.IO) { _word.postValue(repo.getWordById(id)) }
    }

    fun deleteWord() {
        /*
           The viewModelScope.launch(Dispatchers.IO) {} is needed to perform the asynchronous
           action because the main thread might be blocked if used.
       */
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteWord(word.value!!)
                showSnackbar.postValue("Deleted Successfully.")
            } catch (e: Exception) { showSnackbar.postValue(e.message) }
            triggerFinish()
        }
    }

    fun completeWord() {
        /*
            The viewModelScope.launch(Dispatchers.IO) {} is needed to perform the asynchronous
            action because the main thread might be blocked if used.
        */
        viewModelScope.launch(Dispatchers.IO) {
            val completedWord = word.value!!.copy(isCompleted = true)
            try {
                repo.updateWord(completedWord)
                showSnackbar.postValue("Word Completed.")
            } catch (e: Exception) { showSnackbar.postValue(e.message) }
            triggerFinish()
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { ViewWordViewModel((this[APPLICATION_KEY] as MyApp).repo) }
        }
    }
}