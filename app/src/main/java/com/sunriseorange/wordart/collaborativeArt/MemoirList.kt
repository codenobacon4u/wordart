package com.sunriseorange.wordart.collaborativeArt

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sunriseorange.wordart.R

class MemoirList (private val context: Activity, private var memoirs: List<Memoir>) :
    ArrayAdapter<Memoir>(context, R.layout.layout_memoir_list, memoirs){

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = context.layoutInflater
            val listViewItem = inflater.inflate(R.layout.layout_memoir_list, parent, false)

            val textViewName = listViewItem.findViewById<TextView>(R.id.user)
            val textViewMemoir = listViewItem.findViewById<TextView>(R.id.sixWordMemoir)

            val memoir = memoirs[position]
            textViewName.text = memoir.username
            textViewMemoir.text = memoir.memoir

            return listViewItem
        }
}