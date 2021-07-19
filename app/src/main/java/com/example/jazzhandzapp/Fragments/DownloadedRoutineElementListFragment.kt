package com.example.jazzhandzapp.Fragments

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzhandzapp.Activities.PracticeActivity
import com.example.jazzhandzapp.Adapters.DownloadedRoutineElementFragmentAdapter
import com.example.jazzhandzapp.Adapters.RoutineElementFragmentAdapter
import com.example.jazzhandzapp.DataClassses.DownloadedRoutines
import com.example.jazzhandzapp.DataClassses.Elements
import com.example.jazzhandzapp.DataClassses.Moves
import com.example.jazzhandzapp.Database.AppDatabase
import com.example.jazzhandzapp.Database.DanceMove
import com.example.jazzhandzapp.Database.DatabaseRepository
import com.example.jazzhandzapp.Database.RoutineElement
import com.example.jazzhandzapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.downloaded_routine_elements_recycler_fragment.*
import kotlinx.android.synthetic.main.routineelements_recycler_fragment.*
import kotlinx.coroutines.GlobalScope

//class DownloadedRoutineElementListFragment (routine_id: Int, practicemode: Boolean, track_tempo: String): Fragment() {
class DownloadedRoutineElementListFragment(routine_path: String) : Fragment() {

    protected lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RoutineElementFragmentAdapter

//    val routineElements: LiveData<List<RoutineElement>>

    val routinepath = routine_path
//    val practicemode = practicemode
//    val track_tempo = track_tempo

    val repository: DatabaseRepository
//    val allCreators: LiveData<List<Creator>>
//
//    val routineCreators: LiveData<List<CreatorRoutines>>
//    val allRoutines: LiveData<List<RoutineName>>
//    val allDanceMoves: LiveData<List<DanceMove>>


    init {


//        val routineElementDAO = AppDatabase.getDatabase(Application(), lifecycleScope).routineElementDAO()
        val routineElementDAO = AppDatabase.getDatabase(Application(), scope = GlobalScope).routineElementDAO()


        val routinepath = routine_path  //  this works - just need to get routine_id from Adapter or read the LiveData

//        d("Paul", " Routine Path $routinepath")

//        routineElements = routineElementDAO.getRoutineElements(routineid)

        // from here  not using much of this?

//        val creatorsDAO = AppDatabase.getDatabase(Application(), lifecycleScope).creatorDAO()
        val creatorDAO = AppDatabase.getDatabase(Application(), scope = GlobalScope).creatorDAO()
//        val routineNamesDAO = AppDatabase.getDatabase(Application(), lifecycleScope).routineNameDAO()
        val routineNameDAO = AppDatabase.getDatabase(Application(), scope = GlobalScope).routineNameDAO()
//        val danceMovesDAO = AppDatabase.getDatabase(Application(), lifecycleScope).danceMoveDAO()
        val danceMovesDAO = AppDatabase.getDatabase(Application(), scope = GlobalScope).danceMoveDAO()

        repository = DatabaseRepository(creatorDAO, routineNameDAO, danceMovesDAO, routineElementDAO)

    }


    companion object {
        var TAG = DownloadedRoutineElementFragmentAdapter::class.java.simpleName
        const val ARG_POSITION: String = "position"

        val routinepath = DownloadedRoutineElementListFragment::routinepath
//        val practicemode = DownloadedRoutineElementListFragment::practicemode.toString().toBoolean()

        //        fun newInstance(routine_id: Int, practicemode: Boolean, track_tempo: String): DownloadedRoutineElementListFragment {
        fun newInstance(routine_path: String): DownloadedRoutineElementListFragment {
//        fun newInstance(routine_id: Int): RoutineElementListFragment {
//            val routine_id = routineid.toString().toInt()
//            val fragment = DownloadedRoutineElementListFragment(routine_id, practicemode, track_tempo);
            val fragment = DownloadedRoutineElementListFragment(routine_path);
            val args = Bundle()
            args.putString(ARG_POSITION, routine_path)
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
        rootView = inflater.inflate(R.layout.downloaded_routine_elements_recycler_fragment, container, false);
        initView()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        textview_first.text = "View Updated"
        downloadedroutineelementrecyclerview.layoutManager = LinearLayoutManager(activity)

    }

    private fun initView() {
//        setUpAdapter()
        initializeRecyclerView()
//        setUpDummyData()   // don't need this
    }

    val documentlist: ArrayList<String> = arrayListOf()
    val documentlistpath: ArrayList<String> = arrayListOf()

    //        val downloadedroutines: ArrayList<DownloadedRoutines> = arrayListOf()
    val downloadedelements: ArrayList<Elements> = arrayListOf()

    private fun initializeRecyclerView() {

//        val documentlist: ArrayList<String> = arrayListOf()
//        val documentlistpath: ArrayList<String> = arrayListOf()
////        val downloadedroutines: ArrayList<DownloadedRoutines> = arrayListOf()
//        val downloadedelements: ArrayList<Elements> = arrayListOf()

        var db = FirebaseFirestore.getInstance()

        db
            .collection(routinepath)
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
//                    d("Paul", "data ${task.result!!} documents ${task.result!!.documents}")

                    for (document in task.result!!.documents) {
                        documentlist.add(document.id)
                        documentlistpath.add(document.reference.path)
                    }
                }

//                d("Paul", "downloaded routines $documentlist")

                for (i in documentlistpath.indices) {

                    db
                        .document(documentlistpath[i])
                        .get()
                        .addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                            if (task.isSuccessful) {

//                                var downloaded_routine_time = task.result!!["routine_name"].toString()
                                var downloaded_routine_time = task.result!!["element_time"].toString()

                                var downloaded_routine_beat = task.result!!["element_beat"].toString()
                                var downloaded_routine_move_id = task.result!!["element_move"].toString()
                                var downloaded_routine_element_time = task.result!!["element_routinetime"].toString()
                                var downloaded_routine_element_comment = task.result!!["element_commment"].toString()
                                var downloaded_routine_element_weight = task.result!!["element_weighton"].toString()

                                downloadedelements.add(Elements("0",
                                        downloaded_routine_time, downloaded_routine_beat, downloaded_routine_move_id, downloaded_routine_element_time,
                                        downloaded_routine_element_comment, downloaded_routine_element_weight)
                                )


                            }
//                            d("Paul", "downloaded routines $downloadedelements")

                            repository.allDanceMoves.removeObservers(this)

                            if (i == documentlistpath.size -1) {
                                repository.allDanceMoves.observe(
                                    viewLifecycleOwner,
                                    Observer { dancemove_id -> dancemove_id?.let { setDanceMoves(dancemove_id, downloadedelements) } })
                            }
                        })
                }


            })

//        d("Paul", "downloaded routines $documentlist")  // not here

//        routineElements.observe(viewLifecycleOwner, Observer { routine_id -> routine_id?.let { this.setElements(routine_id) } })  // Need This!!

        recyclerView = rootView.findViewById(R.id.downloadedroutineelementrecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

    }


    private fun setDanceMoves(dancemoves: List<DanceMove>, elementlist: ArrayList<Elements>) {

        var movelist: ArrayList<Moves> = ArrayList<Moves>()

        for (i in dancemoves.indices) {
            var dancemoveid = dancemoves[i].dancemoveId
            var dancemove = dancemoves[i].dancemove

            movelist.add(Moves(dancemoveid, dancemove))
        }

        repository.allDanceMoves.removeObservers(this)

        var elementmovelist: ArrayList<Elements> = ArrayList<Elements>()

//        d("Paul", "Move list $elementlist")

//        elementmovelist.add(Elements("Time", "Beat", "Move"))  // put in textbox in fragment?

        for (i in elementlist.indices) {

//            if (i > 1) {
            var routineid = elementlist[i].routineid

            var elementtime = elementlist[i].elementtime
            var elementbeat = elementlist[i].elementbeat
            var dancemoveid = elementlist[i].elementmove.toString().toInt()
            var dancemove = dancemoves[dancemoveid - 1].dancemove

            var routineelementtime = elementlist[i].routineelementtime
            var elementcomment = elementlist[i].comments
            var weighton = elementlist[i].weighton
//                d("Paul", "dancemove: $dancemove")  //

            elementmovelist.add(Elements(routineid, elementtime, elementbeat, dancemove, routineelementtime, elementcomment, weighton))
//            }
        }

//        elementmovelist.sortBy { it.elementtime }

        val sortedelementmovelist = elementmovelist.sortedByDescending { it.elementtime!!.toInt() }.toList().reversed()

//        d("Paul", "Sorted Move list $sortedelementmovelist")

//        routineElements.removeObservers(this)

//        adapter.addItems(elementmovelist)
        adapter.addItems(sortedelementmovelist as ArrayList<Elements>)


    }

}
