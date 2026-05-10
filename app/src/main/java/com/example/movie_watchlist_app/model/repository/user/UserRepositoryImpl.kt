package com.example.movie_watchlist_app.model.repository.user

import com.example.movie_watchlist_app.model.data.local.dao.UserDao
import com.example.movie_watchlist_app.model.data.local.entity.UserEntity
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {
    override suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
    override suspend fun getUserByEmailAndPassword(email: String, password: String) = userDao.getUserByEmailAndPassword(email, password)
    override suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)
    override suspend fun getUserById(id: Int) = userDao.getUserById(id)
    override fun observeUserById(id: Int) = userDao.observeUserById(id)
    override suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
    override suspend fun deleteUser(user: UserEntity) = userDao.deleteUser(user)
}