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

class AddEditWordViewModel(
    private val repo: WordRepo
):ViewModel() {
    private val _word: MutableLiveData<Word> = MutableLiveData()
    val word: LiveData<Word> = _word
    val title: MutableLiveData<String> = MutableLiveData("")
    val definition: MutableLiveData<String> = MutableLiveData("")
    val synonyms: MutableLiveData<String> = MutableLiveData("")
    val details: MutableLiveData<String> = MutableLiveData("")
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

    fun setWord() {
        val word = word.value
        if(word != null) {
            title.value = word.title
            definition.value = word.definition
            synonyms.value = word.synonyms
            details.value = word.details
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                showSnackbar.postValue("An Unexpected Error Occurred.")
                triggerFinish()
            }
        }
    }

    fun addWord() {
        /*
            The viewModelScope.launch(Dispatchers.IO) {} is needed to perform the asynchronous
            action because the main thread might be blocked if used.
        */
        viewModelScope.launch(Dispatchers.IO) {
            if(title.value != "" && definition.value != "") {
                try {
                    repo.addWord(Word(
                        title = title.value!!,
                        definition = definition.value!!,
                        synonyms = synonyms.value!!,
                        details = details.value!!
                    ))
                    showSnackbar.postValue("Added Successfully.")
                } catch (e: Exception) { showSnackbar.postValue(e.message) }
                triggerFinish()
            } else {
                showSnackbar.postValue("Title and Definition cannot be empty!")
            }
        }
    }

    fun updateWord() {
        /*
            The viewModelScope.launch(Dispatchers.IO) {} is needed to perform the asynchronous
            action because the main thread might be blocked if used.
        */
        viewModelScope.launch(Dispatchers.IO) {
            val word = word.value
            if(word != null) {
                if(
                    title.value == word.title &&
                    definition.value == word.definition &&
                    synonyms.value == word.synonyms &&
                    details.value == word.details
                ) {
                    showSnackbar.postValue("Nothing to update...")
                } else if(title.value == "" || definition.value == "") {
                    showSnackbar.postValue("Title and Definition cannot be empty!")
                } else {
                    try {
                        repo.updateWord(word.copy(
                            title = title.value!!,
                            definition = definition.value!!,
                            synonyms = synonyms.value!!,
                            details = details.value!!
                        ))
                        showSnackbar.postValue("Updated Successfully.")
                    } catch (e: Exception) { showSnackbar.postValue(e.message) }
                    triggerFinish()
                }
            } else {
                showSnackbar.postValue("An Unexpected Error Occurred.")
                triggerFinish()
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { AddEditWordViewModel((this[APPLICATION_KEY] as MyApp).repo) }
        }
    }
}