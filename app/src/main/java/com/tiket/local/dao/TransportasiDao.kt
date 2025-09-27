package com.tiket.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tiket.local.entity.Transportasi

@Dao
interface TransportasiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(t: Transportasi): Long

    @Update
    suspend fun update(t: Transportasi)

    @Delete
    suspend fun delete(t: Transportasi)

    @Query("SELECT * FROM transportasi")
    suspend fun getAll(): List<Transportasi>

    @Query("SELECT * FROM transportasi WHERE id = :id")
    suspend fun getById(id: Int): Transportasi?

    @Query("SELECT * FROM transportasi WHERE tujuan LIKE '%' || :tujuan || '%'")
    suspend fun searchByTujuan(tujuan: String): List<Transportasi>

    @Query("SELECT * FROM transportasi WHERE waktuBerangkat BETWEEN :startDate AND :endDate")
    suspend fun searchByTanggal(startDate: Long, endDate: Long): List<Transportasi>

    @Query("""
        SELECT * FROM transportasi 
        WHERE tujuan LIKE '%' || :tujuan || '%' 
        AND waktuBerangkat BETWEEN :startDate AND :endDate
    """)
    suspend fun searchByTanggalAndTujuan(
        tujuan: String,
        startDate: Long,
        endDate: Long
    ): List<Transportasi>

    @Query("SELECT * FROM transportasi WHERE jenis = 'pesawat'")
    suspend fun getAllPesawat(): List<Transportasi>

    @Query("SELECT * FROM transportasi WHERE jenis = 'kereta'")
    suspend fun getAllKereta(): List<Transportasi>

    @Query("SELECT * FROM transportasi WHERE jenis = 'pesawat' AND tujuan LIKE '%' || :tujuan || '%'")
    suspend fun searchPesawatByTujuan(tujuan: String): List<Transportasi>

    @Query("SELECT * FROM transportasi WHERE jenis = 'kereta' AND tujuan LIKE '%' || :tujuan || '%'")
    suspend fun searchKeretaByTujuan(tujuan: String): List<Transportasi>

    // Query untuk search pesawat berdasarkan tanggal
    @Query("SELECT * FROM transportasi WHERE jenis = 'pesawat' AND waktuBerangkat BETWEEN :startDate AND :endDate")
    suspend fun searchPesawatByTanggal(startDate: Long, endDate: Long): List<Transportasi>

    // Query untuk search kereta berdasarkan tanggal
    @Query("SELECT * FROM transportasi WHERE jenis = 'kereta' AND waktuBerangkat BETWEEN :startDate AND :endDate")
    suspend fun searchKeretaByTanggal(startDate: Long, endDate: Long): List<Transportasi>

    // Query untuk mendapatkan pesawat by ID
    @Query("SELECT * FROM transportasi WHERE jenis = 'pesawat' AND id = :id")
    suspend fun getPesawatById(id: Int): Transportasi?

    // Query untuk mendapatkan kereta by ID
    @Query("SELECT * FROM transportasi WHERE jenis = 'kereta' AND id = :id")
    suspend fun getKeretaById(id: Int): Transportasi?
}