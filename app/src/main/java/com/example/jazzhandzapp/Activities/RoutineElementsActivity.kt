package com.example.jazzhandzapp.Activities

import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.jazzhandzapp.DataClassses.Creators
import com.example.jazzhandzapp.DataClassses.Elements
import com.example.jazzhandzapp.Database.AppDatabase
import com.example.jazzhandzapp.Database.Creator
import com.example.jazzhandzapp.Database.RoutineElement
import com.example.jazzhandzapp.Database.RoutineName
import com.example.jazzhandzapp.Fragments.EmailPasswordFragment
import com.example.jazzhandzapp.Fragments.RoutineElementListFragment
import com.example.jazzhandzapp.Fragments.RoutinePlayBackFragment
import com.example.jazzhandzapp.R
import com.example.jazzhandzapp.ViewModels.RoutineElementListViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_view_downloadlist_fragment.*
import kotlinx.android.synthetic.main.nav_view_downloadlist_fragment.nav_view_fragment
import kotlinx.android.synthetic.main.nav_view_routineelements_fragment.*
import kotlinx.android.synthetic.main.routineelements_activity.*


class RoutineElementsActivity : AppCompatActivity() {

    private lateinit var routineElementListViewModel: RoutineElementListViewModel
    private lateinit var auth: FirebaseAuth

    var routineElements: LiveData<List<RoutineElement>>
    var routineCreators: LiveData<List<Creator>>
    var routineNames: LiveData<List<RoutineName>>

    init {


        val routineElementDAO = AppDatabase.getDatabase(Application(), lifecycleScope).routineElementDAO()
        val creatorDAO = AppDatabase.getDatabase(Application(), lifecycleScope).creatorDAO()
        val routineNameDAO = AppDatabase.getDatabase(Application(), lifecycleScope).routineNameDAO()

        routineElements = routineElementDAO.getallRoutineElements()
        routineCreators = creatorDAO.getAlphabetizedCreators()
        routineNames = routineNameDAO.getRoutineNames()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.routineelements_activity)
        setContentView(R.layout.nav_view_routineelements_fragment)

        nav_view_fragment.setNavigationItemSelectedListener {
                when (it.itemId) {
//                R.id.actionCommittee -> d("Paul", "Committee was pressed")
                    R.id.actionHome -> startActivity(Intent(this, MainActivity::class.java))
                    R.id.actionLogin -> startActivity(Intent(this, RegisterActivity::class.java))
                    R.id.actionFreestyle -> startActivity(Intent(this, DanceActivity::class.java))
                    R.id.actionCreate -> startActivity(Intent(this, DanceActivity::class.java).putExtra("createmode", true))
                    R.id.actionRoutines -> startActivity(Intent(this, DanceRoutinesActivity::class.java).putExtra("createmode", false))
                    R.id.actionDownloadedRoutines -> startActivity(Intent(this, DownloadRoutinesActivity::class.java))
                }
                it.isChecked = true
                routineelements_drawer_layout.closeDrawers()
                true

        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }

        val routine_id = intent.getIntExtra("routine_id", 1)
        routine_id_view.text = routine_id.toString()

        val creator_name = intent.getStringExtra("creator_name")
        routinecreatorelementview.text = creator_name

        val routine_name = intent.getStringExtra("routine_name")
        routinenameelementview.text = routine_name

        val track_name = intent.getStringExtra("track_name")
        tracknameelementview.text = track_name

        val track_tempo = intent.getStringExtra("track_tempo")
        tempoelementview.text = track_tempo

        routineElementListViewModel = ViewModelProvider(this).get(RoutineElementListViewModel::class.java)

        auth = Firebase.auth

        practiceRoutine.setOnClickListener {

//            d("Paul", "routine id $routine_id")

            if (routine_id == 1) {
                AlertDialog.Builder(this)
                    .setMessage("The latest routine must be saved before pracicing it. Save now?")
                    .setPositiveButton("Save")
                    {p0, p1 ->
                        val fragment = supportFragmentManager.findFragmentByTag(RoutineElementListFragment.TAG)
                        if (fragment != null) {
                            if (savedInstanceState == null) {
                                supportFragmentManager.beginTransaction()
                                    .remove(
                                        fragment
                                    )
                                    .commit()
                            }
                        }

                        newroutinelabel.visibility = View.VISIBLE
                        newroutinecreator.visibility = View.VISIBLE
                        newroutinename.visibility = View.VISIBLE
                        newroutinetrack.visibility = View.VISIBLE
                        saveroutinebutton.visibility = View.VISIBLE
                        cancelsaveroutinebutton.visibility = View.VISIBLE

                        saveRoutine.visibility = View.INVISIBLE
                        replayRoutine.visibility = View.INVISIBLE
                        practiceRoutine.visibility = View.INVISIBLE
                        saveRoutinetoFirebase.visibility = View.INVISIBLE

//                        save
                    }
                    .setNegativeButton("Cancel")
                    {p0, p1 ->
                        saveRoutine.visibility = View.VISIBLE
                        replayRoutine.visibility = View.VISIBLE
                        practiceRoutine.visibility = View.VISIBLE
                        saveRoutinetoFirebase.visibility = View.VISIBLE

                        newroutinelabel.visibility = View.INVISIBLE
                        newroutinecreator.visibility = View.INVISIBLE
                        newroutinename.visibility = View.INVISIBLE
                        newroutinetrack.visibility = View.INVISIBLE
                        saveroutinebutton.visibility = View.INVISIBLE
                        cancelsaveroutinebutton.visibility = View.INVISIBLE

                        if (savedInstanceState == null) {
                            supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.frameLayout,
                                    RoutineElementListFragment.newInstance(routine_id, false, track_tempo),
//                                    RoutineElementListFragment.newInstance(routine_id),
                                    RoutineElementListFragment.TAG
                                )
                                .commit()
                        }
                    }

                    .create()
                    .show()
            } else {

                AlertDialog.Builder(this)
                    .setMessage("Starting practice mode will delete the last saved routine. Continue?")
                    .setPositiveButton("Continue to Practice")
                    {p0, p1 ->
                        // delete Last Routine
                        RoutineElementListViewModel(application).deleteRoutineElements(1)

                        if (savedInstanceState == null) {
                            supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.frameLayout,
                                    RoutineElementListFragment.newInstance(routine_id, true, track_tempo),
//                                    RoutineElementListFragment.newInstance(routine_id),
                                    RoutineElementListFragment.TAG
                                )
                                .commit()
                        }

                    }
                    .setNegativeButton("Save last routine")
                    {p0, p1 ->
                        val fragment = supportFragmentManager.findFragmentByTag(RoutineElementListFragment.TAG)
                        if (fragment != null) {
                            if (savedInstanceState == null) {
                                supportFragmentManager.beginTransaction()
                                    .remove(
                                        fragment
                                    )
                                    .commit()
                            }
                        }

                        newroutinelabel.visibility = View.VISIBLE
                        newroutinecreator.visibility = View.VISIBLE
                        newroutinename.visibility = View.VISIBLE
                        newroutinetrack.visibility = View.VISIBLE
                        saveroutinebutton.visibility = View.VISIBLE
                        cancelsaveroutinebutton.visibility = View.VISIBLE

                        saveRoutine.visibility = View.INVISIBLE
                        replayRoutine.visibility = View.INVISIBLE
                        practiceRoutine.visibility = View.INVISIBLE
                        saveRoutinetoFirebase.visibility = View.INVISIBLE
                    }

                    .create()
                    .show()

//                if (savedInstanceState == null) {
//                    supportFragmentManager.beginTransaction()
//                        .replace(
//                            R.id.frameLayout,
//                            RoutineElementListFragment.newInstance(routine_id, true, track_tempo),
////                                    RoutineElementListFragment.newInstance(routine_id),
//                            RoutineElementListFragment.TAG
//                        )
//                        .commit()
//                }


            }
        }

        showroutineelements.setOnClickListener {

            newroutinelabel.visibility = View.INVISIBLE
            newroutinecreator.visibility = View.INVISIBLE
            newroutinename.visibility = View.INVISIBLE
            newroutinetrack.visibility = View.INVISIBLE
            saveroutinebutton.visibility = View.INVISIBLE
            cancelsaveroutinebutton.visibility = View.INVISIBLE

            saveRoutine.visibility = View.VISIBLE
            replayRoutine.visibility = View.VISIBLE
            practiceRoutine.visibility = View.VISIBLE
            saveRoutinetoFirebase.visibility = View.VISIBLE

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        RoutineElementListFragment.newInstance(routine_id, false, track_tempo),
                        RoutineElementListFragment.TAG
                    )
                    .commit()

            }

        }

        replayRoutine.setOnClickListener() {

            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.frameLayout,
                    RoutinePlayBackFragment.newInstance(routine_id),
                    RoutinePlayBackFragment.TAG
                )
                .commit()
        }

        saveRoutine.setOnClickListener() {
            val fragment = supportFragmentManager.findFragmentByTag(RoutineElementListFragment.TAG)
            if (fragment != null) {
                if (savedInstanceState == null) {
                    supportFragmentManager.beginTransaction()
                        .remove(
                            fragment
                        )
                        .commit()
                }
            }

            newroutinelabel.visibility = View.VISIBLE
            newroutinecreator.visibility = View.VISIBLE
            newroutinename.visibility = View.VISIBLE
            newroutinetrack.visibility = View.VISIBLE
            saveroutinebutton.visibility = View.VISIBLE
            cancelsaveroutinebutton.visibility = View.VISIBLE

            saveRoutine.visibility = View.INVISIBLE
            replayRoutine.visibility = View.INVISIBLE
            practiceRoutine.visibility = View.INVISIBLE
            saveRoutinetoFirebase.visibility = View.INVISIBLE

        }

        saveRoutinetoFirebase.setOnClickListener() {
            val fragment = supportFragmentManager.findFragmentByTag(RoutineElementListFragment.TAG)

            AlertDialog.Builder(this)
                .setMessage("This will save this routine and all details above to the Jazz Handz online database.  If you already have an uploaded routine with the same name, it will be overwritten. Continue?")
                .setPositiveButton("Upload to Database")
                {p0, p1 ->

                    val routineid = routine_id

                    routineElements.removeObservers(this)

                    routineElements.observe(  //  add new creator id
                        this,
                        Observer { routine_id -> routine_id?.let { this.uploadRoutineElements(routine_id, routineid) } })

                    saveRoutine.visibility = View.VISIBLE
                    replayRoutine.visibility = View.VISIBLE
                    practiceRoutine.visibility = View.VISIBLE
                    saveRoutinetoFirebase.visibility = View.VISIBLE

                }
                .setNegativeButton("Cancel")
                {p0, p1 ->


                    saveRoutine.visibility = View.VISIBLE
                    replayRoutine.visibility = View.VISIBLE
                    practiceRoutine.visibility = View.VISIBLE
                    saveRoutinetoFirebase.visibility = View.VISIBLE
                }

                .create()
                .show()

        }

        cancelsaveroutinebutton.setOnClickListener() {
            saveRoutine.visibility = View.VISIBLE
            replayRoutine.visibility = View.VISIBLE
            practiceRoutine.visibility = View.VISIBLE
            saveRoutinetoFirebase.visibility = View.VISIBLE

            newroutinelabel.visibility = View.INVISIBLE
            newroutinecreator.visibility = View.INVISIBLE
            newroutinename.visibility = View.INVISIBLE
            newroutinetrack.visibility = View.INVISIBLE
            saveroutinebutton.visibility = View.INVISIBLE
            cancelsaveroutinebutton.visibility = View.INVISIBLE

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        RoutineElementListFragment.newInstance(routine_id, false, track_tempo),
                        RoutineElementListFragment.TAG
                    )
                    .commit()

            }
        }

        saveroutinebutton.setOnClickListener() {

            saveRoutine.visibility = View.VISIBLE
            replayRoutine.visibility = View.VISIBLE
            practiceRoutine.visibility = View.VISIBLE
            saveRoutinetoFirebase.visibility = View.VISIBLE

            newroutinelabel.visibility = View.INVISIBLE
            newroutinecreator.visibility = View.INVISIBLE
            newroutinename.visibility = View.INVISIBLE
            newroutinetrack.visibility = View.INVISIBLE
            saveroutinebutton.visibility = View.INVISIBLE
            cancelsaveroutinebutton.visibility = View.INVISIBLE

            val routinetempotext = tempoelementview.text.toString().toInt()
            val routinetempo = routinetempotext.toString().toInt()

            val newrouteinenametext = newroutinename.text
            val newroutinename = newrouteinenametext.toString()

            val newrouteinetracktext = newroutinetrack.text
            val newrouteinetrack = newrouteinetracktext.toString()

            val newrouteinecreatortext = newroutinecreator.text
            val newrouteinecreator = newrouteinecreatortext.toString()

            val routinename = RoutineName(0, newroutinename, 1, routinetempo, newrouteinetrack)  // works so far!

//            routineElements.observe(this, Observer { routine_id -> routine_id?.let { this.saveRoutineElements(routine_id,newrouteinecreator,routinename) } })
//            routineElements.observe(viewLifecycleOwner, Observer { routine_id -> routine_id?.let { this.setElements(routine_id) } })

//            saveRoutineElements(newrouteinecreator, routinename)
            getRoutineElements(newrouteinecreator, routinename)

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        RoutineElementListFragment.newInstance(routine_id, false, track_tempo),
                        RoutineElementListFragment.TAG
                    )
                    .commit()
            }

//            routineElementListViewModel.saveRoutine(routinename)   // this works
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        routineelements_drawer_layout.openDrawer(GravityCompat.START)
        return true
    }

    fun getRoutineElements(newroutinecreator: String, routinename: RoutineName) {

        routineCreators.removeObservers(this)

        routineCreators.observe(this, Observer { creator_id -> creator_id?.let { this.getRoutineCreatorID(creator_id, routinename) } })

//        routineElements.observe(  //  move this until after creater has been sorted out
//            this,
////            Observer { routine_id -> routine_id?.let { this.getRoutineElements(routine_id, newroutinecreator, routinename) } })
//            Observer { routine_id -> routine_id?.let { this.getRoutineElements(routine_id) } })

    }

    fun getRoutineCreatorID(creators: List<Creator>, routinename: RoutineName) {

        var routinename = routinename
        val newroutinecreator = newroutinecreator.text.toString()

        var creatorlist: ArrayList<Creators> = ArrayList<Creators>()
        val creatorlistsize = creators.size

        for (i in creators.indices) {
            var creatorid = creators[i].creatorId.toString()
            var creatorname = creators[i].creator

            creatorlist.add(Creators(creatorid, creatorname))
        }

        var creatorid: Int

        for (i in creatorlist.indices) {

//            creatorid = creatorlist[i].creatorid.toString().toInt()

//            d("Paul", "new creator $newroutinecreator creator list id $i creator size $creatorlistsize name ${creatorlist[i].creatorname} ")

            if (creatorlist[i].creatorname == newroutinecreator) {

                creatorid = creatorlist[i].creatorid.toString().toInt()

//                d("Paul", "existing creator id $creatorid  creator list size $creatorlistsize")
                routinename = RoutineName(0, routinename.routinename, creatorid, routinename.tempo, routinename.track)

                routineCreators.removeObservers(this)  // stop observing after getting creator

                routineElementListViewModel.saveRoutine(routinename)

            } else {

                if (i == creatorlistsize -1) {

                    creatorid = creatorlistsize + 1
//                    d("Paul", "new creator id $creatorid")

                    if (routinename.routinecreatorId == 1) {
                        routinename = RoutineName(0, routinename.routinename, creatorid, routinename.tempo, routinename.track)
                    }

                    routineCreators.removeObservers(this)  // stop observing after getting list

//                    d("Paul", "New creator")

                    val newroutinecreator_id = creatorid

                    routineElementListViewModel.newRoutineCreator(newroutinecreator, newroutinecreator_id)

                    routineElementListViewModel.saveRoutine(routinename)

                }
            }
        }

        routineElements.removeObservers(this)

        routineElements.observe(  //  add new creator id
            this,
            Observer { routine_id -> routine_id?.let { this.getRoutineElements(routine_id) } })

    }


//    fun getRoutineElements(elements: List<RoutineElement>, newroutinecreator: String, routinename: RoutineName) {
    fun getRoutineElements(elements: List<RoutineElement>) {

        val currentroutineid = routine_id_view.text.toString().toInt()

//        d("Paul", "save routine elements routine id $currentroutineid")

        var elementlist: ArrayList<Elements> = ArrayList<Elements>()


        for (i in elements.indices) {
//            var elementtime = elements[i].elementtime.toString()
            var routineid = elements[i].routineelementId.toString()

            var elementroutineid = routineid.toInt()

            var elementtime = elements[i].elementtime.toString()
            var elementbeat = elements[i].elementbeat.toString()
            var elementmove = elements[i].routinedancemoveId.toString()
            var routineelementtime = elements[i].routineelementtime.toString()
            var elementcomment = elements[i].comment.toString()
            var weighton = elements[i].weighton.toString()

            if (elementroutineid == currentroutineid) {
                elementlist.add(Elements(routineid, elementtime, elementbeat, elementmove, routineelementtime, elementcomment, weighton))
            }


//            elementlist.add(Elements(routineid, elementtime, elementbeat, elementmove, routineelementtime, elementcomment))

        }

        routineElements.removeObservers(this)  // stop observing after getting list

//        d("Paul", "$elementlist")

        getRoutineID(elementlist)

//        routineElementListViewModel.insertElements(elementlist)

    }

    fun getRoutineID(elementlist: ArrayList<Elements>) {

        routineNames.removeObservers(this)
//        routineCreators.observe(this, Observer { creator_id -> creator_id?.let { this.getRoutineCreatorID(creator_id, routinename) } })
        routineNames.observe(this, Observer { routine_id -> routine_id?.let { this.saveRoutineElements(routine_id, elementlist) } })

    }

    fun saveRoutineElements(routinelist: List<RoutineName>, elementlist: ArrayList<Elements>) {

//        val routinelistsize = routinelist.size + 1
        val routinelistsize = routinelist.size

//        d("Paul", "Routine list size $routinelistsize")

        routineNames.removeObservers(this)

        routineElementListViewModel.insertElements(routinelistsize, elementlist)
    }

    fun uploadRoutineElements(elements: List<RoutineElement>, routine_id: Int) {

        var elementlist: ArrayList<Elements> = ArrayList<Elements>()

        for (i in elements.indices) {
//            var elementtime = elements[i].elementtime.toString()
            var routineid = elements[i].routineelementId.toString()

            var elementroutineid = routineid.toInt()

            var elementtime = elements[i].elementtime.toString()
            var elementbeat = elements[i].elementbeat.toString()
            var elementmove = elements[i].routinedancemoveId.toString()
            var routineelementtime = elements[i].routineelementtime.toString()
            var elementcomment = elements[i].comment.toString()
            var weighton = elements[i].weighton.toString()

            if (elementroutineid == routine_id) {
                elementlist.add(Elements(routineid, elementtime, elementbeat, elementmove, routineelementtime, elementcomment, weighton))
            }
        }

        routineElements.removeObservers(this)

        var db = FirebaseFirestore.getInstance()

        val creator: MutableMap<String, Any> = HashMap()
        creator["creator_name"] = routinecreatorelementview.text.toString()
        creator["routine_name"] = routinenameelementview.text.toString()
        creator["track_name"] = tracknameelementview.text.toString()
        creator["tempo"] = tempoelementview.text.toString()

        val routinename = routinenameelementview.text.toString()

        var userID: String

        if (auth.currentUser != null) {

            auth.currentUser!!.reload().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userID = auth.currentUser!!.uid

//                    d("Paul", "UID = $userID")
//                Toast.makeText(this, "User found", Toast.LENGTH_SHORT).show()


                    for (i in elementlist.indices) {
                        // Create a new user with a first and last name
                        val dancemove: MutableMap<String, Any> = HashMap()
                        dancemove["element_time"] = elementlist[i].elementtime.toString()
                        dancemove["element_beat"] = elementlist[i].elementbeat.toString()
                        dancemove["element_move"] = elementlist[i].elementmove.toString()
                        dancemove["element_routinetime"] = elementlist[i].routineelementtime.toString()
                        dancemove["element_commment"] = elementlist[i].comments.toString()
                        dancemove["element_weighton"] = elementlist[i].weighton.toString()

                        db
                            .collection("Users")
//                .document(routinecreatorelementview.text.toString() + " - " + routinenameelementview.text.toString())
                            .document(userID)
                            .collection("Routines")
                            .document(routinename)
                            .collection("This routine moves")
                            .document("Move $i")
                            .set(dancemove)

                        //  Note this is not perfect as if the routine is shorter than before the later moves may not be deleted


//                .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
//                    d(
//                        "Paul",
//                        "DocumentSnapshot added with ID: " + documentReference.id
//                    )
//                })
//                .addOnFailureListener(OnFailureListener { e -> d("Paul", "Error adding document", e) })
                    }



                    db
                        .collection("Users")
                        .document(userID)
                        .collection("Routines")
                        .document(routinename)
                        .collection("This routine details")
                        .document("Details")
                        .set(creator)
                    db.clearPersistence()

                    Toast.makeText(this, "Routine Uploaded", Toast.LENGTH_SHORT).show()

                } else {
//                    Log.e(EmailPasswordFragment.TAG, "reload", task.exception)
                    Toast.makeText(this, "Failed to reload user.", Toast.LENGTH_SHORT).show()
                }
            }

        }
        else {
            Toast.makeText(this, "Error - not logged in", Toast.LENGTH_SHORT).show()

            AlertDialog.Builder(this)
                .setMessage("An account is needed to upload routines.  Login/Register?")
                .setPositiveButton("Yes")
                {p0, p1 ->

                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)

                }
                .setNegativeButton("Cancel")
                {p0, p1 ->


                }

                .create()
                .show()
        }
    }

}


////   Old Version  -- this works for allRoutineElements but not for one Routine
//
//class RoutineElementsActivity: AppCompatActivity() {
//
//private lateinit var routineElementListViewModel: RoutineElementListViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.routineelements_activity)
//
//        val routine_id = intent.getIntExtra("routine_id", 1)
//        routine_id_view.text = routine_id.toString()
//
//        val creator_name = intent.getStringExtra("creator_name")
//        routinecreatorelementview.text = creator_name
//
//        val routine_name = intent.getStringExtra("routine_name")
//        routinenameelementview.text = routine_name
//
//        val track_name = intent.getStringExtra("track_name")
//        tracknameelementview.text = track_name
//
//        val track_tempo = intent.getStringExtra("track_tempo")
//        tempoelementview.text = track_tempo
//
//        val routinesrecyclerview = findViewById<RecyclerView>(R.id.routineelementrecyclerview)
//
//        val adapter = RoutineElementAdapter(this)
//        routinesrecyclerview.adapter = adapter
//        routinesrecyclerview.layoutManager = LinearLayoutManager(this)
//
//        routineElementListViewModel = ViewModelProvider(this).get(RoutineElementListViewModel::class.java)
//
//        routineElementListViewModel.routineElements.observe(this, Observer { routine_id -> routine_id?.let { adapter.setAllElements(routine_id) } })
//
//    }
//}