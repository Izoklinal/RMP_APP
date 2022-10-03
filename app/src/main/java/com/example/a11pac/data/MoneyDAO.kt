package com.example.a11pac.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.a11pac.data.models.Cost
import com.example.a11pac.data.models.CostType

@Dao
interface MoneyDAO {
    /* Таблица CostType */
    @Query("SELECT * FROM $TYPES_TABLE")
    fun getAllTypes(): LiveData<List<CostType>>

    @Insert
    fun addType(costType: CostType)
    @Update
    fun saveType(costType: CostType)
    @Delete
    fun killType(costType: CostType)

    /* Таблица CostType */
    @Query("SELECT * FROM $COSTS_TABLE")
    fun getAllCosts(): LiveData<List<Cost>>
    @Query("SELECT * FROM $COSTS_TABLE WHERE _id=:id")
    fun getCost(id:Int): LiveData<Cost>
    @Insert
    fun addCost(cost: Cost)
    @Update
    fun saveCost(cost: Cost)
    @Delete
    fun killCost(cost: Cost)
}

