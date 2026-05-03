package com.noteflow.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.noteflow.app.R
import com.noteflow.app.adapter.NoteAdapter
import com.noteflow.app.databinding.ActivityMainBinding
import com.noteflow.app.model.Note
import com.noteflow.app.viewmodel.NoteViewModel

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private var currentSearchQuery: String = ""
    
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
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                currentSearchQuery = newText ?: ""
                performSearch(currentSearchQuery)
                return true
            }
        })
        
        return true
    }
    
    private fun performSearch(query: String) {
        if (query.isBlank()) {
            noteViewModel.allNotes.observe(this) { notes ->
                updateNotesList(notes)
            }
        } else {
            noteViewModel.searchNotes(query).observe(this) { notes ->
                updateNotesList(notes)
            }
        }
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
            if (currentSearchQuery.isBlank()) {
                updateNotesList(notes)
            }
        }
    }
    
    private fun updateNotesList(notes: List<Note>) {
        if (notes.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            noteAdapter.submitList(notes)
        }
    }
    
    private fun openNoteDetail(note: Note) {
        val intent = Intent(this, NoteDetailActivity::class.java).apply {
            putExtra("note_id", note.id)
            putExtra("note_title", note.title)
            putExtra("note_content", note.content)
            putExtra("note_created_at", note.createdAt)
            putExtra("note_updated_at", note.updatedAt)
            putExtra("note_is_favorite", note.isFavorite)
            putExtra("note_is_locked", note.isLocked)
            putExtra("note_folder_id", note.folderId)
        }
        startActivity(intent)
    }
}
