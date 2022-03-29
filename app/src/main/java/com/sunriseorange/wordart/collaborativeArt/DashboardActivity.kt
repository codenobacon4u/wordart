package com.sunriseorange.wordart.collaborativeArt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        databaseMemoirs = FirebaseDatabase.getInstance().getReference("memoirs")

        buttonAddMemoir = findViewById(R.id.buttonAddMemoir)
        listViewMemoirs = findViewById(R.id.listMemoirs)

        memoirs = ArrayList()

        buttonAddMemoir.setOnClickListener {
            val intent = Intent(this@DashboardActivity, AddMemoirActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

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

                val memoirAdapter = MemoirList(this@DashboardActivity, memoirs)
                listViewMemoirs.adapter = memoirAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
