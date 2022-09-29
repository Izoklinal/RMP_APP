package com.example.a11pac.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.a11pac.data.models.Cost
import com.example.a11pac.data.models.CostType
import com.example.a11pac.data.models.DatesTypeConverter

@Database(entities = [CostType::class, Cost::class], version = 1)
@TypeConverters(DatesTypeConverter::class)
abstract class MoneyDataBase: RoomDatabase() {
    abstract fun moneyDAO(): MoneyDAO
}
