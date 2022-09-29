package com.example.a11pac.data.models

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.a11pac.data.TYPES_TABLE

@Entity(tableName = TYPES_TABLE)
data class CostType (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID)
    val id: Int,
    var title: String
)