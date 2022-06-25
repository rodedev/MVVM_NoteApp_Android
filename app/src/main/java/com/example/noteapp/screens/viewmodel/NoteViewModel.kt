package com.example.noteapp.screens.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.noteapp.data.repository.NoteRepository
import com.example.noteapp.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    val notes = noteRepository.getAllNotes().asLiveData()

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteRepository.addNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.updateNote(note)
        }
    }
}

/*
private val _notes = MutableLiveData<List<Note>>()
val notes: LiveData<List<Note>> get() = _notes

private val _notes = MutableStateFlow<List<Note>>(emptyList())
val notes: StateFlow<List<Note>> = _notes.asStateFlow()

init {
    viewModelScope.launch {
        noteRepository.getAllNotes().collect {
            _notes.value = it
        }
    }
}*/
