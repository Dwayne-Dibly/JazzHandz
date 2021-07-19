package com.example.jazzhandzapp.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzhandzapp.Adapters.DownloadedRoutineListAdapter
import com.example.jazzhandzapp.DataClassses.DownloadedRoutines
import com.example.jazzhandzapp.R
import com.example.jazzhandzapp.ViewModels.DownloadedRoutineListViewModel
import com.example.jazzhandzapp.ViewModels.RoutineListViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_view_downloadlist_fragment.*
import kotlinx.android.synthetic.main.nav_view_downloadlist_fragment.nav_view_fragment
import kotlinx.android.synthetic.main.nav_view_routinelist_fragment.*
import java.nio.file.Path


class DownloadRoutinesActivity: AppCompatActivity() {

    private lateinit var downloadedRoutineListViewModel: DownloadedRoutineListViewModel

    lateinit var adapter: DownloadedRoutineListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.signin_register_fragment)

        setContentView(R.layout.nav_view_downloadlist_fragment)

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
                downloadlist_drawer_layout.closeDrawers()
                true

        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }

        downloadedRoutineListViewModel = ViewModelProvider(this).get(DownloadedRoutineListViewModel::class.java)



        var db = FirebaseFirestore.getInstance()

        val uidlist: ArrayList<String> = arrayListOf()
        val documentlist: ArrayList<String> = arrayListOf()
        val documentlistpath: ArrayList<String> = arrayListOf()
        val downloadedroutines: ArrayList<DownloadedRoutines> = arrayListOf()

        val routinesrecyclerview = findViewById<RecyclerView>(R.id.downloadedroutines_recyclerview)
        val adapter = DownloadedRoutineListAdapter(this) //{}

        routinesrecyclerview.adapter = adapter
        routinesrecyclerview.layoutManager = LinearLayoutManager(this)

        db
            .collectionGroup("This routine details")
//            .collectionGroup("Users")
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {

//                    d("Paul", "data ${task.result!!} documents ${task.result!!.documents}")

                    for (document in task.result!!.documents) {
                        uidlist.add(document.reference.path)
                        documentlist.add(document.id)
                        documentlistpath.add(document.reference.path)

                    }

                    for (i in documentlistpath.indices) {

                        db
                            .document(documentlistpath[i])
                            .get()
                            .addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                                if (task.isSuccessful) {

                                    var useruid = uidlist[i].split("/")[1]

//                                    d("Paul", "useruid = ${useruid} ")

                                    var downloadedroutine = task.result!!["routine_name"].toString()
                                    var downloadedcreator = task.result!!["creator_name"].toString()
                                    var downloadedtempo = task.result!!["tempo"].toString()
                                    var downloadedtrack = task.result!!["track_name"].toString()

                                    downloadedroutines.add(DownloadedRoutines(useruid,downloadedroutine, downloadedcreator, downloadedtempo, downloadedtrack))

//                                    d("Paul", "downloaded routines $downloadedroutines")

                                    adapter.setRoutines(downloadedroutines)

                                }
                            })
                    }

                }
            })

        db.clearPersistence()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        downloadlist_drawer_layout.openDrawer(GravityCompat.START)
        return true
    }
}