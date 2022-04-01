package com.sunriseorange.wordart.collaborativeArt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.sunriseorange.wordart.R

class DashboardActivity : Activity() {
    companion object{
        const val TAG = "Collaborative-Art-Project"
    }

    private lateinit var buttonAddMemoir: FloatingActionButton
    private lateinit var listViewMemoirs: ListView
    private lateinit var memoirs: MutableList<Memoir>
    private lateinit var databaseMemoirs: DatabaseReference
    private lateinit var searchBar : SearchView
    private lateinit var memoirAdapter : ListAdapter

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

        listViewMemoirs.onItemClickListener = AdapterView.OnItemClickListener { _, _, item, _ ->
            val memoir = memoirs[item]
            
            val intent = Intent(this@DashboardActivity, MemoirView::class.java)

            intent.putExtra("user", memoir.username)
            intent.putExtra("memoir", memoir.memoir)
            intent.putExtra("location", memoir.location)
            intent.putExtra("id", memoir.id)

            startActivity(intent)
        }

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(
                    this@DashboardActivity,
                    "No Match found",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    override fun onStart() {
        super.onStart()

        // Getting list of Memoirs to display on main page
        displayMainPage()
    }

    private fun memoirQuery(memoirs: MutableList<Memoir>, query: String): Boolean {
        for(quote in memoirs){
            if(quote.username.compareTo(query) == 0){
                return true
            }
        }
        return false
    }

    private fun displayMainPage(){
        databaseMemoirs.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                memoirs.clear()

                var memoir: Memoir? = null
                for(postSnapshot in dataSnapshot.children){
                    try{
                        memoir = postSnapshot.getValue(Memoir::class.java)
                    }catch(e : Exception){
                        Log.e(TAG, e.toString())
                    }finally {
                        memoirs.add(memoir!!)
                    }
                }

                memoirAdapter = MemoirList(this@DashboardActivity, memoirs)
                listViewMemoirs.adapter = memoirAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
