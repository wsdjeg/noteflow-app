package com.noteflow.app.repository

import androidx.lifecycle.LiveData
import com.noteflow.app.data.*
import com.noteflow.app.model.*

class NoteRepository(
    private val noteDao: NoteDao,
    private val tagDao: TagDao,
    private val folderDao: FolderDao,
    private val templateDao: TemplateDao
) {
    
    // ========== 笔记相关 ==========
    
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()
    
    val noteCount: LiveData<Int> = noteDao.getNoteCount()
    
    val favoriteNotes: LiveData<List<Note>> = noteDao.getFavoriteNotes()
    
    val uncategorizedNotes: LiveData<List<Note>> = noteDao.getUncategorizedNotes()
    
    suspend fun insertNote(note: Note): Long {
        return noteDao.insert(note)
    }
    
    suspend fun updateNote(note: Note) {
        noteDao.update(note)
    }
    
    suspend fun deleteNote(note: Note) {
        noteDao.delete(note)
    }
    
    suspend fun getNoteById(noteId: Long): Note? {
        return noteDao.getNoteById(noteId)
    }
    
    fun searchNotes(query: String): LiveData<List<Note>> {
        return noteDao.searchNotes(query)
    }
    
    fun getNotesByFolder(folderId: Long): LiveData<List<Note>> {
        return noteDao.getNotesByFolder(folderId)
    }
    
    suspend fun updateFavoriteStatus(noteId: Long, isFavorite: Boolean) {
        noteDao.updateFavoriteStatus(noteId, isFavorite)
    }
    
    suspend fun updateLockedStatus(noteId: Long, isLocked: Boolean) {
        noteDao.updateLockedStatus(noteId, isLocked)
    }
    
    suspend fun moveNoteToFolder(noteId: Long, folderId: Long?) {
        noteDao.moveToFolder(noteId, folderId)
    }
    
    // ========== 标签相关 ==========
    
    val allTags: LiveData<List<Tag>> = tagDao.getAllTags()
    
    suspend fun insertTag(tag: Tag): Long {
        return tagDao.insert(tag)
    }
    
    suspend fun updateTag(tag: Tag) {
        tagDao.update(tag)
    }
    
    suspend fun deleteTag(tag: Tag) {
        tagDao.deleteAllNotesForTag(tag.id)
        tagDao.delete(tag)
    }
    
    suspend fun getTagById(tagId: Long): Tag? {
        return tagDao.getTagById(tagId)
    }
    
    suspend fun getTagByName(name: String): Tag? {
        return tagDao.getTagByName(name)
    }
    
    fun searchTags(query: String): LiveData<List<Tag>> {
        return tagDao.searchTags(query)
    }
    
    fun getTagsForNote(noteId: Long): LiveData<List<Tag>> {
        return tagDao.getTagsForNote(noteId)
    }
    
    suspend fun addTagToNote(noteId: Long, tagId: Long) {
        tagDao.addTagToNote(NoteTag(noteId, tagId))
    }
    
    suspend fun removeTagFromNote(noteId: Long, tagId: Long) {
        tagDao.removeTagFromNote(NoteTag(noteId, tagId))
    }
    
    suspend fun deleteAllTagsForNote(noteId: Long) {
        tagDao.deleteAllTagsForNote(noteId)
    }
    
    // ========== 文件夹相关 ==========
    
    val allFolders: LiveData<List<Folder>> = folderDao.getAllFolders()
    
    val rootFolders: LiveData<List<Folder>> = folderDao.getRootFolders()
    
    suspend fun insertFolder(folder: Folder): Long {
        return folderDao.insert(folder)
    }
    
    suspend fun updateFolder(folder: Folder) {
        folderDao.update(folder)
    }
    
    suspend fun deleteFolder(folder: Folder) {
        folderDao.delete(folder)
    }
    
    suspend fun getFolderById(folderId: Long): Folder? {
        return folderDao.getFolderById(folderId)
    }
    
    fun getChildFolders(parentId: Long): LiveData<List<Folder>> {
        return folderDao.getChildFolders(parentId)
    }
    
    fun searchFolders(query: String): LiveData<List<Folder>> {
        return folderDao.searchFolders(query)
    }
    
    fun getNoteCountInFolder(folderId: Long): LiveData<Int> {
        return folderDao.getNoteCountInFolder(folderId)
    }
    
    // ========== 模板相关 ==========
    
    val allTemplates: LiveData<List<Template>> = templateDao.getAllTemplates()
    
    val defaultTemplate: LiveData<Template?> = templateDao.getDefaultTemplate()
    
    suspend fun insertTemplate(template: Template): Long {
        return templateDao.insert(template)
    }
    
    suspend fun updateTemplate(template: Template) {
        templateDao.update(template)
    }
    
    suspend fun deleteTemplate(template: Template) {
        templateDao.delete(template)
    }
    
    suspend fun getTemplateById(templateId: Long): Template? {
        return templateDao.getTemplateById(templateId)
    }
    
    suspend fun setDefaultTemplate(templateId: Long) {
        templateDao.clearDefaultTemplate()
        templateDao.setDefaultTemplate(templateId)
    }
}
