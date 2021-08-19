package com.ks.food_runner

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Home : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        sharedPreferences =
            getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE)

        toolbar=findViewById(R.id.toolbar)
        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        navigationView=findViewById(R.id.navigationView)
        settingToolbar()

        var toogleState= ActionBarDrawerToggle(this@Home,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toogleState)//add click listeners to the toogleButton
        toogleState.syncState() //it changes the hamburger button to back button when the navigation bar open

        sharedPreferences =
            getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE)

        openHome()

        navigationView.setNavigationItemSelectedListener {

            var id=it.itemId

            when(id){
                R.id.home->{
                    openHome()
                }
                R.id.userProfile->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,Faq())
                        .addToBackStack("profile")
                        .commit()
                    supportActionBar?.title="Testing"
                    drawerLayout.closeDrawers()
                }
                R.id.orderHistory->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,OrderHistory())
                        .addToBackStack("order History")
                        .commit()
                    supportActionBar?.title="Order History"
                    drawerLayout.closeDrawers()
                }
                R.id.favRestraunt->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,Profile())
                        .addToBackStack("fav restraunt")
                        .commit()
                    supportActionBar?.title="Favouraites Restraunts"
                    drawerLayout.closeDrawers()
                }
                R.id.faq->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,Faq())
                        .addToBackStack("faq")
                        .commit()
                    supportActionBar?.title="Favouraites Restraunts"
                    drawerLayout.closeDrawers()
                }
                R.id.logout->{
                    Toast.makeText(this@Home,"logout",Toast.LENGTH_LONG).show()
                }
            }

            return@setNavigationItemSelectedListener true
        }
    }
    fun settingToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id=item.itemId //extracting the id of item clicked android.R.id.home gives the id of hamburger button
        if(id== android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun openHome(){
        supportFragmentManager.beginTransaction().replace(R.id.frame,HomeFragment())
            .commit()
        supportActionBar?.title="Home"
        drawerLayout.closeDrawers()
    }
}
