package com.noteflow.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.noteflow.app.adapter.NoteAdapter
import com.noteflow.app.databinding.ActivityMainBinding
import com.noteflow.app.model.Note
import com.noteflow.app.viewmodel.NoteViewModel

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupRecyclerView()
        setupFab()
        observeNotes()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "NoteFlow"
    }
    
    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter { note ->
            openNoteDetail(note)
        }
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
            setHasFixedSize(true)
        }
    }
    
    private fun setupFab() {
        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun observeNotes() {
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        
        noteViewModel.allNotes.observe(this) { notes ->
            if (notes.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                noteAdapter.submitList(notes)
            }
        }
    }
    
    private fun openNoteDetail(note: Note) {
        val intent = Intent(this, NoteDetailActivity::class.java).apply {
            putExtra("note_id", note.id)
            putExtra("note_title", note.title)
            putExtra("note_content", note.content)
            putExtra("note_timestamp", note.timestamp)
        }
        startActivity(intent)
    }
}
