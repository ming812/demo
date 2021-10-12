package com.example.weatherdemo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class City(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id : String,
    val name : String,
    val lat : String,
    val lon : String,
    val lastModifierDate : Long
)
