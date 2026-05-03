package com.noteflow.app.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.noteflow.app.R
import com.noteflow.app.databinding.ActivityNoteDetailBinding
import com.noteflow.app.model.Note
import com.noteflow.app.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*

class NoteDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityNoteDetailBinding
    private lateinit var noteViewModel: NoteViewModel
    private var noteId: Long = -1
    private var isEditing = false
    
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
            val timestamp = intent.getLongExtra("note_timestamp", System.currentTimeMillis())
            
            binding.editTitle.setText(title)
            binding.editContent.setText(content)
            
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            binding.textTimestamp.text = "最后编辑: ${dateFormat.format(Date(timestamp))}"
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
        
        val note = Note(
            id = if (noteId == -1L) 0 else noteId,
            title = title,
            content = content,
            timestamp = System.currentTimeMillis()
        )
        
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
            else -> super.onOptionsItemSelected(item)
        }
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
        val note = Note(
            id = noteId,
            title = binding.editTitle.text.toString(),
            content = binding.editContent.text.toString(),
            timestamp = intent.getLongExtra("note_timestamp", System.currentTimeMillis())
        )
        
        noteViewModel.delete(note)
        Toast.makeText(this, "笔记已删除", Toast.LENGTH_SHORT).show()
        finish()
    }
}
