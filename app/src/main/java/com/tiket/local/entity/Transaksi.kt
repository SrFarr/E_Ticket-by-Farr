package com.tiket.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaksi",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Transportasi::class,
            parentColumns = ["id"],
            childColumns = ["transportasiId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Transaksi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val transportasiId: Int,
    val jumlah: Int,
    val totalHarga: Double,
    val kodeBooking: String
)


