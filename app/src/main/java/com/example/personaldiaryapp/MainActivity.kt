package com.example.personaldiaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

/*This project has been done by taking reference from GeeksforGeeks video on Youtube.
  The link for the same is given below:
  https://youtu.be/D2F5t-phP04
  */

class MainActivity : AppCompatActivity(), NoteClickDeleteInterface, NoteClickInterface {
    lateinit var notesRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var viewModel: NoteViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notesRV = findViewById(R.id.idRVNotes)
        addFAB = findViewById(R.id.idFABAddNote)
        notesRV.layoutManager = LinearLayoutManager(this)

        val noteRVAdapter = NoteRVAdapter(this, this, this)
        notesRV.adapter = noteRVAdapter
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)
        viewModel.allNotes.observe(this) { list ->
            list?.let {
                noteRVAdapter.updateList(it)
            }
        }
        addFAB.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onDeleteIconClick(note: Note) {
        viewModel.deleteNote(note)
        Toast.makeText(this, "${note.noteTitle} Deleted", Toast.LENGTH_LONG).show()
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("noteID", note.id)
        startActivity(intent)
        this.finish()
    }
}