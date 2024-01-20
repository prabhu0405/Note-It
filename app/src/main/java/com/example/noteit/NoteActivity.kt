package com.example.noteit

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class NoteActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveNoteBtn: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        // Initialize your views here
        titleEditText = findViewById(R.id.note_text)
        contentEditText = findViewById(R.id.content_text)
        saveNoteBtn = findViewById(R.id.save_btn)


        saveNoteBtn.setOnClickListener { v -> saveNote() }
        // Add your logic for the NoteActivity here
    }

    private fun saveNote() {
        val noteTitle: String = titleEditText.text.toString()
        val noteContent: String = contentEditText.text.toString()
        if (noteTitle.isBlank()) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        val note = Note()
        note.title = noteTitle
        note.content = noteContent


        saveNoteToFirebase(note);
    }
    private fun saveNoteToFirebase(note: Note) {
        val collectionReference = Utility.getCollectionReferenceForNotes()
        collectionReference.add(note)
            .addOnSuccessListener {
                Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save note: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}