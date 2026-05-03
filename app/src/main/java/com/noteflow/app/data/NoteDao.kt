package com.noteflow.app.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noteflow.app.model.Note

@Dao
interface NoteDao {
    
    // 获取所有笔记（按时间倒序）
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): LiveData<List<Note>>
    
    // 根据ID获取笔记
    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): Note?
    
    // 插入笔记
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long
    
    // 更新笔记
    @Update
    suspend fun update(note: Note)
    
    // 删除笔记
    @Delete
    suspend fun delete(note: Note)
    
    // 删除所有笔记
    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
    
    // 搜索笔记（标题或内容）
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchNotes(query: String): LiveData<List<Note>>
    
    // ========== 新增查询方法 ==========
    
    // 获取文件夹内的笔记
    @Query("SELECT * FROM notes WHERE folderId = :folderId ORDER BY timestamp DESC")
    fun getNotesByFolder(folderId: Long): LiveData<List<Note>>
    
    // 获取未分类的笔记（没有文件夹）
    @Query("SELECT * FROM notes WHERE folderId IS NULL ORDER BY timestamp DESC")
    fun getUncategorizedNotes(): LiveData<List<Note>>
    
    // 获取收藏的笔记
    @Query("SELECT * FROM notes WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteNotes(): LiveData<List<Note>>
    
    // 获取锁定的笔记
    @Query("SELECT * FROM notes WHERE isLocked = 1 ORDER BY timestamp DESC")
    fun getLockedNotes(): LiveData<List<Note>>
    
    // 更新收藏状态
    @Query("UPDATE notes SET isFavorite = :isFavorite WHERE id = :noteId")
    suspend fun updateFavoriteStatus(noteId: Long, isFavorite: Boolean)
    
    // 更新锁定状态
    @Query("UPDATE notes SET isLocked = :isLocked WHERE id = :noteId")
    suspend fun updateLockedStatus(noteId: Long, isLocked: Boolean)
    
    // 移动笔记到文件夹
    @Query("UPDATE notes SET folderId = :folderId WHERE id = :noteId")
    suspend fun moveToFolder(noteId: Long, folderId: Long?)
    
    // 获取笔记总数
    @Query("SELECT COUNT(*) FROM notes")
    fun getNoteCount(): LiveData<Int>
    
    // 获取文件夹内的笔记数量
    @Query("SELECT COUNT(*) FROM notes WHERE folderId = :folderId")
    fun getNoteCountByFolder(folderId: Long): LiveData<Int>
}
