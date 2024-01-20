package com.example.noteit

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


object Utility {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getCollectionReferenceForNotes(): CollectionReference {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return FirebaseFirestore.getInstance().collection("notes")
            .document(currentUser?.uid ?: throw IllegalStateException("User not authenticated"))
            .collection("my_notes")
    }

}

