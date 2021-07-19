package com.example.jazzhandzapp.Activities

import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.os.Bundle
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
import com.example.jazzhandzapp.Database.*
import com.example.jazzhandzapp.Fragments.DownloadedRoutineElementListFragment
import com.example.jazzhandzapp.Fragments.RoutineElementListFragment
import com.example.jazzhandzapp.Fragments.RoutinePlayBackFragment
import com.example.jazzhandzapp.R
import com.example.jazzhandzapp.ViewModels.DownloadedRoutineElementListViewModel
import com.example.jazzhandzapp.ViewModels.RoutineElementListViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.downloadedroutineelements_activity.*
import kotlinx.android.synthetic.main.nav_view_downloadelements_fragment.*
import kotlinx.android.synthetic.main.nav_view_downloadlist_fragment.*
import kotlinx.android.synthetic.main.nav_view_downloadlist_fragment.nav_view_fragment
import kotlinx.android.synthetic.main.routineelements_activity.*
import kotlinx.android.synthetic.main.routineelements_activity.cancelsaveroutinebutton
import kotlinx.android.synthetic.main.routineelements_activity.newroutinecreator
import kotlinx.android.synthetic.main.routineelements_activity.newroutinelabel
import kotlinx.android.synthetic.main.routineelements_activity.newroutinename
import kotlinx.android.synthetic.main.routineelements_activity.newroutinetrack
import kotlinx.android.synthetic.main.routineelements_activity.saveroutinebutton
import kotlinx.android.synthetic.main.routineelements_activity.showroutineelements
import kotlinx.coroutines.GlobalScope
import java.lang.Exception

class DownloadedRoutineElementsActivity : AppCompatActivity() {


    //    private lateinit var downloadedRoutineElementListViewModel: DownloadedRoutineElementListViewModel
    private lateinit var downloadedRoutineElementListViewModel: DownloadedRoutineElementListViewModel

    val repository: DatabaseRepository

    var routineElements: LiveData<List<RoutineElement>>
    var routineCreators: LiveData<List<Creator>>
    var routineNames: LiveData<List<RoutineName>>

    init {


//        val routineElementDAO = AppDatabase.getDatabase(Application(), lifecycleScope).routineElementDAO()
        val routineElementDAO = AppDatabase.getDatabase(Application(), scope = GlobalScope).routineElementDAO()
//        val creatorDAO = AppDatabase.getDatabase(Application(), lifecycleScope).creatorDAO()
        val creatorDAO = AppDatabase.getDatabase(Application(), scope = GlobalScope).creatorDAO()
//        val routineNameDAO = AppDatabase.getDatabase(Application(), lifecycleScope).routineNameDAO()
        val routineNameDAO = AppDatabase.getDatabase(Application(), scope = GlobalScope).routineNameDAO()

        val danceMovesDAO = AppDatabase.getDatabase(Application(), scope = GlobalScope).danceMoveDAO()

        routineElements = routineElementDAO.getallRoutineElements()
        routineCreators = creatorDAO.getAlphabetizedCreators()
        routineNames = routineNameDAO.getRoutineNames()

        repository = DatabaseRepository(creatorDAO, routineNameDAO, danceMovesDAO, routineElementDAO)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.downloadedroutineelements_activity)
        setContentView(R.layout.nav_view_downloadelements_fragment)

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
                downloadelements_drawer_layout.closeDrawers()
                true

        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }

        val routine_path = intent.getStringExtra("path")
        downloaded_routine_id_view.text = routine_path

        val creator_name = intent.getStringExtra("creator_name")
        downloadedroutinecreatorelementview.text = creator_name

        val routine_name = intent.getStringExtra("routine_name")
        downloadedroutinenameelementview.text = routine_name

        val track_name = intent.getStringExtra("track_name")
        downloadedtracknameelementview.text = track_name

        val track_tempo = intent.getStringExtra("track_tempo")
        downloadedtempoelementview.text = track_tempo

//        downloadedRoutineElementListViewModel = ViewModelProvider(this).get(DownloadedRoutineElementListViewModel::class.java)
//        downloadedRoutineElementListViewModel = ViewModelProvider(this).get(RoutineElementListViewModel::class.java)

        downloadedRoutineElementListViewModel = ViewModelProvider(this).get(DownloadedRoutineElementListViewModel::class.java)


        showdownloadedroutineelements.setOnClickListener {

            savedownloadedroutinebutton.visibility = View.INVISIBLE
            cancelsavedownloadedroutinebutton.visibility = View.INVISIBLE

            saveDownloadedRoutine.visibility = View.VISIBLE
            replayDownloadedRoutine.visibility = View.VISIBLE

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        DownloadedRoutineElementListFragment.newInstance(routine_path),
                        DownloadedRoutineElementListFragment.TAG
                    )
                    .commit()

            }

        }

        replayDownloadedRoutine.setOnClickListener() {

//            supportFragmentManager.beginTransaction()
//                .replace(
//                    R.id.frameLayout,
//                    DownloadedRoutinePlayBackFragment.newInstance(routine_id),
//                    DownloadedRoutinePlayBackFragment.TAG
//                )
//                .commit()
        }


        saveDownloadedRoutine.setOnClickListener() {
            val fragment = supportFragmentManager.findFragmentByTag(DownloadedRoutineElementListFragment.TAG)
            if (fragment != null) {
                if (savedInstanceState == null) {
                    supportFragmentManager.beginTransaction()
                        .remove(
                            fragment
                        )
                        .commit()
                }
            }

            savedownloadedroutinebutton.visibility = View.VISIBLE
            cancelsavedownloadedroutinebutton.visibility = View.VISIBLE

            saveDownloadedRoutine.visibility = View.INVISIBLE
            replayDownloadedRoutine.visibility = View.INVISIBLE

        }



        cancelsavedownloadedroutinebutton.setOnClickListener() {
            saveDownloadedRoutine.visibility = View.VISIBLE
            replayDownloadedRoutine.visibility = View.VISIBLE

            savedownloadedroutinebutton.visibility = View.INVISIBLE
            cancelsavedownloadedroutinebutton.visibility = View.INVISIBLE

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        DownloadedRoutineElementListFragment.newInstance(routine_path),
                        DownloadedRoutineElementListFragment.TAG
                    )
                    .commit()

            }
        }

        savedownloadedroutinebutton.setOnClickListener() {

            saveDownloadedRoutine.visibility = View.VISIBLE
            replayDownloadedRoutine.visibility = View.VISIBLE

            savedownloadedroutinebutton.visibility = View.INVISIBLE
            cancelsavedownloadedroutinebutton.visibility = View.INVISIBLE

//            val routinetempotext = downloadedtempoelementview.text.toString().toInt()
            val routinetempotext = downloadedtempoelementview.text
            val routinetempo = routinetempotext.toString().toInt()

            val newrouteinenametext = downloadedroutinenameelementview.text
            val newroutinename = newrouteinenametext.toString()

            val newrouteinetracktext = downloadedtracknameelementview.text
            val newrouteinetrack = newrouteinetracktext.toString()

            val newrouteinecreatortext = downloadedroutinecreatorelementview.text
            val newrouteinecreator = newrouteinecreatortext.toString()

            val routinename = RoutineName(0, newroutinename, 1, routinetempo, newrouteinetrack)  // works so far!

//            d("Paul", "downloaded routine $routinename")

            getDownloadedRoutineElements(routine_path, routinename)

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        downloadelements_drawer_layout.openDrawer(GravityCompat.START)
        return true
    }

    val documentlist: ArrayList<String> = arrayListOf()
    val documentlistpath: ArrayList<String> = arrayListOf()

    val downloadedelements: ArrayList<Elements> = arrayListOf()

    fun getDownloadedRoutineElements(routine_path: String, routinename: RoutineName) {

        var db = FirebaseFirestore.getInstance()

        d("Paul", "routine path - $routine_path")

        db
            .collection(routine_path)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    d("Paul", "data ${task.result!!} documents ${task.result!!.documents}")

                    for (document in task.result!!.documents) {
                        documentlist.add(document.id)
                        documentlistpath.add(document.reference.path)
                    }
//                    d("Paul", "documents - $documentlistpath")
//                }
//            }

//        db.clearPersistence()

                    d("Paul", "routine element paths $documentlistpath")

                    for (i in documentlistpath.indices) {

                        db
                            .document(documentlistpath[i])
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {

//                                d("Paul", "downloaded elements here")

//                                var downloaded_routine_time = task.result!!["routine_name"].toString()
                                    var downloaded_routine_time = task.result!!["element_time"].toString()

                                    var downloaded_routine_beat = task.result!!["element_beat"].toString()
                                    var downloaded_routine_move_id = task.result!!["element_move"].toString()
                                    var downloaded_routine_element_time = task.result!!["element_routinetime"].toString()
                                    var downloaded_routine_element_comment = task.result!!["element_commment"].toString()
                                    var downloaded_routine_element_weight = task.result!!["element_weighton"].toString()

                                    downloadedelements.add(
                                        Elements(
                                            "0",
                                            downloaded_routine_time,
                                            downloaded_routine_beat,
                                            downloaded_routine_move_id,
                                            downloaded_routine_element_time,
                                            downloaded_routine_element_comment,
                                            downloaded_routine_element_weight
                                        )
                                    )

                                }

//                                routineCreators.removeObservers(this)
//                                repository.allCreators.removeObservers(this)

//                                d("Paul", "downloaded routine elements $downloadedelements")
//
//                                if (i == documentlistpath.size - 1) {
//
//                                    d("Paul", "adding downloaded elements to database")
//
//                                    repository.allCreators.observe(
////                        viewLifecycleOwner,
//                                        this,
//                                        Observer { dancemove_id ->
//                                            dancemove_id?.let {
//                                                getRoutineCreatorID(
//                                                    dancemove_id,
//                                                    routinename,
//                                                    downloadedelements
//                                                )
//                                            }
//                                        })
//                                }

//                                d("Paul", "downloaded routine elements $downloadedelements")

//                                d("paul", "elements in routine - ${documentlistpath.size}   number of downloaded elements ${downloadedelements.size}")

//                                routineCreators.removeObservers(this)
//                                repository.allCreators.removeObservers(this)

                                if (documentlistpath.size == downloadedelements.size) {
//
                                    d("Paul", "last element downloaded, start saving")

                                    repository.allCreators.removeObservers(this)
//
                                    repository.allCreators.observe(
                                        this,
                                        Observer { dancemove_id ->
                                            dancemove_id?.let {
                                                getRoutineCreatorID(
                                                    dancemove_id,
                                                    routinename,
                                                    downloadedelements
                                                )
                                            }
                                        })

                                }
                            }


//                        db.clearPersistence()
                    }

//                    d("Paul", "downloaded routine elements $downloadedelements")
                }

//                d("Paul", "downloaded routine elements $downloadedelements")
//
////                if (i == documentlistpath.size - 1) {
//
//                    repository.allCreators.observe(
////                        viewLifecycleOwner,
//                        this,
//                        Observer { dancemove_id ->
//                            dancemove_id?.let {
//                                getRoutineCreatorID(
//                                    dancemove_id,
//                                    routinename,
//                                    downloadedelements
//                                )
//                            }
//                        })
//                }

                Toast.makeText(this, "Routine Downloaded", Toast.LENGTH_SHORT).show()


                db.clearPersistence()

//        Toast.makeText(this, "Routine Downloaded", Toast.LENGTH_SHORT).show()

//                }
            }
    }

    fun getRoutineCreatorID(creators: List<Creator>, routinename: RoutineName, downloadedelements: List<Elements>) {

        var routinename = routinename
//        val newroutinecreator = newroutinecreator.text.toString()

        val newrouteinecreatortext = downloadedroutinecreatorelementview.text
        val newrouteinecreator = newrouteinecreatortext.toString()

//        d("Paul", "downloaded routine creator $newrouteinecreatortext")

        var creatorlist: ArrayList<Creators> = ArrayList<Creators>()
        val creatorlistsize = creators.size

        for (i in creators.indices) {
            var creatorid = creators[i].creatorId.toString()
            var creatorname = creators[i].creator

            creatorlist.add(Creators(creatorid, creatorname))
        }

        repository.allCreators.removeObservers(this)  // stop observing after getting creator list

        var creatorid: Int

        var existingcreatorid = 0

        for (i in creatorlist.indices) {

            if (creatorlist[i].creatorname == newrouteinecreator) {

                existingcreatorid = creatorlist[i].creatorid.toString().toInt()

//                d("Paul", "existing creator id $existingcreatorid")
            }
        }

        routineCreators.removeObservers(this)

//        d("Paul", "final existing creator id (0 = new) $existingcreatorid")

        if (existingcreatorid == 0) {

            creatorid = creatorlistsize + 1
//          d("Paul", "new creator id $creatorid")

            downloadedRoutineElementListViewModel.newRoutineCreator(newrouteinecreator, creatorid)

            routinename = RoutineName(0, routinename.routinename, creatorid, routinename.tempo, routinename.track)

            downloadedRoutineElementListViewModel.saveRoutine(routinename)

        } else {

            routinename = RoutineName(0, routinename.routinename, existingcreatorid, routinename.tempo, routinename.track)

//            downloadedRoutineElementListViewModel.newRoutineCreator(newrouteinecreator, newroutinecreator_id)
//            downloadedRoutineElementListViewModel.newRoutineCreator(newrouteinecreator, creatorid)
//
            downloadedRoutineElementListViewModel.saveRoutine(routinename)
        }

        routineNames.observe(
            this,
            Observer { routine_id -> routine_id?.let { this.getRoutineElements(routine_id, downloadedelements) } })


    }


    //    fun getRoutineElements(elements: List<RoutineElement>, newroutinecreator: String, routinename: RoutineName) {
    fun getRoutineElements(routinenames: List<RoutineName>, downloadedelements: List<Elements>) {

//        val currentroutineid = downloaded_routine_id_view.text.toString().toInt()  // not this
        val currentroutineid = routinenames.size

//        d("Paul", "save routine elements routine id $currentroutineid")

        var elementlist: ArrayList<Elements> = ArrayList<Elements>()


        for (i in downloadedelements.indices) {
//            var elementtime = elements[i].elementtime.toString()
            var routineid = currentroutineid.toString()

//            var elementroutineid = routineid.toInt()

            var elementtime = downloadedelements[i].elementtime.toString()
            var elementbeat = downloadedelements[i].elementbeat.toString()
            var elementmove = downloadedelements[i].elementmove.toString()
            var routineelementtime = downloadedelements[i].routineelementtime.toString()
            var elementcomment = downloadedelements[i].comments.toString()
            var weighton = downloadedelements[i].weighton.toString()

//            if (elementroutineid == currentroutineid) {
            elementlist.add(Elements(routineid, elementtime, elementbeat, elementmove, routineelementtime, elementcomment, weighton))
//            }

        }

        routineNames.removeObservers(this)  // stop observing after getting list

//        d("Paul", "$elementlist")

        val sortedelementlist = elementlist.sortedByDescending { it.elementtime!!.toInt() }.toList().reversed()

//        try {

        downloadedRoutineElementListViewModel.insertElements(currentroutineid, sortedelementlist as ArrayList<Elements>)
//        }
//        catch (Exception: Exception) {
//
//        }
    }

}

