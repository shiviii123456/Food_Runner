package com.ks.food_runner

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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ks.food_runner.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class Reset_Pass_Confirmation : AppCompatActivity() {
    lateinit var otp:EditText
    lateinit var resetPassword:EditText
    lateinit var resetCnfPassword:EditText
    lateinit var submit: Button
    lateinit var loginSharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pass_confirmation)
        otp=findViewById(R.id.otp)
        resetPassword=findViewById(R.id.resetPassword)
        resetCnfPassword=findViewById(R.id.resetCnfPassword)
       submit=findViewById(R.id.submit)
        loginSharedPreferences=getSharedPreferences(getString(R.string.login_pref), Context.MODE_PRIVATE)
        //resume from here
        //How to send the id from here
        submit.setOnClickListener(){
            var sentOtp=otp.text.toString()
            var reset_Password=resetPassword.text.toString()
            var reset_cnf=resetCnfPassword.text.toString()
            var mobile=intent.getStringExtra("mobile").toString()
            if(ConnectionManager().checkConnection(this@Reset_Pass_Confirmation)){
                try{
                    var newRequest= Volley.newRequestQueue(this@Reset_Pass_Confirmation)
                    var url="http://13.235.250.119/v2/reset_password/fetch_result"
                    var jsonObject= JSONObject()
                    jsonObject.put("mobile_number",mobile)
                    jsonObject.put("password",reset_Password)
                    jsonObject.put("otp",sentOtp)
                    Log.d("mobile","$jsonObject")
                    var request=object: JsonObjectRequest(
                        Method.POST,url,jsonObject, Response.Listener {
                        var data=it.getJSONObject("data")
                        var success=data.getBoolean("success")
                            Log.d("hello","$success")
                        if(success){
                            Toast.makeText(this@Reset_Pass_Confirmation,"Password Successfully chganged",Toast.LENGTH_LONG).show()
                            val intent=Intent(this@Reset_Pass_Confirmation,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            otp.text=null
                            resetPassword.text=null
                            resetCnfPassword.text=null
                            Toast.makeText(this@Reset_Pass_Confirmation,"Try Again",Toast.LENGTH_LONG).show()
                        }
                    },
                        Response.ErrorListener {
                            Toast.makeText(this@Reset_Pass_Confirmation,"Something Went Wrong",Toast.LENGTH_LONG).show()
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
                catch(e:JSONException){
                    Toast.makeText(this@Reset_Pass_Confirmation,"Json Error occured", Toast.LENGTH_LONG).show()
                }
            }
            else{
                var dialog= AlertDialog.Builder(this@Reset_Pass_Confirmation)
                dialog.setTitle("Error")
                dialog.setMessage("No Internet Connection")
                dialog.setPositiveButton("Open Setting"){text,listener->
                    var setting= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(setting)
                    this@Reset_Pass_Confirmation?.finish()
                }
                dialog.setNegativeButton("Cancel"){text,listener->
                    ActivityCompat.finishAffinity(this@Reset_Pass_Confirmation)
                }
                dialog.create()
                dialog.show()
            }
        }
    }
}