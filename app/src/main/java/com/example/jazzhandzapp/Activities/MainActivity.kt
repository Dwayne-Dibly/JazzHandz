package com.example.jazzhandzapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.jazzhandzapp.R
import com.example.jazzhandzapp.ViewModels.MainActivityViewModel
import com.google.firebase.auth.ktx.actionCodeSettings
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.side_menu.*


class MainActivity : AppCompatActivity() {

    private lateinit var mainactivityViewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainactivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.actionHome -> startActivity(Intent(this, MainActivity::class.java))
                R.id.actionLogin -> startActivity(Intent(this, RegisterActivity::class.java))
                R.id.actionFreestyle -> startActivity(Intent(this, DanceActivity::class.java))
                R.id.actionCreate -> startActivity(Intent(this, DanceActivity::class.java).putExtra("createmode", true))
                R.id.actionRoutines -> startActivity(Intent(this, DanceRoutinesActivity::class.java).putExtra("createmode", false))
                R.id.actionDownloadedRoutines -> startActivity(Intent(this, DownloadRoutinesActivity::class.java))
            }
            it.isChecked = true
            drawerLayout.closeDrawers()
            true
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }

        createroutinebutton.setOnClickListener {
            val intent = Intent(this, DanceActivity::class.java)
            intent.putExtra(
                "createmode",
                true
            )
            startActivity(intent)
        }

        freeplaybutton.setOnClickListener {
            val intent = Intent(this, DanceActivity::class.java)
            intent.putExtra(
                "createmode",
                false
            )
            startActivity(intent)
        }

        signinbutton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        drawerLayout.openDrawer(GravityCompat.START)
        return true
    }

}
