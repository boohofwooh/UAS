package com.example.uas

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wtrip_table")
data class wTrip (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    @ColumnInfo(name = "tgl_brgkt")
    val tgl_brgkt: String,

    @ColumnInfo(name = "tgl_kembali")
    val tgl_kembali: String,

    @ColumnInfo(name = "st_awal")
    val st_awal: String,

    @ColumnInfo(name = "st_tujuan")
    val st_tujuan: String
)