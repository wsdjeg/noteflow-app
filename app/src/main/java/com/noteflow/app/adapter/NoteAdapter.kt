package com.noteflow.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noteflow.app.R
import com.noteflow.app.databinding.ItemNoteBinding
import com.noteflow.app.model.Note
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(
    private val onNoteClick: (Note) -> Unit
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class NoteViewHolder(
        private val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onNoteClick(getItem(position))
                }
            }
        }
        
        fun bind(note: Note) {
            binding.apply {
                textTitle.text = note.title
                textContent.text = note.content
                
                val dateFormat = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())
                textTimestamp.text = dateFormat.format(Date(note.updatedAt))
                
                // 显示收藏图标
                if (note.isFavorite) {
                    imageFavorite.setImageResource(R.drawable.ic_favorite_filled)
                    imageFavorite.visibility = android.view.View.VISIBLE
                } else {
                    imageFavorite.visibility = android.view.View.GONE
                }
                
                // 设置内容预览，最多显示2行
                textContent.maxLines = 2
            }
        }
    }
    
    class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}
