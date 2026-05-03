package com.noteflow.app.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noteflow.app.model.Tag
import com.noteflow.app.model.NoteTag

@Dao
interface TagDao {
    
    // 获取所有标签
    @Query("SELECT * FROM tags ORDER BY name ASC")
    fun getAllTags(): LiveData<List<Tag>>
    
    // 根据ID获取标签
    @Query("SELECT * FROM tags WHERE id = :tagId")
    suspend fun getTagById(tagId: Long): Tag?
    
    // 插入标签
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: Tag): Long
    
    // 更新标签
    @Update
    suspend fun update(tag: Tag)
    
    // 删除标签
    @Delete
    suspend fun delete(tag: Tag)
    
    // 根据名称搜索标签
    @Query("SELECT * FROM tags WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchTags(query: String): LiveData<List<Tag>>
    
    // 检查标签名是否存在
    @Query("SELECT * FROM tags WHERE name = :name LIMIT 1")
    suspend fun getTagByName(name: String): Tag?
    
    // ========== 笔记-标签关联操作 ==========
    
    // 为笔记添加标签
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTagToNote(noteTag: NoteTag)
    
    // 移除笔记的标签
    @Delete
    suspend fun removeTagFromNote(noteTag: NoteTag)
    
    // 获取笔记的所有标签
    @Query("""
        SELECT t.* FROM tags t
        INNER JOIN note_tags nt ON t.id = nt.tagId
        WHERE nt.noteId = :noteId
        ORDER BY t.name ASC
    """)
    fun getTagsForNote(noteId: Long): LiveData<List<Tag>>
    
    // 获取标签下的所有笔记ID
    @Query("SELECT noteId FROM note_tags WHERE tagId = :tagId")
    fun getNoteIdsForTag(tagId: Long): LiveData<List<Long>>
    
    // 删除笔记的所有标签关联
    @Query("DELETE FROM note_tags WHERE noteId = :noteId")
    suspend fun deleteAllTagsForNote(noteId: Long)
    
    // 删除标签的所有笔记关联
    @Query("DELETE FROM note_tags WHERE tagId = :tagId")
    suspend fun deleteAllNotesForTag(tagId: Long)
}
