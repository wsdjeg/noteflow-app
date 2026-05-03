package com.noteflow.app.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noteflow.app.model.Folder

@Dao
interface FolderDao {
    
    // 获取所有文件夹
    @Query("SELECT * FROM folders ORDER BY `order` ASC, createdAt ASC")
    fun getAllFolders(): LiveData<List<Folder>>
    
    // 根据ID获取文件夹
    @Query("SELECT * FROM folders WHERE id = :folderId")
    suspend fun getFolderById(folderId: Long): Folder?
    
    // 插入文件夹
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(folder: Folder): Long
    
    // 更新文件夹
    @Update
    suspend fun update(folder: Folder)
    
    // 删除文件夹
    @Delete
    suspend fun delete(folder: Folder)
    
    // 获取根文件夹（没有父文件夹的）
    @Query("SELECT * FROM folders WHERE parentId IS NULL ORDER BY `order` ASC, createdAt ASC")
    fun getRootFolders(): LiveData<List<Folder>>
    
    // 获取子文件夹
    @Query("SELECT * FROM folders WHERE parentId = :parentId ORDER BY `order` ASC, createdAt ASC")
    fun getChildFolders(parentId: Long): LiveData<List<Folder>>
    
    // 搜索文件夹
    @Query("SELECT * FROM folders WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchFolders(query: String): LiveData<List<Folder>>
    
    // 检查文件夹名是否存在
    @Query("SELECT * FROM folders WHERE name = :name AND parentId = :parentId LIMIT 1")
    suspend fun getFolderByName(name: String, parentId: Long?): Folder?
    
    // 更新文件夹排序
    @Query("UPDATE folders SET `order` = :order WHERE id = :folderId")
    suspend fun updateOrder(folderId: Long, order: Int)
    
    // 获取文件夹内的笔记数量
    @Query("SELECT COUNT(*) FROM notes WHERE folderId = :folderId")
    fun getNoteCountInFolder(folderId: Long): LiveData<Int>
}
