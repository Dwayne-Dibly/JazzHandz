package com.example.jazzhandzapp.Fragments

import android.animation.ObjectAnimator
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.jazzhandzapp.Adapters.RoutineElementFragmentAdapter
import com.example.jazzhandzapp.DataClassses.Elements
import com.example.jazzhandzapp.DataClassses.Moves
import com.example.jazzhandzapp.Database.AppDatabase
import com.example.jazzhandzapp.Database.DanceMove
import com.example.jazzhandzapp.Database.DatabaseRepository
import com.example.jazzhandzapp.Database.RoutineElement
import com.example.jazzhandzapp.R
import kotlinx.android.synthetic.main.routine_playback_fragment.*
import kotlinx.coroutines.GlobalScope
import java.lang.Exception
import kotlin.collections.ArrayList

class RoutinePlayBackFragment (routine_id: Int): Fragment() {

    protected lateinit var rootView: View
//    lateinit var recyclerView: RecyclerView
//    lateinit var adapter: RoutineElementFragmentAdapter

    val routineElements: LiveData<List<RoutineElement>>
    val routineid = routine_id

    val repository: DatabaseRepository

    init {

        val routineElementDAO = AppDatabase.getDatabase(Application(), scope = GlobalScope).routineElementDAO()
        val routineid = routine_id  //  this works - just need to get routine_id from Adapter or read the LiveData
//        Log.d("Paul", "Playback Routine Id $routineid")

        routineElements = routineElementDAO.getRoutineElements(routineid)

        // from here  not using much of this?

        val creatorsDAO = AppDatabase.getDatabase(Application(), lifecycleScope).creatorDAO()
        val routineNamesDAO = AppDatabase.getDatabase(Application(), lifecycleScope).routineNameDAO()
        val danceMovesDAO = AppDatabase.getDatabase(Application(), lifecycleScope).danceMoveDAO()

        repository = DatabaseRepository(creatorsDAO, routineNamesDAO, danceMovesDAO, routineElementDAO)

    }


    companion object {  // maybe not needed?
        var TAG = RoutineElementFragmentAdapter::class.java.simpleName
        const val ARG_POSITION: String = "position"

        val routineid = RoutinePlayBackFragment::routineid

        fun newInstance(routine_id: Int): RoutinePlayBackFragment {
//            val routine_id = routineid.toString().toInt()
            val fragment = RoutinePlayBackFragment(routine_id);
            val args = Bundle()
            args.putInt(ARG_POSITION, routine_id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.routine_playback_fragment, container, false);
        initView()



        return rootView
    }

    private fun initView(){
//        setUpAdapter()
        initializeRoutinePlaybackView()
//        setUpDummyData()   // don't need this
    }

    private fun initializeRoutinePlaybackView() {

        routineElements.removeObservers(this)

        routineElements.observe(viewLifecycleOwner, Observer { routine_id -> routine_id?.let { this.setElements(routine_id) } })  // Need This!!




    }

    private fun setElements(elements: List<RoutineElement>) {

        var elementlist: ArrayList<Elements> = ArrayList<Elements>()
        for (i in elements.indices) {
//            var elementtime = elements[i].elementtime.toString()
            var routineid = elements[i].routineelementId.toString()

            var elementtime = elements[i].elementtime.toString()
            var elementbeat = elements[i].elementbeat.toString()

            var elementmove = elements[i].routinedancemoveId.toString()
            var routineelementtime = elements[i].routineelementtime.toString()
            var elementcomment = elements[i].comment.toString()
            var weighton = elements[i].weighton.toString()

            elementlist.add(Elements(routineid, elementtime, elementbeat, elementmove, routineelementtime, elementcomment, weighton))

        }

//        d("Paul", "Playback Element List $elementlist")

//        setMoveImages((elementlist))

        repository.allDanceMoves.removeObservers(this)

        repository.allDanceMoves.observe(
            viewLifecycleOwner,
            Observer { dancemove_id -> dancemove_id?.let { setMoveImages(dancemove_id, elementlist) } })
    }

    private fun setMoveImages(dancemoves: List<DanceMove>, elementlist: ArrayList<Elements>) {

        var movelist: ArrayList<Moves> = ArrayList<Moves>()

        for (i in dancemoves.indices) {
            var dancemoveid = dancemoves[i].dancemoveId
            var dancemove = dancemoves[i].dancemove

            movelist.add(Moves(dancemoveid, dancemove))
        }

        var elementmovelist: ArrayList<Elements> = ArrayList<Elements>()

        val imagelist: ArrayList<String> = arrayListOf("0","0")
        val imageidlist: ArrayList<Int> = arrayListOf()
//        val imageidlist: ArrayList<Int> = arrayListOf(0 ,0, 0)

        for (i in elementlist.indices) {

            var routineid = elementlist[i].routineid

            var elementtime = elementlist[i].elementtime
            var elementbeat = elementlist[i].elementbeat
            var dancemoveid = elementlist[i].elementmove.toString().toInt()
            var dancemove = dancemoves[dancemoveid -1].dancemove


            var routineelementtime = elementlist[i].routineelementtime
            var elementcomment = elementlist[i].comments
            var weighton = elementlist[i].weighton

            elementmovelist.add(Elements(routineid, elementtime, elementbeat, dancemove, routineelementtime, elementcomment, weighton))

            var dancemoveimage = when {
                dancemoveid == 1 -> "leftstepimage"
                dancemoveid == 2 -> "rightstepimage"
                dancemoveid == 3 -> "lefthopimage"
                dancemoveid == 4 -> "righthopimage"
                dancemoveid == 5 -> "lefttapimage"
                dancemoveid == 6 -> "righttapimage"
                dancemoveid == 7 -> "lefttoetapimage"
                dancemoveid == 8 -> "righttoetapimage"
                dancemoveid == 9 -> "leftstepimage" // ball-changes
                dancemoveid == 10 -> "rightstepimage"
                dancemoveid == 11 -> "leftkickimage"
                dancemoveid == 12 -> "rightkickimage"
                dancemoveid == 13 -> "leftkickcrunchimage"
                dancemoveid == 14 -> "rightkickcrunchimage"
                dancemoveid == 15 -> "leftsuzieimage"
                dancemoveid == 16 -> "rightsuzieimage"
                else -> "jazzhandz"
            }
            val dancemoveimageid = context?.getResources()?.getIdentifier(dancemoveimage, "drawable", context?.getPackageName())!!.toInt()

            imagelist.add(dancemoveimage)
            imageidlist.add(dancemoveimageid)

//            d("Paul", "image id list $imagelist")
        }

        startPlayback(elementmovelist, imageidlist)
    }

//    val playbackstarttime = System.currentTimeMillis()

    fun startPlayback(elementmovelist: ArrayList<Elements>, imageidlist: List<Int>){

//        d("Paul", "time start $playbackstarttime time since playback ${System.currentTimeMillis() - playbackstarttime}")
//        d("Paul", "elementmovesize ${elementmovelist.size} imageid size ${imageidlist.size}")

        for (i in elementmovelist.indices) {

            if (i > 1) {

                try {
                    val movetime = (elementmovelist[i].elementtime!!.toInt() - elementmovelist[1].elementtime!!.toInt()).toLong()

                    lastAction.post(Runnable {
                        kotlin.run {
                            Handler().postDelayed({
                                lastAction.text = elementmovelist[i].elementmove.toString()
                                danceImage.setImageResource(imageidlist[i])
                                routineTime.text = elementmovelist[i].routineelementtime.toString()
                                routineBeat.text = elementmovelist[i].elementbeat
//                                val elapsedtime = System.currentTimeMillis() - playbackstarttime
//                                d("Paul", "Update move things $i  $movetime  ${elementmovelist[i].elementmove} time since start $elapsedtime")

                                playbackSeekBar.setProgress(i)

                            }, movetime)
//                            lastAction.text = elementmovelist[i].elementmove.toString()
                        }
                    })

                } catch (exception: Exception) {
//                    d("Paul", "playback exception")
                }
            }

        }

        // Set a SeekBar change listener
        playbackSeekBar.max = elementmovelist.size - 1

        playbackSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {


            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {

                // Display the current progress of SeekBar
//                lastAction.text = "Progress : $i"
                lastAction.text = elementmovelist[i].elementmove.toString()
                danceImage.setImageResource(imageidlist[i])
                routineTime.text = elementmovelist[i].routineelementtime.toString()
                routineBeat.text = elementmovelist[i].elementbeat
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
//                Toast.makeText(applicationContext,"start tracking",Toast.LENGTH_SHORT).show()
                Toast.makeText(context,"start tracking",Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
//                Toast.makeText(applicationContext,"stop tracking",Toast.LENGTH_SHORT).show()
                Toast.makeText(context,"stop tracking",Toast.LENGTH_SHORT).show()
            }
        })

    }
}