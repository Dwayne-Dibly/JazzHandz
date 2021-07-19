package com.example.jazzhandzapp.Adapters

import android.os.Handler
import android.os.Looper
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecyclerViewAdapter<T>:  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<T>? = ArrayList<T>()
    protected var itemClickListener: AdapterView.OnItemClickListener? = null

    fun addItems(items: ArrayList<T>) {
        this.list?.addAll(items)
        reload()
    }

    fun clear() {
        this.list?.clear()
        reload()
    }

    fun getItem(position: Int): T? {
        return this.list?.get(position)
    }

//    fun setOnItemClickListener(onItemClickListener: AdapterView.OnItemClickListener) {
//        this.itemClickListener = onItemClickListener
//    }

    override fun getItemCount(): Int = list!!.size

    private fun reload() {
        Handler(Looper.getMainLooper()).post { notifyDataSetChanged() }
    }
}