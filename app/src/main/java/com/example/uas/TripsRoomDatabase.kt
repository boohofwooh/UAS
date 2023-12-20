package com.example.uas

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Trips::class], version = 2, exportSchema = false)
abstract class TripsRoomDatabase : RoomDatabase() {
    abstract fun tripsDao(): WtripDao

    companion object {
        @Volatile
        private var INSTANCE: TripsRoomDatabase? = null

        fun getInstance(context: Context): TripsRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripsRoomDatabase::class.java, "trip_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
