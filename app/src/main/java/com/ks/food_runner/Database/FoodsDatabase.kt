package com.ks.food_runner.Database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FoodEntity::class],version = 1)
abstract class FoodsDatabase:RoomDatabase() {
    abstract fun foodDao():FoodDao
}