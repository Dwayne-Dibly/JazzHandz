package com.example.jazzhandzapp.Fragments

import com.example.jazzhandzapp.Adapters.PracticeRoutineElementFragmentAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzhandzapp.DataClassses.Elements
import com.example.jazzhandzapp.R
import kotlinx.android.synthetic.main.practiceelements_recycler_fragment.*

class PracticeRoutineElementFragment (elementmovelist: ArrayList<Elements>): Fragment() {

    protected lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: PracticeRoutineElementFragmentAdapter

    val elementmovelist = elementmovelist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent()
    }

    private fun onCreateComponent() {

        adapter = PracticeRoutineElementFragmentAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.practiceelements_recycler_fragment, container, false);
        initView()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        practiceroutineelementrecyclerview.layoutManager = LinearLayoutManager(activity)
    }

    private fun initView(){
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        recyclerView = rootView.findViewById(R.id.practiceroutineelementrecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        setDanceMoves(elementmovelist)
    }

    private fun setDanceMoves(dancemoves: List<Elements>) {
        adapter.addItems(elementmovelist)
    }

}
