package com.example.jazzhandzapp.Activities

import RoutineListAdapter
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jazzhandzapp.R
import com.example.jazzhandzapp.ViewModels.RoutineListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_view_routinelist_fragment.*


class DanceRoutinesActivity: AppCompatActivity() {

    private lateinit var routineListViewModel: RoutineListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.danceroutines_activity)
        setContentView(R.layout.nav_view_routinelist_fragment)

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
                routinelist_drawer_layout.closeDrawers()
                true

        }

//        nav_view.setNavigationItemSelectedListener {
//            when (it.itemId) {
////                R.id.actionCommittee -> d("Paul", "Committee was pressed")
//                R.id.actionHome -> startActivity(Intent(this, MainActivity::class.java))
//                R.id.actionLogin -> startActivity(Intent(this, RegisterActivity::class.java))
//                R.id.actionFreestyle -> startActivity(Intent(this, DanceActivity::class.java))
//                R.id.actionCreate -> startActivity(Intent(this, DanceActivity::class.java).putExtra("createmode", true))
//                R.id.actionRoutines -> startActivity(Intent(this, DanceRoutinesActivity::class.java).putExtra("createmode", false))
//                R.id.actionDownloadedRoutines -> startActivity(Intent(this, DownloadRoutinesActivity::class.java))
//            }
//            it.isChecked = true
//            drawerLayout.closeDrawers()
//            true
//        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }

        val routinesrecyclerview = findViewById<RecyclerView>(R.id.recyclerview)

//        val adapter = RoutineListAdapter(this, AppDatabase.getDatabase(this, lifecycleScope).routineElementDAO()) //{}
        val adapter = RoutineListAdapter(this) //{}

        routinesrecyclerview.adapter = adapter
        routinesrecyclerview.layoutManager = LinearLayoutManager(this)

        routineListViewModel = ViewModelProvider(this).get(RoutineListViewModel::class.java)

        routineListViewModel.routineCreators.removeObservers(this)
        routineListViewModel.allRoutines.removeObservers(this)

        routineListViewModel.routineCreators.observe(this, Observer { creators -> creators?.let { adapter.setCreators(it) } })  //
        routineListViewModel.allRoutines.observe(this, Observer { routines -> routines?.let { adapter.setRoutines(it) } })  //
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        routinelist_drawer_layout.openDrawer(GravityCompat.START)
        return true
    }
}