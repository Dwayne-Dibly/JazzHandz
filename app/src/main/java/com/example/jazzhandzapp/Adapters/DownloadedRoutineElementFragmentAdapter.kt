package com.example.jazzhandzapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzhandzapp.DataClassses.Elements
import com.example.jazzhandzapp.R

class DownloadedRoutineElementFragmentAdapter : BaseRecyclerViewAdapter<Elements>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.downloadedroutineelementsrecyclerview_griditem, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as? MyViewHolder
        myHolder?.setUpView(element = getItem(position))
//        d("Paul","$position")
    }

    //    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {  // might want Listener later
    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {



        private val elementtimeView: TextView = view.findViewById(R.id.downloadedelementtimegridview)
        private val elementbeatView: TextView = view.findViewById(R.id.downloadedelementbeatgridview)
        private val elementmoveView: TextView = view.findViewById(R.id.downloadedelementmovegridview)

//        init {
//            view.setOnClickListener(this)
//        }

        fun setUpView(element: Elements?) {

            elementmoveView.text = element?.elementmove.toString()
            elementtimeView.text = element?.routineelementtime.toString()
            elementbeatView.text = element?.elementbeat.toString()

        }

//        override fun onClick(v: View?) {
//            itemClickListener?.onItemClick(adapterPosition, v)
//        }
    }
}
