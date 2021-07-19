import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzhandzapp.*
import com.example.jazzhandzapp.Activities.RoutineElementsActivity
import com.example.jazzhandzapp.Database.CreatorRoutines
import com.example.jazzhandzapp.Database.RoutineName

class RoutineListAdapter internal constructor(
context: Context
) : RecyclerView.Adapter<RoutineListAdapter.RoutineViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var creators = emptyList<CreatorRoutines>()
    private var routines = emptyList<RoutineName>()
    private var tempos = emptyList<RoutineName>()
    private var tracks = emptyList<RoutineName>()

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val creatorItemView: TextView = itemView.findViewById(R.id.creatorgridview)
        val routineItemView: TextView = itemView.findViewById(R.id.routinenamegridview)
        val trackItemView: TextView = itemView.findViewById(R.id.tracknamegridview)
        val tempoItemView: TextView = itemView.findViewById(R.id.tempogridview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {

        val itemView  = inflater.inflate(R.layout.routinesrecyclerview_griditem,parent,false)
        val holder = RoutineViewHolder(itemView)   // view build starts with empty lists so retuns for holder adapeterpositions are null here

        itemView.setOnClickListener() {

            val routine_id = holder.adapterPosition + 1
            val intent = Intent(parent.context, RoutineElementsActivity()::class.java)

            intent.putExtra("routine_name", routines[holder.adapterPosition].routinename)
            intent.putExtra("track_name", routines[holder.adapterPosition].track)
            intent.putExtra("track_tempo", routines[holder.adapterPosition].tempo.toString())
            intent.putExtra("creator_name", creators[holder.adapterPosition].creator?.creator)

//            d("Paul", "track tempo ${routines[holder.adapterPosition].tempo}")

            intent.putExtra(
                "routine_id",
                holder.adapterPosition + 1
            )  // note - index starts from 0, primary key starts at 1  // Don't do it this way??

            parent.context.startActivity(intent)

        }

        return holder
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val currentcreator = creators[position]
        val currentroutine = routines[position]

        holder.creatorItemView.text = currentcreator.creator?.creator
        holder.routineItemView.text = currentroutine.routinename
        holder.trackItemView.text = currentroutine.track
//        holder.tempoItemView.text = currentroutine.tempo.toString()
        holder.tempoItemView.text = currentroutine.tempo.toString()

//        d("Paul", "tempo ${currentroutine.tempo.toString()}")

    }

    internal fun setCreators(creators: List<CreatorRoutines>){
//    internal fun setCreators(routines: CreatorRoutines){
        this.creators = creators
//        d("Paul", "${CreatorRoutines().creator}")
        notifyDataSetChanged()
    }

    internal fun setRoutines(routinenames: List<RoutineName>){
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

    override fun getItemCount() = creators.size



}

//abstract class RoutineIdListener (val clickListener: (routine_id: Int) -> Int) {
//class RoutineIdListener (itemClick: (Int) -> Unit){
//class RoutineIdListener (itemClick: (Int) -> Unit){
////    val routine_id = intentroutineid.getIntExtra(routine_id)
//
//    val routine_id = itemClick
//
////    val routine_id = 2  // this works
//
////    val routine_id = Log.println(Log.DEBUG,"Paul","Test")
////    val routine_id = Log.getStackTraceString(t)
//}

