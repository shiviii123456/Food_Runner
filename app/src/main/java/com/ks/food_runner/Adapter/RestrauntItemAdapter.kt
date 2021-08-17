package com.ks.food_runner.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ks.food_runner.Model.RestrauntItem
import com.ks.food_runner.R

class RestrauntItemAdapter(val context: Context, val food:List<RestrauntItem>):RecyclerView.Adapter<RestrauntItemAdapter.ViewHolder>() {
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
        holder.itemId.text=food[position].ItemId
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
}