package com.example.weatherdemo.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDao {

    @Query("SELECT * FROM city_table")
    fun getAllRecord() : LiveData<List<City>>

    @Query("SELECT * FROM city_table ORDER BY lastModifierDate DESC LIMIT 1")
    fun getLastRecord() : LiveData<City>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(city: City)

    @Query("DELETE FROM city_table")
    fun deleteAll()
}