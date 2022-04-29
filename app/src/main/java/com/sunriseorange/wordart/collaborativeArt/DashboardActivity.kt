package com.sunriseorange.wordart.collaborativeArt

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.sunriseorange.wordart.R

class DashboardActivity : Activity() {
    companion object {
        const val TAG = "Collaborative-Art-Project"
    }

    private lateinit var buttonAddMemoir: FloatingActionButton
    private lateinit var listViewMemoirs: ListView
    private lateinit var memoirs: MutableList<Memoir>
    private lateinit var databaseMemoirs: DatabaseReference
    private lateinit var searchBar: SearchView
    private lateinit var memoirAdapter: ListAdapter

    // Creates where all the memoirs are listed (feed)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Setting up the Realtime Database
        databaseMemoirs = FirebaseDatabase.getInstance().getReference("memoirs")

        buttonAddMemoir = findViewById(R.id.buttonAddMemoir)
        listViewMemoirs = findViewById(R.id.listMemoirs)
        searchBar = findViewById(R.id.searchMemoirs)

        memoirs = ArrayList()

        buttonAddMemoir.setOnClickListener {
            val intent = Intent(
                this@DashboardActivity,
                AddMemoirActivity::class.java
            )
            startActivity(intent)
        }

        databaseMemoirs.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                memoirs.clear()

                var memoir: Memoir? = null
                for (postSnapshot in dataSnapshot.children) {
                    try {
                        memoir = postSnapshot.getValue(Memoir::class.java)
                    } catch (e: Exception) {
                        Log.e(TAG, e.toString())
                    } finally {
                        memoirs.add(memoir!!)
                    }
                }
                memoirs.sortBy { it.location }
                memoirAdapter = MemoirList(this@DashboardActivity, memoirs)
                listViewMemoirs.adapter = memoirAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchBar.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBar.clearFocus()
                if (query == "text") {
                    val adapter = listViewMemoirs.adapter as ArrayAdapter<*>
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(applicationContext, "Not Found", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter = listViewMemoirs.adapter as ArrayAdapter<*>
                if (newText != null) {
                    adapter.filter.filter(newText)
                }
                return false
            }
        })

        searchBar.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isIconifiedByDefault = false
        }
    }
}
