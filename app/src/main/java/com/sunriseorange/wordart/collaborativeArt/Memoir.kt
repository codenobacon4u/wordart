package com.sunriseorange.wordart.collaborativeArt

// Data Structure for a Memoir that is stored in Database
data class Memoir (
    val id: String = "", // (unused - no need for it)
    val username: String = "", // Author (basically also used as id)
    val memoir: String = "", // 6 word memoir
    val location: String = "" // location
)