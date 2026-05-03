package com.noteflow.app.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noteflow.app.model.Note

@Dao
interface NoteDao {
    
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): LiveData<List<Note>>
    
    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): Note?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long
    
    @Update
    suspend fun update(note: Note)
    
    @Delete
    suspend fun delete(note: Note)
    
    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
    
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchNotes(query: String): LiveData<List<Note>>
}
