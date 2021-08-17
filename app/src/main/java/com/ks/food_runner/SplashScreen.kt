package com.ks.food_runner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    private var splashScreenTime:Long=3500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen2)

        Handler(Looper.myLooper()!!).postDelayed({
               var intent=Intent(this@SplashScreen,MainActivity::class.java)
            startActivity(intent)
        },splashScreenTime)


    }
}