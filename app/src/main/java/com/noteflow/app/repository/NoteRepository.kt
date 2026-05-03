package com.noteflow.app.repository

import androidx.lifecycle.LiveData
import com.noteflow.app.data.NoteDao
import com.noteflow.app.model.Note

class NoteRepository(private val noteDao: NoteDao) {
    
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()
    
    suspend fun insert(note: Note): Long {
        return noteDao.insert(note)
    }
    
    suspend fun update(note: Note) {
        noteDao.update(note)
    }
    
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
    
    suspend fun deleteAllNotes() {
        noteDao.deleteAllNotes()
    }
    
    suspend fun getNoteById(noteId: Long): Note? {
        return noteDao.getNoteById(noteId)
    }
    
    fun searchNotes(query: String): LiveData<List<Note>> {
        return noteDao.searchNotes(query)
    }
}
