package com.example.jazzhandzapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzhandzapp.DataClassses.Elements
import com.example.jazzhandzapp.R

class PracticeAttemptFragmentAdapter: BaseRecyclerViewAdapter<Elements>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.practiceattemptelements_recyclerview_griditem, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as? MyViewHolder
        myHolder?.setUpView(user = getItem(position))
//        d("Paul","$position")
    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        private val elementtimeView: TextView = view.findViewById(R.id.practiceattempttimegridview)
        private val elementbeatView: TextView = view.findViewById(R.id.practiceattemptbeatgridview)
        private val elementmoveView: TextView = view.findViewById(R.id.practiceattemptmovegridview)

        fun setUpView(user: Elements?) {

            elementmoveView.text = user?.elementmove.toString()
            elementtimeView.text = user?.routineelementtime.toString()
            elementbeatView.text = user?.elementbeat.toString()

        }
    }
}