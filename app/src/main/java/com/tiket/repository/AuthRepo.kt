package com.tiket.repository

import com.tiket.local.dao.AuthDao
import com.tiket.local.entity.User

class AuthRepo(private val dao: AuthDao) {

    suspend fun register(user: User): Boolean {
        val existing = dao.getUserByEmail(user.email)
        if (existing != null) return false
        dao.insertUser(user)
        return true
    }

    suspend fun login(email: String, password: String): User? {
        return dao.login(email, password)
    }

}