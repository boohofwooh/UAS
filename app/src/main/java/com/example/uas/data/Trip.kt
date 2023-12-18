package com.example.uas.data

data class Trip(
    var id: String = "",
    val tgl_brgkt: String = "",
    val tgl_kembali: String = "",
    val st_awal: String = "",
    val st_tujuan: String = "",
    val kelas: String = "",
    val kursi: String = "",
    val harga: String = "",
    val list_paket: List<String> = emptyList()
)
