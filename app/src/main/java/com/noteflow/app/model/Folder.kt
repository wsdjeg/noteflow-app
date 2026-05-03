package com.noteflow.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// 文件夹实体
@Entity(tableName = "folders")
data class Folder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String = "folder",          // 图标名称
    val color: String = "#2196F3",        // 文件夹颜色
    val parentId: Long? = null,           // 父文件夹ID
    val order: Int = 0,                   // 排序顺序
    val createdAt: Long = System.currentTimeMillis()
)
