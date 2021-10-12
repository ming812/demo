package com.example.weatherdemo.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class CityRoomDatabase : RoomDatabase() {

    abstract fun cityDao() : CityDao

    private class CityDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var cityDao = database.cityDao()

                    // Delete all content here.
//                    cityDao.deleteAll()
//
//                    // Add sample words.
//                    var word = City("Hello")
//                    cityDao.insert(word)
//                    word = City("World!")
//                    cityDao.insert(word)
//
//                    // TODO: Add your own words!
//                    word = City("TODO!")
//                    cityDao.insert(word)
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CityRoomDatabase? = null

        fun getDatabase(context: Context , scope: CoroutineScope): CityRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityRoomDatabase::class.java,
                    "city_database"
                ).addCallback(CityDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}