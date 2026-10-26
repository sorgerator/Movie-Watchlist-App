package com.example.filmfolio.data.repository

import com.example.filmfolio.data.local.dao.UserDao
import com.example.filmfolio.data.mappers.toDomain
import com.example.filmfolio.data.mappers.toEntity
import com.example.filmfolio.data.local.entity.User
import com.example.filmfolio.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun register(fullName: String, email: String, pass: String): Long = withContext(Dispatchers.IO) {
        val user = User(0, fullName, email, System.currentTimeMillis())
        userDao.insertUser(user.toEntity(pass))
    }

    override suspend fun login(email: String, pass: String): User? = withContext(Dispatchers.IO) {
        val entity = userDao.getUserByEmail(email)
        if (entity != null && entity.password == pass) {
            entity.toDomain()
        } else null
    }

    override suspend fun getUserById(id: Long): User? = withContext(Dispatchers.IO) {
        userDao.getUserById(id)?.toDomain()
    }

    override suspend fun getUserByEmail(email: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByEmail(email)?.toDomain()
    }

    override fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers().map { entities ->
        entities.map { it.toDomain() }
    }
}
