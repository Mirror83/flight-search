package com.example.flightsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Airport::class, Favorite::class], version = 1, exportSchema = false)
abstract class FlightSearchDatabase: RoomDatabase() {
    abstract fun getAirportDao() : AirportDao
    abstract fun getFavoriteDao() : FavoriteDao

    companion object {
        @Volatile
        private var Instance: FlightSearchDatabase? = null

        fun getDatabase(context: Context) : FlightSearchDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FlightSearchDatabase::class.java, "flights")
                    .fallbackToDestructiveMigration()
                    // Uncomment this for the first run to use the database file in the assets
                    // folder and comment this for subsequent runs to prevent the
                    // database from being reset
//                    .createFromAsset("database/flight_search.db")
                    .build()
                    .also { Instance = it }
            }
        }

    }
}