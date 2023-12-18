package com.example.uas

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(wTrip: wTrip)

    @Update
    fun update(wTrip: wTrip)

    @Delete
    fun delete(wTrip: wTrip)

    @Query("SELECT * from wtrip_table ORDER BY id ASC")
    fun getAllClasses(): LiveData<List<wTrip>>
}