package com.example.jazzhandzapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.jazzhandzapp.Fragments.EmailPasswordFragment
import com.example.jazzhandzapp.R
import kotlinx.android.synthetic.main.nav_view_registeractivity_fragment.*


class RegisterActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_view_registeractivity_fragment)

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
            registeractivity_drawer_layout.closeDrawers()
            true

        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.frameLayout,
                    EmailPasswordFragment(),
                    EmailPasswordFragment.TAG
                )
                .commit()

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        registeractivity_drawer_layout.openDrawer(GravityCompat.START)
        return true
    }
}