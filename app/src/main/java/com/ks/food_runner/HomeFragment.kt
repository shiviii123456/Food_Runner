package com.ks.food_runner

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.ks.food_runner.Adapter.HomeAdapter
import com.ks.food_runner.Model.Restaurant
import com.ks.food_runner.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    lateinit var restaurant: Restaurant
    lateinit var recyclerviewRes: RecyclerView
    lateinit var progressbarHome:ProgressBar
    var foodArray= mutableListOf<Restaurant>()
    var tempList= mutableListOf<Restaurant>()
    lateinit var searchItem:SearchView
//    var restrauntSharedPreferences: SharedPreferences?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_home, container, false)

        progressbarHome=view.findViewById(R.id.progressbarHome)
        searchItem=view.findViewById(R.id.searchItem)
//        restrauntSharedPreferences=context?.getSharedPreferences(getString(R.string.restrauntName),Context.MODE_PRIVATE)

        var newRequest= Volley.newRequestQueue(activity as Context)
        var url="http://13.235.250.119/v2/restaurants/fetch_result/"

        if(ConnectionManager().checkConnection(activity as Context)){
            //if internet is available

            var jsonObject=object:JsonObjectRequest(Method.GET,url,null,Response.Listener {
                try{
                    var datas=it.getJSONObject("data")
                    var success=datas.getBoolean("success")
                    if(success){
                        progressbarHome.setVisibility(View.GONE)
                        var result=datas.getJSONArray("data")

                        for(i in 0 until result.length()){
                            var FoodObject=result.getJSONObject(i)

                            val foodInfo=Restaurant(
                                FoodObject.getString("id"),
                                FoodObject.getString("name"),
                                FoodObject.getString("cost_for_one"),
                                FoodObject.getString("rating"),
                                FoodObject.getString("image_url")
                            )
                            foodArray.add(foodInfo)
                            recyclerviewRes=view.findViewById(R.id.recyclerviewRes)
                            recyclerviewRes.layoutManager=LinearLayoutManager(activity)
                            recyclerviewRes.adapter=HomeAdapter(activity as Context,tempList)
                        }
//                        val gson = Gson()
//                        val jsonObject=gson.toJson(foodArray)
//                       restrauntSharedPreferences?.edit()?.putString("food",jsonObject)?.apply()
//                        restrauntSharedPreferences?.edit()?.putString("name",  FoodObject.getString("name"))?.apply()
                        //new
                        tempList.addAll(foodArray)
                        searchItem.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
                            override fun onQueryTextSubmit(p0: String?): Boolean {
                                TODO("Not yet implemented")
                            }
                            override fun onQueryTextChange(p0: String?): Boolean {
                                tempList.clear()
                                val searchText=p0!!.toLowerCase(Locale.getDefault())
                                if(searchText.isNotEmpty()){
                                    foodArray.forEach{
                                        if(it.restaurantName.toLowerCase(Locale.getDefault()).contains(searchText)){
                                            tempList.add(it)
                                            recyclerviewRes.adapter!!.notifyDataSetChanged()
                                        }
                                    }
                                }else{
                                    tempList.clear()
                                    tempList.addAll(foodArray)
                                    recyclerviewRes.adapter!!.notifyDataSetChanged()
                                }
                                return false
                            }
                        })
                    }
                }
                catch(e:JSONException){
                    Toast.makeText(activity as Context,"Some Unexpected Error Occur",Toast.LENGTH_LONG).show()
                }
            },
                Response.ErrorListener {
                    Toast.makeText(activity as Context,"Volley Error Occured $it",Toast.LENGTH_LONG).show()
                }
            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val header=HashMap<String,String>()
                    header["Content-type"]="application/json"
                    header["token"]="9bf534118365f1"
                    return header
                }
            }
            newRequest.add(jsonObject)

        }else{
            //if internet is not available
            var dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection")
            dialog.setPositiveButton("Open Setting"){text,listener->
                var setting= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(setting)
                activity?.finish()
            }
            dialog.setNegativeButton("Cancel"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }
}