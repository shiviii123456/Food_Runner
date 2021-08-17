package com.ks.food_runner

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ks.food_runner.Adapter.RestrauntItemAdapter
import com.ks.food_runner.Model.RestrauntItem
import com.ks.food_runner.util.ConnectionManager

class Restraunt_item : AppCompatActivity() {
    lateinit var toolbarItem:Toolbar
    lateinit var recyclerviewItem:RecyclerView
    lateinit var progressbarItem:ProgressBar
    var ItemArray= mutableListOf<RestrauntItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restraunt_item)

        recyclerviewItem=findViewById(R.id.recyclerviewItem)
        progressbarItem=findViewById(R.id.progressbarItem)
        if(intent != null){
            var itemid=intent.getStringExtra("foodId")
            var id=itemid?.toString() as String
           var restrauntName=intent.getStringExtra("restrauntName")
            addToolbar(restrauntName)
            val newRequest= Volley.newRequestQueue(this@Restraunt_item)
            var url: String ="http://13.235.250.119/v2/restaurants/fetch_result/$id"
            if(ConnectionManager().checkConnection(this@Restraunt_item)){
                var jsonObjectRequest=object:JsonObjectRequest(Method.GET,url,null,Response.Listener {
                    val data=it.getJSONObject("data")
                    val success=data.getBoolean("success")
                    if(success){
                        progressbarItem.setVisibility(View.GONE)
                        val foodData=data.getJSONArray("data")
                        for(i in 0 until foodData.length()){
                            var itemInfo= foodData.getJSONObject(i)
                            val foodObject= RestrauntItem(
                                "${i+1}",
                                itemInfo.getString("name"),
                                itemInfo.getString("cost_for_one"),
                                itemInfo.getString("restaurant_id")
                            )

                            ItemArray.add(foodObject)
                            recyclerviewItem.layoutManager=LinearLayoutManager(this@Restraunt_item)
                            recyclerviewItem.adapter=RestrauntItemAdapter(this@Restraunt_item,ItemArray)
                        }
                    }
                    else{
                        Toast.makeText(this@Restraunt_item,"Some Error occured",Toast.LENGTH_LONG).show()
                    }

                },
                    Response.ErrorListener {
                        Toast.makeText(this@Restraunt_item,"error occured",Toast.LENGTH_LONG).show()
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        var header=HashMap<String,String>()
                        header["Content-Type"]="application/json"
                        header["token"]="9bf534118365f1"
                        return header
                    }
                }
                newRequest.add(jsonObjectRequest)
            }
            else{
                var dialog=AlertDialog.Builder(this@Restraunt_item)
                dialog.setTitle("Error")
                dialog.setMessage("No Internet Connection")
                dialog.setPositiveButton("Open Setting"){text,listener->
                    var setting= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(setting)
                    this@Restraunt_item?.finish()
                }
                dialog.setNegativeButton("Cancel"){text,listener->
                    ActivityCompat.finishAffinity(this@Restraunt_item)
                }
                dialog.create()
                dialog.show()
            }
        }
        else{
            Toast.makeText(this@Restraunt_item,"Some Error occured",Toast.LENGTH_LONG).show()
        }

    }
    private fun addToolbar(restrauntName:String?){
        toolbarItem=findViewById(R.id.toolbarItem)
        setSupportActionBar(toolbarItem)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title=restrauntName
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}