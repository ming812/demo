package com.example.weatherdemo.model

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class CityRepository(private val cityDao: CityDao) {

    val allCity : LiveData<List<City>>  = cityDao.getAllRecord()
    val lastRecord : LiveData<City> = cityDao.getLastRecord()

    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
    suspend fun insert(city: City) {
        cityDao.insert(city)
    }

    fun deleteAll(){
        cityDao.deleteAll()
    }

}