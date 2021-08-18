package com.ks.food_runner

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.ks.food_runner.Adapter.ProfileAdapter
import com.ks.food_runner.Database.FoodEntity
import com.ks.food_runner.Database.FoodsDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Profile : Fragment() {
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
    lateinit var recyclerviewProfile:RecyclerView
    lateinit var progressBarProfile: ProgressBar
    var foodLists= listOf<FoodEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
        recyclerviewProfile=view.findViewById(R.id.recyclerviewProfile)
        progressBarProfile=view.findViewById(R.id.progressBarProfile)
        foodLists=TotalFoodProfile(activity as Context).execute().get()

        Toast.makeText(activity as Context,"$foodLists",Toast.LENGTH_LONG).show()
        if(activity != null){
            progressBarProfile.setVisibility(View.GONE)
            recyclerviewProfile.layoutManager= LinearLayoutManager(activity)
            recyclerviewProfile.adapter= ProfileAdapter(activity as Context, foodLists)
        }
        else{
            Toast.makeText(activity as Context,"Some Error Occured",Toast.LENGTH_LONG).show()
        }
        return view
    }
    class TotalFoodProfile(val context: Context): AsyncTask<Void, Void, List<FoodEntity>>(){
        var db= Room.databaseBuilder(context, FoodsDatabase::class.java,"food-database").build()
        override fun doInBackground(vararg p0: Void?): List<FoodEntity> {
            return db.foodDao().allFood()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderHistory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderHistory().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}