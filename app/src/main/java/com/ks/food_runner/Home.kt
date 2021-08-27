package com.ks.food_runner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import org.w3c.dom.Text

class Home : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var loginPreferences: SharedPreferences
    lateinit var name:TextView
    lateinit var phone:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        var previousMenuItem:MenuItem?=null
        sharedPreferences = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE)
        loginPreferences=getSharedPreferences(getString(R.string.login_pref),Context.MODE_PRIVATE)

        toolbar=findViewById(R.id.toolbar)
        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        navigationView=findViewById(R.id.navigationView)
        settingToolbar()

        val headerView: View =navigationView.getHeaderView(0)
        name=headerView.findViewById(R.id.name)
        phone=headerView.findViewById(R.id.phone)
        name.text=loginPreferences.getString("name","")
        phone.text="+91 "+loginPreferences.getString("mobile_number","")

        var toogleState= ActionBarDrawerToggle(this@Home,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toogleState)//add click listeners to the toogleButton
        toogleState.syncState() //it changes the hamburger button to back button when the navigation bar open

        sharedPreferences = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE)

        openHome()

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem != null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            var id=it.itemId

            when(id){
                R.id.home->{
                    openHome()
                }
                R.id.userProfile->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,UserProfile())
                        .commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.orderHistory->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,OrderHistory())
                        .commit()
                    supportActionBar?.title="Order History"
                    drawerLayout.closeDrawers()
                }
                R.id.favRestraunt->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,Profile())
                        .commit()
                    supportActionBar?.title="Favouraites Restraunts"
                    drawerLayout.closeDrawers()
                }
                R.id.faq->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,Faq())
                        .commit()
                    supportActionBar?.title="Frequently Answered Questions"
                    drawerLayout.closeDrawers()
                }
                R.id.logout->{
                    var dialog = AlertDialog.Builder(this@Home)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to logout")
                    dialog.setPositiveButton("Yes") { text, Listener ->
                        sharedPreferences.edit().clear().apply()
                        loginPreferences.edit().clear().apply()
                        val intent = Intent(this@Home, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialog.setNegativeButton("No") { text, Listener ->

                    }
                    dialog.show()
                    dialog.create()
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

    override fun onBackPressed() {
        val fragment=supportFragmentManager.findFragmentById(R.id.frame)
        when(fragment){
            !is HomeFragment->{
                 supportFragmentManager.beginTransaction()
                     .replace(R.id.frame,HomeFragment()).commit()
                supportActionBar?.title="Home"
            }
            else->{
                super.onBackPressed()
                finish()
            }
        }
    }
}
