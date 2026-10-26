package com.example.filmfolio.data.repository

import android.content.Context
import com.example.filmfolio.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun login(email: String, pass: String): Resource<AuthResult?>
    suspend fun register(email: String, pass: String, fullName: String): Resource<AuthResult?>
    suspend fun signInWithGoogle(context: Context): Resource<AuthResult?>
    fun logout()
    fun getCurrentUser(): FirebaseUser?
}
