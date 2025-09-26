package com.tiket.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tiket.local.db.AppDb
import com.tiket.repository.TransportasiRepo

class TransportasiVmFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransportasiVm::class.java)) {
            val database = AppDb.getDatabase(context)
            val transportasiDao = database.transportasiDao()
            val repo = TransportasiRepo(transportasiDao)
            return TransportasiVm(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
