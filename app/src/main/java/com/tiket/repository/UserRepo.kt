package com.tiket.repository

import com.tiket.local.dao.UserDao
import com.tiket.local.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepo(private val userDao: UserDao) {
    suspend fun insertUser(user: User) = userDao.insert(user)

    suspend fun updateUser(user: User) = userDao.update(user)

    suspend fun deleteUser(user: User) = userDao.delete(user)

    suspend fun getAllUsers() = userDao.getAllUsers()

    suspend fun getUserById(id: Int): User? = userDao.getUserById(id)

    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
}