package com.ks.food_runner.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FoodDao {
    @Insert
    fun insertFood(foodFav:FoodEntity)

    @Delete
    fun removeFood(foodFav:FoodEntity)

    @Query("SELECT * FROM FoodTable")
    fun allFood():List<FoodEntity>

    @Query("SELECT * FROM FoodTable WHERE id=:foodId")
    fun checkFook(foodId:String):FoodEntity

    @Insert
    fun insertItem(cartEntities: CartEntities)

    @Delete
    fun deleteItem(cartEntities: CartEntities)

    @Query("SELECT * FROM CartEntity WHERE restaurantId=:foodItem_id")
    fun checkItem(foodItem_id: Int): CartEntities

    @Query("SELECT * FROM CartEntity WHERE foodItem_Id=:restaurant_id")
    fun menuList(restaurant_id:String):List<CartEntities>

//    @Query("SELECT * FROM CartEntity WHERE restaurantId IN (:addedItems)")
//    fun test(addedItems:List<Int>):List<CartEntities>
}