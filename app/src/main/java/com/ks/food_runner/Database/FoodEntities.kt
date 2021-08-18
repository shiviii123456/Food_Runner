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


@Entity(tableName = "CartEntity")
data class CartEntities (
    @PrimaryKey val restaurantId:Int,
    @ColumnInfo(name="foodItem_Id") val foodItemId:String,
    @ColumnInfo(name="foodItem_Price") val foodItemPrice:String,
    @ColumnInfo(name="foodItem_Name") val foodItemName:String,
)