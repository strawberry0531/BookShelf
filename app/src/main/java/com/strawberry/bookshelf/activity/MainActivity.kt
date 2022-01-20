package com.strawberry.bookshelf.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.strawberry.bookshelf.*

import com.strawberry.bookshelf.fragment.AboutFragment
import com.strawberry.bookshelf.fragment.DashboardFragment
import com.strawberry.bookshelf.fragment.FavouritesFragment
import com.strawberry.bookshelf.fragment.ProfileFragment
import com.strawberry.bookshelf.R
import com.strawberry.bookshelf.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var previousMenuItem: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpToolBar()
        openDashboard()
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            binding.drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener {
            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it
            when (it.itemId) {
                R.id.dashboard -> {
                    openDashboard()
                    binding.drawerLayout.closeDrawers()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame_layout,
                            FavouritesFragment()
                        ).addToBackStack("Favourites").commit()
                    supportActionBar?.title = "Favourites"
                    binding.drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame_layout,
                            ProfileFragment()
                        ).addToBackStack("Profile").commit()
                    supportActionBar?.title = "Profile"
                    binding.drawerLayout.closeDrawers()
                }
                R.id.about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame_layout,
                            AboutFragment()
                        ).addToBackStack("About").commit()
                    supportActionBar?.title = "About Us"
                    binding.drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setUpToolBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home)
            binding.drawerLayout.openDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }

    private fun openDashboard() {
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack("Dashboard")
        supportActionBar?.title = "Dashboard"
        transaction.commit()
        binding.navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.frame_layout)) {
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }
    }
}
