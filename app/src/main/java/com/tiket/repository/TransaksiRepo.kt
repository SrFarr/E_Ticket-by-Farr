package com.tiket.repository

import com.tiket.local.dao.TransaksiDao
import com.tiket.local.entity.Transaksi

class TransaksiRepo(private val dao: TransaksiDao) {

    suspend fun insert(transaksi: Transaksi) = dao.insert(transaksi)
    suspend fun update(transaksi: Transaksi) = dao.update(transaksi)
    suspend fun delete(transaksi: Transaksi) = dao.delete(transaksi)
    suspend fun deleteAll() = dao.deleteAll()
    suspend fun getAll() = dao.getAll()
    suspend fun getById(id: Int) = dao.getById(id)

    // ==== Extra ====
    suspend fun getByUserId(userId: Int) = dao.getByUserId(userId)
    suspend fun getByKodeBooking(kodeBooking: String) = dao.getByKodeBooking(kodeBooking)
}