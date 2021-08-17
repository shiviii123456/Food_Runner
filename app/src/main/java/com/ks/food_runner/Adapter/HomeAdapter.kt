package com.ks.food_runner.Adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.ks.food_runner.Database.FoodEntity
import com.ks.food_runner.Database.FoodsDatabase
import com.ks.food_runner.Model.Restaurant
import com.ks.food_runner.R
import com.ks.food_runner.Restraunt_item
import com.squareup.picasso.Picasso

class HomeAdapter(val context: Context, val food:List<Restaurant>):RecyclerView.Adapter<HomeAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recycler_home,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
       holder.restaurantName.text=food[position].restaurantName
        holder.foodPrice.text=food[position].foodPrice
        holder.rating.text=food[position].rating
        Picasso.get().load(food[position].restaurantImage).error(R.drawable.food1).into(holder.restaurantImage)

                            val foodEntity=FoodEntity(
                                food[position].foodId?.toInt() as Int,
                                food[position].restaurantName,
                                food[position].foodPrice,
                                food[position].rating,
                                food[position].restaurantImage
                            )
                            val checkFood=DbAsyncTak(context,1,foodEntity).execute()
                            val checkFoodBool=checkFood.get()
                            if(checkFoodBool){
                                holder.heart.setImageResource(R.drawable.ic_fillheart)
                            }
                            else{
                                holder.heart.setImageResource(R.drawable.ic_heart)
                            }
                            holder.heart.setOnClickListener(){
                                if(!DbAsyncTak(context,1,foodEntity).execute().get()){
                                    val food=DbAsyncTak(context,2,foodEntity).execute()
                                    val foodBool=food.get()
                                    if(foodBool){
                                        holder.heart.setImageResource(R.drawable.ic_fillheart)
                                        Toast.makeText(context,"added",Toast.LENGTH_LONG).show()
                                    }
                                    else{
                                        Toast.makeText(context,"Some error Occured",Toast.LENGTH_LONG).show()
                                    }
                                }
                              else{
                                    val food=DbAsyncTak(context,3,foodEntity).execute()
                                    val foodBool=food.get()
                                    if(foodBool){
                                        holder.heart.setImageResource(R.drawable.ic_heart)
                                        Toast.makeText(context,"remove",Toast.LENGTH_LONG).show()
                                    }
                                    else{
                                        Toast.makeText(context,"Some Error Occured",Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
        holder.cardviewRes.setOnClickListener(){
            val intent=Intent(context,Restraunt_item::class.java)
            intent.putExtra("foodId",food[position].foodId)
            intent.putExtra("restrauntName",food[position].restaurantName)
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
       return food.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
      var restaurantImage:ImageView=itemView.findViewById(R.id.restaurantImage)
        var restaurantName:TextView=itemView.findViewById(R.id.restaurantName)
        var foodPrice:TextView=itemView.findViewById(R.id.foodPrice)
        var rating:TextView=itemView.findViewById(R.id.rating)
        var cardviewRes:CardView=itemView.findViewById(R.id.cardviewRes)
        var cardLinearLayout:LinearLayout=itemView.findViewById(R.id.cardLinearLayout)
        var heart:ImageView=itemView.findViewById(R.id.heart)
    }

    class DbAsyncTak(val context: Context,val mode:Int,val foodEntity: FoodEntity): AsyncTask<Void, Void, Boolean>(){

        val db= Room.databaseBuilder(context, FoodsDatabase::class.java,"food-database").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1->{
                   val food:FoodEntity?=db.foodDao().checkFook(foodEntity.id.toString())
                   db.close()
                    return food != null
                }
                2->{
                   db.foodDao().insertFood(foodEntity)
                 db.close()
                   return true
                }
                3->{
                   db.foodDao().removeFood(foodEntity)
                  db.close()
                    return true
                }
            }
            return false
        }
    }
}