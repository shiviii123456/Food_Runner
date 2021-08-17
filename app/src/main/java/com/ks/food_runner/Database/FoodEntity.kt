package com.ks.food_runner.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FoodTable")
data class FoodEntity(
    @PrimaryKey val id:Int,
    @ColumnInfo(name = "restaurant_Name")  val restaurantName:String,
    @ColumnInfo(name = "food_price") val foodPrice:String,
    @ColumnInfo(name = "ratings") val rating:String,
   @ColumnInfo(name="restaurant_Image") val restaurantImage:String
)