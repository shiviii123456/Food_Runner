package com.ks.food_runner

import android.app.AlertDialog
import android.content.Intent
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

class Reset_Password : AppCompatActivity() {
    lateinit var mobileNumber:EditText
    lateinit var userEmail:EditText
    lateinit var next: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        mobileNumber=findViewById(R.id.mobileNumber)
        userEmail=findViewById(R.id.userEmail)
        next=findViewById(R.id.next)
        next.setOnClickListener(){
             if(ConnectionManager().checkConnection(this@Reset_Password)){
                 var mobile_number=mobileNumber.text.toString()
                 var user_email=userEmail.text.toString()
                 var newRequest= Volley.newRequestQueue(this@Reset_Password)
                 var url="http://13.235.250.119/v2/forgot_password/fetch_result"
                 var jsonObject=JSONObject()
                 jsonObject.put("mobile_number",mobile_number)
                 jsonObject.put("email",user_email)
                 Log.d("json",user_email)
                 var request=object:JsonObjectRequest(Method.POST,url,jsonObject,Response.Listener {
                     var data=it.getJSONObject("data")
                     var success=data.getBoolean("success")
                     if(success){
                         Toast.makeText(this@Reset_Password,"Otp Sent",Toast.LENGTH_LONG).show()
                         val intent=Intent(this@Reset_Password,Reset_Pass_Confirmation::class.java)
                         intent.putExtra("mobile",mobile_number)
                         startActivity(intent)
                         finish()
                     }
                     else{
                         mobileNumber.text=null
                         userEmail.text=null
                         Toast.makeText(this@Reset_Password,"Try Again",Toast.LENGTH_LONG).show()
                     }
                 },
                 Response.ErrorListener {
                     Toast.makeText(this@Reset_Password,"Something Went Wrong",Toast.LENGTH_LONG).show()
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
                 var dialog= AlertDialog.Builder(this@Reset_Password)
                 dialog.setTitle("Error")
                 dialog.setMessage("No Internet Connection")
                 dialog.setPositiveButton("Open Setting"){text,listener->
                     var setting= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                     startActivity(setting)
                     this@Reset_Password?.finish()
                 }
                 dialog.setNegativeButton("Cancel"){text,listener->
                     ActivityCompat.finishAffinity(this@Reset_Password)
                 }
                 dialog.create()
                 dialog.show()
             }
        }

    }
}