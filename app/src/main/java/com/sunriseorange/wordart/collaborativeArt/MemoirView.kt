package com.sunriseorange.wordart.collaborativeArt

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.maps.MapView
import com.sunriseorange.wordart.R

class MemoirView : Activity() {

    private lateinit var author : TextView
    private lateinit var memoir : TextView
    private lateinit var location : TextView
    private lateinit var mapView : MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memoir_view)

        val intent = intent

        author = findViewById(R.id.authorText)
        memoir = findViewById(R.id.memoirText)
        location = findViewById(R.id.locationText)

        author.text = intent.getStringExtra("user")
        memoir.text = intent.getStringExtra("memoir")
        location.text = intent.getStringExtra("location")
    }
}