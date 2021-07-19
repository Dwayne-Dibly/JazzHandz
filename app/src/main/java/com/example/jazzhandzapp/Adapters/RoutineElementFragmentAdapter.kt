package com.example.jazzhandzapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzhandzapp.DataClassses.Elements
import com.example.jazzhandzapp.R

class RoutineElementFragmentAdapter: BaseRecyclerViewAdapter<Elements>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.routineelementsrecyclerview_griditem, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as? MyViewHolder
        myHolder?.setUpView(user = getItem(position))
//        d("Paul","$position")
    }

//    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {  // might want Listener later
    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {



        private val elementtimeView: TextView = view.findViewById(R.id.elementtimegridview)
        private val elementbeatView: TextView = view.findViewById(R.id.elementbeatgridview)
        private val elementmoveView: TextView = view.findViewById(R.id.elementmovegridview)

//        init {
//            view.setOnClickListener(this)
//        }

        fun setUpView(user: Elements?) {
//            elementmoveView.text = user?.elementmove.toString()
//            elementtimeView.text = user?.routineelementtime.toString()
//            elementbeatView.text = user?.elementbeat.toString()

            elementmoveView.text = user?.elementmove.toString()
            elementtimeView.text = user?.routineelementtime.toString()
            elementbeatView.text = user?.elementbeat.toString()


        }

//        override fun onClick(v: View?) {
//            itemClickListener?.onItemClick(adapterPosition, v)
//        }
    }
}












//   Previous version

//
//class RoutineElementFragmentAdapter(
////    context: Context
////     private val routineElements: LiveData<List<RoutineElement>>
////    private val routineElements: List<RoutineElement>
////    private val products: ArrayList<Elements>
//)
//    : RecyclerView.Adapter<RoutineElementFragmentAdapter.ViewHolder>() {
//
////    private var elements = emptyList<RoutineElement>()
//
////    private var elements = listOf(Elements(eventtime = 1))
//
////    private var elements = products
//
//
////    private var products =  listOf<Elements.Element>()
////    private var products =  arrayListOf<Elements>()
//    private var products =  ArrayList<Elements.Element>()
//
////    fun updateElements (elements: ArrayList<Elements>) {
//    fun updateElements (elements: ArrayList<Elements.Element>) {
//        this.products = elements
//        d("Paul","Adapter $products")
//        notifyDataSetChanged()
////        products.get(position).elements
//    }
//
//
//
//
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val elementtimeItemView: TextView = itemView.findViewById(R.id.elementtimegridview)
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineElementFragmentAdapter.ViewHolder {
//
//        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
//
////        val itemView = inflater.inflate(R.layout.routineelementsrecyclerview_griditem,parent,false)
//        val itemView = inflater.inflate(R.layout.routineelementsrecyclerview_griditem,parent,true)
//        d("Paul","Viewholder created")
//
//        notifyDataSetChanged()
//
////        return ViewHolder(itemView)
//        return ViewHolder(itemView)
//    }
//
//
//    override fun onBindViewHolder(holder: RoutineElementFragmentAdapter.ViewHolder, position: Int) {
//
////    fun updateElements (elements: Elements) {
////        this.products = Elements.Element
////        notifyDataSetChanged()
//////        products.get(position).elements
////    }
//
//
//
////        val currentelement = elements[position]
////        val currentelement = products.elements[position]
//        val currentelement = products[position]
//
//        holder.elementtimeItemView.text = currentelement.toString()
////        holder.elementtimeItemView.text = currentelement.eventtime.toString()
//
//        d("Paul", "${currentelement.elements}")
////        d("Paul", "${currentelement.eventtime}")
//        notifyDataSetChanged()
//    }
//
//
////    override fun getItemCount() = elements.size
//    override fun getItemCount() = products.size
////    override fun getItemCount() = Elements.elements.size
////    d("Paul")
////
//
//
////    internal fun setElements(elements: List<RoutineElement>) {
//////    internal fun setElements() {
////
////
////        this.elements = elements
////
////        d("Paul", "Steps in Routine: ${elements.size}")
////
////        val products = arrayListOf<Elements>()
////
////        for (i in elements.indices)
////        {
////            products.add(
////                Elements(
////                    elements[i].elementtime
////
////
////                )
////            )
////        }
////        d("Paul", " Element List ${products}")
////
////
////
////
////        notifyDataSetChanged()
////
////
////    }
//
//
//}
