package com.example.noteapp.data.repository

import android.util.Log
import com.example.noteapp.data.data_source.NoteDao
import com.example.noteapp.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {

    fun getAllNotes(): Flow<List<Note>> = flow {
        try {
            emitAll(noteDao.getAllNotes())
        }catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }

    fun searchNote(query: String?): Flow<List<Note>> = noteDao.searchNote(query)

    suspend fun addNote(note: Note) = noteDao.addNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

}