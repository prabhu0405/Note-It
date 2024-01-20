package com.example.noteit


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var addNoteBtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuBtn: ImageButton
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var userArrayList: ArrayList<Note>

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addNoteBtn = findViewById(R.id.add_note)
        recyclerView = findViewById(R.id.recycler_view)
        menuBtn = findViewById(R.id.menu_btn)

        userArrayList = arrayListOf()
        noteAdapter = NoteAdapter(userArrayList)
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        EventChangeListener()

        addNoteBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, NoteActivity::class.java))
        }
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Note")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            userArrayList.add(dc.document.toObject(Note::class.java))
                        }
                    }
                    noteAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun showMenu() {
        // todo
    }
}









