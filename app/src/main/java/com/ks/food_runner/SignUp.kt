package com.ks.food_runner

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ks.food_runner.util.ConnectionManager
import org.json.JSONObject

class SignUp : AppCompatActivity() {
    lateinit var userName:EditText
    lateinit var userEmail:EditText
    lateinit var userMobile:EditText
    lateinit var userAddress:EditText
    lateinit var userPassword:EditText
    lateinit var userCnfPassword:EditText
    lateinit var register: Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        sharedPreferences=getSharedPreferences(getString(R.string.shared_pref),Context.MODE_PRIVATE)
        userName=findViewById(R.id.userName)
        userEmail=findViewById(R.id.userEmail)
        userMobile=findViewById(R.id.userMobile)
        userAddress=findViewById(R.id.userAddress)
        userPassword=findViewById(R.id.userPassword)
        userCnfPassword=findViewById(R.id.userCnfPassword)
        register=findViewById(R.id.register)

        register.setOnClickListener(){

            var user_name=userName.text.toString()
            var user_email=userEmail.text.toString()
            var user_mobile=userMobile.text.toString()
            var user_address=userAddress.text.toString()
            var user_password=userPassword.text.toString()
            var user_cnfpassword=userCnfPassword.text.toString()

        var newRequest= Volley.newRequestQueue(this@SignUp)
        val url="http://13.235.250.119/v2/register/fetch_result"
        var jsonParams= JSONObject()
        jsonParams.put("name",user_name)
            jsonParams.put("mobile_number",user_mobile)
            jsonParams.put("password",user_password)
            jsonParams.put("address",user_address)
            jsonParams.put("email",user_email)

        if(ConnectionManager().checkConnection(this@SignUp)){
            var request=object: JsonObjectRequest(Method.POST,url,jsonParams, Response.Listener {
                var data=it.getJSONObject("data")
                var success=data.getBoolean("success")
                if(success){
                    sharedPrefrenceSignup()
                    val intent= Intent(this@SignUp,Home::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this@SignUp,"Already Registered", Toast.LENGTH_LONG).show()
                }
            },
                Response.ErrorListener {
                    Toast.makeText(this@SignUp,"Volley Error Occues", Toast.LENGTH_LONG).show()
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
            var dialog= AlertDialog.Builder(this@SignUp)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection")
            dialog.setPositiveButton("Open Setting"){text,listener->
                var setting= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(setting)
                this@SignUp?.finish()
            }
            dialog.setNegativeButton("Cancel"){text,listener->
                ActivityCompat.finishAffinity(this@SignUp)
            }
            dialog.create()
            dialog.show()
        }
    }
    }
    fun sharedPrefrenceSignup(){
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
    }
}