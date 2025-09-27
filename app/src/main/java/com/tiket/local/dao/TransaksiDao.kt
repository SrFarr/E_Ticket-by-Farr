package com.tiket.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tiket.local.entity.Transaksi

@Dao
interface TransaksiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaksi: Transaksi): Long

    @Update
    suspend fun update(transaksi: Transaksi)

    @Delete
    suspend fun delete(transaksi: Transaksi)

    @Query("DELETE FROM transaksi")
    suspend fun deleteAll()

    @Query("SELECT * FROM transaksi")
    suspend fun getAll(): List<Transaksi>

    @Query("SELECT * FROM transaksi WHERE id = :id")
    suspend fun getById(id: Int): Transaksi?

    @Query("SELECT * FROM transaksi WHERE userId = :userId")
    suspend fun getByUserId(userId: Int): List<Transaksi>

    @Query("SELECT * FROM transaksi WHERE kodeBooking = :kodeBooking LIMIT 1")
    suspend fun getByKodeBooking(kodeBooking: String): Transaksi?
}