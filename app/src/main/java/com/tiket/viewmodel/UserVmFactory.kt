package com.tiket.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tiket.local.db.AppDb
import com.tiket.repository.UserRepo

class UserVmFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDb.getDatabase(context)
        val repo = UserRepo(db.userDao())
        return UserVm(repo) as T
    }
}
