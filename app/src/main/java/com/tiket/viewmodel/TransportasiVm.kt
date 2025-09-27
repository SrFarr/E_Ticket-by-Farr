package com.tiket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiket.local.entity.Transportasi
import com.tiket.repository.TransportasiRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransportasiVm(private val repo: TransportasiRepo): ViewModel() {
    private val _transportasiList = MutableStateFlow<List<Transportasi>>(emptyList())
    val transportasiList: StateFlow<List<Transportasi>> = _transportasiList

    private val _selectedTransportasi = MutableStateFlow<Transportasi?>(null)
    val selectedTransportasi: StateFlow<Transportasi?> = _selectedTransportasi

    private val _pesawatList = MutableStateFlow<List<Transportasi>>(emptyList())
    val pesawatList: StateFlow<List<Transportasi>> = _pesawatList

    private val _keretaList = MutableStateFlow<List<Transportasi>>(emptyList())
    val keretaList: StateFlow<List<Transportasi>> = _keretaList

    private val _searchResults = MutableStateFlow<List<Transportasi>>(emptyList())
    val searchResults: StateFlow<List<Transportasi>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadAll() = viewModelScope.launch {
        _isLoading.value = true
        try {
            _transportasiList.value = repo.getAll()
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Gagal memuat data: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun loadAllPesawat() = viewModelScope.launch {
        _isLoading.value = true
        try {
            _pesawatList.value = repo.getAllPesawat()
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Gagal memuat data pesawat: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun loadAllKereta() = viewModelScope.launch {
        _isLoading.value = true
        try {
            _keretaList.value = repo.getAllKereta()
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Gagal memuat data kereta: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    // CRUD Operations
    fun insert(transportasi: Transportasi) = viewModelScope.launch {
        _isLoading.value = true
        try {
            repo.insert(transportasi)
            when (transportasi.jenis) {
                "pesawat" -> loadAllPesawat()
                "kereta" -> loadAllKereta()
                else -> loadAll()
            }
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Gagal menambah data: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun update(transportasi: Transportasi) = viewModelScope.launch {
        _isLoading.value = true
        try {
            repo.update(transportasi)
            when (transportasi.jenis) {
                "pesawat" -> loadAllPesawat()
                "kereta" -> loadAllKereta()
                else -> loadAll()
            }
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Gagal mengupdate data: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun delete(transportasi: Transportasi) = viewModelScope.launch {
        _isLoading.value = true
        try {
            repo.delete(transportasi)
            when (transportasi.jenis) {
                "pesawat" -> loadAllPesawat()
                "kereta" -> loadAllKereta()
                else -> loadAll()
            }
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Gagal menghapus data: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun searchByTujuan(tujuan: String) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val results = repo.searchByTujuan(tujuan)
            _transportasiList.value = results // 🔥 update langsung UI
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Gagal mencari data: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun searchByTanggal(startDate: Long, endDate: Long) = viewModelScope.launch {
        _transportasiList.value = repo.searchByTanggal(startDate, endDate)
    }

    fun searchByTanggalAndTujuan(tujuan: String, startDate: Long, endDate: Long) = viewModelScope.launch {
        _transportasiList.value = repo.searchByTanggalAndTujuan(tujuan, startDate, endDate)
    }
    fun searchPesawatByTujuan(tujuan: String) = viewModelScope.launch {
        _isLoading.value = true
        try {
            _pesawatList.value = repo.searchPesawatByTujuan(tujuan)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Gagal mencari pesawat: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun searchPesawatByTanggal(startDate: Long, endDate: Long) = viewModelScope.launch {
        _isLoading.value = true
        try {
            _pesawatList.value = repo.searchPesawatByTanggal(startDate, endDate)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Gagal mencari pesawat: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    // Search methods khusus untuk kereta
    fun searchKeretaByTujuan(tujuan: String) = viewModelScope.launch {
        _isLoading.value = true
        try {
            _keretaList.value = repo.searchKeretaByTujuan(tujuan)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Gagal mencari kereta: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun searchKeretaByTanggal(startDate: Long, endDate: Long) = viewModelScope.launch {
        _isLoading.value = true
        try {
            _keretaList.value = repo.searchKeretaByTanggal(startDate, endDate)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Gagal mencari kereta: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
    fun getKeretaById(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            _selectedTransportasi.value = repo.getKeretaById(id)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Gagal memuat data kereta: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    // Clear error message
    fun clearError() {
        _errorMessage.value = null
    }

    // Clear selected transportasi
    fun clearSelected() {
        _selectedTransportasi.value = null
    }

    // Clear search results
    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }
}