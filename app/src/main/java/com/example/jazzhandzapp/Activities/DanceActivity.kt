@file:Suppress("SpellCheckingInspection")

package com.example.jazzhandzapp.Activities

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log.d
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jazzhandzapp.DataClassses.Elements
import com.example.jazzhandzapp.DataClassses.Routines
import com.example.jazzhandzapp.Database.AppDatabase
import com.example.jazzhandzapp.Database.RoutineElement
import com.example.jazzhandzapp.Database.RoutineName
import com.example.jazzhandzapp.R
import com.example.jazzhandzapp.ViewModels.DanceActivityViewModel
import kotlinx.android.synthetic.main.dance_screen.*
import kotlinx.coroutines.GlobalScope
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.nextDown


class DanceActivity : AppCompatActivity(),
    GestureDetector.OnGestureListener
//        GestureDetector.OnDoubleTapListener {

{

    private lateinit var danceactivityviewmodel: DanceActivityViewModel

    private lateinit var mDetector: GestureDetectorCompat
//    private lateinit var viewDrawn: ViewCompat

    private var routineNames: LiveData<List<RoutineName>>
    private var routineElements: LiveData<List<RoutineElement>>

    private val timevalues: ArrayList<Int> = arrayListOf()
    private val tempovalues: ArrayList<Int> = arrayListOf()

    private val createmodelist: ArrayList<Boolean> = arrayListOf()


    init {

        val routineElementDAO = AppDatabase.getDatabase(Application(), scope = GlobalScope).routineElementDAO()

        val routineNameDAO = AppDatabase.getDatabase(Application(), scope = GlobalScope).routineNameDAO()

        routineNames = routineNameDAO.getRoutineNames()
        routineElements = routineElementDAO.getRoutineElements(1)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dance_screen)
//        setContentView(R.layout.nav_view_danceactivity_fragment)

//        nav_view.setNavigationItemSelectedListener {
//        nav_view_fragment_danceactivity.setNavigationItemSelectedListener {
//            when (it.itemId) {
////                R.id.actionCommittee -> d("Paul", "Committee was pressed")
//                R.id.actionHome -> startActivity(Intent(this, MainActivity::class.java))
//            }
//            when (it.itemId) {
//                R.id.actionFreestyle -> startActivity(Intent(this, DanceActivity::class.java))
//            }
//            when (it.itemId) {
//                R.id.actionCreate -> startActivity(Intent(this, DanceActivity::class.java).putExtra("createmode", true))
//            }
//            when (it.itemId) {
//                R.id.actionRoutines -> startActivity(Intent(this, DanceRoutinesActivity::class.java).putExtra("createmode", false))
//            }
//            when (it.itemId) {
////                R.id.actionCommittee -> d("Paul", "Committee was pressed")
//                R.id.actionDownloadedRoutines -> startActivity(Intent(this, DownloadRoutinesActivity::class.java))
//            }
//            it.isChecked = true
////            drawerLayout.closeDrawers()
//            danceactivity_drawer_layout.closeDrawers()
//            true
//
//        }
//
//        supportActionBar?.apply {
//            setDisplayHomeAsUpEnabled(true)
//            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
//        }

        val createroutineintent = intent.getBooleanExtra("createmode",false)
        createmodelist.clear()
        createmodelist.add(createroutineintent)
        val createroutinemode = createmodelist[0]

//        d("Paul", "create routine mode ${createmodelist}"
        createmodetext.text = createmodelist[0].toString()

        if (!createroutinemode){
            deleteImage.visibility = View.INVISIBLE
        }

        mDetector = GestureDetectorCompat(this, this)

        danceactivityviewmodel = ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

        deleteImage.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("This will delete all saved steps in this routine.  Continue?")
                .setPositiveButton("Yes")
                { _, _ -> danceactivityviewmodel.deleteRoutineElements(1)
                            clearLists()}
                .setNegativeButton("No")
                { _, _ ->}
                .create()
                .show()
        }

        getRoutineName() // gets last routine and tempo

        setTempoImage.setOnClickListener {

            if (createroutinemode) {

            AlertDialog.Builder(this)
                .setMessage("Continue from last saved routine?")
                .setPositiveButton("Yes")
                { _, _ ->
                    clearLists()
                    timevalues.clear()
                    tempovalues.clear()
                    getRoutineElements()
                    danceImage.visibility = View.INVISIBLE
                    if (createroutinemode) {
                        deleteImage.visibility = View.INVISIBLE
                    }

                    countinfirstfive.visibility = View.VISIBLE
                    countinfirstfive.setTextColor(Color.GRAY)
                    countinfirstsix.visibility = View.VISIBLE
                    countinfirstsix.setTextColor(Color.GRAY)
                    countinsecondfive.visibility = View.VISIBLE
                    countinsecondfive.setTextColor(Color.GRAY)
                    countinsecondsix.visibility = View.VISIBLE
                    countinsecondsix.setTextColor(Color.GRAY)
                    countinseven.visibility = View.VISIBLE
                    countinseven.setTextColor(Color.GRAY)
                    countineight.visibility = View.VISIBLE
                    countineight.setTextColor(Color.GRAY)
                    clapTempoImage.visibility = View.VISIBLE

                }
                .setNegativeButton("No")
                { _, _ ->
                    danceactivityviewmodel.deleteRoutineElements(1)
                    clearLists()
                    timevalues.clear()
                    tempovalues.clear()
                    danceImage.visibility = View.INVISIBLE
                    if (createroutinemode) {
                        deleteImage.visibility = View.INVISIBLE
                    }

                    countinfirstfive.visibility = View.VISIBLE
                    countinfirstfive.setTextColor(Color.GRAY)
                    countinfirstsix.visibility = View.VISIBLE
                    countinfirstsix.setTextColor(Color.GRAY)
                    countinsecondfive.visibility = View.VISIBLE
                    countinsecondfive.setTextColor(Color.GRAY)
                    countinsecondsix.visibility = View.VISIBLE
                    countinsecondsix.setTextColor(Color.GRAY)
                    countinseven.visibility = View.VISIBLE
                    countinseven.setTextColor(Color.GRAY)
                    countineight.visibility = View.VISIBLE
                    countineight.setTextColor(Color.GRAY)
                    clapTempoImage.visibility = View.VISIBLE
                }
                // empty lists
                .create()
                .show()
        } else {
            danceactivityviewmodel.deleteRoutineElements(1)
            clearLists()
            timevalues.clear()
            tempovalues.clear()
            danceImage.visibility = View.INVISIBLE

            countinfirstfive.visibility = View.VISIBLE
            countinfirstfive.setTextColor(Color.GRAY)
            countinfirstsix.visibility = View.VISIBLE
            countinfirstsix.setTextColor(Color.GRAY)
            countinsecondfive.visibility = View.VISIBLE
            countinsecondfive.setTextColor(Color.GRAY)
            countinsecondsix.visibility = View.VISIBLE
            countinsecondsix.setTextColor(Color.GRAY)
            countinseven.visibility = View.VISIBLE
            countinseven.setTextColor(Color.GRAY)
            countineight.visibility = View.VISIBLE
            countineight.setTextColor(Color.GRAY)
            clapTempoImage.visibility = View.VISIBLE
        }

            //update tempo display and database in another function
//            val tempo = 150  // for testing only
            // prompt for continue routine or delete elements
//            getUpdatedRoutineName(tempo)

        }

        countinlabel.setOnClickListener {

            if (createroutinemode) {
                deleteImage.visibility = View.INVISIBLE


                AlertDialog.Builder(this)
                    .setMessage("Continue from last saved routine?")
                    .setPositiveButton("Yes")
                    { _, _ ->
                        clearLists()
                        timevalues.clear()
                        tempovalues.clear()
                        getRoutineElements()
                        danceImage.visibility = View.INVISIBLE

                        countinfirstfive.visibility = View.VISIBLE
                        countinfirstfive.setTextColor(Color.GRAY)
                        countinfirstsix.visibility = View.VISIBLE
                        countinfirstsix.setTextColor(Color.GRAY)
                        countinsecondfive.visibility = View.VISIBLE
                        countinsecondfive.setTextColor(Color.GRAY)
                        countinsecondsix.visibility = View.VISIBLE
                        countinsecondsix.setTextColor(Color.GRAY)
                        countinseven.visibility = View.VISIBLE
                        countinseven.setTextColor(Color.GRAY)
                        countineight.visibility = View.VISIBLE
                        countineight.setTextColor(Color.GRAY)
                        clapTempoImage.visibility = View.VISIBLE
                    }
                    .setNegativeButton("No")
                    { _, _ ->
                        danceactivityviewmodel.deleteRoutineElements(1)
                        clearLists()
                        timevalues.clear()
                        tempovalues.clear()
                        danceImage.visibility = View.INVISIBLE

                        countinfirstfive.visibility = View.VISIBLE
                        countinfirstfive.setTextColor(Color.GRAY)
                        countinfirstsix.visibility = View.VISIBLE
                        countinfirstsix.setTextColor(Color.GRAY)
                        countinsecondfive.visibility = View.VISIBLE
                        countinsecondfive.setTextColor(Color.GRAY)
                        countinsecondsix.visibility = View.VISIBLE
                        countinsecondsix.setTextColor(Color.GRAY)
                        countinseven.visibility = View.VISIBLE
                        countinseven.setTextColor(Color.GRAY)
                        countineight.visibility = View.VISIBLE
                        countineight.setTextColor(Color.GRAY)
                        clapTempoImage.visibility = View.VISIBLE
                    }
                    // empty lists
                    .create()
                    .show()
            } else {
                danceactivityviewmodel.deleteRoutineElements(1)
                clearLists()
                timevalues.clear()
                tempovalues.clear()
                danceImage.visibility = View.INVISIBLE

                countinfirstfive.visibility = View.VISIBLE
                countinfirstfive.setTextColor(Color.GRAY)
                countinfirstsix.visibility = View.VISIBLE
                countinfirstsix.setTextColor(Color.GRAY)
                countinsecondfive.visibility = View.VISIBLE
                countinsecondfive.setTextColor(Color.GRAY)
                countinsecondsix.visibility = View.VISIBLE
                countinsecondsix.setTextColor(Color.GRAY)
                countinseven.visibility = View.VISIBLE
                countinseven.setTextColor(Color.GRAY)
                countineight.visibility = View.VISIBLE
                countineight.setTextColor(Color.GRAY)
                clapTempoImage.visibility = View.VISIBLE
            }
            //update tempo display and database in another function
//            val tempo = 150  // for testing only
            // prompt for continue routine or delete elements
//            getUpdatedRoutineName(tempo)

        }

        // Tempo text on change - same function as tempoimage listtener

        danceactivitytempo.addTextChangedListener(myTextWatcher)

        var starttime: Long

        clapTempoImage.setOnClickListener {

            when (timevalues.size) {
                0 -> {
                    starttime = System.currentTimeMillis()
        //                d("Paul", "start time $starttime")
                    timevalues.add(starttime.toInt())

                    countinfirstfive.setTextColor(Color.RED)
        //                tempovalues.add(0)
                }
                1 -> {
                    val time = System.currentTimeMillis()
                    timevalues.add(time.toInt())
                    val nexttempo = 120000/(time.toInt() - timevalues[0])
                    tempovalues.add(nexttempo)
                    countinfirstsix.setTextColor(Color.RED)
                }
                2 -> {
                    val time = System.currentTimeMillis()
                    timevalues.add(time.toInt())
                    val nexttempo = 120000/(time.toInt() - timevalues[1])
                    tempovalues.add(nexttempo)
                    countinsecondfive.setTextColor(Color.RED)
                }
                3 -> {
                    val time = System.currentTimeMillis()
                    timevalues.add(time.toInt())
                    val nexttempo = 60000/(time.toInt() - timevalues[2])
                    tempovalues.add(nexttempo)
                    countinsecondsix.setTextColor(Color.RED)
                }
                4 -> {
                    val time = System.currentTimeMillis()
                    timevalues.add(time.toInt())
                    val nexttempo = 60000/(time.toInt() - timevalues[3])
                    tempovalues.add(nexttempo)
                    countinseven.setTextColor(Color.RED)
                }
                5 -> {
                    val time = System.currentTimeMillis()
                    timevalues.add(time.toInt())
                    val nexttempo = 60000/(time.toInt() - timevalues[4])
                    tempovalues.add(nexttempo)
                    countineight.setTextColor(Color.RED)

                    val tempo = tempovalues.average().toInt()  // for testing only
                    getUpdatedRoutineName(tempo)

                    danceImage.visibility = View.VISIBLE
                    if (createroutinemode){
                        deleteImage.visibility = View.VISIBLE
                    }

                    countinfirstfive.visibility = View.INVISIBLE
                    countinfirstsix.visibility = View.INVISIBLE
                    countinsecondfive.visibility = View.INVISIBLE
                    countinsecondsix.visibility = View.INVISIBLE
                    countinseven.visibility = View.INVISIBLE
                    countineight.visibility = View.INVISIBLE

        //                d("Paul", "count in times $timevalues, $tempovalues")

                    timevalues.clear()
                    tempovalues.clear()

                    countinfirstfive.setTextColor(Color.GRAY)
                    countinfirstsix.setTextColor(Color.GRAY)
                    countinsecondfive.setTextColor(Color.GRAY)
                    countinsecondsix.setTextColor(Color.GRAY)
                    countinseven.setTextColor(Color.GRAY)
                    countineight.setTextColor(Color.GRAY)

                    currenttempo.add(tempo)
                }
            }
        }

    }  // end here for supporting all Gestures

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        danceactivity_drawer_layout.openDrawer(GravityCompat.START)
//        return true
//    }

//    val createroutinemode = createmodelist[0]

    private val myTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {

            if (createmodetext.text == "true") {

                AlertDialog.Builder(this@DanceActivity)
                    .setMessage("Continue from last saved routine?")
                    .setPositiveButton("Yes")
                    { _, _ ->
                        clearLists()
                        try {
                            getRoutineElements()
                        } catch (exception: java.lang.Exception) {  // covers when no saved elements
                        }
                        danceImage.visibility = View.VISIBLE

                        countinfirstfive.visibility = View.INVISIBLE
                        countinfirstsix.visibility = View.INVISIBLE
                        countinsecondfive.visibility = View.INVISIBLE
                        countinsecondsix.visibility = View.INVISIBLE
                        countinseven.visibility = View.INVISIBLE
                        countineight.visibility = View.INVISIBLE
                        clapTempoImage.visibility = View.INVISIBLE
                    }
                    .setNegativeButton("No")
                    { _, _ ->
                        danceactivityviewmodel.deleteRoutineElements(1)
                        clearLists()
                        danceImage.visibility = View.VISIBLE
//                    deleteImage.visibility = View.VISIBLE

                        countinfirstfive.visibility = View.INVISIBLE
                        countinfirstsix.visibility = View.INVISIBLE
                        countinsecondfive.visibility = View.INVISIBLE
                        countinsecondsix.visibility = View.INVISIBLE
                        countinseven.visibility = View.INVISIBLE
                        countineight.visibility = View.INVISIBLE
                        clapTempoImage.visibility = View.INVISIBLE
                    }
                    // empty lists
                    .create()
                    .show()
            } else {
                danceImage.visibility = View.VISIBLE

                countinfirstfive.visibility = View.INVISIBLE
                countinfirstsix.visibility = View.INVISIBLE
                countinsecondfive.visibility = View.INVISIBLE
                countinsecondsix.visibility = View.INVISIBLE
                countinseven.visibility = View.INVISIBLE
                countineight.visibility = View.INVISIBLE
                clapTempoImage.visibility = View.INVISIBLE
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                TODO("Not yet implemented")
        }
    }

    private val downeventlist: ArrayList<Int> = arrayListOf(0, 0, 0)
    private val downeventlist_x: ArrayList<Int> = arrayListOf(0, 0, 0)
    private val downeventlist_y: ArrayList<Int> = arrayListOf(0, 0, 0)

    private val weightonlist: ArrayList<String> = arrayListOf("0", "0", "0")

    private val eventlistid: ArrayList<Int> = arrayListOf()
    private val eventlistbeats: ArrayList<Int> = arrayListOf()
    private val eventlisttime: ArrayList<Int> = arrayListOf()  // absolute time
    private val eventlist: ArrayList<String> = arrayListOf()

    private val routineeventlisttime: ArrayList<Int> = arrayListOf()   // time vs start of routine

    private val currenttempo: ArrayList<Int> = arrayListOf()

    fun clearLists(){
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

    private fun getRoutineName() {
        routineNames.observe(this, Observer { routine_id -> routine_id?.let { this.getInitialTempo(routine_id) } })
    }

    fun getRoutineElements() {
        routineElements.observe(
            this,
            Observer { routine_id -> routine_id?.let { this.setPreviousElementsToLists(routine_id) } })
    }

    private fun setPreviousElementsToLists (elements: List<RoutineElement>) {

        val elementlist: ArrayList<Elements> = ArrayList()

        for (i in elements.indices) {
//            var elementtime = elements[i].elementtime.toString()
            val routineid = elements[i].routineelementId.toString()
            val elementtime = elements[i].elementtime.toString()
            val elementbeat = elements[i].elementbeat.toString()
            val elementmove = elements[i].routinedancemoveId.toString()
            val routineelementtime = elements[i].routineelementtime.toString()
            val elementcomment = elements[i].comment
            val weighton = elements[i].weighton

            elementlist.add(Elements(routineid, elementtime, elementbeat, elementmove, routineelementtime, elementcomment, weighton))
        }

        routineElements.removeObservers(this)

        for (i in elementlist.indices) {
            eventlistid.add(elementlist[i].routineid!!.toInt())
            eventlisttime.add(elementlist[i].elementtime!!.toInt())
            eventlistbeats.add(elementlist[i].elementbeat!!.toInt())
            eventlist.add(elementlist[i].elementmove!!)
            routineeventlisttime.add(elementlist[i].routineelementtime!!.toInt())
            weightonlist.add(elementlist[i].weighton!!)

        }

//        d("Paul", "element ids $eventlistid times:  ${routineeventlisttime}")  // both empty lists as just been emptied

    }

    private fun getUpdatedRoutineName(tempo: Int) {

        routineNames.observe(this, Observer { routine_id -> routine_id?.let { this.getUpdatedTempo(routine_id, tempo) } })

    }

    private fun getInitialTempo(routines: List<RoutineName>) {

        val routinelist: ArrayList<Routines> = ArrayList()

        for (i in routines.indices) {
            val routineid = routines[i].routineId.toString()
            val routinename = routines[i].routinename
            val routinecreatorid = routines[i].routinecreatorId.toString()
            val routinetempo = routines[i].tempo.toString()
            val routinetrack = routines[i].track

            routinelist.add(Routines(routineid, routinename, routinecreatorid, routinetempo, routinetrack))
        }
        routineNames.removeObservers(this)
//        d("Paul", "Routine 1 tempo: ${routinelist[0].tempo}")
        val tempo = routinelist[0].tempo.toString()

        danceactivitytempo.setText(tempo)
        currenttempo.add(tempo.toInt())

    }

    private fun getUpdatedTempo(routines: List<RoutineName>, tempo: Int){

        val routinelist: ArrayList<Routines> = ArrayList()

        for (i in routines.indices) {
            val routineid = routines[i].routineId.toString()
            val routinename = routines[i].routinename
            val routinecreatorid = routines[i].routinecreatorId.toString()
            val routinetempo = routines[i].tempo.toString()
            val routinetrack = routines[i].track

            routinelist.add(Routines(routineid, routinename, routinecreatorid, routinetempo, routinetrack))
        }
        routineNames.removeObservers(this)

        val tempostring = tempo.toString()

//        val tempo = "135"  // for testing only

        danceactivitytempo.removeTextChangedListener(myTextWatcher)
        danceactivitytempo.setText(tempostring)
        danceactivitytempo.addTextChangedListener(myTextWatcher)

        val thisRoutine: RoutineName

        thisRoutine = RoutineName(routinelist[0].routineid!!.toInt(),routinelist[0].routinename!!,routinelist[0].routinecreatorid!!.toInt(),tempo, routinelist[0].track!!)

        danceactivityviewmodel.setDatabaseTempo(thisRoutine)

        currenttempo.add(tempo)

        // clear lists - prompt for continue or delete elements
    }

    // ===============Working OnTouch Override ============
    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        val currenttime = event!!.eventTime
        val currentX = event!!.x
        val currentY = event.y
        if (event.action == MotionEvent.ACTION_DOWN) {

//            d("Paul", "Touch event")

            countinfirstfive.visibility = View.INVISIBLE
            countinfirstsix.visibility = View.INVISIBLE
            countinsecondfive.visibility = View.INVISIBLE
            countinsecondsix.visibility = View.INVISIBLE
            countinseven.visibility = View.INVISIBLE
            countineight.visibility = View.INVISIBLE
            clapTempoImage.visibility = View.INVISIBLE

            danceImage.visibility = View.VISIBLE
            if (createmodelist[0]) {
                deleteImage.visibility = View.VISIBLE
            }

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


            val leftanim = ObjectAnimator.ofInt(leftImage, "backgroundColor", Color.WHITE, Color.RED, Color.WHITE)
            val rightanim = ObjectAnimator.ofInt(rightImage, "backgroundColor", Color.WHITE, Color.RED, Color.WHITE)


            if (currentX > leftimagelocx && currentX < leftimagemaxx && currentY > leftimagelocy && currentY < leftimagemaxy) {
                leftanim.duration = 200
                leftanim.setEvaluator(ArgbEvaluator())
                leftanim.repeatMode = ValueAnimator.REVERSE
                leftanim.repeatCount = Animation.ABSOLUTE
                leftanim.start()

            } else if (currentX > rightimagelocx && currentX < rightimagemaxx && currentY > rightimagelocy && currentY < rightimagemaxy) {
                rightanim.duration = 200
                rightanim.setEvaluator(ArgbEvaluator())
                rightanim.repeatMode = ValueAnimator.REVERSE
                rightanim.repeatCount = Animation.ABSOLUTE
                rightanim.start()

            }
//            else {
////                d("Paul", "No foot")
//            }

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

        e!!

        val weightonlist = weightonlist
        val weightonlistlen = weightonlist.size
        val weighton = weightonlist[weightonlistlen - 1]
        val previousweighton = weightonlist[weightonlistlen - 2]

//        d("Paul", "down events $downeventlist")

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
        val bpm = currenttempo[currenttempolistsize-1]

        val pointerlocx = e.x
        val pointerlocy = e.y

        val whichfoot = when {
            pointerlocx > leftimagelocx && pointerlocx < leftimagemaxx && pointerlocy > leftimagelocy && pointerlocy < leftimagemaxy -> "Leftfoot"
            pointerlocx > rightimagelocx && pointerlocx < rightimagemaxx && pointerlocy > rightimagelocy && pointerlocy < rightimagemaxy -> "Rightfoot"
            else -> "not foot"
        }

//        d("Paul", "initial down events $downeventlist")

        if (whichfoot != "not foot") {

            val currenttime = e.eventTime
            val currentX = e.x
            val currentY = e.y

            downeventlist.add(currenttime.toInt())
            downeventlist_x.add(currentX.toInt())
            downeventlist_y.add(currentY.toInt())

        }

        val newdownlen = downeventlist.size

//        d("Paul", "after whichfoot down events $downeventlist")

        val tapseparation = downeventlist[newdownlen - 1] - downeventlist[newdownlen - 2]  //  different from OnDown and OnFling as down event tap has been added!!
        val previoustapseparation = downeventlist[newdownlen - 2] - downeventlist[newdownlen - 3]  //  different from OnDown and OnFling as down event tap has been added!!
        val beattapseparation = tapseparation + previoustapseparation

//        val tapduration = e!!.eventTime.toInt() - downeventlist[newdownlen - 2]
        val tapduration = downeventlist[newdownlen - 1] - downeventlist[newdownlen - 2]

        val tripletaptimeout = 60000/bpm  // timeout time in ms   /* Length of two beats*/
        val doubletaptimeout = 60000/bpm * 0.3333  // timeout time in ms    /* Longer than this is a single step*/

        val maxtapduration = 50

        val tappossible = when {

            tapduration < maxtapduration && tapseparation > tripletaptimeout -> "Single"   // works for kick and kick-crunch

            tapduration < maxtapduration && tapseparation > doubletaptimeout && previoustapseparation < doubletaptimeout && beattapseparation < tripletaptimeout -> "Step-tap"  //   && beattapseparation < tripletaptimeout  // this works for crunch
            tapduration < maxtapduration && tapseparation > doubletaptimeout && previoustapseparation > doubletaptimeout -> "Step-hop"  //  && beattapseparation < tripletaptimeout   //  works for kick and crunch

            tapduration < maxtapduration && tapseparation < doubletaptimeout && previoustapseparation < doubletaptimeout -> "Ball-step" // do we need < triple???   && beattapseparation < tripletaptimeout // this works for crunch
            tapduration < maxtapduration && tapseparation < doubletaptimeout && previoustapseparation > doubletaptimeout -> "Ball-tap"  //   && beattapseparation < tripletaptimeout // works for kick
            else -> "not possible"
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

            else -> "not left tap"
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

            else -> "not right tap"
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
            eventlist.removeAt(numberevents-1)
            eventlist.add("Left tap")

//            d("Paul Thisevent jazz", "Left tap $eventlisttime")
//                    d(" Left Fling Events", "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}")

            if (createmodelist[0]) {

                val danceelement = RoutineElement(0, 1, 5, eventlisttime[numberevents ], eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Right")

//                    d("Paul L Tap", "L tap time ${eventlisttime[numberevents-1]} routine time ${routineeventlisttime[numberevents-1]}")

                danceactivityviewmodel =
                    ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                danceactivityviewmodel.insertElement(danceelement)

                danceactivityviewmodel.allRoutineElements.removeObservers(this)

//                deleteLastElement(danceelement)
            }

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
            eventlist.removeAt(numberevents-1)
            eventlist.add("Right tap")

//                    d(" Left Fling Events", "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}")

            if (createmodelist[0]) {
                val danceelement = RoutineElement(0, 1, 6, eventlisttime[numberevents], eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Left")

//                    d("Paul L Kick", "R kick time ${eventlisttime[numberevents-1]} routine time ${routineeventlisttime[numberevents-1]}")

                danceactivityviewmodel =
                    ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                danceactivityviewmodel.insertElement(danceelement)

                danceactivityviewmodel.allRoutineElements.removeObservers(this)
//
//                deleteLastElement(danceelement)

            }

            return true
        }

        else if (whichfoot == "not foot") {

            lastAction.setText(R.string.jazzhandz)

//            d("Paul", "List lengths events ${eventlist.size} event times ${eventlisttime.size} routine times ${routineeventlisttime.size} beats ${eventlistbeats.size}    ")

//            d("Paul Thisevent", "Jazz Handz time? Foot: $whichfoot lastfoot $lastfoot stepball $solvestepballchange step $whichstep tap separation $tapseparation prev $previoustapseparation weight now $weighton prev $previousweighton")

//            d(" Jazzhand Events", "Event list id $thiseventid time $eventtime beat $thisbeat event $thisevent  Lengths: id: ${eventlistid.size} time: ${eventlisttime.size} beat: ${eventlistbeats.size} events: ${eventlist.size}")

        }

        return false
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {

        e1!!; e2!!

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
        val bpm = currenttempo[currenttempolistsize-1]
//        currenttempo.size

        val tripletaptimeout = 60000/bpm  // timeout time in ms   /* Length of two beats*/
        val doubletaptimeout = 60000/bpm * 0.3333  // timeout time in ms    /* Longer than this is a single step*/

//        d("Paul", "tempo list $currenttempo size ${currenttempolistsize} bpm $bpm timeout $tripletaptimeout doubletap $doubletaptimeout")
//        d("Paul", "bpm $")

        // truth table for kick possible  //  add <=  as needed

        val kickpossible = when {
            tapseparation > tripletaptimeout -> "Single"   // works for kick and kick-crunch

            tapseparation > doubletaptimeout && previoustapseparation < doubletaptimeout && beattapseparation < tripletaptimeout -> "Step-tap"  //   && beattapseparation < tripletaptimeout  // this works for crunch
            tapseparation > doubletaptimeout && previoustapseparation > doubletaptimeout -> "Step-hop"  //  && beattapseparation < tripletaptimeout   //  works for kick and crunch

            tapseparation < doubletaptimeout && previoustapseparation < doubletaptimeout -> "Ball-step" // do we need < triple???   && beattapseparation < tripletaptimeout // this works for crunch
            tapseparation < doubletaptimeout && previoustapseparation > doubletaptimeout -> "Ball-tap"  //   && beattapseparation < tripletaptimeout // works for kick
            else -> "kick not possible"
        }

        val pointerlocx = e1.x
        val pointerlocy = e1.y

        val whichfoot = when {
            pointerlocx > leftimagelocx && pointerlocx < leftimagemaxx && pointerlocy > leftimagelocy && pointerlocy < leftimagemaxy -> "Leftfoot"
            pointerlocx > rightimagelocx && pointerlocx < rightimagemaxx && pointerlocy > rightimagelocy && pointerlocy < rightimagemaxy -> "Rightfoot"
            else -> "not foot"
        }

        if (whichfoot != "not foot") {

            val currenttime = e1.eventTime
            val currentX = e1.x
            val currentY = e1.y

            downeventlist.add(currenttime.toInt())
            downeventlist_x.add(currentX.toInt())
            downeventlist_y.add(currentY.toInt())
        }

        val textanim = ObjectAnimator.ofInt(lastAction, "textColor", Color.WHITE, Color.RED, Color.GRAY)

        try {
            if (abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH || abs(e1.x - e2.x) > SWIPE_MAX_OFF_PATH)
                return false

            if (e1.x - e2.x > SWIPE_MIN_DISTANCE_X && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // right to left swipe
                //                if (pointerlocx > leftimagelocx && pointerlocx < leftimagemaxx && pointerlocy > leftimagelocy && pointerlocy < leftimagemaxy) {
                //                }
                //                    return false
            }
            //            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE_X && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            //                // left to right swipe
            ////                    Toast.makeText(getApplicationContext(), "Swipe left to right", Toast.LENGTH_SHORT).show();
            //
            //            }
            else if (e1.y - e2.y > SWIPE_MIN_DISTANCE_Y && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                // bottom to top
                //                d("Paul", "Guesture OnFling Listener Swipe bottom to top")
                //   Step is resolving first so weights on kicks are opposite to truth table

//                d("paul bpm", "Kick possible $kickpossible weight on $weighton previous $previousweighton")
//                d("paul bpm", "down events $eventlist times $downeventlist")

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

                    else -> "not left kick"
                }

                val rightkickif = when {
                    whichfoot == "Rightfoot" && kickpossible == "Single" && weighton != "Left" && previousweighton != "Right" -> "Rightkick"
                    whichfoot == "Rightfoot" && kickpossible == "Single" && weighton != "Left" && previousweighton != "Left" -> "Rightkickcrunch"

                    whichfoot == "Rightfoot" && kickpossible == "Step-tap" && weighton != "Left" && previousweighton != "Right" -> "Rightkick"
//                        whichfoot == "Rightfoot" && kickpossible == "Step-tap" && weighton != "Right" && previousweighton != "Left" -> "Rightkickcrunch"  // think not needed

                    whichfoot == "Rightfoot" && kickpossible == "Step-hop" && weighton != "Left" && previousweighton != "Right" -> "Rightkick"
                    whichfoot == "Rightfoot" && kickpossible == "Step-hop" && weighton != "Right" && previousweighton != "Left" -> "Rightkickcrunch"

                    whichfoot == "Rightfoot" && kickpossible == "Ball-step" && weighton != "Left" && previousweighton != "Left" -> "Rightkick"
                    whichfoot == "Rightfoot" && kickpossible == "Ball-step" && weighton != "Right" && previousweighton != "Right" -> "Rightkickcrunch"

                    whichfoot == "Rightfoot" && kickpossible == "Ball-tap" && weighton != "Right" -> "Rightkickcrunch"
                    whichfoot == "Rightfoot" && kickpossible == "Ball-tap" && weighton != "Left" -> "Rightkickcrunch"
                    else -> "not right kick"
                }

                // left or right kick or kickcrunch depending on above variables

                if (leftkickif == "Leftkick") {

//                    d("Paul Thisevent jazz", "Left kick weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")
//                      d("Paul", "Left kick $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")

                    danceImage.setImageResource(R.drawable.leftkickimage)
                    lastAction.setText(R.string.leftkick)

                    textanim.duration = 200
                    textanim.setEvaluator(ArgbEvaluator())
                    textanim.repeatMode = ValueAnimator.REVERSE
                    textanim.repeatCount = Animation.ABSOLUTE
                    textanim.start()

                    weightonlist.removeAt(downlen - 1) // clear stepto Left weight
                    weightonlist.add("Right")
                    eventlist.removeAt(numberevents-1)
                    eventlist.add("Left kick")

//                    d(" Left Fling Events", "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}")

                    val kicktime = when (eventlisttime.size) {
                        2 -> 0
                        else -> eventlisttime[eventlisttime.size - 1]
                    }

                    if (createmodelist[0]) {
//                        var danceelement = RoutineElement(0, 1, 11, eventlisttime[numberevents - 1], eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Right")
                        val danceelement = RoutineElement(0, 1, 11, kicktime, eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Right")

//                    d("Paul L Kick", "R kick time ${eventlisttime[numberevents-1]} routine time ${routineeventlisttime[numberevents-1]}")

                        danceactivityviewmodel =
                            ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                        danceactivityviewmodel.insertElement(danceelement)
                    }

                    return true

                }

                if (leftkickif == "Leftkickcrunch") {

//                        d("Paul", "Left kick-crunch $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")
//                    d("Paul Thisevent jazz", "Left kickcrunch weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")

                    danceImage.setImageResource(R.drawable.leftkickcrunchimage)
                    lastAction.setText(R.string.leftkickreplace)

                    textanim.duration = 200
                    textanim.setEvaluator(ArgbEvaluator())
                    textanim.repeatMode = ValueAnimator.REVERSE
                    textanim.repeatCount = Animation.ABSOLUTE
                    textanim.start()

                    weightonlist.removeAt(downlen - 1) // clear stepto Left weight
                    weightonlist.add("Right")
                    eventlist.removeAt(numberevents-1)
                    eventlist.add("Left kick-replace")

                    val kicktime = when (eventlisttime.size) {
                        2 -> 0
                        else -> eventlisttime[eventlisttime.size - 1]
                    }

//                    d(" Left Fling Events", "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}")

                    if (createmodelist[0]) {
                        val danceelement = RoutineElement(0, 1, 13, kicktime, eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Right"
                        )

//                        d("Paul L Kick-R", "L Kick-R time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                        danceactivityviewmodel =
                            ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                        danceactivityviewmodel.insertElement(danceelement)
                    }

                    return true
                }


                if (rightkickif == "Rightkick") {

//                    d("Paul Thisevent jazz", "Right kick weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")

                    danceImage.setImageResource(R.drawable.rightkickimage)
                    lastAction.setText(R.string.rightkick)

//                        downeventlist.add(currenttime)
                    weightonlist.removeAt(downlen - 1) // clear stepto Right weight
                    weightonlist.add("Left")
                    eventlist.removeAt(numberevents-1)
                    eventlist.add("Right kick")

                    val kicktime = when (eventlisttime.size) {
                        2 -> 0
                        else -> eventlisttime[eventlisttime.size - 1]
                    }

//                        d("Paul", "Right kick weightwason $previousweighton")
//                    d(" Paul R Kick", "Right kick $eventlist  event times $eventlisttime routine times $routineeventlisttime}")

                    if (createmodelist[0]) {
//                        var danceelement = RoutineElement(0, 1, 12, eventlisttime[numberevents - 1], eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Left")
                        val danceelement = RoutineElement(0, 1, 12, kicktime, eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Left")

//                        d("Paul R Kick", "R kick time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                        danceactivityviewmodel =
                            ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                        danceactivityviewmodel.insertElement(danceelement)
                    }

                    return true
                }

                if (rightkickif == "Rightkickcrunch") {

//                    d("Paul Thisevent jazz", "Right kickcrunch weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")

                    danceImage.setImageResource(R.drawable.rightkickcrunchimage)
                    lastAction.setText(R.string.rightkickrepalce)

                    weightonlist.removeAt(downlen - 1) // clear step to right weight
                    weightonlist.add("Left")

                    eventlist.removeAt(numberevents-1)
                    eventlist.add("Right kick-replace")

                    val kicktime = when (eventlisttime.size) {
                        2 -> 0
                        else -> eventlisttime[eventlisttime.size - 1]
                    }

//                        d("Paul", "Right kickcrunch weightwason $previousweighton")
//                    d(" Right Fling Events", "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}")

                    if (createmodelist[0]) {

                        val danceelement = RoutineElement(0, 1, 14, kicktime, eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Left")

//                        d("Paul R Kick", "R kick time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                        danceactivityviewmodel =
                            ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                        danceactivityviewmodel.insertElement(danceelement)
                    }

                    return true
                }

                else if (whichfoot == "not foot"){

                    lastAction.setText(R.string.jazzhandz)

//                    d("Paul", "List lengths events ${eventlist.size} event times ${eventlisttime.size} routine times ${routineeventlisttime.size} beats ${eventlistbeats.size}    ")

//            d("Paul Thisevent", "Jazz Handz time? Foot: $whichfoot lastfoot $lastfoot stepball $solvestepballchange step $whichstep tap separation $tapseparation prev $previoustapseparation weight now $weighton prev $previousweighton")

//            d(" Jazzhand Events", "Event list id $thiseventid time $eventtime beat $thisbeat event $thisevent  Lengths: id: ${eventlistid.size} time: ${eventlisttime.size} beat: ${eventlistbeats.size} events: ${eventlist.size}")

                }

//                d("Invalid kick", "Event list id ${eventlistid.max()} time ${eventlisttime.max()} beat ${eventlistbeats.max()} event ${eventlist.max()}")

            }
//            else if (e2.y - e1.y > SWIPE_MIN_DISTANCE_Y && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//                // top to bottom
//                //                    Toast.makeText(getApplicationContext(), "Swipe top to bottom", Toast.LENGTH_SHORT).show();
////                d("Paul", "Guesture OnFling Listener Swipe top to bottom")
//            }

        } catch (exception: Exception) {
            //                // nothing
        }

        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {

//        val currenttime = e1!!.eventTime
//        val currentX = e1.x
//        val currentY = e1.y
//
//        var downlen = downeventlist.size

//        d("Paul", "down events before scroll $downeventlist")
//        d("Paul", "event times before scroll $eventlisttime")
//
//        try {
//            if (downeventlist[downlen - 1] == downeventlist[downlen - 2]) {   // think most touches count as a scroll, prevent double addition from small movement
//                downeventlist.removeAt(downlen - 1)
//                downeventlist_x.removeAt(downlen - 1)
//                downeventlist_y.removeAt(downlen - 1)
//                downlen = downeventlist.size
//            }
//        } catch (exception: Exception){
//        }

//        d("Paul", "down events on scroll $downeventlist")

        e1!!; e2!!

        val weightonlist = weightonlist
        val weightonlistlen = weightonlist.size
        val weighton = weightonlist[weightonlistlen - 1]
//        val previousweighton = weightonlist[weightonlistlen - 2]  // not used here

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
        val bpm = currenttempo[currenttempolistsize-1]
//        currenttempo.size

        val tripletaptimeout = 60000/bpm  // timeout time in ms   /* Length of two beats*/
        val doubletaptimeout = 60000/bpm * 0.3333  // timeout time in ms    /* Longer than this is a single step*/

        val pointerlocx = e1.x
        val pointerlocy = e1.y

        val whichfoot = when {
            pointerlocx > leftimagelocx && pointerlocx < leftimagemaxx && pointerlocy > leftimagelocy && pointerlocy < leftimagemaxy -> "Leftfoot"
            pointerlocx > rightimagelocx && pointerlocx < rightimagemaxx && pointerlocy > rightimagelocy && pointerlocy < rightimagemaxy -> "Rightfoot"
            else -> "not foot"
        }

        if (whichfoot != "not foot") {

            val currenttime = e1.eventTime
            val currentX = e1.x
            val currentY = e1.y

            downeventlist.add(currenttime.toInt())
            downeventlist_x.add(currentX.toInt())
            downeventlist_y.add(currentY.toInt())
        }

        val downlen = downeventlist.size

//        val textanim = ObjectAnimator.ofInt(lastAction, "textColor", Color.WHITE, Color.RED, Color.GRAY)

//        val SCROLL_MIN_DISTANCE_X = 80
        val SCROLL_MIN_DISTANCE_X = (leftimagemaxx - leftimagelocx)*6/10
        val SCROLL_MIN_DISTANCE_Y = 80

        /** Swipe max off path.  */
//        private val SWIPE_MAX_OFF_PATH = 250
        val SCROLL_MAX_OFF_PATH = 500

        /** Swipe threshold velocity.  */
        val SCROLL_THRESHOLD_VELOCITY = 10

        try {
            if (abs(e1.y - e2.y) > SCROLL_MAX_OFF_PATH || abs(e1.x - e2.x) > SCROLL_MAX_OFF_PATH)
                return false

            if (e1.x - e2.x > SCROLL_MIN_DISTANCE_X && abs(distanceX) > SCROLL_THRESHOLD_VELOCITY) {
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
                    else -> "scroll not possible"
                }

                val numberevents = eventlist.size

//                d("Paul", "Right left scroll foot $whichfoot scroll $scrollpossible weighton $weighton tap $tapseparation")
//                d("Paul", "Right left scroll $downeventlist")

                val leftSuzieQif = when {
                    whichfoot == "Leftfoot" && scrollpossible == "Single" && weighton != "Right" -> "LeftSuzieQ"  //  Truth table version
                    whichfoot == "Leftfoot" && scrollpossible == "Step-tap" && weighton != "Right" -> "LeftSuzieQ"
                    whichfoot == "Leftfoot" && scrollpossible == "Step-hop" && weighton != "Right" -> "LeftSuzieQ"

                    whichfoot == "Leftfoot" && scrollpossible == "Ball-step" && weighton != "Right" -> "LeftSuzieQ"
                    whichfoot == "Leftfoot" && scrollpossible == "Ball-tap" && weighton != "Right" -> "LeftSuzieQ"

                    else -> "not left suzieq"
                }

//                d("Paul", "Susie Q  $leftSuzieQif")

                if (leftSuzieQif == "LeftSuzieQ") {

//                    d("Paul Thisevent jazz", "Right kick weightwason $previousweighton downlen $downlen weightlistlen ${weightonlist.size}")

                    danceImage.setImageResource(R.drawable.leftsuzieimage)
                    lastAction.setText(R.string.lefttsuzieq)

                    weightonlist.removeAt(downlen - 1) // clear stepto Right weight
                    weightonlist.add("Right")
                    eventlist.removeAt(numberevents-1)
                    eventlist.add("Left SuzieQ")

//                    d("Paul", "number events $numberevents event times on scroll $eventlisttime")
//                    d("Paul", "event list $eventlist  number events $numberevents event times on scroll $eventlisttime")
//
//                    d("Paul", "event times on scroll $eventlisttime")
//                    d("Paul", "routine times on scroll $routineeventlisttime")

                    val suzieqtime = when (eventlisttime.size) {
                        2 -> 0
                        else -> eventlisttime[eventlisttime.size - 1]
                    }

//                        d("Paul", "Left Suzie Q weightwason $previousweighton")
//                    d("Paul L Suzie", "L Suzie time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                    if (createmodelist[0]) {
//                        var danceelement = RoutineElement(0, 1, 15, eventlisttime[numberevents - 1], eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Right")
                        val danceelement = RoutineElement(0, 1, 15, suzieqtime, eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Right")

//                        d("Paul L Suzie", "L Suzie time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                        danceactivityviewmodel =
                            ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                        danceactivityviewmodel.insertElement(danceelement)
                    }

                    return true
                }

            }

            if (e2.x - e1.x > SCROLL_MIN_DISTANCE_X && abs(distanceX) > SCROLL_THRESHOLD_VELOCITY) {
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
                    else -> "scroll not possible"
                }

                val numberevents = eventlist.size

//                d("Paul", "Left right scroll foot $whichfoot scroll $scrollpossible weighton $weighton tap $tapseparation")
//                d("Paul", "Right left scroll $downeventlist")

                val rightSuzieQif = when {
                    whichfoot == "Rightfoot" && scrollpossible == "Single" && weighton != "Left" -> "RightSuzieQ"  //  Truth table version
                    whichfoot == "Rightfoot" && scrollpossible == "Step-tap" && weighton != "Left" -> "RightSuzieQ"
                    whichfoot == "Rightfoot" && scrollpossible == "Step-hop" && weighton != "Left" -> "RightSuzieQ"

                    whichfoot == "Rightfoot" && scrollpossible == "Ball-step" && weighton != "Left" -> "RightSuzieQ"
                    whichfoot == "Rightfoot" && scrollpossible == "Ball-tap" && weighton != "Left" -> "RightSuzieQ"

                    else -> "not right suzieq"
                }

//                d("Paul", "Susie Q  $rightSuzieQif")

                if (rightSuzieQif == "RightSuzieQ") {

                    danceImage.setImageResource(R.drawable.rightsuzieimage)
                    lastAction.setText(R.string.rightsuzieq)

                    weightonlist.removeAt(downlen - 1) // clear stepto Right weight
                    weightonlist.add("Left")
                    eventlist.removeAt(numberevents-1)
                    eventlist.add("Left SuzieQ")

//                        d("Paul", "Left Suzie Q weightwason $previousweighton")

                    val suzieqtime = when (eventlisttime.size) {
                        2 -> 0
                        else -> eventlisttime[eventlisttime.size - 1]
                    }

//                    d("Paul R Suzie", "R Suzie time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                    if (createmodelist[0]) {
//                        var danceelement = RoutineElement(0, 1, 16, eventlisttime[numberevents - 1], eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Left")
                        val danceelement = RoutineElement(0, 1, 16, suzieqtime, eventlistbeats[numberevents - 1], routineeventlisttime[numberevents - 1], "None", "Left")

//                        d("Paul R Suzie", "R Suzie time ${eventlisttime[numberevents - 1]} routine time ${routineeventlisttime[numberevents - 1]}")

                        danceactivityviewmodel =
                            ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                        danceactivityviewmodel.insertElement(danceelement)
                    }

                    return true
                }
            }

        } catch (exception: Exception){

            d("Paul", "Scroll exception")

        }
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {

//        d("Paul", "tempo list $currenttempo size $currenttempolistsize bpm $bpm beat timeout $tripletaptimeout doubletap $doubletaptimeout")

        val currenttempolistsize = currenttempo.size
        val bpm = currenttempo[currenttempolistsize-1]
//        currenttempo.size

        val tripletaptimeout = 60000/bpm  // timeout time in ms   /* Length of two beats* - test value 600ms/
        val doubletaptimeout = 60000/bpm * 0.3333  // timeout time in ms    /* Longer than this is a single step* -- test value 300ms/

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
            pointerlocx > leftimagelocx && pointerlocx < leftimagemaxx && pointerlocy > leftimagelocy && pointerlocy < leftimagemaxy -> "Leftfoot"
            pointerlocx > rightimagelocx && pointerlocx < rightimagemaxx && pointerlocy > rightimagelocy && pointerlocy < rightimagemaxy -> "Rightfoot"
            else -> "not foot"
        }

        if (whichfoot != "not foot") {

            val currenttime = e.eventTime
            val currentX = e.x
            val currentY = e.y

            downeventlist.add(currenttime.toInt())
            downeventlist_x.add(currentX.toInt())
            downeventlist_y.add(currentY.toInt())
        }

        val downlen = downeventlist.size

//        d("Paul bpm", "down event list $downeventlist")

        val tapseparation = downeventlist[downlen - 1] - downeventlist[downlen - 2]
        val previoustapseparation = downeventlist[downlen - 2] - downeventlist[downlen - 3]
        val beattapseparation = tapseparation + previoustapseparation

        val lastdownx = downeventlist_x[downlen - 2]
        val lastdowny = downeventlist_y[downlen - 2]

        val secondlastdownx = downeventlist_x[downlen - 3]
        val secondlastdowny = downeventlist_y[downlen - 3]

        val lastfoot = when {
            lastdownx in (leftimagelocx + 1) until leftimagemaxx && lastdowny > leftimagelocy && lastdowny < leftimagemaxy -> "Leftfoot"
            lastdownx in (rightimagelocx + 1) until rightimagemaxx && lastdowny > rightimagelocy && lastdowny < rightimagemaxy -> "Rightfoot"
            else -> "false"
        }

        val secondlastfoot = when {
            secondlastdownx in (leftimagelocx + 1) until leftimagemaxx && secondlastdowny > leftimagelocy && secondlastdowny < leftimagemaxy -> "Leftfoot"
            secondlastdownx in (rightimagelocx + 1) until rightimagemaxx && secondlastdowny > rightimagelocy && secondlastdowny < rightimagemaxy -> "Rightfoot"
            else -> "false"
        }

        // Step types

        val stepif = when {
            tapseparation > tripletaptimeout -> "Single"  // step
            tapseparation < 0 -> "Single"  // step - first in routine
            tapseparation > doubletaptimeout && previoustapseparation > doubletaptimeout -> "Step-hop" // step step
            tapseparation > doubletaptimeout && previoustapseparation < doubletaptimeout && beattapseparation > tripletaptimeout -> "Step-hop"  // ball-change step
//            tapseparation > doubletaptimeout && previoustapseparation < tripletaptimeout -> true
//            tapseparation > doubletaptimeout && tapseparation < tripletaptimeout && beattapseparation > doubletaptimeout -> true
            else -> "not step"
        }

        val ballchangestepif = when {
            tapseparation > doubletaptimeout && previoustapseparation < doubletaptimeout && beattapseparation < tripletaptimeout -> "Change-step" // may resolve different than step-hop above as this is within two beats - default to steps and hops

//            tapseparation > doubletaptimeout && previoustapseparation < doubletaptimeout && beattapseparation > tripletaptimeout -> "Step-hop"  // ball-change step
//            tapseparation > doubletaptimeout && previoustapseparation < tripletaptimeout -> true
//            tapseparation > doubletaptimeout && tapseparation < tripletaptimeout && beattapseparation > doubletaptimeout -> true
            else -> "not ballchangestep"
        }

        val ballchangetapif = when {
            tapseparation < doubletaptimeout && beattapseparation > doubletaptimeout && previoustapseparation > doubletaptimeout -> "stepballchange"  // changed beat separation to > double from > triple
            tapseparation < doubletaptimeout && previoustapseparation < doubletaptimeout -> "stepballchange"  // put this back in
            tapseparation < doubletaptimeout && beattapseparation > tripletaptimeout && previoustapseparation > doubletaptimeout -> "stepballchange"

            tapseparation < doubletaptimeout && beattapseparation > doubletaptimeout && beattapseparation < tripletaptimeout && previoustapseparation > doubletaptimeout -> "ballchangetap"

//            beattapseparation > doubletaptimeout && beattapseparation < tripletaptimeout && weighton == "0" -> true
//            tapseparation < doubletaptimeout && previoustapseparation > tripletaptimeout -> true  // maybe not needed?
//            tapseparation < doubletaptimeout && beattapseparation < tripletaptimeout && previoustapseparation < doubletaptimeout -> true  // maybe not needed?
            else -> "not ballchangetap"
        }

        val solvestepballchange = when {
            ballchangetapif == "stepballchange" && weighton != "Right" && lastfoot == "Leftfoot" && whichfoot == "Rightfoot" -> "righttapball1"
            ballchangetapif == "stepballchange" && weighton != "Left" && lastfoot == "Rightfoot" && whichfoot == "Lefttfoot" -> "lefttapball1"

            ballchangetapif == "stepballchange" && previousweighton == "Right" && weighton != "Right" && lastfoot == "Rightfoot" && whichfoot == "Rightfoot" -> "righttapball2"
            ballchangetapif == "stepballchange" && previousweighton == "Left" && weighton != "Left" && lastfoot == "Leftfoot" && whichfoot == "Leftfoot" -> "lefttapball2" // may resolve to left toe-tap if onUp method used

            // may resolve to left toe-tap if onUp method used
            ballchangetapif == "stepballchange" && previousweighton == "Left" && weighton != "Right" && lastfoot == "Leftfoot" && whichfoot == "Rightfoot" -> "righttapball3"
            ballchangetapif == "stepballchange" && previousweighton == "Right" && weighton != "Left" && lastfoot == "Rightfoot" && whichfoot == "Leftfoot" -> "lefttapball3"
            else -> "not stepballchange"
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
            stepif == "Step-hop" && previousweighton == "Left" && lastfoot == "Rightfoot "&& weighton == "Left" && whichfoot == "Rightfoot" -> "rightstep"  // this is also in to resolve taps better

            stepif == "Step-hop" && previousweighton == "Left" && weighton == "Left" && whichfoot == "Leftfoot" -> "lefthop" //
            stepif == "Step-hop" && previousweighton == "Right" && lastfoot == "Rightfoot" && weighton == "Left" && whichfoot == "Leftfoot" -> "lefthop" // for hop after right kickcrunch
            stepif == "Step-hop" && previousweighton == "Right" && lastfoot == "Leftfoot" && weighton == "Left" && whichfoot == "Leftfoot" -> "lefthop" // toetap - step then this hop
            stepif == "Step-hop" && previousweighton == "Left" && weighton == "0" && whichfoot == "Leftfoot" -> "lefthop" //

            stepif == "Step-hop" && previousweighton == "Right" && weighton == "Right" && whichfoot == "Rightfoot" -> "righthop"  // step prior to hop
            stepif == "Step-hop" && previousweighton == "Left" && lastfoot == "Leftfoot" && weighton == "Right" && whichfoot == "Rightfoot" -> "righthop" // kickcrunch then this hop
            stepif == "Step-hop" && previousweighton == "Left" && lastfoot == "Rightfoot" && weighton == "Right" && whichfoot == "Rightfoot" -> "righthop" // toetap - step then this hop
            stepif == "Step-hop" && previousweighton == "Right" && weighton == "0" && whichfoot == "Rightfoot" -> "righthop" //

            stepif == "Step-hop" && lastfoot == "Leftfoot" && weighton == "Right" && whichfoot == "Leftfoot" -> "leftstep" // Left kickcrunch -> tap  //  *removed - revert to step - doesn't work
            stepif == "Step-hop" && lastfoot == "Rightfoot" && weighton == "Left" && whichfoot == "Rightfoot" -> "rightstep" // Left kickcrunch -> tap // *removed - revert to step - doesn't work

            ballchangetapif == "stepballchange" && previousweighton == "Left" && weighton == "Left" && whichfoot == "Leftfoot" -> "lefthop" // need previous weight because leftdown resolves in step
            ballchangetapif == "stepballchange" && previousweighton == "Right" && weighton == "Right" && whichfoot == "Rightfoot" -> "righthop"

            ballchangetapif == "stepballchange" && previousweighton == "Right" && weighton != "Right" && lastfoot == "Leftfoot" && whichfoot == "Rightfoot" -> "ballchangetoright" // may resolve to left toe-tap if onUp method used
            ballchangetapif == "stepballchange" && previousweighton == "Left" && weighton != "Left" && lastfoot == "Rightfoot" && whichfoot == "Leftfoot" -> "ballchangetoleft" // may resolve to left toe-tap if onUp method used

            ballchangetapif == "stepballchange"  && weighton != "Right" && lastfoot == "Leftfoot" && whichfoot == "Leftfoot" -> "lefttoetap"  // specify previous weight as otherwise catches right tap???  && previousweighton =="Right"
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
            solvestepballchange == "lefttapball2" && secondlastfoot == "Lefttfoot" && lastfoot == "Leftfoot" && whichfoot == "Leftfoot" -> "lefttap"   // button mashing Left

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

            else -> "no step"
        }

        // note downevent list is added to on touch - save time and then add for each movement below

        val currenttime = downeventlist[downlen - 1]  // Kotlin index starts at sero

//        d("Thisevent", "Foot: $whichfoot lastfoot $lastfoot stepif $stepif stepball $solvestepballchange step $whichstep tap separation $tapseparation prev $previoustapseparation weight now $weighton prev $previousweighton")

// set inital parameters for event list (modified after event resolved

        if (whichfoot != "not foot") {

            val thiseventid = eventlisttime.size - 1
            eventlistid.add(thiseventid)

            if (eventlisttime.size == 0) {
                eventlisttime.add(currenttime)
                eventlisttime.add(0)
            }

            val eventlisttimesize = eventlisttime.size

//            d("bpm", "before mod eventlisttime size $eventlisttimesize list $eventlisttime")
//            d("bpm", "before mod  tap $tapseparation routine times $routineeventlisttime ")

            if (eventlisttime.size > 4 && tapseparation.absoluteValue > 5000) {   // if long delay remove last and replace assuming latest event is on a beat

                if (tapseparation.absoluteValue < 1000000) {

                    eventlisttime.removeAt(eventlisttimesize-1)

                    routineeventlisttime.removeAt(eventlisttimesize-4)

                    eventlistbeats.removeAt(eventlisttimesize-4)

                    var routineeventtime = (eventlisttime[eventlisttimesize - 2] - eventlisttime[1]).div(1000)
                    routineeventlisttime.add(routineeventtime)

                    var thisbeattoround =
                        (eventlisttime[eventlisttimesize - 2] - eventlisttime[1]).div(tripletaptimeout) //  need to think how to resolve for jazz handz - hold
                    var thisroundeddownbeat = thisbeattoround.toDouble().nextDown().toInt()
                    eventlistbeats.add(thisroundeddownbeat)

                    eventlisttime.add(((eventlisttime[eventlisttimesize - 2]) + tripletaptimeout))
                    routineeventtime = (eventlisttime[eventlisttimesize - 1] - eventlisttime[1]).div(1000)
                    routineeventlisttime.add(routineeventtime)

                    thisbeattoround =
                        (eventlisttime[eventlisttimesize - 1] - eventlisttime[1]).div(tripletaptimeout) //  need to think how to resolve for jazz handz - hold
                    thisroundeddownbeat = thisbeattoround.toDouble().nextDown().toInt()
                    eventlistbeats.add(thisroundeddownbeat)

//                    d("bpm", "4+ with delay eventlisttime size $eventlisttimesize list $eventlisttime")
//                    d("bpm", "4+ with delay  tap $tapseparation routine times $routineeventlisttime ")

                } else {  // restarting routine
                    eventlisttime.removeAt(eventlisttimesize-1)
                    routineeventlisttime.removeAt(eventlisttimesize-4)
                    eventlistbeats.removeAt(eventlisttimesize-4)

                    eventlisttime.add(((eventlisttime[eventlisttimesize - 2]) + tripletaptimeout))

                    val routineeventtime = (eventlisttime[eventlisttimesize - 1] - eventlisttime[1]).div(1000)
                    routineeventlisttime.add(routineeventtime)

                    val thisbeattoround =
                        (eventlisttime[eventlisttimesize - 1] - eventlisttime[1]).div(tripletaptimeout) //  need to think how to resolve for jazz handz - hold
                    val thisroundeddownbeat = thisbeattoround.toDouble().nextDown().toInt()
                    eventlistbeats.add(thisroundeddownbeat)

//                    d("bpm", "here - not right?")
//                    d("bpm", "4+ restart routine eventlisttime size $eventlisttimesize list $eventlisttime")
//                    d("bpm", "4+ restart routine tap $tapseparation routine times $routineeventlisttime ")
                }

            } else if (eventlisttime.size > 4) {     //  normal case, take previous time and add tap separation (with or without a delay)

                val eventlisttimeseparation1 = eventlisttime[eventlisttimesize - 1] - eventlisttime[eventlisttimesize - 2]

                when {
                    eventlisttimeseparation1 > 5000 -> {

                        val replacedeventlisttime = eventlisttime[eventlisttimesize - 2] + tripletaptimeout

                        eventlisttime.removeAt(eventlisttimesize-1)

                        eventlisttime.add(replacedeventlisttime)

            //                    d("bpm", "3+ previous delay eventlisttime size $eventlisttimesize list $eventlisttime")
            //                    d("bpm", "3+ previous delay tap $tapseparation routine times $routineeventlisttime ")

                    }
                    eventlisttimeseparation1.absoluteValue > 1000000 -> {

                        val previouseventlisttime = eventlisttime[eventlisttimesize - 2]

                        eventlisttime.removeAt(eventlisttimesize - 1)

                        eventlisttime.add(previouseventlisttime + tapseparation)

            //                    d("bpm", "3+ previous delay eventlisttime size $eventlisttimesize list $eventlisttime")
            //                    d("bpm", "3+ previous delay tap $tapseparation routine times $routineeventlisttime ")
                    }
                    else -> {

                        val previouseventlisttime = eventlisttime[eventlisttimesize - 1]

                        eventlisttime.add(previouseventlisttime + tapseparation)
                    }
                }

            } else if (eventlisttime.size == 1) {  // case if first down touch is not on a foot and eventlisttime has been cleared
                if (tapseparation.absoluteValue > 5000) {  // delay before second action
                    eventlisttime.clear()
                    eventlisttime.add(currenttime)
                    eventlisttime.add(0)
                    eventlisttime.add(tripletaptimeout + 1)   // + 1 to ensure first move is on beat 1
                }

                else {
//                    eventlisttime.add(tapseparation)    /* this doesn't do anything currently */
                    val lasteventlisttime = eventlisttime[eventlisttime.size - 1]
                    eventlisttime.add(lasteventlisttime)   // this should work
                }

            } else if (eventlisttime.size == 2) {  // delay before second action
                if (tapseparation.absoluteValue > 5000) {  // delay before second action  - currently this is always true
                    eventlisttime.clear()
                    eventlisttime.add(currenttime)
                    eventlisttime.add(0)
                    eventlisttime.add(tripletaptimeout + 1)   // + 1 to ensure first move is on beat 1
                }

                else {
//                    eventlisttime.add(tapseparation)    /* this doesn't do anything currently */
                    val lasteventlisttime = eventlisttime[eventlisttime.size - 1]
                    eventlisttime.add(lasteventlisttime)   // this should work
                }

            } else if (eventlisttime.size == 3) {
                if (tapseparation.absoluteValue > 5000) {  // delay before first action
                    eventlisttime.clear()
                    eventlisttime.add(currenttime)
                    eventlisttime.add(0)
                    eventlisttime.add(tripletaptimeout)
                    eventlisttime.add(tripletaptimeout + tapseparation)  //  this should work - should it be spacertime?
                } else {
                    val lasttapseparation = downeventlist[downeventlist.size - 1] - downeventlist[downeventlist.size - 2]
                    eventlisttime.add(tripletaptimeout + lasttapseparation)  // this works
                }

            } else if (eventlisttime.size == 4) {

                if (tapseparation.absoluteValue > 5000) {  // delay before second action
                    eventlisttime.clear()
                    eventlisttime.add(currenttime)
                    eventlisttime.add(0)
                    eventlisttime.add(tripletaptimeout)
                    eventlisttime.add(tripletaptimeout + previoustapseparation)
//                    eventlisttime.add(spacertime + previoustapseparation + tapseparation)  //
                    eventlisttime.add(tripletaptimeout + previoustapseparation + tripletaptimeout)  // if a over 5s delay, add spacertime for 1 beat
                } else {
                    val lasteventlisttime = eventlisttime[eventlisttime.size - 1]
                    eventlisttime.add(lasteventlisttime + tapseparation)   // this should work
                }
            }

//            d("bpm", "after mod eventlisttime size $eventlisttimesize list $eventlisttime")

            val routineeventtime = (eventlisttime[eventlisttimesize-1] - eventlisttime[1]).div(1000)
            routineeventlisttime.add(routineeventtime)

//            val thisbeattoround = (eventlisttime[eventlisttimesize-1]  - eventlisttime[1]).div(tripletaptimeout) //  need to think how to resolve for jazz handz - hold
//            val thisroundeddownbeat = thisbeattoround.toDouble().nextDown().toInt()

//            d("Paul", "event size $eventlisttimesize times $eventlisttime")

            val thisbeattoround = eventlisttime[eventlisttime.size - 1].toFloat().div(tripletaptimeout)

            val thisroundeddownbeat = thisbeattoround.nextDown().toInt()

            eventlistbeats.add(thisroundeddownbeat)

//            d("bpm", "final mod eventlisttime size $eventlisttimesize list $eventlisttime")
//            d("bpm", "final mod eventlisttime beats $thisbeattoround list $eventlistbeats")
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

//        val eventtime = eventlisttime[numberevents-1]
        val eventtime =  when {
            eventlisttime.size > 2 -> eventlisttime[eventlisttime.size - 1]
            else -> 0
        }

//        val thisevent = eventlist[numberevents-1]
//        val thiseventid = eventlistid[numberevents-1]

        val thisbeat = eventlistbeats[numberevents-1]


        val routineeventtime = routineeventlisttime[numberevents - 1]
        val thiscomment = "None"
//        d("Paul", "routineeventtime $routineeventtime")

        if (whichstep == "ballchangetoright") {

//            d("Paul", "Ball-change (to right)  $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")

            danceImage.setImageResource(R.drawable.rightstepimage)  // left start
            lastAction.setText(R.string.ballchangetoright)

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

            weightonlist.add("Right")
            eventlist.removeAt(numberevents-1)
            eventlist.add("Ball-change to right")

//            d("Paul", "B-C to right $ballchangetapif weighton $previousweighton $downlen ${weightonlist.size}")
//            d(" B-c toright Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

            if (createmodelist[0]) {
                val danceelement = RoutineElement(0, 1, 10, eventtime, thisbeat, routineeventtime, thiscomment, "Right")
//
                danceactivityviewmodel =
                    ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                danceactivityviewmodel.insertElement(danceelement)
            }

            return true
        }

        if (whichstep == "ballchangetoleft") {

//            d("Paul", "B-C to left *   $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")

            danceImage.setImageResource(R.drawable.leftstepimage)  // Put a delay here to go to Right tap after xxxx millisecs
            lastAction.setText(R.string.ballchangetoleft)

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

            weightonlist.add("Left")
            eventlist.removeAt(numberevents-1)
            eventlist.add("Ball-change to left")

//            d("Paul", "Ball-change to left $ballchangetapif weighton $previousweighton $downlen ${weightonlist.size}")
//            d(" B-c toleft Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

            if (createmodelist[0]) {
                val danceelement = RoutineElement(0, 1, 9, eventtime, thisbeat, routineeventtime, thiscomment, "Left")

                danceactivityviewmodel =
                    ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                danceactivityviewmodel.insertElement(danceelement)
            }
            return true
        }

        if (whichstep == "lefthop") {

//            d("Paul", "left hop *   $pointerlocx $pointerlocy left image  max x $leftimagemaxx y $leftimagemaxy")

            danceImage.setImageResource(R.drawable.lefthopimage)  // Want a different image here - toe without lines?
            lastAction.setText(R.string.lefthop)

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

            if (stepif != "not step") {   //
                weightonlist.add("Left")  //
                eventlist.removeAt(numberevents-1)
                eventlist.add("Left hop")
            }

            if (ballchangestepif != "not ballchangestep") {   //
                weightonlist.add("Left")  //
                eventlist.removeAt(numberevents-1)
                eventlist.add("Left hop")
            }

//            if (ballchangetapif == "stepballchange") {   //
            if (ballchangetapif != "not ballchangestap") {   //
                weightonlist.add("Left")  //
                eventlist.removeAt(numberevents-1)
                eventlist.add("Left hop")
            }

//            d("Paul", "Left hop $ballchangetapif weighton $previousweighton $downlen ${weightonlist.size}")

//            d(" Lefthop Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

            if (createmodelist[0]) {
                val danceelement = RoutineElement(0, 1, 3, eventtime, thisbeat, routineeventtime, thiscomment, "Left")

                danceactivityviewmodel =
                    ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                danceactivityviewmodel.insertElement(danceelement)
            }

            return true
        }

        if (whichstep == "leftstep") {

            danceImage.setImageResource(R.drawable.leftstepimage)//  right start
            lastAction.setText(R.string.leftstep)

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            d("Paul", "L Step current time $currenttime")

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

            if (stepif != "not step") {
                weightonlist.add("Left")
                eventlist.removeAt(numberevents-1)
                eventlist.add("Step to left")
            }

            if (ballchangestepif != "not ballchangestep" && weighton != "0") {  // don't think we need && previousweighton != "0"
                weightonlist.add("Left")
                eventlist.removeAt(numberevents-1)
                eventlist.add("Step to left")
            }

            if (ballchangetapif != "not ballchangetap") {
                weightonlist.add("Left")
                eventlist.removeAt(numberevents-1)
                eventlist.add("Step to left")
            }

//            d("Paul", "Step to Left $ballchangetapif weighton $previousweighton downevents $downeventlist length  $downlen  weight length ${weightonlist.size}")
//            d(" Leftstep Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")
//            d(" Paul L Step", "Left Step id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

            if (createmodelist[0]) {
                val danceelement = RoutineElement(0, 1, 1, eventtime, thisbeat, routineeventtime, thiscomment, "Left")

//            d(" Paul L Step", "Left Step $eventlist  last event ${eventlist[numberevents-1]}  time ${routineeventlisttime[numberevents - 1]}")

                danceactivityviewmodel =
                    ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                danceactivityviewmodel.insertElement(danceelement)
            }

            return false
        }

        if (whichstep == "righthop") {

            danceImage.setImageResource(R.drawable.righthopimage)
            lastAction.setText(R.string.righthop)

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

            if (stepif != "not step") {
                weightonlist.add("Right")
                eventlist.removeAt(numberevents-1)
                eventlist.add("Right hop")
            }

            if (ballchangestepif != "not ballchangestep") {
                weightonlist.add("Right")
                eventlist.removeAt(numberevents-1)
                eventlist.add("Right hop")
            }

            if (ballchangetapif != "not ballchangetap") {
                weightonlist.add("Right")  //
                eventlist.removeAt(numberevents-1)
                eventlist.add("Right hop")
            }

//            d("Paul", "Right hop $ballchangetapif weighton $previousweighton $downlen ${weightonlist.size}")
//            d(" Righthop Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

            if (createmodelist[0]) {
                val danceelement = RoutineElement(0, 1, 4, eventtime, thisbeat, routineeventtime, thiscomment, "Right")

                danceactivityviewmodel =
                    ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                danceactivityviewmodel.insertElement(danceelement)
            }

            return true
        }

        if (whichstep == "rightstep") {

            danceImage.setImageResource(R.drawable.rightstepimage)//  right start
//            lastAction.text = "Right step"

            lastAction.setText(R.string.rightstep)

            textanim.duration = 200
            textanim.setEvaluator(ArgbEvaluator())
            textanim.repeatMode = ValueAnimator.REVERSE
            textanim.repeatCount = Animation.ABSOLUTE
            textanim.start()

            downeventlist.add(currenttime)
            downeventlist_x.add(pointerlocx.toInt())
            downeventlist_y.add(pointerlocy.toInt())

            if (stepif != "not step") {
                weightonlist.add("Right")
                eventlist.removeAt(numberevents-1)
                eventlist.add("Step to right")
            }

            if (ballchangestepif != "not ballchangestep" && weighton != "0") {   // don't think we need && previousweighton != "0"
                weightonlist.add("Right")
                eventlist.removeAt(numberevents-1)
                eventlist.add("Step to right")
            }

            if (ballchangetapif != "not ballchangetap") {
                weightonlist.add("Right")
                eventlist.removeAt(numberevents-1)
                eventlist.add("Step to right")

            }
//            d("Paul", "Step to right $ballchangetapif weighton $previousweighton downevents $downeventlist length  $downlen  weight length  $downlen ${weightonlist.size}")
//            d(" Rightstep Events", "Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")
//            d("Paul", "R Step Event list id $thiseventid time $eventtime beat $thisbeat event ${eventlist.max()}")

            if (createmodelist[0]) {
                val danceelement = RoutineElement(0, 1, 2, eventtime, thisbeat, routineeventtime, thiscomment, "Right")
//            d(" Paul", "R Step Event list id ${danceelement.elementId} time ${danceelement.elementtime} beat ${danceelement.elementbeat}")

                danceactivityviewmodel =
                    ViewModelProvider(this).get(DanceActivityViewModel::class.java)  // may not need this until routine calling is added?

                danceactivityviewmodel.insertElement(danceelement)
            }

//            d("Paul", "R Step List lengths events ${eventlist.size} event times ${eventlisttime.size} routine times ${routineeventlisttime.size} beats ${eventlistbeats.size}    ")

            return false
        }

        // end step hop section
        // Do jazz hands if we get to here?

        else if (whichfoot == "not foot"){

            lastAction.setText(R.string.jazzhandz)

            d("Paul", "List lengths events ${eventlist.size} weights ${weightonlist.size} event times ${eventlisttime.size} routine times ${routineeventlisttime.size} beats ${eventlistbeats.size}    ")

//            d("Paul Thisevent", "Jazz Handz time? Foot: $whichfoot lastfoot $lastfoot stepball $solvestepballchange step $whichstep tap separation $tapseparation prev $previoustapseparation weight now $weighton prev $previousweighton")

//            d(" Jazzhand Events", "Event list id $thiseventid time $eventtime beat $thisbeat event $thisevent  Lengths: id: ${eventlistid.size} time: ${eventlisttime.size} beat: ${eventlistbeats.size} events: ${eventlist.size}")

        }

//        d(" Down Events", "Event list id $thiseventid time $eventtime beat $thisbeat event $eventlist")

        return true
        // end of onDown
    }

    override fun onLongPress(e: MotionEvent?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    private fun getRoutineElementSize(elements: List<RoutineElement>, danceelement: RoutineElement) {
//
////        d("Paul", "Element List current size ${elements.lastIndex + 1} ")
//
////        d("Paul", "Element Index ${danceelement.elementId}")
////        d("Paul", "Last Element Index ${elements[elements.size -1].elementId}")
//
//        danceactivityviewmodel.allRoutineElements.removeObservers(this)
////
////        val lastElementIndex = elements[elements.size -1].elementId
////
////        d("Paul", "Last Element Index $lastElementIndex")
////
////        if (danceelement.routinedancemoveId == 5 && elements[lastElementIndex].routinedancemoveId == 1 && danceelement.elementtime == elements[lastElementIndex].elementtime) {
////            danceactivityviewmodel.deleteLastElement(lastElementIndex-1)
////        } else
////            if (danceelement.routinedancemoveId == 6 && elements[lastElementIndex].routinedancemoveId == 2 && danceelement.elementtime == elements[lastElementIndex].elementtime) {
////            danceactivityviewmodel.deleteLastElement(lastElementIndex-1)
////        } else {
////        }
//
////        danceactivityviewmodel.allRoutineElements.removeObservers(this)
//
//        val newelementindex = elements.lastIndex + 2   // index starts at 0, need next available primary key - key starts at 1
//
//        val newdanceelement = RoutineElement(newelementindex, danceelement.routineelementId, danceelement.routinedancemoveId, danceelement.elementtime, danceelement.elementbeat, danceelement.routineelementtime, danceelement.comment, danceelement.weighton)
//
////        d("Paul", "New Element Index ${newdanceelement.elementId}")
//
////        danceactivityviewmodel.insertElement(newdanceelement)
//
//    }

//    private fun deleteLastElement(danceelement: RoutineElement) {
//
//        d("Paul", "Delete Element here ")
//        danceactivityviewmodel.allRoutineElements.removeObservers(this)
//
//        danceactivityviewmodel.allRoutineElements.observe(this, Observer { element_id -> element_id?.let { this.getLastElement(element_id, danceelement) } })
//    }
//
//    val elementidlist: ArrayList<Int> = arrayListOf()
//
//    fun getLastElement(elements: List<RoutineElement>, danceelement: RoutineElement){
//
//        elementidlist.clear()
//        danceactivityviewmodel.allRoutineElements.removeObservers(this)
//
//        for (i in elements.indices){
//            elementidlist.add(elements[i].elementId)
//        }
//
//        val lastelementindex = elementidlist[elementidlist.size - 2]
//
//
//        d("Paul","delete element list size ${elementidlist.size} element id $lastelementindex")
//
////        danceactivityviewmodel.deleteLastElement(lastelementindex)
//
////        danceactivityviewmodel.allRoutineElements.removeObservers(this)
//    }

}

//            Handler().postDelayed({
//                d("Paul", "Down Event Delay tap separation $tapseparation ")
//                Toast.makeText(
//                    getApplicationContext(),
//                    "Step",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }, 600)

