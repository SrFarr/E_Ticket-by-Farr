package com.tiket.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transportasi")
data class Transportasi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val maskapai: String,
    val jenis: String,
    val asal: String,
    val tujuan: String,
    val waktuBerangkat: Long,
    val harga: Double
)
