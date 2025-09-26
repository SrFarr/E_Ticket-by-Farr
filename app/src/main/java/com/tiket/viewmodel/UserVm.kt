package com.tiket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiket.local.entity.User
import com.tiket.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserVm( private val repo: UserRepo) : ViewModel() {


    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser

    fun loadAllUsers() = viewModelScope.launch(Dispatchers.IO) {
        repo.getAllUsers().collect { users ->
            _userList.value = users
        }
    }


    fun insertUser(user: User) = viewModelScope.launch {
        repo.insertUser(user)
        loadAllUsers()
    }

    fun updateUser(user: User) = viewModelScope.launch {
        repo.updateUser(user)
        loadAllUsers()
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        repo.deleteUser(user)
        loadAllUsers()
    }

    fun getUserById(id: Int) = viewModelScope.launch {
        _selectedUser.value = repo.getUserById(id)
    }
}