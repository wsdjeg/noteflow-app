package com.noteflow.app.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.noteflow.app.R
import com.noteflow.app.databinding.ActivityNoteDetailBinding
import com.noteflow.app.model.Note
import com.noteflow.app.viewmodel.NoteViewModel
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import java.text.SimpleDateFormat
import java.util.*

class NoteDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityNoteDetailBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var markwon: Markwon
    private var noteId: Long = -1
    private var isFavorite: Boolean = false
    private var isLocked: Boolean = false
    private var folderId: Long? = null
    private var isEditMode: Boolean = true
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // 初始化 Markwon
        markwon = Markwon.create(this)
        
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        
        setupToolbar()
        setupEditMode()
        loadNoteData()
        setupSaveButton()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (noteId == -1L) "新建笔记" else "编辑笔记"
    }
    
    private fun setupEditMode() {
        // 设置编辑框样式
        binding.editContent.background = null
        binding.editContent.setPadding(24, 24, 24, 24)
        binding.editContent.movementMethod = ScrollingMovementMethod.getInstance()
        
        // 设置预览框
        binding.textPreview.movementMethod = ScrollingMovementMethod.getInstance()
        binding.textPreview.setPadding(24, 24, 24, 24)
        
        // 设置标题编辑框
        binding.editTitle.background = null
        binding.editTitle.setPadding(24, 24, 24, 0)
        
        // 添加 Markdown 编辑支持
        val editor = MarkwonEditor.create(markwon)
        binding.editContent.addTextChangedListener(
            MarkwonEditorTextWatcher.withProcess(editor)
        )
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
            binding.textTimestamp.visibility = View.VISIBLE
            
            // 显示预览
            updatePreview(content)
        } else {
            binding.textTimestamp.visibility = View.GONE
        }
    }
    
    private fun updatePreview(content: String) {
        markwon.setMarkdown(binding.textPreview, content)
    }
    
    private fun toggleMode() {
        isEditMode = !isEditMode
        
        if (isEditMode) {
            // 切换到编辑模式
            binding.editTitle.visibility = View.VISIBLE
            binding.editContent.visibility = View.VISIBLE
            binding.textPreview.visibility = View.GONE
            binding.scrollViewEdit.visibility = View.VISIBLE
            binding.scrollViewPreview.visibility = View.GONE
        } else {
            // 切换到预览模式
            binding.editTitle.visibility = View.GONE
            binding.editContent.visibility = View.GONE
            binding.textPreview.visibility = View.VISIBLE
            binding.scrollViewEdit.visibility = View.GONE
            binding.scrollViewPreview.visibility = View.VISIBLE
            
            // 更新预览内容
            val content = binding.editContent.text.toString()
            updatePreview(content)
            
            // 显示标题（预览模式）
            binding.textPreviewTitle.visibility = View.VISIBLE
            binding.textPreviewTitle.text = binding.editTitle.text.toString()
        }
        
        // 更新菜单按钮
        invalidateOptionsMenu()
        
        Toast.makeText(
            this,
            if (isEditMode) "编辑模式" else "预览模式",
            Toast.LENGTH_SHORT
        ).show()
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
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show()
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
        menuInflater.inflate(R.menu.menu_note_detail, menu)
        
        // 更新编辑/预览切换按钮
        val modeItem = menu.findItem(R.id.action_toggle_mode)
        modeItem?.setIcon(
            if (isEditMode) R.drawable.ic_preview
            else R.drawable.ic_edit
        )
        modeItem?.title = if (isEditMode) "预览" else "编辑"
        
        // 更新收藏图标状态（仅编辑已有笔记时）
        if (noteId != -1L) {
            val favoriteItem = menu.findItem(R.id.action_favorite)
            favoriteItem?.setIcon(
                if (isFavorite) R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite_outline
            )
        } else {
            // 新建笔记时隐藏收藏和删除按钮
            menu.findItem(R.id.action_favorite)?.isVisible = false
            menu.findItem(R.id.action_delete)?.isVisible = false
        }
        
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_toggle_mode -> {
                toggleMode()
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
