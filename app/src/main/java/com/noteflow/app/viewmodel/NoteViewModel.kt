package com.noteflow.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.noteflow.app.data.NoteDatabase
import com.noteflow.app.model.Folder
import com.noteflow.app.model.Note
import com.noteflow.app.model.Tag
import com.noteflow.app.model.Template
import com.noteflow.app.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: NoteRepository
    
    // 笔记相关
    val allNotes: LiveData<List<Note>>
    val favoriteNotes: LiveData<List<Note>>
    val noteCount: LiveData<Int>
    
    // 文件夹相关
    val allFolders: LiveData<List<Folder>>
    
    // 标签相关
    val allTags: LiveData<List<Tag>>
    
    // 模板相关
    val allTemplates: LiveData<List<Template>>
    
    init {
        val database = NoteDatabase.getDatabase(application)
        repository = NoteRepository(
            noteDao = database.noteDao(),
            tagDao = database.tagDao(),
            folderDao = database.folderDao(),
            templateDao = database.templateDao()
        )
        
        // 初始化 LiveData
        allNotes = repository.allNotes
        favoriteNotes = repository.favoriteNotes
        noteCount = repository.noteCount
        allFolders = repository.allFolders
        allTags = repository.allTags
        allTemplates = repository.allTemplates
    }
    
    // ========== 笔记操作 ==========
    
    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertNote(note)
    }
    
    fun update(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(note)
    }
    
    fun delete(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(note)
    }
    
    fun searchNotes(query: String): LiveData<List<Note>> {
        return repository.searchNotes(query)
    }
    
    fun getNotesByFolder(folderId: Long): LiveData<List<Note>> {
        return repository.getNotesByFolder(folderId)
    }
    
    fun getUncategorizedNotes(): LiveData<List<Note>> {
        return repository.uncategorizedNotes
    }
    
    suspend fun getNoteById(noteId: Long): Note? {
        return repository.getNoteById(noteId)
    }
    
    fun toggleFavorite(noteId: Long, isFavorite: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateFavoriteStatus(noteId, isFavorite)
    }
    
    fun toggleLocked(noteId: Long, isLocked: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateLockedStatus(noteId, isLocked)
    }
    
    fun moveToFolder(noteId: Long, folderId: Long?) = viewModelScope.launch(Dispatchers.IO) {
        repository.moveNoteToFolder(noteId, folderId)
    }
    
    // ========== 文件夹操作 ==========
    
    fun insertFolder(folder: Folder) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertFolder(folder)
    }
    
    fun updateFolder(folder: Folder) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateFolder(folder)
    }
    
    fun deleteFolder(folder: Folder) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFolder(folder)
    }
    
    fun getChildFolders(parentId: Long): LiveData<List<Folder>> {
        return repository.getChildFolders(parentId)
    }
    
    fun getNoteCountInFolder(folderId: Long): LiveData<Int> {
        return repository.getNoteCountInFolder(folderId)
    }
    
    // ========== 标签操作 ==========
    
    fun insertTag(tag: Tag) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTag(tag)
    }
    
    fun updateTag(tag: Tag) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTag(tag)
    }
    
    fun deleteTag(tag: Tag) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTag(tag)
    }
    
    fun getTagsForNote(noteId: Long): LiveData<List<Tag>> {
        return repository.getTagsForNote(noteId)
    }
    
    fun addTagToNote(noteId: Long, tagId: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.addTagToNote(noteId, tagId)
    }
    
    fun removeTagFromNote(noteId: Long, tagId: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.removeTagFromNote(noteId, tagId)
    }
    
    // ========== 模板操作 ==========
    
    fun insertTemplate(template: Template) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTemplate(template)
    }
    
    fun updateTemplate(template: Template) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTemplate(template)
    }
    
    fun deleteTemplate(template: Template) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTemplate(template)
    }
    
    suspend fun getTemplateById(templateId: Long): Template? {
        return repository.getTemplateById(templateId)
    }
    
    fun setDefaultTemplate(templateId: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.setDefaultTemplate(templateId)
    }
}
