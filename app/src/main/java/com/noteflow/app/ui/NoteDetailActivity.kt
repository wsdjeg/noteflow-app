package com.noteflow.app.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.noteflow.app.R
import com.noteflow.app.databinding.ActivityNoteDetailBinding
import com.noteflow.app.model.Note
import com.noteflow.app.viewmodel.NoteViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NoteDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityNoteDetailBinding
    private lateinit var noteViewModel: NoteViewModel
    private var noteId: Long = -1
    private var isFavorite: Boolean = false
    private var isLocked: Boolean = false
    private var folderId: Long? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        
        setupToolbar()
        loadNoteData()
        setupSaveButton()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (noteId == -1L) "新建笔记" else "编辑笔记"
    }
    
    private fun loadNoteData() {
        noteId = intent.getLongExtra("note_id", -1)
        
        if (noteId != -1L) {
            val title = intent.getStringExtra("note_title") ?: ""
            val content = intent.getStringExtra("note_content") ?: ""
            val createdAt = intent.getLongExtra("note_created_at", System.currentTimeMillis())
            val updatedAt = intent.getLongExtra("note_updated_at", createdAt)
            isFavorite = intent.getBooleanExtra("note_is_favorite", false)
            isLocked = intent.getBooleanExtra("note_is_locked", false)
            folderId = if (intent.hasExtra("note_folder_id")) {
                intent.getLongExtra("note_folder_id", -1).takeIf { it != -1L }
            } else null
            
            binding.editTitle.setText(title)
            binding.editContent.setText(content)
            
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            binding.textTimestamp.text = "最后编辑: ${dateFormat.format(Date(updatedAt))}"
            binding.textTimestamp.visibility = android.view.View.VISIBLE
        } else {
            binding.textTimestamp.visibility = android.view.View.GONE
        }
    }
    
    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            saveNote()
        }
    }
    
    private fun saveNote() {
        val title = binding.editTitle.text.toString().trim()
        val content = binding.editContent.text.toString().trim()
        
        if (title.isEmpty()) {
            binding.editTitle.error = "请输入标题"
            return
        }
        
        val currentTime = System.currentTimeMillis()
        
        val note = if (noteId == -1L) {
            // 新建笔记
            Note(
                title = title,
                content = content,
                createdAt = currentTime,
                updatedAt = currentTime,
                isFavorite = false,
                isLocked = false,
                folderId = null
            )
        } else {
            // 更新笔记
            Note(
                id = noteId,
                title = title,
                content = content,
                createdAt = intent.getLongExtra("note_created_at", currentTime),
                updatedAt = currentTime,
                isFavorite = isFavorite,
                isLocked = isLocked,
                folderId = folderId
            )
        }
        
        if (noteId == -1L) {
            noteViewModel.insert(note)
            Toast.makeText(this, "笔记已保存", Toast.LENGTH_SHORT).show()
        } else {
            noteViewModel.update(note)
            Toast.makeText(this, "笔记已更新", Toast.LENGTH_SHORT).show()
        }
        
        finish()
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (noteId != -1L) {
            menuInflater.inflate(R.menu.menu_note_detail, menu)
            
            // 更新收藏图标状态
            val favoriteItem = menu.findItem(R.id.action_favorite)
            favoriteItem?.setIcon(
                if (isFavorite) R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite_outline
            )
        }
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_delete -> {
                showDeleteConfirmation()
                true
            }
            R.id.action_favorite -> {
                toggleFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun toggleFavorite() {
        isFavorite = !isFavorite
        noteViewModel.toggleFavorite(noteId, isFavorite)
        
        // 更新菜单图标
        invalidateOptionsMenu()
        
        Toast.makeText(
            this,
            if (isFavorite) "已添加到收藏" else "已取消收藏",
            Toast.LENGTH_SHORT
        ).show()
    }
    
    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("删除笔记")
            .setMessage("确定要删除这条笔记吗？此操作不可撤销。")
            .setPositiveButton("删除") { _, _ ->
                deleteNote()
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun deleteNote() {
        val currentTime = System.currentTimeMillis()
        val createdAt = intent.getLongExtra("note_created_at", currentTime)
        
        val note = Note(
            id = noteId,
            title = binding.editTitle.text.toString(),
            content = binding.editContent.text.toString(),
            createdAt = createdAt,
            updatedAt = currentTime,
            isFavorite = isFavorite,
            isLocked = isLocked,
            folderId = folderId
        )
        
        noteViewModel.delete(note)
        Toast.makeText(this, "笔记已删除", Toast.LENGTH_SHORT).show()
        finish()
    }
}
