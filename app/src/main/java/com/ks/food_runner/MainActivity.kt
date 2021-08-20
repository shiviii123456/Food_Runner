package com.ks.food_runner

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ks.food_runner.util.ConnectionManager
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var loginbtn: Button
    lateinit var password:EditText
    lateinit var mobileNumber:EditText
    lateinit var signUp:TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var loginSharedPreferences: SharedPreferences
    var isLoggedIn=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences=getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE)
        loginSharedPreferences=getSharedPreferences(getString(R.string.login_pref),Context.MODE_PRIVATE)
        isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false)
        signUp=findViewById(R.id.signUp)
        loginbtn=findViewById(R.id.loginbtn)

       if(isLoggedIn){
           val intent= Intent(this@MainActivity,Home::class.java)
           startActivity(intent)
           finish()
       }
        loginbtn.setOnClickListener(){
            password=findViewById(R.id.password)
            var  pass=password.text.toString()
            mobileNumber=findViewById(R.id.mobileNumber)
            var mobile=mobileNumber.text.toString()

            var newRequest=Volley.newRequestQueue(this@MainActivity)
            val url="http://13.235.250.119/v2/login/fetch_result"
            var jsonParams=JSONObject()
            jsonParams.put("mobile_number",mobile)
            jsonParams.put("password",pass)
            if(ConnectionManager().checkConnection(this@MainActivity)){
                var request=object:JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {
                    var data=it.getJSONObject("data")
                    var success=data.getBoolean("success")
                    if(success){
                        sharedPreferencesfunc()
                        var userData=data.getJSONObject("data")
                        val user_id=userData.getString("user_id")
                        val name=userData.getString("name")
                        val email=userData.getString("email")
                        val mobile_number=userData.getString("mobile_number")
                        val address=userData.getString("address")

                        loginSharedPreferences.edit().putString("user_id",user_id).apply()
                        loginSharedPreferences.edit().putString("name",name).apply()
                        loginSharedPreferences.edit().putString("email",email).apply()
                        loginSharedPreferences.edit().putString("mobile_number",mobile_number).apply()
                        loginSharedPreferences.edit().putString("address",address).apply()

                        val intent= Intent(this@MainActivity,Home::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this@MainActivity,"Invalid Email and Password",Toast.LENGTH_LONG).show()
                    }
                },
                    Response.ErrorListener {
                        Toast.makeText(this@MainActivity,"Volley Error Occues",Toast.LENGTH_LONG).show()
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val header=HashMap<String,String>()
                        header["Content-type"]="application/json"
                        header["token"]="9bf534118365f1"
                        return header
                    }
                }
                newRequest.add(request)
            }
            else{
                //if internet is not available
                var dialog= AlertDialog.Builder(this@MainActivity)
                dialog.setTitle("Error")
                dialog.setMessage("No Internet Connection")
                dialog.setPositiveButton("Open Setting"){text,listener->
                    var setting= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(setting)
                    this@MainActivity?.finish()
                }
                dialog.setNegativeButton("Cancel"){text,listener->
                    ActivityCompat.finishAffinity(this@MainActivity)
                }
                dialog.create()
                dialog.show()
            }
        }

//        loginbtn.setOnClickListener(){
//            val intent= Intent(this@MainActivity,Home::class.java)
//            startActivity(intent)
//        }
        signUp.setOnClickListener(){
           val intent= Intent(this@MainActivity,SignUp::class.java)
            startActivity(intent)
            finish()
        }
    }
   public fun sharedPreferencesfunc(){
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
    }
}