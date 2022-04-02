package com.sunriseorange.wordart.collaborativeArt

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.sunriseorange.wordart.R
import com.sunriseorange.wordart.collaborativeArt.DashboardActivity.Companion.TAG

class MemoirList (private val context: Activity, private val memoirs: List<Memoir>):
    ArrayAdapter<Memoir>(context, R.layout.layout_memoir_list, memoirs),
    Filterable {

    private var mFilterList: List<Memoir> = memoirs
    // Utilized in order to display what we want in the dashboard when it comes to
    // the memoir list
    override fun getCount(): Int {
        return mFilterList.size
    }

    override fun getItem(position: Int): Memoir? {
        return mFilterList[position]
    }

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
        val memoir = mFilterList[position]
        textViewMemoir.text = memoir.memoir
        "By: ${memoir.username}".also { textViewName.text = it }
        "From: ${memoir.location}".also { textViewLocation.text = it }
        listViewItem.setOnClickListener {
            val intent = Intent(context, MemoirView::class.java)

            intent.putExtra("user", memoir.username)
            intent.putExtra("memoir", memoir.memoir)
            intent.putExtra("location", memoir.location)
            intent.putExtra("id", memoir.id)

            context.startActivity(intent)
        }

        return listViewItem
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                Log.d(TAG, "**** PUBLISHING RESULTS for: $constraint")
                mFilterList = results.values as List<Memoir>
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                Log.d(TAG, "**** PERFORM FILTERING for: $constraint")
                val queryString = constraint.toString().lowercase()
                val filteredResults = FilterResults()
                filteredResults.values = if (queryString.isEmpty())
                    memoirs
                else
                    memoirs.filter {
                        it.location.lowercase().contains(queryString)
                    }
                Log.d(TAG, "**** RESULTS: ${filteredResults.values}")
                return filteredResults
            }
        }
    }
}