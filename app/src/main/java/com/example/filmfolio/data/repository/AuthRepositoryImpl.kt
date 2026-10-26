package com.example.filmfolio.data.repository

import android.content.Context
import com.example.filmfolio.data.local.dao.UserDao
import com.example.filmfolio.data.local.entity.UserEntity
import com.example.filmfolio.data.repository.AuthRepository
import com.example.filmfolio.presentation.util.GoogleAuthClient
import com.example.filmfolio.util.Resource
import com.example.filmfolio.util.AppConfig
import com.example.filmfolio.data.di.SessionManager
import com.example.filmfolio.data.remote.AuthApiService
import com.example.filmfolio.data.remote.UserLoginDto
import com.example.filmfolio.data.remote.UserRegisterDto
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleAuthClient: GoogleAuthClient,
    private val authApiService: AuthApiService,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun login(email: String, pass: String): Resource<AuthResult?> {
        val cleanEmail = email.trim()
        return try {
            if (AppConfig.useFirebase) {
                try {
                    val result = firebaseAuth.signInWithEmailAndPassword(cleanEmail, pass).await()
                    val localUser = withContext(Dispatchers.IO) { userDao.getUserByEmail(cleanEmail) }
                    SessionManager.email = result.user?.email ?: cleanEmail
                    SessionManager.fullName = result.user?.displayName?.takeIf { it.isNotBlank() }
                        ?: localUser?.fullName?.takeIf { it.isNotBlank() }
                        ?: "User"
                    Resource.Success(result)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    // Fallback to local Room DB if user exists locally with matching password
                    val localUser = withContext(Dispatchers.IO) { userDao.getUserByEmail(cleanEmail) }
                    if (localUser != null && localUser.password.isNotEmpty() && localUser.password == pass) {
                        SessionManager.saveSession("local_token_${localUser.id}")
                        SessionManager.email = localUser.email
                        SessionManager.fullName = localUser.fullName
                        Resource.Success(null)
                    } else {
                        Resource.Error(e.message ?: "Login failed")
                    }
                }
            } else {
                try {
                    // Call REST Auth API
                    val response = authApiService.login(UserLoginDto(cleanEmail, pass))
                    // Save session credentials
                    SessionManager.saveSession(response.token)
                    SessionManager.email = response.user.email
                    SessionManager.fullName = response.user.fullName
                    Resource.Success(null)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    // Offline fallback to local Room DB
                    val localUser = withContext(Dispatchers.IO) { userDao.getUserByEmail(cleanEmail) }
                    if (localUser != null && localUser.password == pass) {
                        SessionManager.saveSession("local_token_${localUser.id}")
                        SessionManager.email = localUser.email
                        SessionManager.fullName = localUser.fullName
                        Resource.Success(null)
                    } else {
                        Resource.Error("Invalid email or password")
                    }
                }
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun register(email: String, pass: String, fullName: String): Resource<AuthResult?> {
        val cleanEmail = email.trim()
        val cleanName = fullName.trim()
        return try {
            if (AppConfig.useFirebase) {
                try {
                    val result = firebaseAuth.createUserWithEmailAndPassword(cleanEmail, pass).await()
                    val user = result.user
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(cleanName)
                        .build()
                    user?.updateProfile(profileUpdates)?.await()
                    SessionManager.email = cleanEmail
                    SessionManager.fullName = cleanName
                    Resource.Success(result)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    if (e is FirebaseAuthUserCollisionException ||
                        e is FirebaseAuthWeakPasswordException ||
                        e is FirebaseAuthInvalidCredentialsException
                    ) {
                        return Resource.Error(e.message ?: "Registration failed")
                    }
                    // Fallback to local Room DB if Firebase is unreachable or provider not enabled
                    val existingLocal = withContext(Dispatchers.IO) { userDao.getUserByEmail(cleanEmail) }
                    if (existingLocal != null) {
                        Resource.Error("An account with this email already exists")
                    } else {
                        withContext(Dispatchers.IO) {
                            userDao.insertUser(
                                UserEntity(
                                    fullName = cleanName,
                                    email = cleanEmail,
                                    password = pass
                                )
                            )
                        }
                        SessionManager.email = cleanEmail
                        SessionManager.fullName = cleanName
                        Resource.Success(null)
                    }
                }
            } else {
                try {
                    // Call REST Registration API
                    val response = authApiService.register(UserRegisterDto(cleanEmail, pass, cleanName))
                    SessionManager.email = response.email
                    SessionManager.fullName = response.fullName
                    Resource.Success(null)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    // Offline fallback to local Room DB
                    val existingLocal = withContext(Dispatchers.IO) { userDao.getUserByEmail(cleanEmail) }
                    if (existingLocal != null) {
                        Resource.Error("An account with this email already exists")
                    } else {
                        withContext(Dispatchers.IO) {
                            userDao.insertUser(
                                UserEntity(
                                    fullName = cleanName,
                                    email = cleanEmail,
                                    password = pass
                                )
                            )
                        }
                        SessionManager.email = cleanEmail
                        SessionManager.fullName = cleanName
                        Resource.Success(null)
                    }
                }
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun signInWithGoogle(context: Context): Resource<AuthResult?> {
        return try {
            val result = googleAuthClient.signIn(context)
            if (result != null) {
                SessionManager.email = result.user?.email
                SessionManager.fullName = result.user?.displayName ?: "Google User"
                result.user?.uid?.let { SessionManager.saveSession(it) }
                Resource.Success(result)
            } else {
                Resource.Error("Google Sign-In failed or was cancelled")
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
        SessionManager.clearSession()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return if (AppConfig.useFirebase) {
            firebaseAuth.currentUser
        } else {
            null
        }
    }
}
