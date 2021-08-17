package com.ks.food_runner.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ks.food_runner.Database.FoodEntity
import com.ks.food_runner.R
import com.squareup.picasso.Picasso

class ProfileAdapter(val context: Context,val food:List<FoodEntity>):RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recycler_profile,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.restaurantNameProfile.text=food[position].restaurantName
        holder.ratingProfile.text=food[position].rating
        holder.foodPriceProfile.text=food[position].foodPrice
        Picasso.get().load(food[position].restaurantImage).error(R.drawable.food1).into(holder.restaurantImageProfile)
    }

    override fun getItemCount(): Int {
     return food.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
          val restaurantNameProfile:TextView=itemView.findViewById(R.id.restaurantNameProfile)
        val foodPriceProfile:TextView=itemView.findViewById(R.id.foodPriceProfile)
        val ratingProfile:TextView=itemView.findViewById(R.id.ratingProfile)
        val restaurantImageProfile:ImageView=itemView.findViewById(R.id.restaurantImageProfile)
    }
}