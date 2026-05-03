package com.noteflow.app.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noteflow.app.model.Template

@Dao
interface TemplateDao {
    
    // 获取所有模板
    @Query("SELECT * FROM templates ORDER BY name ASC")
    fun getAllTemplates(): LiveData<List<Template>>
    
    // 根据ID获取模板
    @Query("SELECT * FROM templates WHERE id = :templateId")
    suspend fun getTemplateById(templateId: Long): Template?
    
    // 插入模板
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(template: Template): Long
    
    // 更新模板
    @Update
    suspend fun update(template: Template)
    
    // 删除模板
    @Delete
    suspend fun delete(template: Template)
    
    // 搜索模板
    @Query("SELECT * FROM templates WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchTemplates(query: String): LiveData<List<Template>>
    
    // 获取默认模板
    @Query("SELECT * FROM templates WHERE isDefault = 1 LIMIT 1")
    fun getDefaultTemplate(): LiveData<Template?>
    
    // 清除默认模板
    @Query("UPDATE templates SET isDefault = 0")
    suspend fun clearDefaultTemplate()
    
    // 设置默认模板
    @Query("UPDATE templates SET isDefault = 1 WHERE id = :templateId")
    suspend fun setDefaultTemplate(templateId: Long)
    
    // 检查模板名是否存在
    @Query("SELECT * FROM templates WHERE name = :name LIMIT 1")
    suspend fun getTemplateByName(name: String): Template?
}
