package com.sunriseorange.wordart.collaborativeArt

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.google.firebase.database.core.Constants
import com.sunriseorange.wordart.R
import com.sunriseorange.wordart.collaborativeArt.DashboardActivity.Companion.TAG

class MemoirList (private val context: Activity, private var memoirs: List<Memoir>) :

    ArrayAdapter<Memoir>(context, R.layout.layout_memoir_list, memoirs){

        // Utilized in order to display what we want in the dashboard when it comes to
        // the memoir list

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = context.layoutInflater
            val listViewItem = inflater.inflate(
                R.layout.layout_memoir_list,
                parent,
                false
            )
            val textViewMemoir = listViewItem.findViewById<TextView>(R.id.sixWordMemoir)
            val textViewName = listViewItem.findViewById<TextView>(R.id.user)
            val textViewLocation = listViewItem.findViewById<TextView>(R.id.location)
            val memoir = memoirs[position]
            textViewMemoir.text = memoir.memoir
            textViewName.text = "By: " + memoir.username
            textViewLocation.text = "From: " + memoir.location
            return listViewItem
        }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                Log.d(TAG, "**** PUBLISHING RESULTS for: $constraint")
                memoirs = results.values as List<Memoir>
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                Log.d(TAG, "**** PERFORM FILTERING for: $constraint")
                val filteredResults: List<Memoir> = getFilteredResults(constraint)
                val results = FilterResults()
                results.values = filteredResults
                return results
            }

            private fun getFilteredResults(constraint: CharSequence): List<Memoir> {
                val filteredList : ArrayList<Memoir> = ArrayList()
                if(constraint != null){
                    for(memoir in memoirs){
                        if(memoir.location.lowercase().contains(constraint.toString().lowercase())){
                            filteredList.add(memoir)
                        }
                    }
                }
                return filteredList
            }
        }
    }
}