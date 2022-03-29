package com.sunriseorange.wordart.collaborativeArt

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sunriseorange.wordart.R

class AddMemoirActivity : Activity() {
    companion object {
        const val TAG = "Collaborative-Art-Project"
    }

    private lateinit var editTextMemoirs: EditText
    private lateinit var editTextAuthor: EditText
    private lateinit var saveButton: Button

    private lateinit var databaseMemoirs: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_memoir)

        editTextMemoirs = findViewById(R.id.editTextMemoir)
        editTextAuthor = findViewById(R.id.editTextAuthor)
        saveButton = findViewById(R.id.button)

        databaseMemoirs = FirebaseDatabase.getInstance().getReference("memoirs")

        saveButton.setOnClickListener {
            val text = editTextMemoirs.text
            val author = editTextAuthor.text
            when {
                text.isBlank() -> Toast.makeText(this, "Please enter a memoir", Toast.LENGTH_SHORT).show()
                author.isBlank() -> Toast.makeText(this, "Please enter an author name", Toast.LENGTH_SHORT).show()
                text.split(" ").size != 6 -> Toast.makeText(this, "Each memoir must be six words, no more, no less!", Toast.LENGTH_LONG).show()
                else -> addMemoir()
            }
        }
    }

    private fun addMemoir() {
        val text = editTextMemoirs.text
        val author = editTextAuthor.text.toString().trim { it <= ' ' }

        val id = databaseMemoirs.push().key

        val memoir = Memoir(id!!, author, text.toString())

        databaseMemoirs.child(id).setValue(memoir)

        Toast.makeText(this, "Memoir Added", Toast.LENGTH_LONG).show()
        finish()
    }
}