package com.example.jazzhandzapp.Fragments

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzhandzapp.Activities.PracticeActivity
import com.example.jazzhandzapp.Adapters.RoutineElementFragmentAdapter
import com.example.jazzhandzapp.DataClassses.Elements
import com.example.jazzhandzapp.DataClassses.Moves
import com.example.jazzhandzapp.Database.*
import com.example.jazzhandzapp.R
import kotlinx.android.synthetic.main.routineelements_recycler_fragment.*
import kotlinx.coroutines.GlobalScope

class RoutineElementListFragment (routine_id: Int, practicemode: Boolean, track_tempo: String): Fragment() {

    protected lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RoutineElementFragmentAdapter

    val routineElements: LiveData<List<RoutineElement>>
    val routineid = routine_id
    val practicemode = practicemode
    val track_tempo = track_tempo

    val repository: DatabaseRepository
//    val allCreators: LiveData<List<Creator>>
//
//    val routineCreators: LiveData<List<CreatorRoutines>>
//    val allRoutines: LiveData<List<RoutineName>>
//    val allDanceMoves: LiveData<List<DanceMove>>


    init {


//        val routineElementDAO = AppDatabase.getDatabase(Application(), lifecycleScope).routineElementDAO()
        val routineElementDAO = AppDatabase.getDatabase(Application(), scope = GlobalScope).routineElementDAO()


        val routineid = routine_id  //  this works - just need to get routine_id from Adapter or read the LiveData
//        d("Paul", " Routine Id $routineid")

        routineElements = routineElementDAO.getRoutineElements(routineid)

        // from here  not using much of this?

        val creatorsDAO = AppDatabase.getDatabase(Application(), lifecycleScope).creatorDAO()
        val routineNamesDAO = AppDatabase.getDatabase(Application(), lifecycleScope).routineNameDAO()
        val danceMovesDAO = AppDatabase.getDatabase(Application(), lifecycleScope).danceMoveDAO()

        repository = DatabaseRepository(creatorsDAO, routineNamesDAO, danceMovesDAO, routineElementDAO)

    }


    companion object {
        var TAG = RoutineElementFragmentAdapter::class.java.simpleName
        const val ARG_POSITION: String = "position"

        val routineid = RoutineElementListFragment::routineid
        val practicemode = RoutineElementListFragment::practicemode.toString().toBoolean()

        fun newInstance(routine_id: Int, practicemode: Boolean, track_tempo: String): RoutineElementListFragment {
//        fun newInstance(routine_id: Int): RoutineElementListFragment {
//            val routine_id = routineid.toString().toInt()
            val fragment = RoutineElementListFragment(routine_id, practicemode, track_tempo);
            val args = Bundle()
            args.putInt(ARG_POSITION, routine_id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent()
    }

    private fun onCreateComponent() {

        adapter = RoutineElementFragmentAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.routineelements_recycler_fragment, container, false);
        initView()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        textview_first.text = "View Updated"
        routineelementrecyclerview.layoutManager = LinearLayoutManager(activity)

    }

    private fun initView(){
//        setUpAdapter()
        initializeRecyclerView()
//        setUpDummyData()   // don't need this
    }

    private fun initializeRecyclerView() {

        routineElements.removeObservers(this)

        routineElements.observe(viewLifecycleOwner, Observer { routine_id -> routine_id?.let { this.setElements(routine_id) } })  // Need This!!

        recyclerView = rootView.findViewById(R.id.routineelementrecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

    }

    private fun setElements(elements: List<RoutineElement>) {

        var elementlist: ArrayList<Elements> = ArrayList<Elements>()

//    elementlist.add(Elements("Time", "Beat", "Move"))  // test

        for (i in elements.indices) {
//            var elementtime = elements[i].elementtime.toString()
            var routineid = elements[i].routineelementId.toString()

            var elementtime = elements[i].elementtime.toString()
            var elementbeat = elements[i].elementbeat.toString()

            var elementmove = elements[i].routinedancemoveId.toString()
            var routineelementtime = elements[i].routineelementtime.toString()
            var elementcomment = elements[i].comment.toString()
            var weighton = elements[i].weighton.toString()

            elementlist.add(Elements(routineid, elementtime, elementbeat, elementmove, routineelementtime, elementcomment,weighton))

        }

        routineElements.removeObservers(this)

        repository.allDanceMoves.removeObservers(this)

        repository.allDanceMoves.observe(
            viewLifecycleOwner,
            Observer { dancemove_id -> dancemove_id?.let { setDanceMoves(dancemove_id, elementlist) } })

    }

    private fun setDanceMoves(dancemoves: List<DanceMove>, elementlist: ArrayList<Elements>) {

        var movelist: ArrayList<Moves> = ArrayList<Moves>()

        for (i in dancemoves.indices) {
            var dancemoveid = dancemoves[i].dancemoveId
            var dancemove = dancemoves[i].dancemove

            movelist.add(Moves(dancemoveid, dancemove))
        }

        var elementmovelist: ArrayList<Elements> = ArrayList<Elements>()

//        elementmovelist.add(Elements("Time", "Beat", "Move"))  // put in textbox in fragment?

        for (i in elementlist.indices) {

//            if (i > 1) {
                var routineid = elementlist[i].routineid

                var elementtime = elementlist[i].elementtime
                var elementbeat = elementlist[i].elementbeat
                var dancemoveid = elementlist[i].elementmove.toString().toInt()
                var dancemove = dancemoves[dancemoveid -1].dancemove

                var routineelementtime = elementlist[i].routineelementtime
                var elementcomment = elementlist[i].comments
                var weighton = elementlist[i].weighton
//                d("Paul", "dancemove: $dancemove")  //

                elementmovelist.add(Elements(routineid, elementtime, elementbeat, dancemove, routineelementtime, elementcomment, weighton))
//            }
        }

        routineElements.removeObservers(this)


        adapter.addItems(elementmovelist)

        if (practicemode == true) {

//            val intent = Intent(parent.context, RoutineElementsActivity::class.java)
            val intent = Intent(context, PracticeActivity::class.java)

            intent.putExtra(
                "elementmovelist",
                elementmovelist
            )
            intent.putExtra("tempo",
            track_tempo
                )

            context?.startActivity(intent)

//            startActivity(Intent(context, PracticeActivity::class.java))
        }
    }

    private fun setUpDummyData() {
//        var list: ArrayList<User> = ArrayList<User>()
//        list.add(User("User 1", R.drawable.ic_menu_black_24dp))
//        list.add(User("User 2", R.drawable.ic_menu_black_24dp))
//        list.add(User("User 3", R.drawable.ic_menu_black_24dp))
//        list.add(User("User 4", R.drawable.ic_menu_black_24dp))
//        list.add(User("User 5", R.drawable.ic_menu_black_24dp))
//        list.add(User("User 6", R.drawable.ic_menu_black_24dp))
//        list.add(User("User 7", R.drawable.ic_menu_black_24dp))
//        list.add(User("User 8", R.drawable.ic_menu_black_24dp))
//        list.add(User("User 9", R.drawable.ic_menu_black_24dp))
//        adapter.addItems(list)
    }

}
