package com.example.jazzhandzapp.Activities

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.RED
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.jazzhandzapp.DataClassses.Elements
import com.example.jazzhandzapp.DataClassses.Moves
import com.example.jazzhandzapp.Database.AppDatabase
import com.example.jazzhandzapp.Database.DanceMove
import com.example.jazzhandzapp.Database.RoutineElement
import com.example.jazzhandzapp.Database.RoutineName
import com.example.jazzhandzapp.Fragments.PracticeAttemptElementFragment
import com.example.jazzhandzapp.Fragments.PracticeRoutineElementFragment
import com.example.jazzhandzapp.R
import com.example.jazzhandzapp.ViewModels.DanceActivityViewModel
import kotlinx.android.synthetic.main.dance_screen.countineight
import kotlinx.android.synthetic.main.dance_screen.countinfirstfive
import kotlinx.android.synthetic.main.dance_screen.countinfirstsix
import kotlinx.android.synthetic.main.dance_screen.countinsecondfive
import kotlinx.android.synthetic.main.dance_screen.countinsecondsix
import kotlinx.android.synthetic.main.dance_screen.countinseven
import kotlinx.android.synthetic.main.dance_screen.danceImage
import kotlinx.android.synthetic.main.dance_screen.lastAction
import kotlinx.android.synthetic.main.dance_screen.leftImage
import kotlinx.android.synthetic.main.dance_screen.rightImage
import kotlinx.android.synthetic.main.nav_view_downloadlist_fragment.*
import kotlinx.android.synthetic.main.nav_view_practiceactivity_fragment.*
import kotlinx.android.synthetic.main.practice_dance_screen.*
import kotlinx.android.synthetic.main.practiceelements_recycler_fragment.*
import kotlinx.android.synthetic.main.routineelements_activity.showroutineelements
import java.math.RoundingMode
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.nextDown

class PracticeActivity : AppCompatActivity(),
    GestureDetector.OnGestureListener
//        GestureDetector.OnDoubleTapListener {

{
    private lateinit var danceactivityviewmodel: DanceActivityViewModel
    private lateinit var mDetector: GestureDetectorCompat
    var routineNames: LiveData<List<RoutineName>>
    var routineElements: LiveData<List<RoutineElement>>

    init {

        val routineNameDAO = AppDatabase.getDatabase(Application(), lifecycleScope).routineNameDAO()
        val routineElementDAO = AppDatabase.getDatabase(Application(), lifecycleScope).routineElementDAO()

        routineNames = routineNameDAO.getRoutineNames()
        routineElements = routineElementDAO.getRoutineElements(1)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.practice_dance_screen)
//        setContentView(R.layout.nav_view_practiceactivity_fragment)

//        nav_view_fragment_practiceactivity.setNavigationItemSelectedListener {
//            when (it.itemId) {
////                R.id.actionCommittee -> d("Paul", "Committee was pressed")
//                R.id.actionHome -> startActivity(Intent(this, MainActivity::class.java))
//            }
//            when (it.itemId) {
////                R.id.actionCommittee -> d("Paul", "Committee was pressed")
//                R.id.actionFreestyle -> startActivity(Intent(this, DanceActivity::class.java))
//            }
//            when (it.itemId) {
////                R.id.actionCommittee -> d("Paul", "Committee was pressed")
////                R.id.actionCreate -> startActivity(Intent(this, DanceActivity::class.java))
//                R.id.actionCreate -> startActivity(Intent(this, DanceActivity::class.java).putExtra("createmode", true))
//            }
//            when (it.itemId) {
////                R.id.actionCommittee -> d("Paul", "Committee was pressed")
//                R.id.actionRoutines -> startActivity(Intent(this, DanceRoutinesActivity::class.java).putExtra("createmode", false))
//            }
//            when (it.itemId) {
////                R.id.actionCommittee -> d("Paul", "Committee was pressed")
//                R.id.actionDownloadedRoutines -> startActivity(Intent(this, DownloadRoutinesActivity::class.java))
//            }
//            it.isChecked = true
////            drawerLayout.closeDrawers()
//            practiceactivity_drawer_layout.closeDrawers()
//            true
//
//        }
//
//        supportActionBar?.apply {
//            setDisplayHomeAsUpEnabled(true)
//            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
//        }

        danceactivityviewmodel = ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

        val elementmovelist = intent.getStringArrayListExtra("elementmovelist") as ArrayList<Elements>

        val currenttempo = intent.getStringExtra("tempo")
        practiceactivitytempo.text = currenttempo!!.toString()

        showroutineelements.setOnClickListener {

            practiceresults.visibility = View.INVISIBLE

            var practiceelementlist = elementmovelist

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.practiceframelayout,
                        PracticeRoutineElementFragment(practiceelementlist)
                    )
                    .commit()
            }

            showroutineelements.visibility = View.INVISIBLE
            startpractice.visibility = View.VISIBLE
            savedroutinelabel.visibility = View.VISIBLE
            routineattemptlabel.visibility = View.INVISIBLE
        }

        startpractice.setOnClickListener {

            danceImage.visibility = View.VISIBLE
            practiceresults.visibility = View.VISIBLE

            // remove previous results

//                val attemptfragment = supportFragmentManager.findFragmentById(PracticeRoutineElementFragment.)
//                if (attemptfragment != null) {
//                    if (savedInstanceState == null) {
//                        supportFragmentManager.beginTransaction()
//                            .remove(
//                                attemptfragment
//                            )
//                            .commit()
//                    }
//                }

            clearLists()
            danceactivityviewmodel.deleteRoutineElements(1)

            var practiceelementlist: ArrayList<Elements> = ArrayList<Elements>()

            for (i in elementmovelist) {

                var routineid = i.routineid.toString()
                var elementtime = i.elementtime.toString()
                var elementbeat = i.elementbeat.toString()
                var elementmove = i.elementmove.toString()
                var routineelementtime = i.routineelementtime.toString()
                var elementcomment = i.comments.toString()
                var weighton = i.weighton.toString()

                practiceelementlist.add(Elements(routineid, elementtime, elementbeat, elementmove, routineelementtime, elementcomment, weighton))

            }

            AlertDialog.Builder(this)
                .setMessage("Count in (5, 5, 5-6-7-8), or start immediately?")
                .setPositiveButton("Count me in")
                { p0, p1 ->
                    danceactivityviewmodel.deleteRoutineElements(1)
//                    clearLists()  not here?
//                        getRoutineElements(practiceroutineid)
//                    danceImage.visibility = View.INVISIBLE
                    savedroutinelabel.visibility = View.INVISIBLE

                    countinfirstfive.visibility = View.VISIBLE
                    countinfirstsix.visibility = View.VISIBLE
                    countinsecondfive.visibility = View.VISIBLE
                    countinsecondsix.visibility = View.VISIBLE
                    countinseven.visibility = View.VISIBLE
                    countineight.visibility = View.VISIBLE
//                    clapTempoImage.visibility = View.VISIBLE
//                        deleteImage.visibility = View.INVISIBLE

                    var beattime = 60000.div(currenttempo.toInt()).toLong()
                    var twobeattime = beattime * 2

                    lastAction.post(Runnable {
                        kotlin.run {
                            Handler().postDelayed({
                                countinfirstfive.setTextColor(RED)
                            }, 10)
                            Handler().postDelayed({
                                countinfirstsix.setTextColor(RED)
                            }, 10 + twobeattime)
                            Handler().postDelayed({
                                countinsecondfive.setTextColor(RED)
                            }, 10 + twobeattime + beattime)
                            Handler().postDelayed({
                                countinsecondsix.setTextColor(RED)
                            }, 10 + twobeattime + beattime + beattime)
                            Handler().postDelayed({
                                countinseven.setTextColor(RED)
                            }, 10 + twobeattime + beattime + beattime + beattime)
                            Handler().postDelayed({
                                countineight.setTextColor(RED)
//                                val practicestarttime = System.currentTimeMillis().toInt() + (10 + twobeattime + beattime + beattime + beattime + beattime).toInt()  // add time for count in
                                val practicestarttime =
                                    System.currentTimeMillis().toInt() + (10 + beattime).toInt()  // countin time starts when posted?
//                                val practicestarttime = System.currentTimeMillis().toInt() + (10).toInt()  // countin time starts when posted?
                                clearLists()

                                // restore routine fragment
                                manageElementScrolling(practicestarttime, practiceelementlist)

                                leftImage.visibility = View.VISIBLE
                                rightImage.visibility = View.VISIBLE
                                danceImage.visibility = View.VISIBLE


                            }, 10 + twobeattime + beattime + beattime + beattime + beattime)

                            Handler().postDelayed({
                                countinfirstfive.visibility = View.INVISIBLE
                                countinfirstsix.visibility = View.INVISIBLE
                                countinsecondfive.visibility = View.INVISIBLE
                                countinsecondsix.visibility = View.INVISIBLE
                                countinseven.visibility = View.INVISIBLE
                                countineight.visibility = View.INVISIBLE

                                savedroutinelabel.visibility = View.VISIBLE

                            }, 10 + twobeattime + beattime + beattime + beattime + beattime + beattime)
                        }
                    })

                }
                .setNegativeButton("Start now")
                { p0, p1 ->
                    danceactivityviewmodel.deleteRoutineElements(1)
                    clearLists()
                    danceImage.visibility = View.VISIBLE
                    countinfirstfive.visibility = View.INVISIBLE
                    countinfirstsix.visibility = View.INVISIBLE
                    countinsecondfive.visibility = View.INVISIBLE
                    countinsecondsix.visibility = View.INVISIBLE
                    countinseven.visibility = View.INVISIBLE
                    countineight.visibility = View.INVISIBLE

                    savedroutinelabel.visibility = View.VISIBLE
                    routineattemptlabel.visibility = View.INVISIBLE
//                    clapTempoImage.visibility = View.VISIBLE
//                        deleteImage.visibility = View.INVISIBLE
                    val practicestarttime = System.currentTimeMillis().toInt()
                    manageElementScrolling(practicestarttime, practiceelementlist)

                    leftImage.visibility = View.VISIBLE
                    rightImage.visibility = View.VISIBLE
                }
                // empty lists
                .create()
                .show()

//            val practicestarttime = System.currentTimeMillis().toInt()
//
//            manageElementScrolling(practicestarttime, practiceelementlist)

        }

        mDetector = GestureDetectorCompat(this, this)

        getInitialTempo(currenttempo.toInt())

        practiceresults.setOnClickListener {

            danceImage.visibility = View.INVISIBLE
            practiceattemptframelayout.visibility = View.VISIBLE

            leftImage.visibility = View.INVISIBLE
            rightImage.visibility = View.INVISIBLE

            practiceroutineelementrecyclerview.scrollToPosition(0)

            routineElements.removeObservers(this)

            routineElements.observe(
                this, Observer { routine_id ->
                    routine_id?.let {
                        this.setPracticeAttemptElements(
                            routine_id, elementmovelist, currenttempo
                        )
                    }
                })
        }


    }  // end here for supporting all Gestures

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        practiceactivity_drawer_layout.openDrawer(GravityCompat.START)
//        return true
//    }

    //        val downeventlist:  MutableList<Long> = mutableListOf()
    val downeventlist: ArrayList<Int> = arrayListOf(0, 0, 0)
    val downeventlist_x: ArrayList<Int> = arrayListOf(0, 0, 0)
    val downeventlist_y: ArrayList<Int> = arrayListOf(0, 0, 0)

    val weightonlist: ArrayList<String> = arrayListOf("0", "0", "0")

    val eventlistid: ArrayList<Int> = arrayListOf()
    val eventlistbeats: ArrayList<Int> = arrayListOf()
    val eventlisttime: ArrayList<Int> = arrayListOf()  // absolute time
    val eventlist: ArrayList<String> = arrayListOf()

    val routineeventlisttime: ArrayList<Int> = arrayListOf()   // time vs start of routine

//        val currenttempo = intent.getStringExtra("track_tempo")
//        val currenttempo = curr

    val currenttempo: ArrayList<Int> = arrayListOf()  // this needs to be fixed

    fun clearLists() {
        downeventlist.clear()
        downeventlist.add(0)
        downeventlist.add(0)
        downeventlist.add(0)

        downeventlist_x.clear()
        downeventlist_x.add(0)
        downeventlist_x.add(0)
        downeventlist_x.add(0)
        downeventlist_y.clear()
        downeventlist_y.add(0)
        downeventlist_y.add(0)
        downeventlist_y.add(0)

        weightonlist.clear()
        weightonlist.add("0")
        weightonlist.add("0")
        weightonlist.add("0")

        eventlistid.clear()
        eventlistbeats.clear()
        eventlisttime.clear()
        eventlist.clear()
        routineeventlisttime.clear()
    }


    fun getInitialTempo(tempo: Int) {
        currenttempo.add(tempo)
    }

    fun manageElementScrolling(practicestarttime: Int, practiceelementlist: ArrayList<Elements>) {

        for (i in practiceelementlist.indices) {

            if (i > 1) {

                try {
                    val movetime = (practiceelementlist[i].elementtime!!.toInt() - practiceelementlist[1].elementtime!!.toInt()).toLong()
//                        d("Paul", "Update move things $i  $movetime")

//                        practiceelementsscrollview.smoothScrollBy(0, 200)
                    practiceroutineelementrecyclerview.scrollToPosition(i)

                    kotlin.run {
                        Handler().postDelayed({
//                                practiceroutineelementrecyclerview.scrollToPosition(i)
                            practiceroutineelementrecyclerview.smoothScrollToPosition(i)
                        }, movetime)
                    }

                } catch (exception: java.lang.Exception) {
//                    d("Paul", "playback exception")
                }
            }
        }
    }

    fun setPracticeAttemptElements(attemptelements: List<RoutineElement>, practiceelements: ArrayList<Elements>, currenttempo: String) {

        var attemptelementlist: ArrayList<Elements> = ArrayList<Elements>()

//    elementlist.add(Elements("Time", "Beat", "Move"))  // test

        for (i in attemptelements.indices) {
//            var elementtime = elements[i].elementtime.toString()
            var routineid = attemptelements[i].routineelementId.toString()
            var elementtime = attemptelements[i].elementtime.toString()
            var elementbeat = attemptelements[i].elementbeat.toString()
            var elementmove = attemptelements[i].routinedancemoveId.toString()
            var routineelementtime = attemptelements[i].routineelementtime.toString()
            var elementcomment = attemptelements[i].comment.toString()
            var weighton = attemptelements[i].weighton.toString()

            attemptelementlist.add(Elements(routineid, elementtime, elementbeat, elementmove, routineelementtime, elementcomment, weighton))

        }

        routineElements.removeObservers(this)

        danceactivityviewmodel.repository.allDanceMoves.observe(this,
            Observer { dancemove_id -> dancemove_id?.let { setAttemptDanceMoves(dancemove_id, attemptelementlist, practiceelements, currenttempo) } })

//            repository.allDanceMoves.observe(
//                viewLifecycleOwner,
//                Observer { dancemove_id -> dancemove_id?.let { setDanceMoves(dancemove_id, elementlist) } })

    }

    fun setAttemptDanceMoves(
        dancemoves: List<DanceMove>,
        attemptelementlist: ArrayList<Elements>,
        practiceelementmovelist: ArrayList<Elements>,
        currenttempo: String
    ) {

        var movelist: ArrayList<Moves> = ArrayList<Moves>()

        for (i in dancemoves.indices) {
            var dancemoveid = dancemoves[i].dancemoveId
            var dancemove = dancemoves[i].dancemove

            movelist.add(Moves(dancemoveid, dancemove))
        }

        var attemptelementmovelist: ArrayList<Elements> = ArrayList<Elements>()


        for (i in attemptelementlist.indices) {
            var routineid = attemptelementlist[i].routineid
            var elementtime = attemptelementlist[i].elementtime
            var elementbeat = attemptelementlist[i].elementbeat
            var dancemoveid = attemptelementlist[i].elementmove.toString().toInt()
            var dancemove = dancemoves[dancemoveid - 1].dancemove
            var routineelementtime = attemptelementlist[i].routineelementtime
            var elementcomment = attemptelementlist[i].comments
            var weighton = attemptelementlist[i].weighton

            attemptelementmovelist.add(Elements(routineid, elementtime, elementbeat, dancemove, routineelementtime, elementcomment, weighton))

        }

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.practiceattemptframelayout,
                PracticeAttemptElementFragment(attemptelementmovelist),
                PracticeAttemptElementFragment.TAG
            )
            .commit()


        danceactivityviewmodel.repository.allDanceMoves.removeObservers(this)

        var practiceelementlist: ArrayList<Elements> = ArrayList<Elements>()

        for (j in practiceelementmovelist.indices) {
            var routineid = practiceelementmovelist[j].routineid
            var elementtime = practiceelementmovelist[j].elementtime
            var elementbeat = practiceelementmovelist[j].elementbeat

            var dancemove = practiceelementmovelist[j].elementmove.toString()

            var routineelementtime = practiceelementmovelist[j].routineelementtime
            var elementcomment = practiceelementmovelist[j].comments
            var weighton = practiceelementmovelist[j].weighton


            for (k in dancemoves.indices) {
                if (dancemoves[k].dancemove == dancemove) {
                    var dancemoveid = dancemoves[k].dancemoveId.toString()
                    practiceelementlist.add(Elements(routineid, elementtime, elementbeat, dancemoveid, routineelementtime, elementcomment, weighton))
                }
            }

//            var dancemoveid = attemptelementlist[i].elementmove.toString().toInt()
//            var dancemove = dancemoves[dancemoveid - 1].dancemove


//            practiceelementlist.add(Elements(routineid, elementtime, elementbeat, dancemoveid, routineelementtime, elementcomment, weighton))
        }

        practiceAttemptScore(practiceelementlist, attemptelementlist, currenttempo)

    }

    fun practiceAttemptScore(practiceelementlist: ArrayList<Elements>, attemptelementlist: ArrayList<Elements>, currenttempo: String) {

        val tempo = currenttempo.toInt()
//        val tolerance = (60000.div(tempo)).div(2)
        val tolerance = (60000.div(tempo)).times(10).div(7)

        val practiceroutinesize = practiceelementlist.size - 1
        val attemptroutinesize = attemptelementlist.size - 1

        val matchingelements: ArrayList<Elements> = arrayListOf()
        val matchedelementtimes: ArrayList<Int> = arrayListOf(-1)

        val timescore: ArrayList<Int> = arrayListOf()
        val footscore: ArrayList<Int> = arrayListOf()
        val movescore: ArrayList<Int> = arrayListOf()

        val sortingscore: ArrayList<Int> = arrayListOf()

        val finalscores: ArrayList<Int> = arrayListOf()

//        d("Paul", "Practice Routine $practiceelementlist")
//        d("Paul", "Attempt Routine $attemptelementlist")

//        for (i in attemptelementlist.indices) {
        for (j in practiceelementlist.indices) {
            // things to score:  Timing, Feet, Move

            val matchingtimes: ArrayList<Int> = arrayListOf()
            val lastmatchedtime = matchedelementtimes[matchedelementtimes.size - 1]

            for (i in attemptelementlist.indices) {

                if (attemptelementlist[i].elementtime!!.toInt() > (practiceelementlist[j].elementtime!!.toInt() - tolerance)
                    && attemptelementlist[i].elementtime!!.toInt() < (practiceelementlist[j].elementtime!!.toInt() + tolerance)
                ) {
                    matchingelements.add(attemptelementlist[i])
//                            matchingelementindices.add(j.toString().toInt())
                    matchingtimes.add(practiceelementlist[j].elementtime!!.toInt())
//                            d("Paul", "Time Match practice index $matchedelementindices elements $matchingelements")
//                            d("Paul", "Time Match practice index $matchedelementtimes elements $matchingelements")
////                            d("Paul", "Time Match practice j = $j index $matchedelementindices")
//                            d("Paul", "Time Match tolerance $tolerance practice j = $j index $matchingtimes")

                }
            }

//            d("Paul", "Time Match practice time $matchedelementtimes")
////                            d("Paul", "Time Match practice j = $j index $matchedelementindices")
//            d("Paul", "Time Match tolerance $tolerance practice times $matchingtimes")


            for (k in matchingelements.indices) {

                val matchingscore = when {
                    matchingelements[k].elementtime!!.toInt() < lastmatchedtime -> "next"
                    matchingelements[k].elementmove == practiceelementlist[j].elementmove -> "samemove"
                    matchingelements[k].elementmove != practiceelementlist[j].elementmove && matchingelements[k].elementmove!!.toInt() % 2 == practiceelementlist[j].elementmove!!.toInt() % 2 -> "samefoot"

                    else -> "next"
                }

                if (matchingscore == "next") {
                    timescore.add(0)
                    footscore.add(0)
                    movescore.add(0)
                } else if (matchingscore == "samefoot") {
                    timescore.add(10)
                    footscore.add(25)
                    movescore.add(0)
                } else if (matchingscore == "samemove") {
                    timescore.add(10)
                    footscore.add(25)
                    movescore.add(100)
//                    matchedelementtimes.add(matchingelements[k].elementtime!!.toInt())
                }


                for (l in timescore.indices) {
                    sortingscore.add(timescore[l] + footscore[l] + movescore[l])
                }

                val highestscore = when {
                    sortingscore.size > 0 -> sortingscore.max()!!
                    else -> 0
                }

            }

//            d("Paul", "Sorting Scores $sortingscore highest score ${sortingscore.max()!!}")

            for (m in matchingelements.indices.sortedDescending()) {
                if (sortingscore.size > 1 && sortingscore[m] != sortingscore.max()!!) {
                    matchingelements.removeAt(m)
                }
            }

            if (matchingelements.size > 0) {
                matchedelementtimes.add(matchingelements[0].elementtime!!.toInt())

                finalscores.add(sortingscore.max()!!)

//                d("Paul", "Matched element ${matchingelements[0]}")
//                d("Paul", "Sorting Score $sortingscore")
            }

            routineattemptlabel.visibility = View.VISIBLE

            matchingelements.clear()
            timescore.clear()
            footscore.clear()
            movescore.clear()
            sortingscore.clear()
        }

        val numbereventsscore = abs(practiceelementlist.size - attemptelementlist.size) * -10

//        val finalscore = (finalscores.sum() + numbereventsscore)   //.div(practiceroutinesize*135).toFloat()
        val maxscore = (practiceelementlist.size * 135).toFloat()

        val finalscore = finalscores.sum().toFloat() - abs(practiceelementlist.size - attemptelementlist.size) * 10

        val percentscore = (finalscore.div(maxscore)) * 100

//        d("Paul", "Final score $finalscore max score $maxscore % = $percentscore  scores $finalscores")
//        matchingelements.clear()

        danceImage.visibility = View.INVISIBLE
        lastAction.text = "Results:"

        practiceresults.visibility = View.INVISIBLE
        showroutineelements.visibility = View.INVISIBLE
        startpractice.visibility = View.INVISIBLE

        savedroutinelabel.visibility = View.VISIBLE
        routineattemptlabel.visibility = View.VISIBLE

//        practiceframelayout.visibility = View.INVISIBLE
//        practiceattemptframelayout.visibility = View.INVISIBLE

        finalScoreLabel.visibility = View.VISIBLE
        finalScore.visibility = View.VISIBLE
        val finalscoretext = percentscore.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString() + "%"

        finalScore.text = finalscoretext
    }


    // ===============Working OnTouch Override ============
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val currenttime = event!!.eventTime
        val currentX = event.x
        val currentY = event.y

        if (event.action == MotionEvent.ACTION_DOWN) {

            val leftimageloc = IntArray(2)
            rightImage.getLocationOnScreen(leftimageloc)

            leftImage.getLocationOnScreen(leftimageloc)

            val leftimagelocx = leftimageloc[0]
            val leftimagelocy = leftimageloc[1]
            val leftimagemaxx = leftimagelocx + leftImage.width
            val leftimagemaxy = leftimagelocy + leftImage.height

            val rightimageloc = IntArray(2)
            rightImage.getLocationOnScreen(rightimageloc)
            val rightimagelocx = rightimageloc[0]
            val rightimagelocy = rightimageloc[1]
            val rightimagemaxx = rightimagelocx + rightImage.width
            val rightimagemaxy = rightimagelocy + rightImage.height


            val leftanim = ObjectAnimator.ofInt(leftImage, "backgroundColor", Color.WHITE, Color.RED, Color.WHITE)
            val rightanim = ObjectAnimator.ofInt(rightImage, "backgroundColor", Color.WHITE, Color.RED, Color.WHITE)


            if (currentX > leftimagelocx && currentX < leftimagemaxx && currentY > leftimagelocy && currentY < leftimagemaxy && leftImage.visibility == View.VISIBLE) {
                leftanim.duration = 200
                leftanim.setEvaluator(ArgbEvaluator())
                leftanim.repeatMode = ValueAnimator.REVERSE
                leftanim.repeatCount = Animation.ABSOLUTE
                leftanim.start()

//                downeventlist.add(currenttime.toInt())
//                downeventlist_x.add(currentX.toInt())
//                downeventlist_y.add(currentY.toInt())

            } else if (currentX > rightimagelocx && currentX < rightimagemaxx && currentY > rightimagelocy && currentY < rightimagemaxy && rightImage.visibility == View.VISIBLE) {
                rightanim.duration = 200
                rightanim.setEvaluator(ArgbEvaluator())
                rightanim.repeatMode = ValueAnimator.REVERSE
                rightanim.repeatCount = Animation.ABSOLUTE
                rightanim.start()

//                downeventlist.add(currenttime.toInt())
//                downeventlist_x.add(currentX.toInt())
//                downeventlist_y.add(currentY.toInt())

//            } else if (downeventlist.size == 2) {
            } else {

                d("Paul", "No foot")
//                downeventlist.add(0)
//                downeventlist_x.add(0)
//                downeventlist_y.add(0)
            }
        }
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            return super.onTouchEvent(event)
        }
    }

    // fling method from https://www.ssaurel.com/blog/implement-a-gesture-detector-in-android/

    /** Swipe min distance.  */
//        private val SWIPE_MIN_DISTANCE = 60
    private val SWIPE_MIN_DISTANCE_X = 30
    private val SWIPE_MIN_DISTANCE_Y = 30

    /** Swipe max off path.  */
//        private val SWIPE_MAX_OFF_PATH = 250
    private val SWIPE_MAX_OFF_PATH = 500

    /** Swipe threshold velocity.  */
//        private val SWIPE_THRESHOLD_VELOCITY = 100
    private val SWIPE_THRESHOLD_VELOCITY = 50


    /** Gesture detector. */

    override fun onShowPress(e: MotionEvent?) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            d("Paul", "Guesture ShowPress Listener")
//            true

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {

        val weightonlist = weightonlist
        val weightonlistlen = weightonlist.size
        val weighton = weightonlist[weightonlistlen - 1]
        val previousweighton = weightonlist[weightonlistlen - 2]

//        var downlen = downeventlist.size
//        val tapseparation = downeventlist[downlen - 1] - downeventlist[downlen - 2]  //  different from OnDown and OnFling as down event tap has been added!!
//        val previoustapseparation = downeventlist[downlen - 2] - downeventlist[downlen - 3]  //  different from OnDown and OnFling as down event tap has been added!!
//        val beattapseparation = tapseparation + previoustapseparation
//
//        downeventlist.removeAt(downlen - 1)
//        downeventlist_x.removeAt(downlen - 1)
//        downeventlist_y.removeAt(downlen -1)
//
//        downlen = downeventlist.size

//        if (downeventlist[downlen - 1] == downeventlist[downlen - 2]) {   // tap up counts as a touch event, remove from lists
//            downeventlist.removeAt(downlen - 1)
//            downeventlist_x.removeAt(downlen - 1)
//            downeventlist_y.removeAt(downlen -1)
//            downlen = downeventlist.size
//        }

        val leftimageloc = IntArray(2)
        leftImage.getLocationOnScreen(leftimageloc)
        val leftimagelocx = leftimageloc[0]
        val leftimagelocy = leftimageloc[1]
        val leftimagemaxx = leftimagelocx + leftImage.width
        val leftimagemaxy = leftimagelocy + leftImage.height

        val rightimageloc = IntArray(2)
        rightImage.getLocationOnScreen(rightimageloc)
        val rightimagelocx = rightimageloc[0]
        val rightimagelocy = rightimageloc[1]
        val rightimagemaxx = rightimagelocx + rightImage.width
        val rightimagemaxy = rightimagelocy + rightImage.height

        val currenttempolistsize = currenttempo.size
        val bpm = currenttempo[currenttempolistsize - 1]

//        currenttempo.size

        val pointerlocx = e!!.x
        val pointerlocy = e.y

        val whichfoot = when {
            pointerlocx > leftimagelocx && pointerlocx < leftimagemaxx && pointerlocy > leftimagelocy && pointerlocy < leftimagemaxy -> "Leftfoot"
            pointerlocx > rightimagelocx && pointerlocx < rightimagemaxx && pointerlocy > rightimagelocy && pointerlocy < rightimagemaxy -> "Rightfoot"
            else -> false
        }

        if (whichfoot != false) {

            val currenttime = e!!.eventTime
            val currentX = e.x
            val currentY = e.y

            downeventlist.add(currenttime.toInt())
            downeventlist_x.add(currentX.toInt())
            downeventlist_y.add(currentY.toInt())

        }

        val newdownlen = downeventlist.size

        val tapseparation =
            downeventlist[newdownlen - 1] - downeventlist[newdownlen - 2]  //  different from OnDown and OnFling as down event tap has been added!!
        val previoustapseparation =
            downeventlist[newdownlen - 2] - downeventlist[newdownlen - 3]  //  different from OnDown and OnFling as down event tap has been added!!
        val beattapseparation = tapseparation + previoustapseparation

//        val tapduration = e!!.eventTime.toInt() - downeventlist[newdownlen - 2]
        val tapduration = downeventlist[newdownlen - 1] - downeventlist[newdownlen - 2]
//        val tapuptime = e!!.eventTime
//        val tapduration = e!!.eventTime - downeventlist[downlen - 1]


        val tripletaptimeout = 60000 / bpm  // timeout time in ms   /* Length of two beats*/
        val doubletaptimeout = 60000 / bpm * 0.3333  // timeout time in ms    /* Longer than this is a single step*/

        val maxtapduration = 50

        val tappossible = when {

            tapduration < maxtapduration && tapseparation > tripletaptimeout -> "Single"   // works for kick and kick-crunch

            tapduration < maxtapduration && tapseparation > doubletaptimeout && previoustapseparation < doubletaptimeout && beattapseparation < tripletaptimeout -> "Step-tap"  //   && beattapseparation < tripletaptimeout  // this works for crunch
            tapduration < maxtapduration && tapseparation > doubletaptimeout && previoustapseparation > doubletaptimeout -> "Step-hop"  //  && beattapseparation < tripletaptimeout   //  works for kick and crunch

            tapduration < maxtapduration && tapseparation < doubletaptimeout && previoustapseparation < doubletaptimeout -> "Ball-step" // do we need < triple???   && beattapseparation < tripletaptimeout // this works for crunch
            tapduration < maxtapduration && tapseparation < doubletaptimeout && previoustapseparation > doubletaptimeout -> "Ball-tap"  //   && beattapseparation < tripletaptimeout // works for kick
            else -> false
        }

        val textanim = ObjectAnimator.ofInt(lastAction, "textColor", Color.WHITE, Color.RED, Color.GRAY)

        val numberevents = eventlist.size

        val lefttapif = when {
//                        whichfoot == "Leftfoot" && kickpossible == "Single" && weighton != "Left" -> "Leftkick"  //  Truth table version
            whichfoot == "Leftfoot" && tappossible == "Single" && weighton != "Right" && previousweighton != "Left" -> "Lefttap"
            whichfoot == "Leftfoot" && tappossible == "Single" && weighton != "Right" && previousweighton != "Right" -> "Lefttap"

            whichfoot == "Leftfoot" && tappossible == "Step-tap" && weighton != "Right" && previousweighton != "Left" -> "Lefttap"
//                        whichfoot == "Leftfoot" && tappossible == "Step-tap" && weighton != "Left" && previousweighton != "Right"-> "Leftkickcrunch"  // think not needed

            whichfoot == "Leftfoot" && tappossible == "Step-hop" && weighton != "Right" && previousweighton != "Right" -> "Lefttap"
            whichfoot == "Leftfoot" && tappossible == "Step-hop" && weighton != "Right" && previousweighton != "Left" -> "Lefttap"
//
//            // look at these after ball-change-steps done
//
            whichfoot == "Leftfoot" && tappossible == "Ball-step" && weighton != "Right" -> "Lefttap"
//            kickpossible == "Ball-step" && weighton != "Left" && previousweighton != "Left" -> "Leftkickcrunch"  // think this doesn't matter
            whichfoot == "Leftfoot" && tappossible == "Ball-step" && weighton != "Left" -> "Lefttap"

            whichfoot == "Leftfoot" && tappossible == "Ball-tap" && weighton != "Left" -> "Lefttap"
            whichfoot == "Leftfoot" && tappossible == "Ball-tap" && weighton != "Right" -> "Lefttap"

            else -> false
        }

        val righttapif = when {
//                        whichfoot == "Leftfoot" && kickpossible == "Single" && weighton != "Left" -> "Leftkick"  //  Truth table version
            whichfoot == "Rightfoot" && tappossible == "Single" && weighton != "Left" && previousweighton != "Right" -> "Righttap"
            whichfoot == "Rightfoot" && tappossible == "Single" && weighton != "Left" && previousweighton != "Left" -> "Righttap"

            whichfoot == "Rightfoot" && tappossible == "Step-tap" && weighton != "Left" && previousweighton != "Right" -> "Righttap"
//                        whichfoot == "Leftfoot" && kickpossible == "Step-tap" && weighton != "Left" && previousweighton != "Right"-> "Leftkickcrunch"  // think not needed

            whichfoot == "Rightfoot" && tappossible == "Step-hop" && weighton != "Left" && previousweighton != "Left" -> "Righttap"
            whichfoot == "Rightfoot" && tappossible == "Step-hop" && weighton != "Left" && previousweighton != "Right" -> "Righttap"
//
//            // look at these after ball-change-steps done
//
            whichfoot == "Rightfoot" && tappossible == "Ball-step" && weighton != "Left" -> "Righttap"
//            kickpossible == "BallRightfoot-step" && weighton != "Left" && previousweighton != "Left" -> "Leftkickcrunch"  // think this doesn't matter
            whichfoot == "Rightfoot" && tappossible == "Ball-step" && weighton != "Right" -> "Righttap"

            whichfoot == "Rightfoot" && tappossible == "Ball-tap" && weighton != "Right" -> "Righttap"
            whichfoot == "Rightfoot" && tappossible == "Ball-tap" && weighton != "Left" -> "Righttap"

            else -> false
        }

        if (lefttapif == "Lefttap") {

//            d("Paul Thisevent jazz", "Left tap weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")
//                    d("Paul Thisevent jazz", "Left kick weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")
//                      d("Paul", "Left kick $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")

            danceImage.setImageResource(R.drawable.lefttapimage)
            lastAction.text = "Left tap"

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

//            weightonlist.removeAt(downlen - 1) // clear previous weight
            weightonlist.removeAt(weightonlistlen - 1) // clear previous weight
            weightonlist.add("Right")
            eventlist.removeAt(numberevents - 1)
            eventlist.add("Left tap")

//            d("Paul Thisevent jazz", "Left tap $eventlisttime")

//                    d(" Left Fling Events", "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}")

            var danceelement = RoutineElement(
                0,
                1,
                5,
                eventlisttime[numberevents],
                eventlistbeats[numberevents - 1],
                routineeventlisttime[numberevents - 1],
                "None",
                "Right"
            )

//                    d("Paul L Tap", "L tap time ${eventlisttime[numberevents-1]} routine time ${routineeventlisttime[numberevents-1]}")

            danceactivityviewmodel =
                ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//            danceactivityviewmodel.allRoutineElements.observe(
//                    this,
//                    Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

            danceactivityviewmodel.insertElement(danceelement)


            return true

        }

        if (righttapif == "Righttap") {

//            d("Paul Thisevent jazz", "Right tap weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")
//                      d("Paul", "Left kick $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")

            danceImage.setImageResource(R.drawable.righttapimage)
            lastAction.text = "Right tap"

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            weightonlist.removeAt(weightonlistlen - 1 - 1) // clear stepto Left weight
            weightonlist.add("Left")
            eventlist.removeAt(numberevents - 1)
            eventlist.add("Right tap")

//                    d(" Left Fling Events", "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}")

            var danceelement = RoutineElement(
                0,
                1,
                6,
                eventlisttime[numberevents],
                eventlistbeats[numberevents - 1],
                routineeventlisttime[numberevents - 1],
                "None",
                "Left"
            )

//                    d("Paul L Kick", "R kick time ${eventlisttime[numberevents-1]} routine time ${routineeventlisttime[numberevents-1]}")

            danceactivityviewmodel =
                ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//            danceactivityviewmodel.allRoutineElements.observe(
//                    this,
//                    Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

            danceactivityviewmodel.insertElement(danceelement)


            return true

        }

        return false
    }


    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {

        val weightonlist = weightonlist
        val weightonlistlen = weightonlist.size
        val weighton = weightonlist[weightonlistlen - 1]
        val previousweighton = weightonlist[weightonlistlen - 2]

        val downlen = downeventlist.size
        val tapseparation = downeventlist[downlen - 1] - downeventlist[downlen - 2]
        val previoustapseparation = downeventlist[downlen - 2] - downeventlist[downlen - 3]
        val beattapseparation = tapseparation + previoustapseparation


        val leftimageloc = IntArray(2)
        leftImage.getLocationOnScreen(leftimageloc)
        val leftimagelocx = leftimageloc[0]
        val leftimagelocy = leftimageloc[1]
        val leftimagemaxx = leftimagelocx + leftImage.width
        val leftimagemaxy = leftimagelocy + leftImage.height

        val rightimageloc = IntArray(2)
        rightImage.getLocationOnScreen(rightimageloc)
        val rightimagelocx = rightimageloc[0]
        val rightimagelocy = rightimageloc[1]
        val rightimagemaxx = rightimagelocx + rightImage.width
        val rightimagemaxy = rightimagelocy + rightImage.height

        val currenttempolistsize = currenttempo.size
        val bpm = currenttempo[currenttempolistsize - 1]
//        currenttempo.size

        val tripletaptimeout = 60000 / bpm  // timeout time in ms   /* Length of two beats*/
        val doubletaptimeout = 60000 / bpm * 0.3333  // timeout time in ms    /* Longer than this is a single step*/

        // truth table for kick possible  //  add <=  as needed

        val kickpossible = when {
            tapseparation > tripletaptimeout -> "Single"   // works for kick and kick-crunch

            tapseparation > doubletaptimeout && previoustapseparation < doubletaptimeout && beattapseparation < tripletaptimeout -> "Step-tap"  //   && beattapseparation < tripletaptimeout  // this works for crunch
            tapseparation > doubletaptimeout && previoustapseparation > doubletaptimeout -> "Step-hop"  //  && beattapseparation < tripletaptimeout   //  works for kick and crunch

            tapseparation < doubletaptimeout && previoustapseparation < doubletaptimeout -> "Ball-step" // do we need < triple???   && beattapseparation < tripletaptimeout // this works for crunch
            tapseparation < doubletaptimeout && previoustapseparation > doubletaptimeout -> "Ball-tap"  //   && beattapseparation < tripletaptimeout // works for kick
            else -> false
        }

        // need to make variables for left and right kick here

        val pointerlocx = e1!!.x
        val pointerlocy = e1.y

        val whichfoot = when {
            leftImage.visibility == View.VISIBLE && pointerlocx > leftimagelocx && pointerlocx < leftimagemaxx && pointerlocy > leftimagelocy && pointerlocy < leftimagemaxy -> "Leftfoot"
            rightImage.visibility == View.VISIBLE && pointerlocx > rightimagelocx && pointerlocx < rightimagemaxx && pointerlocy > rightimagelocy && pointerlocy < rightimagemaxy -> "Rightfoot"
            else -> false
        }

        if (whichfoot != false) {

            val currenttime = e1!!.eventTime
            val currentX = e1.x
            val currentY = e1.y

            downeventlist.add(currenttime.toInt())
            downeventlist_x.add(currentX.toInt())
            downeventlist_y.add(currentY.toInt())
        }

        val textanim = ObjectAnimator.ofInt(lastAction, "textColor", Color.WHITE, Color.RED, Color.GRAY)

        try {
            if (Math.abs(e1.y - e2!!.y) > SWIPE_MAX_OFF_PATH || Math.abs(e1.x - e2.x) > SWIPE_MAX_OFF_PATH)
                return false

            if (e1.x - e2.x > SWIPE_MIN_DISTANCE_X && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // right to left swipe
                //                if (pointerlocx > leftimagelocx && pointerlocx < leftimagemaxx && pointerlocy > leftimagelocy && pointerlocy < leftimagemaxy) {

                //                    d(
                //                        "Paul",
                //                        "Swipe right to left on Left $pointerlocx $pointerlocy image x Whatever  max x $leftimagemaxx y $leftimagemaxy"
                //                    )
                ////                    return false
                //                }
                //                else if (pointerlocx > rightimagelocx && pointerlocx < rightimagemaxx && pointerlocy > rightimagelocy && pointerlocy < rightimagemaxy) {
                //                    d(
                //                        "Paul",
                //                        "Swipe right to left on Right $pointerlocx $pointerlocy image x Whatever max x $leftimagemaxx y $leftimagemaxy"
                //                    )
                //                }
                //                    return false
            }
            //            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE_X && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            //                // left to right swipe
            ////                    Toast.makeText(getApplicationContext(), "Swipe left to right", Toast.LENGTH_SHORT).show();
            //                d("Paul", "Guesture OnFling Listener Swipe left to right")
            //
            //            }
            else if (e1.y - e2.y > SWIPE_MIN_DISTANCE_Y && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                // bottom to top
                //                Toast.makeText(getApplicationContext(), "Swipe bottom to top", Toast.LENGTH_SHORT)
                //                    .show()
                //                d("Paul", "Guesture OnFling Listener Swipe bottom to top")

                //   Step is resolving first so weights on kicks are opposite to truth table

                val numberevents = eventlist.size

                val leftkickif = when {
//                        whichfoot == "Leftfoot" && kickpossible == "Single" && weighton != "Left" -> "Leftkick"  //  Truth table version
                    whichfoot == "Leftfoot" && kickpossible == "Single" && weighton != "Right" && previousweighton != "Left" -> "Leftkick"
                    whichfoot == "Leftfoot" && kickpossible == "Single" && weighton != "Right" && previousweighton != "Right" -> "Leftkickcrunch"

                    whichfoot == "Leftfoot" && kickpossible == "Step-tap" && weighton != "Right" && previousweighton != "Left" -> "Leftkick"
//                        whichfoot == "Leftfoot" && kickpossible == "Step-tap" && weighton != "Left" && previousweighton != "Right"-> "Leftkickcrunch"  // think not needed

                    whichfoot == "Leftfoot" && kickpossible == "Step-hop" && weighton != "Right" && previousweighton != "Right" -> "Leftkick"
                    whichfoot == "Leftfoot" && kickpossible == "Step-hop" && weighton != "Right" && previousweighton != "Left" -> "Leftkickcrunch"

                    // look at these after ball-change-steps done

                    whichfoot == "Leftfoot" && kickpossible == "Ball-step" && weighton != "Right" -> "Leftkick"
//            kickpossible == "Ball-step" && weighton != "Left" && previousweighton != "Left" -> "Leftkickcrunch"  // think this doesn't matter
                    whichfoot == "Leftfoot" && kickpossible == "Ball-step" && weighton != "Left" -> "Leftkickcrunch"

                    whichfoot == "Leftfoot" && kickpossible == "Ball-tap" && weighton != "Left" -> "Leftkickcrunch"
                    whichfoot == "Leftfoot" && kickpossible == "Ball-tap" && weighton != "Right" -> "Leftkickcrunch"

                    else -> false
                }

                val rightkickif = when {
                    whichfoot == "Rightfoot" && kickpossible == "Single" && weighton != "Left" && previousweighton != "Right" -> "Rightkick"
                    whichfoot == "Rightfoot" && kickpossible == "Single" && weighton != "Left" && previousweighton != "Left" -> "Rightkickcrunch"

                    whichfoot == "Rightfoot" && kickpossible == "Step-tap" && weighton != "Left" && previousweighton != "Right" -> "Rightkick"
//                        whichfoot == "Rightfoot" && kickpossible == "Step-tap" && weighton != "Right" && previousweighton != "Left" -> "Rightkickcrunch"  // think not needed

                    whichfoot == "Rightfoot" && kickpossible == "Step-hop" && weighton != "Left" && previousweighton != "Right" -> "Rightkick"
                    whichfoot == "Rightfoot" && kickpossible == "Step-hop" && weighton != "Right" && previousweighton != "Left" -> "Rightkickcrunch"

                    //

                    whichfoot == "Rightfoot" && kickpossible == "Ball-step" && weighton != "Left" && previousweighton != "Left" -> "Rightkick"
                    whichfoot == "Rightfoot" && kickpossible == "Ball-step" && weighton != "Right" && previousweighton != "Right" -> "Rightkickcrunch"

                    whichfoot == "Rightfoot" && kickpossible == "Ball-tap" && weighton != "Right" -> "Rightkickcrunch"
                    whichfoot == "Rightfoot" && kickpossible == "Ball-tap" && weighton != "Left" -> "Rightkickcrunch"
                    else -> false
                }

                // left or right kick or kickcrunch depending on above variables

                if (leftkickif == "Leftkick") {

//                        d("Paul Thisevent jazz", "Left kick weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")
//                      d("Paul", "Left kick $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")

                    danceImage.setImageResource(R.drawable.leftkickimage)
                    lastAction.text = "Left kick"

                    textanim.duration = 200
                    textanim.setEvaluator(ArgbEvaluator())
                    textanim.repeatMode = ValueAnimator.REVERSE
                    textanim.repeatCount = Animation.ABSOLUTE
                    textanim.start()

                    weightonlist.removeAt(downlen - 1) // clear stepto Left weight
                    weightonlist.add("Right")
                    eventlist.removeAt(numberevents - 1)
                    eventlist.add("Left kick")

//                        d(" Left Fling Events", "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}" )

                    val kicktime = when {
                        eventlisttime.size == 2 -> 0
                        else -> eventlisttime[eventlisttime.size - 1]
                    }

//                        var danceelement = RoutineElement(0, 1, 11, eventlisttime[numberevents - 1], eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Right")
                    var danceelement =
                        RoutineElement(0, 1, 11, kicktime, eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Right")

//                        d("Paul R Kick", "R kick time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                    danceactivityviewmodel =
                        ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//                        danceactivityviewmodel.allRoutineElements.observe(
//                            this,
//                            Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

                    danceactivityviewmodel.insertElement(danceelement)

                    return true
                }

                if (leftkickif == "Leftkickcrunch") {

//                        d("Paul", "Left kick-crunch $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")
//                        d("Paul Thisevent jazz","Left kickcrunch weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}" )

                    danceImage.setImageResource(R.drawable.leftkickcrunchimage)
                    lastAction.text = "Left kick-replace"

                    textanim.duration = 200
                    textanim.setEvaluator(ArgbEvaluator())
                    textanim.repeatMode = ValueAnimator.REVERSE
                    textanim.repeatCount = Animation.ABSOLUTE
                    textanim.start()

//                        downeventlist.add(currenttime)
                    weightonlist.removeAt(downlen - 1) // clear stepto Left weight
                    weightonlist.add("Right")
                    eventlist.removeAt(numberevents - 1)
                    eventlist.add("Left kick-replace")

//                        d(
//                            " Left Fling Events",
//                            "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}"
//                        )

                    val kicktime = when {
                        eventlisttime.size == 2 -> 0
                        else -> eventlisttime[eventlisttime.size - 1]
                    }

//                    var danceelement = RoutineElement(0, 1, 13, 1, 1,1,"None")
//                        var danceelement = RoutineElement(0, 1, 13, eventlisttime[numberevents - 1], eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Right")
                    var danceelement =
                        RoutineElement(0, 1, 13, kicktime, eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Right")

//                        d("Paul R Kick", "R kick time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                    danceactivityviewmodel =
                        ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//                        danceactivityviewmodel.allRoutineElements.observe(
//                            this,
//                            Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

                    danceactivityviewmodel.insertElement(danceelement)

                    return true
                }


                if (rightkickif == "Rightkick") {

//                    d("Paul Thisevent jazz", "Right kick weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")

                    danceImage.setImageResource(R.drawable.rightkickimage)
                    lastAction.text = "Right kick"

//                        downeventlist.add(currenttime)
                    weightonlist.removeAt(downlen - 1) // clear stepto Right weight
                    weightonlist.add("Left")
                    eventlist.removeAt(numberevents - 1)
                    eventlist.add("Right kick")

//                        d("Paul", "Right kick weightwason $previousweighton")
//                    d(" Right Fling Events", "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}")
//                    d(" Paul R Kick", "Right kick $eventlist  last event ${eventlist[numberevents-1]}")

//                    var danceelement = RoutineElement(0, 1, 12, 0, 0)
//                    d(" Paul R Kick", "Right kick $eventlist  event times $eventlisttime routine times $routineeventlisttime}")

                    val kicktime = when {
                        eventlisttime.size == 2 -> 0
                        else -> eventlisttime[eventlisttime.size - 1]
                    }

//                        var danceelement = RoutineElement(0, 1, 12, eventlisttime[numberevents - 1], eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Left"
                    var danceelement = RoutineElement(
                        0, 1, 12, kicktime, eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Left"
                    )


//                        d("Paul R Kick", "R kick time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                    danceactivityviewmodel =
                        ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//                        danceactivityviewmodel.allRoutineElements.observe(
//                            this,
//                            Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

                    danceactivityviewmodel.insertElement(danceelement)

                    return true
                }

                if (rightkickif == "Rightkickcrunch") {

//                    d("Paul Thisevent jazz", "Right kickcrunch weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")

                    danceImage.setImageResource(R.drawable.rightkickcrunchimage)
                    lastAction.text = "Right kick-replace"

//                        downeventlist.add(currenttime)
                    weightonlist.removeAt(downlen - 1) // clear step to right weight
                    weightonlist.add("Left")

                    eventlist.removeAt(numberevents - 1)
                    eventlist.add("Right kick-replace")

                    val kicktime = when {
                        eventlisttime.size == 2 -> 0
                        else -> eventlisttime[eventlisttime.size - 1]
                    }

//                        d("Paul", "Right kickcrunch weightwason $previousweighton")

//                        d(" Right Fling Events", "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}" )

//                        var danceelement = RoutineElement(0, 1, 14, eventlisttime[numberevents - 1], eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Left"
                    var danceelement = RoutineElement(
                        0, 1, 14, kicktime, eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Left"
                    )

//                        d("Paul R Kick", "R kick time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                    danceactivityviewmodel =
                        ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//                        danceactivityviewmodel.allRoutineElements.observe(
//                            this,
//                            Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

                    danceactivityviewmodel.insertElement(danceelement)

                    return true
                }

//                    d(
//                        "Invalid kick",
//                        "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}"
//                    )

            }
//            else if (e2.y - e1.y > SWIPE_MIN_DISTANCE_Y && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//                // top to bottom
//                //                    Toast.makeText(getApplicationContext(), "Swipe top to bottom", Toast.LENGTH_SHORT).show();
////                d("Paul", "Guesture OnFling Listener Swipe top to bottom")
//            }

        } catch (exception: Exception) {
            //                // nothing
        }
//            }

//        }

//        }
        return false

    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        val currenttime = e1!!.eventTime
        val currentX = e1.x
        val currentY = e1.y

        var downlen = downeventlist.size

        try {
            if (downeventlist[downlen - 1] == downeventlist[downlen - 2]) {   // think most touches count as a scroll, prevent double addition from small movement
                downeventlist.removeAt(downlen - 1)
                downeventlist_x.removeAt(downlen - 1)
                downeventlist_y.removeAt(downlen - 1)
                downlen = downeventlist.size
            }
        } catch (exception: Exception) {
        }

        val weightonlist = weightonlist
        val weightonlistlen = weightonlist.size
        val weighton = weightonlist[weightonlistlen - 1]
        val previousweighton = weightonlist[weightonlistlen - 2]

        val leftimageloc = IntArray(2)
        leftImage.getLocationOnScreen(leftimageloc)
        val leftimagelocx = leftimageloc[0]
        val leftimagelocy = leftimageloc[1]
        val leftimagemaxx = leftimagelocx + leftImage.width
        val leftimagemaxy = leftimagelocy + leftImage.height

        val rightimageloc = IntArray(2)
        rightImage.getLocationOnScreen(rightimageloc)
        val rightimagelocx = rightimageloc[0]
        val rightimagelocy = rightimageloc[1]
        val rightimagemaxx = rightimagelocx + rightImage.width
        val rightimagemaxy = rightimagelocy + rightImage.height

        val currenttempolistsize = currenttempo.size
        val bpm = currenttempo[currenttempolistsize - 1]
        currenttempo.size

        val tripletaptimeout = 60000 / bpm  // timeout time in ms   /* Length of two beats*/
        val doubletaptimeout = 60000 / bpm * 0.3333  // timeout time in ms    /* Longer than this is a single step*/

        val pointerlocx = e1.x
        val pointerlocy = e1.y

        val whichfoot = when {
            pointerlocx > leftimagelocx && pointerlocx < leftimagemaxx && pointerlocy > leftimagelocy && pointerlocy < leftimagemaxy -> "Leftfoot"
            pointerlocx > rightimagelocx && pointerlocx < rightimagemaxx && pointerlocy > rightimagelocy && pointerlocy < rightimagemaxy -> "Rightfoot"
            else -> false
        }

//        val textanim = ObjectAnimator.ofInt(lastAction, "textColor", Color.WHITE, Color.RED, Color.GRAY)

//        val SCROLL_MIN_DISTANCE_X = 80
        val SCROLL_MIN_DISTANCE_X = (leftimagemaxx - leftimagelocx).times(6).div(10)
        val SCROLL_MIN_DISTANCE_Y = 80

        /** Swipe max off path.  */
//        private val SWIPE_MAX_OFF_PATH = 250
        val SCROLL_MAX_OFF_PATH = 500

        /** Swipe threshold velocity.  */
        val SCROLL_THRESHOLD_VELOCITY = 10

        try {
            if (Math.abs(e1.y - e2!!.y) > SCROLL_MAX_OFF_PATH || Math.abs(e1.x - e2.x) > SCROLL_MAX_OFF_PATH)
                return false

            if (e1!!.x - e2!!.x > SCROLL_MIN_DISTANCE_X && Math.abs(distanceX) > SCROLL_THRESHOLD_VELOCITY) {
                // right to left swipe

                val tapseparation = downeventlist[downlen - 1] - downeventlist[downlen - 2]
                val previoustapseparation = downeventlist[downlen - 2] - downeventlist[downlen - 3]
                val beattapseparation = tapseparation + previoustapseparation

                val scrollpossible = when {
                    tapseparation > tripletaptimeout -> "Single"   // works for kick and kick-crunch

                    tapseparation > doubletaptimeout && previoustapseparation < doubletaptimeout && beattapseparation < tripletaptimeout -> "Step-tap"  //   && beattapseparation < tripletaptimeout  // this works for crunch
                    tapseparation > doubletaptimeout && previoustapseparation > doubletaptimeout -> "Step-hop"  //  && beattapseparation < tripletaptimeout   //  works for kick and crunch

                    tapseparation < doubletaptimeout && previoustapseparation < doubletaptimeout -> "Ball-step" // do we need < triple???   && beattapseparation < tripletaptimeout // this works for crunch
                    tapseparation < doubletaptimeout && previoustapseparation > doubletaptimeout -> "Ball-tap"  //   && beattapseparation < tripletaptimeout // works for kick
                    else -> false
                }

                val numberevents = eventlist.size

//                d("Paul", "Right left scroll foot $whichfoot scroll $scrollpossible weighton $weighton tap $tapseparation")
//                d("Paul", "Right left scroll $downeventlist")

                val leftSuzieQif = when {
                    whichfoot == "Leftfoot" && scrollpossible == "Single" && weighton != "Right" -> "LeftSuzieQ"  //  Truth table version
                    whichfoot == "Leftfoot" && scrollpossible == "Step-tap" && weighton != "Right" -> "LeftSuzieQ"
                    whichfoot == "Leftfoot" && scrollpossible == "Step-hop" && weighton != "Right" -> "LeftSuzieQ"

                    // look at these after ball-change-steps done

                    whichfoot == "Leftfoot" && scrollpossible == "Ball-step" && weighton != "Right" -> "LeftSuzieQ"
                    whichfoot == "Leftfoot" && scrollpossible == "Ball-tap" && weighton != "Right" -> "LeftSuzieQ"

                    else -> false
                }

//                d("Paul", "Susie Q  $leftSuzieQif")

                if (leftSuzieQif == "LeftSuzieQ") {

//                    d("Paul Thisevent jazz", "Right kick weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")

                    danceImage.setImageResource(R.drawable.leftsuzieimage)
                    lastAction.text = "Left Suzie Q"


                    weightonlist.removeAt(downlen - 1) // clear stepto Right weight
                    weightonlist.add("Right")
                    eventlist.removeAt(numberevents - 1)
                    eventlist.add("Left SuzieQ")


//                        d("Paul", "Left Suzie Q weightwason $previousweighton")
//                    d(" Paul R Kick", "Right kick $eventlist  event times $eventlisttime routine times $routineeventlisttime}")

                    val suzieqtime = when {
                        eventlisttime.size == 2 -> 0
                        else -> eventlisttime[eventlisttime.size - 1]
                    }

//                    d("Paul L Suzie", "L Suzie time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")


//                    var danceelement = RoutineElement(0, 1, 15, eventlisttime[numberevents - 1], eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Right")
                    var danceelement = RoutineElement(
                        0,
                        1,
                        15,
                        suzieqtime,
                        eventlistbeats[numberevents - 1],
                        routineeventlisttime[numberevents - 1],
                        "None",
                        "Right"
                    )

//                    d("Paul L Suzie", "L Suzie time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                    danceactivityviewmodel =
                        ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//                    danceactivityviewmodel.allRoutineElements.observe(
//                        this, Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

                    danceactivityviewmodel.insertElement(danceelement)

                    return true
                }

            }

            if (e2!!.x - e1!!.x > SCROLL_MIN_DISTANCE_X && Math.abs(distanceX) > SCROLL_THRESHOLD_VELOCITY) {
                // left to right swipe

                val tapseparation = downeventlist[downlen - 1] - downeventlist[downlen - 2]
                val previoustapseparation = downeventlist[downlen - 2] - downeventlist[downlen - 3]
                val beattapseparation = tapseparation + previoustapseparation

                val scrollpossible = when {
                    tapseparation > tripletaptimeout -> "Single"   // works for kick and kick-crunch

                    tapseparation > doubletaptimeout && previoustapseparation < doubletaptimeout && beattapseparation < tripletaptimeout -> "Step-tap"  //   && beattapseparation < tripletaptimeout  // this works for crunch
                    tapseparation > doubletaptimeout && previoustapseparation > doubletaptimeout -> "Step-hop"  //  && beattapseparation < tripletaptimeout   //  works for kick and crunch

                    tapseparation < doubletaptimeout && previoustapseparation < doubletaptimeout -> "Ball-step" // do we need < triple???   && beattapseparation < tripletaptimeout // this works for crunch
                    tapseparation < doubletaptimeout && previoustapseparation > doubletaptimeout -> "Ball-tap"  //   && beattapseparation < tripletaptimeout // works for kick
                    else -> false
                }

                val numberevents = eventlist.size

//                d("Paul", "Left right scroll foot $whichfoot scroll $scrollpossible weighton $weighton tap $tapseparation")
//                d("Paul", "Right left scroll $downeventlist")

                val rightSuzieQif = when {
                    whichfoot == "Rightfoot" && scrollpossible == "Single" && weighton != "Left" -> "RightSuzieQ"  //  Truth table version
                    whichfoot == "Rightfoot" && scrollpossible == "Step-tap" && weighton != "Left" -> "RightSuzieQ"
                    whichfoot == "Rightfoot" && scrollpossible == "Step-hop" && weighton != "Left" -> "RightSuzieQ"

                    // look at these after ball-change-steps done

                    whichfoot == "Rightfoot" && scrollpossible == "Ball-step" && weighton != "Left" -> "RightSuzieQ"
                    whichfoot == "Rightfoot" && scrollpossible == "Ball-tap" && weighton != "Left" -> "RightSuzieQ"

                    else -> false
                }

//                d("Paul", "Susie Q  $rightSuzieQif")

                if (rightSuzieQif == "RightSuzieQ") {

//                    d("Paul Thisevent jazz", "Right kick weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")

                    danceImage.setImageResource(R.drawable.rightsuzieimage)
                    lastAction.text = "Right Suzie Q"

                    weightonlist.removeAt(downlen - 1) // clear stepto Right weight
                    weightonlist.add("Left")
                    eventlist.removeAt(numberevents - 1)
                    eventlist.add("Left SuzieQ")

//                        d("Paul", "Left Suzie Q weightwason $previousweighton")
//                    d(" Paul R Kick", "Right kick $eventlist  event times $eventlisttime routine times $routineeventlisttime}")

//                    d("Paul R Suzie", "R Suzie time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                    val suzieqtime = when {
                        eventlisttime.size == 2 -> 0
                        else -> eventlisttime[eventlisttime.size - 1]
                    }

//                    var danceelement = RoutineElement(0, 1, 16, eventlisttime[numberevents - 1], eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Left")
                    var danceelement =
                        RoutineElement(0, 1, 16, suzieqtime, eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Left")

//                  d("Paul L Suzie", "L Suzie time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                    danceactivityviewmodel =
                        ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//                    danceactivityviewmodel.allRoutineElements.observe(
//                        this, Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

                    danceactivityviewmodel.insertElement(danceelement)


                    return true
                }

            }

        } catch (exception: Exception) {

        }
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {

//        d("Paul", "tempo list $currenttempo size $currenttempolistsize bpm $bpm beat timeout $tripletaptimeout doubletap $doubletaptimeout")

        val currenttempolistsize = currenttempo.size
        val bpm = currenttempo[currenttempolistsize - 1]  // this needs updating
//            currenttempo.size

        val tripletaptimeout = 60000 / bpm  // timeout time in ms   /* Length of two beats* - test value 600ms/
        val doubletaptimeout = 60000 / bpm * 0.3333  // timeout time in ms    /* Longer than this is a single step* -- test value 300ms/

//        d("Paul", "tempo list $currenttempo size ${currenttempolistsize} bpm $bpm timeout $tripletaptimeout doubletap $doubletaptimeout")

        val weightonlist = weightonlist
        val weightonlistlen = weightonlist.size
        val weighton = weightonlist[weightonlistlen - 1]
        val previousweighton = weightonlist[weightonlistlen - 2]

        val pointerlocx = e!!.x
        val pointerlocy = e.y

        val leftimageloc = IntArray(2)
        leftImage.getLocationOnScreen(leftimageloc)
        val leftimagelocx = leftimageloc[0]
        val leftimagelocy = leftimageloc[1]
        val leftimagemaxx = leftimagelocx + leftImage.width
        val leftimagemaxy = leftimagelocy + leftImage.height

        val rightimageloc = IntArray(2)
        rightImage.getLocationOnScreen(rightimageloc)
        val rightimagelocx = rightimageloc[0]
        val rightimagelocy = rightimageloc[1]
        val rightimagemaxx = rightimagelocx + rightImage.width
        val rightimagemaxy = rightimagelocy + rightImage.height

        val whichfoot = when {
            leftImage.visibility == View.VISIBLE && pointerlocx > leftimagelocx && pointerlocx < leftimagemaxx && pointerlocy > leftimagelocy && pointerlocy < leftimagemaxy -> "Leftfoot"
            rightImage.visibility == View.VISIBLE && pointerlocx > rightimagelocx && pointerlocx < rightimagemaxx && pointerlocy > rightimagelocy && pointerlocy < rightimagemaxy -> "Rightfoot"
            else -> false
        }

        if (whichfoot != false) {

            val currenttime = e.eventTime
            val currentX = e.x
            val currentY = e.y

            downeventlist.add(currenttime.toInt())
            downeventlist_x.add(currentX.toInt())
            downeventlist_y.add(currentY.toInt())
        }

        val downlen = downeventlist.size

        d("Paul bpm", "down event list $downeventlist")

        val tapseparation = downeventlist[downlen - 1] - downeventlist[downlen - 2]
        val previoustapseparation = downeventlist[downlen - 2] - downeventlist[downlen - 3]
        val beattapseparation = tapseparation + previoustapseparation

        val lastdownx = downeventlist_x[downlen - 2]
        val lastdowny = downeventlist_y[downlen - 2]

        val secondlastdownx = downeventlist_x[downlen - 3]
        val secondlastdowny = downeventlist_y[downlen - 3]

        val lastfoot = when {
            lastdownx > leftimagelocx && lastdownx < leftimagemaxx && lastdowny > leftimagelocy && lastdowny < leftimagemaxy -> "Leftfoot"
            lastdownx > rightimagelocx && lastdownx < rightimagemaxx && lastdowny > rightimagelocy && lastdowny < rightimagemaxy -> "Rightfoot"
            else -> false
        }

        val secondlastfoot = when {
            secondlastdownx > leftimagelocx && secondlastdownx < leftimagemaxx && secondlastdowny > leftimagelocy && secondlastdowny < leftimagemaxy -> "Leftfoot"
            secondlastdownx > rightimagelocx && secondlastdownx < rightimagemaxx && secondlastdowny > rightimagelocy && secondlastdowny < rightimagemaxy -> "Rightfoot"
            else -> false
        }

        // Step types

        val stepif = when {
            tapseparation > tripletaptimeout -> "Single"  // step
            tapseparation < 0 -> "Single"  // step - first in routine
            tapseparation > doubletaptimeout && previoustapseparation > doubletaptimeout -> "Step-hop" // step step
            tapseparation > doubletaptimeout && previoustapseparation < doubletaptimeout && beattapseparation > tripletaptimeout -> "Step-hop"  // ball-change step
//            tapseparation > doubletaptimeout && previoustapseparation < tripletaptimeout -> true
//            tapseparation > doubletaptimeout && tapseparation < tripletaptimeout && beattapseparation > doubletaptimeout -> true
            else -> false
        }

        val ballchangestepif = when {
            tapseparation > doubletaptimeout && previoustapseparation < doubletaptimeout && beattapseparation < tripletaptimeout -> "Change-step" // may resolve different than step-hop above as this is within two beats - default to steps and hops

//            tapseparation > doubletaptimeout && previoustapseparation < doubletaptimeout && beattapseparation > tripletaptimeout -> "Step-hop"  // ball-change step
//            tapseparation > doubletaptimeout && previoustapseparation < tripletaptimeout -> true
//            tapseparation > doubletaptimeout && tapseparation < tripletaptimeout && beattapseparation > doubletaptimeout -> true
            else -> false
        }

        val ballchangetapif = when {
            tapseparation < doubletaptimeout && beattapseparation > doubletaptimeout && previoustapseparation > doubletaptimeout -> "stepballchange"  // changed beat separation to > double from > triple
            tapseparation < doubletaptimeout && previoustapseparation < doubletaptimeout -> "stepballchange"  // put this back in
            tapseparation < doubletaptimeout && beattapseparation > tripletaptimeout && previoustapseparation > doubletaptimeout -> "stepballchange"

            tapseparation < doubletaptimeout && beattapseparation > doubletaptimeout && beattapseparation < tripletaptimeout && previoustapseparation > doubletaptimeout -> "ballchangetap"

//            beattapseparation > doubletaptimeout && beattapseparation < tripletaptimeout && weighton == "0" -> true
//            tapseparation < doubletaptimeout && previoustapseparation > tripletaptimeout -> true  // maybe not needed?
//            tapseparation < doubletaptimeout && beattapseparation < tripletaptimeout && previoustapseparation < doubletaptimeout -> true  // maybe not needed?
            else -> false
        }

        val solvestepballchange = when {
            ballchangetapif == "stepballchange" && weighton != "Right" && lastfoot == "Leftfoot" && whichfoot == "Rightfoot" -> "righttapball1"
            ballchangetapif == "stepballchange" && weighton != "Left" && lastfoot == "Rightfoot" && whichfoot == "Lefttfoot" -> "lefttapball1"

            ballchangetapif == "stepballchange" && previousweighton == "Right" && weighton != "Right" && lastfoot == "Rightfoot" && whichfoot == "Rightfoot" -> "righttapball2"
            ballchangetapif == "stepballchange" && previousweighton == "Left" && weighton != "Left" && lastfoot == "Leftfoot" && whichfoot == "Leftfoot" -> "lefttapball2" // may resolve to left toe-tap if onUp method used

            // may resolve to left toe-tap if onUp method used
            ballchangetapif == "stepballchange" && previousweighton == "Left" && weighton != "Right" && lastfoot == "Leftfoot" && whichfoot == "Rightfoot" -> "righttapball3"
            ballchangetapif == "stepballchange" && previousweighton == "Right" && weighton != "Left" && lastfoot == "Rightfoot" && whichfoot == "Leftfoot" -> "lefttapball3"
            else -> false
        }

        // Steps resolver

        val whichstep = when {
            stepif == "Single" && weighton != "Left" && whichfoot == "Leftfoot" -> "leftstep"  // && previousweighton != "Left"
            stepif == "Single" && weighton != "Right" && whichfoot == "Rightfoot" -> "rightstep"  // && previousweighton != "Right"

            stepif == "Single" && weighton == "Left" && whichfoot == "Leftfoot" -> "lefthop"  //  && previousweighton =="Left"
            stepif == "Single" && weighton == "Right" && whichfoot == "Rightfoot" -> "righthop"   // && previousweighton =="Right"

            stepif == "Step-hop" && previousweighton != "Left" && weighton == "Right" && whichfoot == "Leftfoot" -> "leftstep"
            stepif == "Step-hop" && previousweighton != "Right" && weighton == "Right" && whichfoot == "Leftfoot" -> "leftstep"  // this is in to resolve taps better

            stepif == "Step-hop" && previousweighton != "Right" && weighton == "Left" && whichfoot == "Rightfoot" -> "rightstep"
            stepif == "Step-hop" && previousweighton != "Left" && weighton == "Left" && whichfoot == "Rightfoot" -> "rightstep"  // this is in to resolve taps better
            stepif == "Step-hop" && previousweighton == "Left" && lastfoot == "Rightfoot " && weighton == "Left" && whichfoot == "Rightfoot" -> "rightstep"  // this is also in to resolve taps better

            stepif == "Step-hop" && previousweighton == "Left" && weighton == "Left" && whichfoot == "Leftfoot" -> "lefthop" //
            stepif == "Step-hop" && previousweighton == "Right" && lastfoot == "Rightfoot" && weighton == "Left" && whichfoot == "Leftfoot" -> "lefthop" // for hop after right kickcrunch
            stepif == "Step-hop" && previousweighton == "Right" && lastfoot == "Leftfoot" && weighton == "Left" && whichfoot == "Leftfoot" -> "lefthop" // toetap - step then this hop
            stepif == "Step-hop" && previousweighton == "Left" && weighton == "0" && whichfoot == "Leftfoot" -> "lefthop" //

            stepif == "Step-hop" && previousweighton == "Right" && weighton == "Right" && whichfoot == "Rightfoot" -> "righthop"  // step prior to hop
            stepif == "Step-hop" && previousweighton == "Left" && lastfoot == "Leftfoot" && weighton == "Right" && whichfoot == "Rightfoot" -> "righthop" // kickcrunch then this hop
            stepif == "Step-hop" && previousweighton == "Left" && lastfoot == "Rightfoot" && weighton == "Right" && whichfoot == "Rightfoot" -> "righthop" // toetap - step then this hop
            stepif == "Step-hop" && previousweighton == "Right" && weighton == "0" && whichfoot == "Rightfoot" -> "righthop" //

            stepif == "Step-hop" && lastfoot == "Leftfoot" && weighton == "Right" && whichfoot == "Leftfoot" -> "leftstep" // Left kickcrunch -> tap  //  *removed - revert to step - doesn't work
            stepif == "Step-hop" && lastfoot == "Rightfoot" && weighton == "Left" && whichfoot == "Rightfoot" -> "rightstep" // Left kickcrunch -> tap  //  *removed - revert to step - doesn't work

            ballchangetapif == "stepballchange" && previousweighton == "Left" && weighton == "Left" && whichfoot == "Leftfoot" -> "lefthop" // need previous weight because leftdown resolves in step
            ballchangetapif == "stepballchange" && previousweighton == "Right" && weighton == "Right" && whichfoot == "Rightfoot" -> "righthop"

            ballchangetapif == "stepballchange" && previousweighton == "Right" && weighton != "Right" && lastfoot == "Leftfoot" && whichfoot == "Rightfoot" -> "ballchangetoright" // may resolve to left toe-tap if onUp method used
            ballchangetapif == "stepballchange" && previousweighton == "Left" && weighton != "Left" && lastfoot == "Rightfoot" && whichfoot == "Leftfoot" -> "ballchangetoleft" // may resolve to left toe-tap if onUp method used

            ballchangetapif == "stepballchange" && weighton != "Right" && lastfoot == "Leftfoot" && whichfoot == "Leftfoot" -> "lefttoetap"  // specify previous weight as otherwise catches right tap???  && previousweighton =="Right"
            ballchangetapif == "stepballchange" && previousweighton == "Right" && secondlastfoot == "Left" && weighton != "Right" && lastfoot == "Leftfoot" && whichfoot == "Leftfoot" -> "lefttoetap" // may resolve to left toe-tap if onUp method used
            ballchangetapif == "stepballchange" && previousweighton == "Right" && secondlastfoot == "Right" && weighton != "Right" && lastfoot == "Leftfoot" && whichfoot == "Leftfoot" -> "lefthop" // step right b-c to left then this

            ballchangetapif == "stepballchange" && weighton != "Left" && lastfoot == "Rightfoot" && whichfoot == "Rightfoot" -> "righttoetap" // as above?  && previousweighton == "Left"
            ballchangetapif == "stepballchange" && previousweighton == "Left" && secondlastfoot == "Right" && weighton != "Left" && lastfoot == "Rightfoot" && whichfoot == "Rightfoot" -> "righttoetap" // may resolve to left toe-tap if onUp method used
            ballchangetapif == "stepballchange" && previousweighton == "Left" && secondlastfoot == "Left" && weighton != "Left" && lastfoot == "Rightfoot" && whichfoot == "Rightfoot" -> "righthop" // step left b-c to right then this

            // tapball resolver outputs

            solvestepballchange == "righttapball1" && previousweighton == "Right" && secondlastfoot == "Rightfoot" && weighton != "Right" && lastfoot == "Rightfoot" && whichfoot == "Rightfoot" -> "ballchangetoright"
            solvestepballchange == "righttapball1" && previousweighton == "Right" && secondlastfoot == "Leftfoot" && weighton != "Right" && lastfoot == "Rightfoot" && whichfoot == "Rightfoot" -> "righttoetap"

            solvestepballchange == "lefttapball1" && previousweighton == "Left" && secondlastfoot == "Leftfoot" && weighton != "Left" && lastfoot == "Leftfoot" && whichfoot == "Leftfoot" -> "ballchangetoleft"
            solvestepballchange == "lefttapball1" && previousweighton == "Left" && secondlastfoot == "Rightfoot" && weighton != "Left" && lastfoot == "Leftfoot" && whichfoot == "Leftfoot" -> "lefttoetap"

            solvestepballchange == "righttapball2" && secondlastfoot == "Rightfoot" && lastfoot == "Leftfoot" && whichfoot == "Rightfoot" -> "ballchangetoright"   // eg b-c to right b-c to left
            solvestepballchange == "righttapball2" && secondlastfoot == "Leftfoot" && lastfoot == "Leftfoot" && whichfoot == "Rightfoot" -> "righttap"   // eg left R kickcrunch tap
            solvestepballchange == "righttapball2" && secondlastfoot == "Leftfoot" && lastfoot == "Rightfoot" && whichfoot == "Rightfoot" -> "righttap"   // eg L kick R kickcrunch tap
            solvestepballchange == "righttapball2" && secondlastfoot == "Rightfoot" && lastfoot == "Rightfoot" && whichfoot == "Rightfoot" -> "righttap"   // button mashing

            solvestepballchange == "lefttapball2" && secondlastfoot == "Leftfoot" && lastfoot == "Rightfoot" && whichfoot == "Leftfoot" -> "ballchangetoleft"   // eg b-c to right b-c to left
            solvestepballchange == "lefttapball2" && secondlastfoot == "Rightfoot" && lastfoot == "Rightfoot" && whichfoot == "Leftfoot" -> "lefttap"   // eg right L kickcrunch tap
            solvestepballchange == "lefttapball2" && secondlastfoot == "Rightfoot" && lastfoot == "Leftfoot" && whichfoot == "Leftfoot" -> "lefttap"   // eg right kick  L kickcrunch tap
            solvestepballchange == "lefttapball2" && secondlastfoot == "Leftfoot" && lastfoot == "Leftfoot" && whichfoot == "Leftfoot" -> "lefttap"   // button mashing

            solvestepballchange == "righttapball3" && secondlastfoot == "Rightfoot" && lastfoot == "Leftfoot" && whichfoot == "Rightfoot" -> "ballchangetoright"   //
            solvestepballchange == "righttapball3" && secondlastfoot == "Leftfoot" && lastfoot == "Leftfoot" && whichfoot == "Rightfoot" -> "righthop"   //
            ballchangetapif == "stepballchange" && previousweighton == "Left" && lastfoot == "Leftfoot" && whichfoot == "Rightfoot" -> "righthop"   //

            solvestepballchange == "lefttapball3" && secondlastfoot == "Leftfoot" && lastfoot == "Rightfoot" && whichfoot == "Leftfoot" -> "ballchangetoleft"   //
            solvestepballchange == "lefttapball3" && secondlastfoot == "Rightfoot" && lastfoot == "Rightfoot" && whichfoot == "Leftfoot" -> "lefthop"   //
            ballchangetapif == "stepballchange" && previousweighton == "Right" && lastfoot == "Rightfoot" && whichfoot == "Leftfoot" -> "lefthop"   //

            // ball-change step outputs

            ballchangestepif == "Change-step" && weighton != "Left" && whichfoot == "Leftfoot" -> "leftstep"  // && previousweighton != "Left"
            ballchangestepif == "Change-step" && weighton != "Right" && whichfoot == "Rightfoot" -> "rightstep"  // && previousweighton != "Right"

            ballchangestepif == "Change-step" && weighton == "Left" && whichfoot == "Leftfoot" -> "lefthop"  //  && previousweighton =="Left"
            ballchangestepif == "Change-step" && previousweighton == "Right" && lastfoot == "Rightfoot" && weighton == "Left" && whichfoot == "Leftfoot" -> "lefthop" // kickcrunch then this hop

            ballchangestepif == "Change-step" && weighton == "Right" && whichfoot == "Rightfoot" -> "righthop"   // && previousweighton =="Right"
            ballchangestepif == "Change-step" && previousweighton == "Left" && lastfoot == "Leftfoot" && weighton == "Right" && whichfoot == "Rightfoot" -> "righthop" //  kickcrunch then this hop

            else -> false
        }

        val currenttime = downeventlist[downlen - 1]  // Kotlin index starts at sero

//            if (whichfoot != false && leftImage.visibility == View.VISIBLE && rightImage.visibility == View.VISIBLE) {

//            if (whichfoot != false) {
//                downeventlist.removeAt(downlen - 1)
//                downeventlist_x.removeAt(downlen - 1)
//                downeventlist_y.removeAt(downlen - 1)
//            }

//        d("Thisevent", "Foot: $whichfoot lastfoot $lastfoot stepif $stepif stepball $solvestepballchange step $whichstep tap separation $tapseparation prev $previoustapseparation weight now $weighton prev $previousweighton")

// set inital parameters for event list (modified after event resolved

        if (whichfoot != false && leftImage.visibility == View.VISIBLE && rightImage.visibility == View.VISIBLE) {
//            if (whichfoot != false) {

            val thiseventid = eventlisttime.size - 1
            eventlistid.add(thiseventid)

            val eventtime = currenttime  // resolve correct event time for each event
            if (eventlisttime.size == 0) {
                eventlisttime.add(eventtime)
                eventlisttime.add(0)

            }

//            val routineeventlisttimesize = routineeventlisttime.size

            val eventlisttimesize = eventlisttime.size
            val spacertime = tripletaptimeout.toInt()

            d("bpm", "before mod eventlisttime size $eventlisttimesize list $eventlisttime")
            d("bpm", "before mod  tap $tapseparation routine times $routineeventlisttime ")

            if (eventlisttime.size > 4 && tapseparation.absoluteValue > 5000) {   // if long delay remove last and replace assuming latest event is on a beat

                if (tapseparation.absoluteValue < 1000000) {

                    eventlisttime.removeAt(eventlisttimesize - 1)

                    routineeventlisttime.removeAt(eventlisttimesize - 4)

                    eventlistbeats.removeAt(eventlisttimesize - 4)

                    var routineeventtime = (eventlisttime[eventlisttimesize - 2] - eventlisttime[1]).div(1000)
                    routineeventlisttime.add(routineeventtime)

                    var thisbeattoround =
                        (eventlisttime[eventlisttimesize - 2] - eventlisttime[1]).div(tripletaptimeout) //  need to think how to resolve for jazz handz - hold
                    var thisroundeddownbeat = thisbeattoround.toDouble().nextDown().toInt()
                    eventlistbeats.add(thisroundeddownbeat)

                    eventlisttime.add(((eventlisttime[eventlisttimesize - 2]) + spacertime))
                    routineeventtime = (eventlisttime[eventlisttimesize - 1] - eventlisttime[1]).div(1000)
                    routineeventlisttime.add(routineeventtime)

                    thisbeattoround =
                        (eventlisttime[eventlisttimesize - 1] - eventlisttime[1]).div(tripletaptimeout) //  need to think how to resolve for jazz handz - hold
                    thisroundeddownbeat = thisbeattoround.toDouble().nextDown().toInt()
                    eventlistbeats.add(thisroundeddownbeat)

//                    d("bpm", "4+ with delay eventlisttime size $eventlisttimesize list $eventlisttime")
//                    d("bpm", "4+ with delay  tap $tapseparation routine times $routineeventlisttime ")

                } else {  // restarting routine
                    eventlisttime.removeAt(eventlisttimesize - 1)
                    routineeventlisttime.removeAt(eventlisttimesize - 4)
                    eventlistbeats.removeAt(eventlisttimesize - 4)

                    eventlisttime.add(((eventlisttime[eventlisttimesize - 2]) + spacertime))

                    var routineeventtime = (eventlisttime[eventlisttimesize - 1] - eventlisttime[1]).div(1000)
                    routineeventlisttime.add(routineeventtime)

                    var thisbeattoround =
                        (eventlisttime[eventlisttimesize - 1] - eventlisttime[1]).div(tripletaptimeout) //  need to think how to resolve for jazz handz - hold
                    var thisroundeddownbeat = thisbeattoround.toDouble().nextDown().toInt()
                    eventlistbeats.add(thisroundeddownbeat)

//                    d("bpm", "4+ restart routine eventlisttime size $eventlisttimesize list $eventlisttime")
//                    d("bpm", "4+ restart routine tap $tapseparation routine times $routineeventlisttime ")
                }

            } else if (eventlisttime.size > 4) {     //  normal case, take previous time and add tap separation (with or without a delay)

                val eventlisttimeseparation1 = eventlisttime[eventlisttimesize - 1] - eventlisttime[eventlisttimesize - 2]

                if (eventlisttimeseparation1 > 5000) {

                    val replacedeventlisttime = eventlisttime[eventlisttimesize - 2] + spacertime

                    eventlisttime.removeAt(eventlisttimesize - 1)

                    eventlisttime.add(replacedeventlisttime)

//                    d("bpm", "3+ previous delay eventlisttime size $eventlisttimesize list $eventlisttime")
//                    d("bpm", "3+ previous delay tap $tapseparation routine times $routineeventlisttime ")

                } else if (eventlisttimeseparation1.absoluteValue > 1000000) {

                    val previouseventlisttime = eventlisttime[eventlisttimesize - 2]

                    eventlisttime.removeAt(eventlisttimesize - 1)

                    eventlisttime.add(previouseventlisttime + tapseparation)

//                    d("bpm", "3+ previous delay eventlisttime size $eventlisttimesize list $eventlisttime")
//                    d("bpm", "3+ previous delay tap $tapseparation routine times $routineeventlisttime ")
                } else {

                    val previouseventlisttime = eventlisttime[eventlisttimesize - 1]

                    eventlisttime.add(previouseventlisttime + tapseparation)
                }

            } else if (eventlisttime.size == 1) {  // case if first down touch is not on a foot and eventlisttime has been cleared
                if (tapseparation.absoluteValue > 5000) {  // delay before second action
                    eventlisttime.clear()
                    eventlisttime.add(eventtime)
                    eventlisttime.add(0)
                    eventlisttime.add(spacertime + 1)   // + 1 to ensure first move is on beat 1
                } else {
//                    eventlisttime.add(tapseparation)    /* this doesn't do anything currently */
                    val lasteventlisttime = eventlisttime[eventlisttime.size - 1]
                    eventlisttime.add(lasteventlisttime)   // this should work
                }

            } else if (eventlisttime.size == 2) {  // delay before second action
                if (tapseparation.absoluteValue > 5000) {  // delay before second action  - currently this is always true
                    eventlisttime.clear()
                    eventlisttime.add(eventtime)
                    eventlisttime.add(0)
                    eventlisttime.add(spacertime + 1)   // + 1 to ensure first move is on beat 1
                } else {
//                    eventlisttime.add(tapseparation)    /* this doesn't do anything currently */
                    val lasteventlisttime = eventlisttime[eventlisttime.size - 1]
                    eventlisttime.add(lasteventlisttime)   // this should work
                }
            } else if (eventlisttime.size == 3) {
                if (tapseparation.absoluteValue > 5000) {  // delay before first action
                    eventlisttime.clear()
                    eventlisttime.add(eventtime)
                    eventlisttime.add(0)
                    eventlisttime.add(spacertime)
                    eventlisttime.add(spacertime + tapseparation)  //  this should work - should it be spacertime?
                } else {
                    val lasttapseparation = downeventlist[downeventlist.size - 1] - downeventlist[downeventlist.size - 2]
                    eventlisttime.add(spacertime + lasttapseparation)  // this works
                }

            } else if (eventlisttime.size == 4) {

                if (tapseparation.absoluteValue > 5000) {  // delay before second action
                    eventlisttime.clear()
                    eventlisttime.add(eventtime)
                    eventlisttime.add(0)
                    eventlisttime.add(spacertime)
                    eventlisttime.add(spacertime + previoustapseparation)
//                    eventlisttime.add(spacertime + previoustapseparation + tapseparation)  //
                    eventlisttime.add(spacertime + previoustapseparation + spacertime)  // if a over 5s delay, add spacertime for 1 beat
                } else {
                    val lasteventlisttime = eventlisttime[eventlisttime.size - 1]
                    eventlisttime.add(lasteventlisttime + tapseparation)   // this should work
                }
            }

            d("bpm", "after mod eventlisttime size $eventlisttimesize list $eventlisttime")

            val routineeventtime = (eventlisttime[eventlisttimesize - 1] - eventlisttime[1]).div(1000)
            routineeventlisttime.add(routineeventtime)

//            val thisbeattoround =
//                (eventlisttime[eventlisttimesize - 1] - eventlisttime[1]).div(tripletaptimeout) //  need to think how to resolve for jazz handz - hold
            val thisbeattoround = eventlisttime[eventlisttime.size - 1].toFloat().div(tripletaptimeout)

            val thisroundeddownbeat = thisbeattoround.nextDown().toInt()

            eventlistbeats.add(thisroundeddownbeat)

//            d("bpm", "final mod eventlisttime size $eventlisttimesize list $eventlisttime")
//            d("bpm", "final mod  tap $tapseparation routine times $routineeventlisttime ")

            val thisevent = "event"
            eventlist.add(thisevent)

        } else {
            val eventtime = 0
            val thiseventid = 0
            val thisevent = "none"

            val thisbeat = 0  //  need to think how to resolve for jazz handz - hold

            if (eventlistid.size == 0) {   //  if this is the first down event and is not on a foot, put filler values in here
                eventlisttime.add(eventtime)
                eventlistid.add(thiseventid)
                eventlistbeats.add(thisbeat)
                eventlist.add(thisevent)

                routineeventlisttime.add(eventtime)
            }
        }


        val textanim = ObjectAnimator.ofInt(lastAction, "textColor", Color.WHITE, Color.RED, Color.GRAY)

        val numberevents = eventlist.size

        val eventtime = when {
//                eventlisttime.size > 2 -> eventlisttime[numberevents-1]
            eventlisttime.size > 2 -> eventlisttime[eventlisttime.size - 1]
            else -> 0        // put time 0 for first element in list
        }

//        d("Paul", "event time list $eventlisttime")

        val thiseventid = eventlistid[numberevents - 1]
        val thisbeat = eventlistbeats[numberevents - 1]
        val thisevent = eventlist[numberevents - 1]

        val routineeventtime = routineeventlisttime[numberevents - 1]
        val thiscomment = "None"
//        d("Paul", "routineeventtime $routineeventtime")


//        val lastfoot = when {
//            lastdownx > leftimagelocx && lastdownx < leftimagemaxx && lastdowny > leftimagelocy && lastdowny < leftimagemaxy -> "Leftfoot"
//            lastdownx > rightimagelocx && lastdownx < rightimagemaxx && lastdowny > rightimagelocy && lastdowny < rightimagemaxy -> "Rightfoot"
//            else -> false
//        }
//
//        val secondlastfoot = when {
//            secondlastdownx > leftimagelocx && secondlastdownx < leftimagemaxx && secondlastdowny > leftimagelocy && secondlastdowny < leftimagemaxy -> "Leftfoot"
//            secondlastdownx > rightimagelocx && secondlastdownx < rightimagemaxx && secondlastdowny > rightimagelocy && secondlastdowny < rightimagemaxy -> "Rightfoot"
//            else -> false
//        }


        // steps go here

        if (whichstep == "ballchangetoright") {

//            d("Paul", "Ball-change (to right)  $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")

            danceImage.setImageResource(R.drawable.rightstepimage)  // left start
            lastAction.text = "Ball-change (to right)"

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

            weightonlist.add("Right")
            eventlist.removeAt(numberevents - 1)
            eventlist.add("Ball-change to right")

//            d("Paul", "B-C to right $ballchangetapif weighton $previousweighton $downlen ${weightonlist.size}")

//            d(" B-c toright Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

//            var danceelement = RoutineElement(0, 1, 10, 1, 1, 1, "None")
            var danceelement = RoutineElement(0, 1, 10, eventtime, thisbeat, routineeventtime, thiscomment, "Right")
//
            danceactivityviewmodel =
                ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//            danceactivityviewmodel.allRoutineElements.observe(
//                this,
//                Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

            danceactivityviewmodel.insertElement(danceelement)

            return true
        }

        if (whichstep == "ballchangetoleft") {

//            d("Paul", "B-C to left *   $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")

            danceImage.setImageResource(R.drawable.leftstepimage)  // Put a delay here to go to Right tap after xxxx millisecs
            lastAction.text = "Ball-change (to Left)"

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

            weightonlist.add("Left")
            eventlist.removeAt(numberevents - 1)
            eventlist.add("Ball-change to left")

//            d("Paul", "Ball-change to left $ballchangetapif weighton $previousweighton $downlen ${weightonlist.size}")

//            d(" B-c toleft Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

            var danceelement = RoutineElement(0, 1, 9, eventtime, thisbeat, routineeventtime, thiscomment, "Left")

            danceactivityviewmodel =
                ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//            danceactivityviewmodel.allRoutineElements.observe(
//                this,
//                Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

            danceactivityviewmodel.insertElement(danceelement)

            return true
        }

//        if (whichstep == "righttoetap") {
//
//            danceImage.setImageResource(R.drawable.righttoetapimage)  // left start
//            lastAction.text = "Right toe-tap"
//
//            textanim.duration = 200
//            textanim.setEvaluator(ArgbEvaluator())
//            textanim.repeatMode = ValueAnimator.REVERSE
//            textanim.repeatCount = Animation.ABSOLUTE
//            textanim.start()
//
//            weightonlist.add("Left")
//            eventlist.removeAt(numberevents - 1)
//            eventlist.add("Left toe-tap")
//
//            downeventlist.add(currenttime)
//            downeventlist_x.add(pointerlocx.toInt())
//            downeventlist_y.add(pointerlocy.toInt())
//
////            d("Paul", "Right toe-tap step $stepif ballchange $ballchangetapif  $downlen")
//
////            d(" Righttoetap Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")
//
//            var danceelement = RoutineElement(0, 1, 8, eventtime, thisbeat, routineeventtime, thiscomment, "Left")
//
//            danceactivityviewmodel =
//                ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?
//
////            danceactivityviewmodel.allRoutineElements.observe(
////                this,
////                Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })
//
//            danceactivityviewmodel.insertElement(danceelement)
//
//            return true
//        }
//
//        if (whichstep == "lefttoetap") {
//
////            d("Paul", "Left toe-tap  $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")
//
//            danceImage.setImageResource(R.drawable.lefttoetapimage)  // left start
//            lastAction.text = "Left toe-tap"
//
//            textanim.duration = 200
//            textanim.setEvaluator(ArgbEvaluator())
//            textanim.repeatMode = ValueAnimator.REVERSE
//            textanim.repeatCount = Animation.ABSOLUTE
//            textanim.start()
//
//            weightonlist.add("Right")
//            eventlist.removeAt(numberevents - 1)
//            eventlist.add("Left toe-tap")
//
//            downeventlist.add(currenttime)
//            downeventlist_x.add(pointerlocx.toInt())
//            downeventlist_y.add(pointerlocy.toInt())
//
////            d("Paul", "Left toe-tap weighton $previousweighton $downlen ${weightonlist.size}")
//
////            d(" Lefttoetap Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")
////
//            var danceelement = RoutineElement(0, 1, 7, eventtime, thisbeat, routineeventtime, thiscomment, "Right")
//
//            danceactivityviewmodel =
//                ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?
//
////            danceactivityviewmodel.allRoutineElements.observe(
////                this,
////                Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })
//
//            danceactivityviewmodel.insertElement(danceelement)
//
//            return true
//        }

        if (whichstep == "righttap") {

            danceImage.setImageResource(R.drawable.righttapimage)
            lastAction.text = "Right tap"

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()


            if (stepif != "Step-hop") {  //  kickcrunch --> tap goes to here, no weight change
                weightonlist.add("Right")
            } else
                weightonlist.add("Left")

            eventlist.removeAt(numberevents - 1)
            eventlist.add("Right tap")

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

//            d("Paul", "Right tap down $stepif  $downlen $downeventlist ")

//            d(" Righttap Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

            var danceelement = RoutineElement(0, 1, 6, eventtime, thisbeat, routineeventtime, thiscomment, "Left")

            danceactivityviewmodel =
                ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//            danceactivityviewmodel.allRoutineElements.observe(
//                this,
//                Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

            danceactivityviewmodel.insertElement(danceelement)

            return true
        }

        if (whichstep == "lefttap") {

            danceImage.setImageResource(R.drawable.lefttapimage)  // left start
            lastAction.text = "Left tap"

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            if (stepif == "Step-hop") {       //  kickcrunch --> tap goes to here, no weight change
                weightonlist.add("Left")

            } else
                weightonlist.add("Right")

            eventlist.removeAt(numberevents - 1)
            eventlist.add("Left tap")

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

//            d("Paul", "Left tap stepif $stepif ballchang $ballchangetapif down len $downlen")

//            d(" Lefttap Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

            var danceelement = RoutineElement(0, 1, 5, eventtime, thisbeat, routineeventtime, thiscomment, "Right")

            danceactivityviewmodel =
                ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//            danceactivityviewmodel.allRoutineElements.observe(
//                this,
//                Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

            danceactivityviewmodel.insertElement(danceelement)

            return true
        }


        if (whichstep == "lefthop") {

//            d("Paul", "left hop *   $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")

            danceImage.setImageResource(R.drawable.lefthopimage)  // Want a different image here - toe without lines?
            lastAction.text = "Left hop"

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

            if (stepif != false) {   //
                weightonlist.add("Left")  //
                eventlist.removeAt(numberevents - 1)
                eventlist.add("Left hop")
            }

            if (ballchangestepif != false) {   //
                weightonlist.add("Left")  //
                eventlist.removeAt(numberevents - 1)
                eventlist.add("Left hop")
            }

//            if (ballchangetapif == "stepballchange") {   //
            if (ballchangetapif != false) {   //
                weightonlist.add("Left")  //
                eventlist.removeAt(numberevents - 1)
                eventlist.add("Left hop")
            }

//            d("Paul", "Left hop $ballchangetapif weighton $previousweighton $downlen ${weightonlist.size}")

//            d(" Lefthop Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

            var danceelement = RoutineElement(0, 1, 3, eventtime, thisbeat, routineeventtime, thiscomment, "Left")

            danceactivityviewmodel =
                ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//            danceactivityviewmodel.allRoutineElements.observe(
//                this,
//                Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

            danceactivityviewmodel.insertElement(danceelement)


            return true
        }

        if (whichstep == "leftstep") {

            danceImage.setImageResource(R.drawable.leftstepimage)//  right start
            lastAction.text = "Left step"

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

            if (stepif != false) {
                weightonlist.add("Left")
                eventlist.removeAt(numberevents - 1)
                eventlist.add("Step to left")
            }

            if (ballchangestepif != false && weighton != "0") {  // don't think we need && previousweighton != "0"
                weightonlist.add("Left")
                eventlist.removeAt(numberevents - 1)
                eventlist.add("Step to left")
            }

            if (ballchangetapif != false) {
                weightonlist.add("Left")
                eventlist.removeAt(numberevents - 1)
                eventlist.add("Step to left")
            }

//            d("Paul", "Step to Left $ballchangetapif wweighton $previousweighton $downlen ${weightonlist.size}")

//            d(" Leftstep Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")
//            d(" Paul L Step", "Left Step id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

            var danceelement = RoutineElement(0, 1, 1, eventtime, thisbeat, routineeventtime, thiscomment, "Left")

//            d("Paul L Step", "Left Step $eventlist  last event ${eventlist[numberevents - 1]}  time ${routineeventlisttime[numberevents - 1]}")

            danceactivityviewmodel =
                ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//            danceactivityviewmodel.allRoutineElements.observe(
//                this,
//                Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

            danceactivityviewmodel.insertElement(danceelement)

            return false
        }

        if (whichstep == "righthop") {

            danceImage.setImageResource(R.drawable.righthopimage)
            lastAction.text = "Right hop"

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

            if (stepif != false) {
                weightonlist.add("Right")
                eventlist.removeAt(numberevents - 1)
                eventlist.add("Right hop")
            }

            if (ballchangestepif != false) {
                weightonlist.add("Right")
                eventlist.removeAt(numberevents - 1)
                eventlist.add("Right hop")
            }

            if (ballchangetapif != false) {
                weightonlist.add("Right")  //
                eventlist.removeAt(numberevents - 1)
                eventlist.add("Right hop")
            }

//            d("Paul", "Right hop $ballchangetapif weighton $previousweighton $downlen ${weightonlist.size}")

//            d(" Righthop Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

            var danceelement = RoutineElement(0, 1, 4, eventtime, thisbeat, routineeventtime, thiscomment, "Right")

            danceactivityviewmodel =
                ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//            danceactivityviewmodel.allRoutineElements.observe(
//                this,
//                Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

            danceactivityviewmodel.insertElement(danceelement)

            return true
        }

        if (whichstep == "rightstep") {

            danceImage.setImageResource(R.drawable.rightstepimage)//  right start
            lastAction.text = "Right step"

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

            if (stepif != false) {
                weightonlist.add("Right")
                eventlist.removeAt(numberevents - 1)
                eventlist.add("Step to right")
            }

            if (ballchangestepif != false && weighton != "0") {   // don't think we need && previousweighton != "0"
                weightonlist.add("Right")
                eventlist.removeAt(numberevents - 1)
                eventlist.add("Step to right")
            }

            if (ballchangetapif != false) {
                weightonlist.add("Right")
                eventlist.removeAt(numberevents - 1)
                eventlist.add("Step to right")

            }
//            d("Paul", "Step to right $ballchangetapif weighton $previousweighton $downlen ${weightonlist.size}")
//            d(" Rightstep Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

//            d("Paul", "R Step Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

//            var danceelement = RoutineElement(0, 1, 2, 0, 1)

            var danceelement = RoutineElement(0, 1, 2, eventtime, thisbeat, routineeventtime, thiscomment, "Right")
//            d("Paul", "R Step Event list id ${danceelement.elementId} time ${danceelement.elementtime} beat ${danceelement.elementbeat}")

            danceactivityviewmodel =
                ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

//            danceactivityviewmodel.allRoutineElements.observe(
//                this,
//                Observer { element_id -> element_id?.let { this.getRoutineElementSize(element_id, danceelement) } })

            danceactivityviewmodel.insertElement(danceelement)

            return false
        }

        // end step hop section

        // Do jazz hands if we get to here?

        else {

            lastAction.text = "Jazz Handz!"

//                d("Paul Thisevent", "Jazz Handz time? Foot: $which?foot lastfoot $lastfoot stepball $solvestepballchange step $whichstep tap separation $tapseparation prev $previoustapseparation weight now $weighton prev $previousweighton")

//                d("Paul Thisevent", "Jazz Handz time? $downeventlis?t $downeventlist_x")

            // maybe needed later

//        downeventlist.removeAt(downlen-1)
//        downeventlist_x.removeAt(downlen-1)
//        downeventlist_y.removeAt(downlen-1)

//            May not be needed in final version -- clear step weight if jazz hands?  don't do this, it does something odd

//            weightonlist.removeAt(weightonlist.size -1)

//                d(" Jazzhand Events", "Event list id $thiseventid time $eventtime beat $thisbeat event $thisevent  Lengths: id: ${eventlistid.size} time: ${eventlisttime.size} beat: ${eventlistbeats.size} events: ${eventlist.size}")

        }

//            d("Down Events", "Event list id $thiseventid time $eventtime beat $thisbeat event $eventlist")

        return true

        // end of onDown

    }

    override fun onLongPress(e: MotionEvent?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getRoutineElementSize(elements: List<RoutineElement>, danceelement: RoutineElement) {

//        d("Paul", "Element List current size ${elements.lastIndex + 1} ")

//        d("Paul", "Element Index ${danceelement.elementId}")

        danceactivityviewmodel.allRoutineElements.removeObservers(this)

        val newelementindex = elements.lastIndex + 2   // index starts at 0, need next available primary key - key starts at 1

        val newdanceelement = RoutineElement(
            newelementindex,
            danceelement.routineelementId,
            danceelement.routinedancemoveId,
            danceelement.elementtime,
            danceelement.elementbeat,
            danceelement.routineelementtime,
            danceelement.comment,
            danceelement.weighton
        )

//        d("Paul", "New Element Index ${newdanceelement.elementId}")

//        danceactivityviewmodel.insertElement(newdanceelement)

    }

}

