package com.tiket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiket.local.entity.Transaksi
import com.tiket.repository.TransaksiRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransaksiVm(private val repo: TransaksiRepo) : ViewModel() {
    private val _transaksiList = MutableStateFlow<List<Transaksi>>(emptyList())
    val transaksiList: StateFlow<List<Transaksi>> = _transaksiList

    private val _selectedTransaksi = MutableStateFlow<Transaksi?>(null)
    val selectedTransaksi: StateFlow<Transaksi?> = _selectedTransaksi

    // ==== CRUD ====
    fun insert(transaksi: Transaksi) = viewModelScope.launch {
        repo.insert(transaksi)
        loadAll()
    }

    fun update(transaksi: Transaksi) = viewModelScope.launch {
        repo.update(transaksi)
        loadAll()
    }

    fun delete(transaksi: Transaksi) = viewModelScope.launch {
        repo.delete(transaksi)
        loadAll()
    }

    fun loadAll() = viewModelScope.launch {
        _transaksiList.value = repo.getAll()
    }

    fun loadById(id: Int) = viewModelScope.launch {
        _selectedTransaksi.value = repo.getById(id)
    }

    fun loadByUserId(userId: Int) = viewModelScope.launch {
        _transaksiList.value = repo.getByUserId(userId)
    }

    fun loadByKodeBooking(kodeBooking: String) = viewModelScope.launch {
        _selectedTransaksi.value = repo.getByKodeBooking(kodeBooking)
    }
}