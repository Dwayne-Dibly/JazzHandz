package com.example.jazzhandzapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzhandzapp.Adapters.PracticeAttemptFragmentAdapter
import com.example.jazzhandzapp.DataClassses.Elements
import com.example.jazzhandzapp.R
import kotlinx.android.synthetic.main.practiceattempt_recycler_fragment.*

class PracticeAttemptElementFragment(elementmovelist: ArrayList<Elements>) : Fragment() {

    protected lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: PracticeAttemptFragmentAdapter

    val elementmovelist = elementmovelist

    companion object {
//        var TAG = RoutineElementFragmentAdapter::class.java.simpleName
        var TAG = PracticeAttemptFragmentAdapter::class.java.simpleName
        const val ARG_POSITION: String = "position"

//        val routineid = RoutineElementListFragment::routineid
//        val practicemode = RoutineElementListFragment::practicemode.toString().toBoolean()

//        fun newInstance(routine_id: Int, practicemode: Boolean, track_tempo: String): RoutineElementListFragment {
//        fun newInstance(): PracticeAttemptElementFragment {
////        fun newInstance(routine_id: Int): RoutineElementListFragment {
////            val routine_id = routineid.toString().toInt()
////            val fragment = RoutineElementListFragment(routine_id, practicemode, track_tempo)
//            val fragment = PracticeAttemptElementFragment
//            val args = Bundle()
////            args.putInt(ARG_POSITION, routine_id)
////            args.putInt(ARG_POSITION)
////            fragment.arguments = args
////            return fragment
//            return fragment   // right?
//        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent()
    }

    private fun onCreateComponent() {

        adapter = PracticeAttemptFragmentAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.practiceattempt_recycler_fragment, container, false);
        initView()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        practiceattemptelementrecyclerview.layoutManager = LinearLayoutManager(activity)
    }

    private fun initView() {
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        recyclerView = rootView.findViewById(R.id.practiceattemptelementrecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        setDanceMoves(elementmovelist)
    }

    private fun setDanceMoves(dancemoves: List<Elements>) {
        adapter.addItems(elementmovelist)
    }

}
