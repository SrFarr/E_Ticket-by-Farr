package com.tiket.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tiket.local.db.AppDb
import com.tiket.repository.AuthRepo
import com.tiket.repository.TransaksiRepo

class TransaksiVmFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDb.getDatabase(context)
        val repo = TransaksiRepo(db.transaksiDao())
        return TransaksiVm(repo) as T
    }
}
