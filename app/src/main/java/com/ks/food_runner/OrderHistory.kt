package com.ks.food_runner

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ks.food_runner.Adapter.PreviousOrderAdapter
import com.ks.food_runner.Model.FoodInfo
import com.ks.food_runner.Model.PreviousOrder
import com.ks.food_runner.util.ConnectionManager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderHistory.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderHistory : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var previousOrder: PreviousOrder
    var previousItem= mutableListOf<PreviousOrder>()
    lateinit var recyclerviewPrevious:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_order_history, container, false)
        recyclerviewPrevious=view.findViewById(R.id.recyclerviewPrevious)
        var orderHistory=activity?.getSharedPreferences(getString(R.string.login_pref),Context.MODE_PRIVATE)
        val id=orderHistory?.getString("user_id","").toString()

        val newRequest= Volley.newRequestQueue(activity as Context)
        var url="http://13.235.250.119/v2/orders/fetch_result/$id"
        if(ConnectionManager().checkConnection(activity as Context)){
            var jsonRequest=object:JsonObjectRequest(Method.GET,url,null,Response.Listener {
                 val data=it.getJSONObject("data")
                val success=data.getBoolean("success")
                if(success){
                    var foodItemInfo=data.getJSONArray("data")
                    for(i in 0 until foodItemInfo.length()){
                      var foodInfo=foodItemInfo.getJSONObject(i)
                        var restaurant_name=foodInfo.getString("restaurant_name")
                        var order_placed_at=foodInfo.getString("order_placed_at")
                        var food_items=foodInfo.getJSONArray("food_items")
                        var foodItem= mutableListOf<FoodInfo>()
                        for(i in 0 until food_items.length()){
                            var foodInformation=food_items.getJSONObject(i)
                            var name=foodInformation.getString("name")
                            var cost=foodInformation.getString("cost")
                            val foodData=FoodInfo(name,cost)
                            foodItem.add(foodData)
                        }
                        previousOrder= PreviousOrder(
                            restaurant_name,
                            order_placed_at,
                            foodItem
                            )
                        Log.d("hello","$foodItem")
                        previousItem.add(previousOrder)
                        recyclerviewPrevious.layoutManager=LinearLayoutManager(activity)
                        recyclerviewPrevious.adapter=PreviousOrderAdapter(activity as Context,previousItem)
                        Log.d("previous","$previousOrder")
                    }
                }
                else{
                    Toast.makeText(activity as Context,"Some Error Occured",Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(activity as Context,"Volley Error Occured",Toast.LENGTH_LONG).show()
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val header=HashMap<String,String>()
                    header["Content-type"]="application/json"
                    header["token"]="9bf534118365f1"
                    return header
                }
            }
            newRequest.add(jsonRequest)
        }
        else{
            //if internet is not available
            var dialog= AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection")
            dialog.setPositiveButton("Open Setting"){text,listener->
                var setting= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(setting)
                activity?.finish()
            }
            dialog.setNegativeButton("Cancel"){text,listener->
//                ActivityCompat.finishAffinity(activity?)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }
    }

