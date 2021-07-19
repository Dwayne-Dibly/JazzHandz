package com.example.jazzhandzapp.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzhandzapp.Activities.DownloadedRoutineElementsActivity
import com.example.jazzhandzapp.Activities.RoutineElementsActivity
import com.example.jazzhandzapp.DataClassses.DownloadedRoutines
import com.example.jazzhandzapp.Database.CreatorRoutines
import com.example.jazzhandzapp.Database.RoutineName
import com.example.jazzhandzapp.R

class DownloadedRoutineListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<DownloadedRoutineListAdapter.DownloadedRoutineViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

//    private var creators = emptyList<CreatorRoutines>()
//    private var routines = emptyList<RoutineName>()
//    private var tempos = emptyList<RoutineName>()
//    private var tracks = emptyList<RoutineName>()

    private var routines = emptyList<DownloadedRoutines>()

//        private var creators = emptyList<String>()

    inner class DownloadedRoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val creatorItemView: TextView = itemView.findViewById(R.id.downloadedcreatorgridview)
        val routineItemView: TextView = itemView.findViewById(R.id.downloadedroutinenamegridview)
        val trackItemView: TextView = itemView.findViewById(R.id.downloadedtracknamegridview)
        val tempoItemView: TextView = itemView.findViewById(R.id.downloadedtempogridview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadedRoutineViewHolder {

        val itemView  = inflater.inflate(R.layout.downloadedroutines_griditem,parent,false)
        val holder = DownloadedRoutineViewHolder(itemView)   // view build starts with empty lists so retuns for holder adapeterpositions are null here

        itemView.setOnClickListener() {

            val intent = Intent(parent.context, DownloadedRoutineElementsActivity()::class.java)
            val path = "Users/${routines[holder.adapterPosition].useruid}" + "/Routines/" + "${routines[holder.adapterPosition].downloadedroutinename}" + "/This routine moves"

//           d("Paul", "routine path = $path")

            intent.putExtra("path", path)
            intent.putExtra("creator_name", routines[holder.adapterPosition].downloadedroutinecreator)
            intent.putExtra("routine_name", routines[holder.adapterPosition].downloadedroutinename)
            intent.putExtra("track_name", routines[holder.adapterPosition].downloadedtrack)
            intent.putExtra("track_tempo", routines[holder.adapterPosition].downloadedtempo)

            parent.context.startActivity(intent)

        }

        return holder
    }

    override fun onBindViewHolder(holder: DownloadedRoutineViewHolder, position: Int) {

        val currentuid = routines[position].useruid.toString()
        val currentcreator = routines[position].downloadedroutinecreator.toString()
        val currentroutine = routines[position].downloadedroutinename.toString()
        val currenttrack = routines[position].downloadedtrack.toString()
        val currenttempo = routines[position].downloadedtempo.toString()


        holder.creatorItemView.text = currentcreator
        holder.routineItemView.text = currentroutine
        holder.trackItemView.text = currenttrack
        holder.tempoItemView.text = currenttempo


//        d("Paul", "routines $currentcreator")   // debug doesn't work?

    }

//    internal fun setCreators(creators: List<String>){
////    internal fun setCreators(routines: CreatorRoutines){
//        this.creators = creators
////        d("Paul", "${CreatorRoutines().creator}")
//        notifyDataSetChanged()
//    }
//
//    internal fun setRoutines(routinenames: List<String>){
////        this.routines = routinenames
//        notifyDataSetChanged()
//    }

    internal fun setRoutines(routinenames: List<DownloadedRoutines>){
        this.routines = routinenames
        notifyDataSetChanged()
    }

//    internal fun setTempos(tempos: List<RoutineName>){
//        this.tempos = tempos
//        notifyDataSetChanged()
//    }
//
//    internal fun setTracks(track: List<RoutineName>){
//        this.tracks = track
//        notifyDataSetChanged()
//    }

    override fun getItemCount() = routines.size



}