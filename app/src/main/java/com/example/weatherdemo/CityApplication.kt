package com.example.weatherdemo

import android.app.Application
import com.example.weatherdemo.model.CityRepository
import com.example.weatherdemo.model.CityRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CityApplication() : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { CityRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { CityRepository(database.cityDao()) }
}