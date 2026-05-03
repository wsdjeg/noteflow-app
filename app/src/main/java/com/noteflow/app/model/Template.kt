package com.noteflow.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// 笔记模板
@Entity(tableName = "templates")
data class Template(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val content: String,
    val icon: String = "description",
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
