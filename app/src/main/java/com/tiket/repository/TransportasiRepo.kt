package com.tiket.repository

import com.tiket.local.dao.TransportasiDao
import com.tiket.local.entity.Transportasi

class TransportasiRepo(private val dao: TransportasiDao) {

    // --- CRUD ---
    suspend fun insert(transportasi: Transportasi) = dao.insert(transportasi)
    suspend fun update(transportasi: Transportasi) = dao.update(transportasi)
    suspend fun delete(transportasi: Transportasi) = dao.delete(transportasi)

    // --- All Transportasi ---
    suspend fun getAll() = dao.getAll()
    suspend fun getById(id: Int) = dao.getById(id)
    suspend fun searchByTujuan(tujuan: String) = dao.searchByTujuan(tujuan)
    suspend fun searchByTanggal(startDate: Long, endDate: Long) =
        dao.searchByTanggal(startDate, endDate)
    suspend fun searchByTanggalAndTujuan(tujuan: String, startDate: Long, endDate: Long) =
        dao.searchByTanggalAndTujuan(tujuan, startDate, endDate)

    // --- Pesawat ---
    suspend fun getAllPesawat() = dao.getAllPesawat()
    suspend fun searchPesawatByTujuan(tujuan: String) = dao.searchPesawatByTujuan(tujuan)
    suspend fun searchPesawatByTanggal(startDate: Long, endDate: Long) =
        dao.searchPesawatByTanggal(startDate, endDate)
    suspend fun searchPesawatByTanggalAndTujuan(
        tujuan: String,
        startDate: Long,
        endDate: Long
    ) = dao.searchPesawatByTanggalAndTujuan(tujuan, startDate, endDate)
    suspend fun getPesawatById(id: Int) = dao.getPesawatById(id)

    // --- Kereta ---
    suspend fun getAllKereta() = dao.getAllKereta()
    suspend fun searchKeretaByTujuan(tujuan: String) = dao.searchKeretaByTujuan(tujuan)
    suspend fun searchKeretaByTanggal(startDate: Long, endDate: Long) =
        dao.searchKeretaByTanggal(startDate, endDate)
    suspend fun searchKeretaByTanggalAndTujuan(
        tujuan: String,
        startDate: Long,
        endDate: Long
    ) = dao.searchKeretaByTanggalAndTujuan(tujuan, startDate, endDate)
    suspend fun getKeretaById(id: Int) = dao.getKeretaById(id)
}
