package com.tiket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiket.local.entity.User
import com.tiket.repository.AuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Registered : AuthState()
    data class LoggedIn(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthVm(private val repo: AuthRepo) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun register(email: String, username: String, password: String, isAdmin: Boolean = false) {
        viewModelScope.launch {
            val success = repo.register(User(0, email, username, password, isAdmin))
            if (success) {
                _authState.value = AuthState.Registered
            } else {
                _authState.value = AuthState.Error("Email sudah digunakan")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = repo.login(email, password)
            if (user != null) {
                _authState.value = AuthState.LoggedIn(user)
            } else {
                _authState.value = AuthState.Error("Email atau password salah")
            }
        }
    }

    fun logout() {
        _authState.value = AuthState.Idle
    }
}