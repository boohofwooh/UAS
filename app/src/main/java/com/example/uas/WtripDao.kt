package com.example.uas

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WtripDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(trips: Trips)

    @Update
    fun update(trips: Trips)

    @Delete
    fun delete(trips: Trips)

    @get:Query("SELECT * from trip_table ORDER BY id ASC")
    val allClasses: LiveData<List<Trips>>
}