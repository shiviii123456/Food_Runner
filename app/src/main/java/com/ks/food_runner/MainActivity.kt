package com.ks.food_runner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var loginbtn: Button
    lateinit var password:EditText
    lateinit var mobileNumber:EditText
    lateinit var signUp:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        password=findViewById(R.id.password)
        mobileNumber=findViewById(R.id.mobileNumber)
        signUp=findViewById(R.id.signUp)
        loginbtn=findViewById(R.id.loginbtn)

        loginbtn.setOnClickListener(){
            val intent= Intent(this@MainActivity,Home::class.java)
            startActivity(intent)
        }
        signUp.setOnClickListener(){
           val intent= Intent(this@MainActivity,SignUp::class.java)
            startActivity(intent)
        }
    }
}