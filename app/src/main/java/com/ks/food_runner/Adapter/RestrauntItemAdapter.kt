package com.ks.food_runner.Adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.ks.food_runner.Database.CartEntities
import com.ks.food_runner.Database.FoodEntity
import com.ks.food_runner.Database.FoodsDatabase
import com.ks.food_runner.Model.RestrauntItem
import com.ks.food_runner.R
import java.lang.Exception

class RestrauntItemAdapter(val context: Context, val food:List<RestrauntItem>):RecyclerView.Adapter<RestrauntItemAdapter.ViewHolder>() {
    var listItem= mutableListOf<Int>()
    var bool=false
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestrauntItemAdapter.ViewHolder {
       val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recycler_restraunt_item,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: RestrauntItemAdapter.ViewHolder, position: Int) {
        holder.restaurantNameItem.text=food[position].itemName
        holder.foodPriceItem.text="Rs. "+food[position].ItemPrice
        holder.itemId.text=food[position].num
        var cartEntities=CartEntities(
            food[position].ItemId?.toInt(),
            food[position].Restraunt_id,
            food[position].ItemPrice,
            food[position].itemName,
        )


            holder.btnItem.setOnClickListener(){
                var foodArray=TotalItem(context).execute().get()
//                Toast.makeText(context,"$foodArray",Toast.LENGTH_LONG).show()
                if(foodArray.size == 0){
//                    Toast.makeText(context,"Hello",Toast.LENGTH_LONG).show()
                    val insert=DbAsyncCart(context,2,cartEntities).execute().get()
                    if(insert){
                        val color=ContextCompat.getColor(context,R.color.secondarycolor)
                        holder.btnItem.setBackgroundColor(color)
                        holder.btnItem.text="REMOVE"
                    }
                    else{
                        Toast.makeText(context,"Some Error Occured",Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    for (i in 0 until foodArray.size) {
                        Log.d("output", "${foodArray.get(i).foodItemId}")
                        if (foodArray.get(i).foodItemId == cartEntities.foodItemId) {
                            bool = true
                        }
                    }
                        if(bool) {
                            var checkItem = TotalAddItem(context, cartEntities).execute().get()
                            Log.d("message","$checkItem")
                            if (TotalAddItem(context, cartEntities).execute().get() == null) {
                                val insert = DbAsyncCart(context, 2, cartEntities).execute().get()
                                if (insert) {
                                    val color = ContextCompat.getColor(context, R.color.secondarycolor)
                                    holder.btnItem.setBackgroundColor(color)
                                    holder.btnItem.text = "REMOVE"
                                } else {
                                    Toast.makeText(context, "Some Error Occured", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                val remove = DbAsyncCart(context, 3, cartEntities).execute().get()
                                if (remove) {
                                    val color = ContextCompat.getColor(context, R.color.appcolor)
                                    holder.btnItem.setBackgroundColor(color)
                                    holder.btnItem.text = "ADD"
                                } else {
                                    Toast.makeText(context, "Some Error Occured", Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                        }
                        else{
                            var dialog= AlertDialog.Builder(context)
                            dialog.setTitle("Error")
                            dialog.setMessage("You can order only from one restraunt at a time")
                            dialog.setPositiveButton("Cancel"){text,listener->

                            }
                            dialog.create()
                            dialog.show()
                            }
                }
                }


        if(TotalAddItem(context,cartEntities).execute().get() == null)
        {
            val color=ContextCompat.getColor(context,R.color.appcolor)
            holder.btnItem.setBackgroundColor(color)
            holder.btnItem.text="ADD"
        }
        else{
            val color=ContextCompat.getColor(context,R.color.secondarycolor)
            holder.btnItem.setBackgroundColor(color)
            holder.btnItem.text="REMOVE"
        }

  }
    override fun getItemCount(): Int {
        return food.size
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var linearLayoutItem:LinearLayout=itemView.findViewById(R.id.linearLayoutItem)
        var itemId:TextView=itemView.findViewById(R.id.itemId)
        var restaurantNameItem:TextView=itemView.findViewById(R.id.restaurantNameItem)
        var foodPriceItem:TextView=itemView.findViewById(R.id.foodPriceItem)
        var btnItem: Button =itemView.findViewById(R.id.btnItem)
    }
    class DbAsyncCart(val context: Context,val mode:Int,val cartEntities: CartEntities): AsyncTask<Void, Void, Boolean>(){

        val db= Room.databaseBuilder(context, FoodsDatabase::class.java,"food-database").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1->{
                    val foodItem:CartEntities?=db.foodDao().checkItem(cartEntities.restaurantId)
                    db.close()
                    return foodItem != null
                }
                2->{
                    db.foodDao().insertItem(cartEntities)
                    db.close()
                    return true
                }
                3->{
                    db.foodDao().deleteItem(cartEntities)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
    class TotalAddItem(val context: Context,var cartEntities: CartEntities): AsyncTask<Void, Void, CartEntities>(){
        var db= Room.databaseBuilder(context, FoodsDatabase::class.java,"food-database").build()
        override fun doInBackground(vararg p0: Void?): CartEntities {
            return  db.foodDao().checkItem(cartEntities.restaurantId)

        }
    }
//    class TotalFoodItem(val context: Context,var cartEntities:CartEntities): AsyncTask<Void, Void, List<CartEntities>>(){
//        var db= Room.databaseBuilder(context, FoodsDatabase::class.java,"food-database").build()
//        override fun doInBackground(vararg p0: Void?): List<CartEntities> {
//            return  db.foodDao().menuList(cartEntities.foodItemId)
//
//        }
//    }
    class TotalItem(val context: Context): AsyncTask<Void, Void, List<CartEntities>>(){
        var db= Room.databaseBuilder(context, FoodsDatabase::class.java,"food-database").build()
        override fun doInBackground(vararg p0: Void?): List<CartEntities> {
            return  db.foodDao().test()
        }
    }
}